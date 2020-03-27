#ifndef BTREE_H
#define BTREE_H

typedef struct BTPageOpaqueData
{
	BlockNumber btpo_prev;		/* left sibling, or P_NONE if leftmost */
	BlockNumber btpo_next;		/* right sibling, or P_NONE if rightmost */
	union
	{
		uint32		level;		/* tree level --- zero for leaf pages */
		TransactionId xact;		/* next transaction ID, if deleted */
	}			btpo;
	uint16		btpo_flags;		/* flag bits, see below */
	CycleId	btpo_cycleid;	/* vacuum cycle ID of latest split */
} BTPageOpaqueData;

typedef BTPageOpaqueData *BTPageOpaque;


/*
 * The max allowed value of a cycle ID is a bit less than 64K.  This is
 * for convenience of pg_filedump and similar utilities: we want to use
 * the last 2 bytes of special space as an index type indicator, and
 * restricting cycle ID lets btree use that space for vacuum cycle IDs
 * while still allowing index type to be identified.
 */
#define MAX_BT_CYCLE_ID		0xFF7F


/*
 * The Meta page is always the first page in the index.
 * Its primary purpose is to point to the location of the btree root page.
 * We also point to the "fast" root, which is the current effective root;
 * see README for discussion.
 */

typedef struct BTMetaPageData
{
	uint32		btm_magic;		/* should contain BTREE_MAGIC */
	uint32		btm_version;	/* should contain BTREE_VERSION */
	BlockNumber btm_root;		/* current root location */
	uint32		btm_level;		/* tree level of the root page */
	BlockNumber btm_fastroot;	/* current "fast" root location */
	uint32		btm_fastlevel;	/* tree level of the "fast" root page */
	/* following fields are available since page version 3 */
	TransactionId btm_oldest_btpo_xact; /* oldest btpo_xact among all deleted
										 * pages */
	float8		btm_last_cleanup_num_heap_tuples;	/* number of heap tuples
													 * during last cleanup */
} BTMetaPageData;

#define BTPageGetMeta(p) \
	((BTMetaPageData *) PageGetContents(p))

#define BTREE_METAPAGE	0		/* first page is meta */
#define BTREE_MAGIC		0x053162	/* magic number of btree pages */
#define BTREE_VERSION	3		/* current version number */
#define BTREE_MIN_VERSION	2	/* minimal supported version number */

/*
 * Maximum size of a btree index entry, including its tuple header.
 *
 * We actually need to be able to fit three items on every page,
 * so restrict any one item to 1/3 the per-page available space.
 */
#define BTMaxItemSize(page) \
	MAXALIGN_DOWN((PageGetPageSize(page) - \
				   MAXALIGN(SizeOfPageHeaderData + 3*sizeof(ItemIdData)) - \
				   MAXALIGN(sizeof(BTPageOpaqueData))) / 3)

/*
 * The leaf-page fillfactor defaults to 90% but is user-adjustable.
 * For pages above the leaf level, we use a fixed 70% fillfactor.
 * The fillfactor is applied during index build and when splitting
 * a rightmost page; when splitting non-rightmost pages we try to
 * divide the data equally.
 */
#define BTREE_MIN_FILLFACTOR		10
#define BTREE_DEFAULT_FILLFACTOR	90
#define BTREE_NONLEAF_FILLFACTOR	70

/*
 *	In general, the btree code tries to localize its knowledge about
 *	page layout to a couple of routines.  However, we need a special
 *	value to indicate "no page number" in those places where we expect
 *	page numbers.  We can use zero for this because we never need to
 *	make a pointer to the metadata page.
 */

#define P_NONE			0

/*
 * Macros to test whether a page is leftmost or rightmost on its tree level,
 * as well as other state info kept in the opaque data.
 */
#define P_LEFTMOST(opaque)		((opaque)->btpo_prev == P_NONE)
#define P_RIGHTMOST(opaque)		((opaque)->btpo_next == P_NONE)
#define P_ISLEAF(opaque)		(((opaque)->btpo_flags & BTP_LEAF) != 0)
#define P_ISROOT(opaque)		(((opaque)->btpo_flags & BTP_ROOT) != 0)
#define P_ISDELETED(opaque)		(((opaque)->btpo_flags & BTP_DELETED) != 0)
#define P_ISMETA(opaque)		(((opaque)->btpo_flags & BTP_META) != 0)
#define P_ISHALFDEAD(opaque)	(((opaque)->btpo_flags & BTP_HALF_DEAD) != 0)
#define P_IGNORE(opaque)		(((opaque)->btpo_flags & (BTP_DELETED|BTP_HALF_DEAD)) != 0)
#define P_HAS_GARBAGE(opaque)	(((opaque)->btpo_flags & BTP_HAS_GARBAGE) != 0)
#define P_INCOMPLETE_SPLIT(opaque)	(((opaque)->btpo_flags & BTP_INCOMPLETE_SPLIT) != 0)

/*
 *	Lehman and Yao's algorithm requires a ``high key'' on every non-rightmost
 *	page.  The high key is not a data key, but gives info about what range of
 *	keys is supposed to be on this page.  The high key on a page is required
 *	to be greater than or equal to any data key that appears on the page.
 *	If we find ourselves trying to insert a key > high key, we know we need
 *	to move right (this should only happen if the page was split since we
 *	examined the parent page).
 *
 *	Our insertion algorithm guarantees that we can use the initial least key
 *	on our right sibling as the high key.  Once a page is created, its high
 *	key changes only if the page is split.
 *
 *	On a non-rightmost page, the high key lives in item 1 and data items
 *	start in item 2.  Rightmost pages have no high key, so we store data
 *	items beginning in item 1.
 */

#define P_HIKEY				((OffsetNumber) 1)
#define P_FIRSTKEY			((OffsetNumber) 2)
#define P_FIRSTDATAKEY(opaque)	(P_RIGHTMOST(opaque) ? P_HIKEY : P_FIRSTKEY)

/*
 * INCLUDE B-Tree indexes have non-key attributes.  These are extra
 * attributes that may be returned by index-only scans, but do not influence
 * the order of items in the index (formally, non-key attributes are not
 * considered to be part of the key space).  Non-key attributes are only
 * present in leaf index tuples whose item pointers actually point to heap
 * tuples.  All other types of index tuples (collectively, "pivot" tuples)
 * only have key attributes, since pivot tuples only ever need to represent
 * how the key space is separated.  In general, any B-Tree index that has
 * more than one level (i.e. any index that does not just consist of a
 * metapage and a single leaf root page) must have some number of pivot
 * tuples, since pivot tuples are used for traversing the tree.
 *
 * We store the number of attributes present inside pivot tuples by abusing
 * their item pointer offset field, since pivot tuples never need to store a
 * real offset (downlinks only need to store a block number).  The offset
 * field only stores the number of attributes when the INDEX_ALT_TID_MASK
 * bit is set (we never assume that pivot tuples must explicitly store the
 * number of attributes, and currently do not bother storing the number of
 * attributes unless indnkeyatts actually differs from indnatts).
 * INDEX_ALT_TID_MASK is only used for pivot tuples at present, though it's
 * possible that it will be used within non-pivot tuples in the future.  Do
 * not assume that a tuple with INDEX_ALT_TID_MASK set must be a pivot
 * tuple.
 *
 * The 12 least significant offset bits are used to represent the number of
 * attributes in INDEX_ALT_TID_MASK tuples, leaving 4 bits that are reserved
 * for future use (BT_RESERVED_OFFSET_MASK bits). BT_N_KEYS_OFFSET_MASK should
 * be large enough to store any number <= INDEX_MAX_KEYS.
 */
#define INDEX_ALT_TID_MASK			INDEX_AM_RESERVED_BIT
#define BT_RESERVED_OFFSET_MASK		0xF000
#define BT_N_KEYS_OFFSET_MASK		0x0FFF

/* Get/set downlink block number */
#define BTreeInnerTupleGetDownLink(itup) \
	ItemPointerGetBlockNumberNoCheck(&((itup)->t_tid))
#define BTreeInnerTupleSetDownLink(itup, blkno) \
	ItemPointerSetBlockNumber(&((itup)->t_tid), (blkno))

/*
 * Get/set leaf page highkey's link. During the second phase of deletion, the
 * target leaf page's high key may point to an ancestor page (at all other
 * times, the leaf level high key's link is not used).  See the nbtree README
 * for full details.
 */
#define BTreeTupleGetTopParent(itup) \
	ItemPointerGetBlockNumberNoCheck(&((itup)->t_tid))
#define BTreeTupleSetTopParent(itup, blkno)	\
	do { \
		ItemPointerSetBlockNumber(&((itup)->t_tid), (blkno)); \
		BTreeTupleSetNAtts((itup), 0); \
	} while(0)

/*
 * Get/set number of attributes within B-tree index tuple. Asserts should be
 * removed when BT_RESERVED_OFFSET_MASK bits will be used.
 */
#define BTreeTupleGetNAtts(itup, rel)	\
	( \
		(itup)->t_info & INDEX_ALT_TID_MASK ? \
		( \
			AssertMacro((ItemPointerGetOffsetNumberNoCheck(&(itup)->t_tid) & BT_RESERVED_OFFSET_MASK) == 0), \
			ItemPointerGetOffsetNumberNoCheck(&(itup)->t_tid) & BT_N_KEYS_OFFSET_MASK \
		) \
		: \
		IndexRelationGetNumberOfAttributes(rel) \
	)
#define BTreeTupleSetNAtts(itup, n) \
	do { \
		(itup)->t_info |= INDEX_ALT_TID_MASK; \
		Assert(((n) & BT_RESERVED_OFFSET_MASK) == 0); \
		ItemPointerSetOffsetNumber(&(itup)->t_tid, (n) & BT_N_KEYS_OFFSET_MASK); \
	} while(0)

/*
 *	Operator strategy numbers for B-tree have been moved to access/stratnum.h,
 *	because many places need to use them in ScanKeyInit() calls.
 *
 *	The strategy numbers are chosen so that we can commute them by
 *	subtraction, thus:
 */
#define BTCommuteStrategyNumber(strat)	(BTMaxStrategyNumber + 1 - (strat))

/*
 *	When a new operator class is declared, we require that the user
 *	supply us with an amproc procedure (BTORDER_PROC) for determining
 *	whether, for two keys a and b, a < b, a = b, or a > b.  This routine
 *	must return < 0, 0, > 0, respectively, in these three cases.
 *
 *	To facilitate accelerated sorting, an operator class may choose to
 *	offer a second procedure (BTSORTSUPPORT_PROC).  For full details, see
 *	src/include/utils/sortsupport.h.
 *
 *	To support window frames defined by "RANGE offset PRECEDING/FOLLOWING",
 *	an operator class may choose to offer a third amproc procedure
 *	(BTINRANGE_PROC), independently of whether it offers sortsupport.
 *	For full details, see doc/src/sgml/btree.sgml.
 */

#define BTORDER_PROC		1
#define BTSORTSUPPORT_PROC	2
#define BTINRANGE_PROC		3
#define BTNProcs			3

/*
 *	We need to be able to tell the difference between read and write
 *	requests for pages, in order to do locking correctly.
 */

#define BT_READ			BUFFER_LOCK_SHARE
#define BT_WRITE		BUFFER_LOCK_EXCLUSIVE


#endif