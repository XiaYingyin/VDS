#ifndef EXTI_H
#define EXTI_H

#include "access/amapi.h"
#include "access/itup.h"
#include "access/sdir.h"
#include "access/xlogreader.h"
#include "catalog/pg_index.h"
#include "lib/stringinfo.h"
#include "storage/bufmgr.h"
#include "storage/shm_toc.h"
#include "nodes/relation.h"
#include "catalog/index.h"
#include "utils/selfuncs.h"

extern void initIndexMeta(Relation index);
extern bool insertIndex(Relation rel, Datum *values, bool *isnull,
		 ItemPointer ht_ctid, Relation heapRel,
		 IndexUniqueCheck checkUnique,
		 struct IndexInfo *indexInfo);
extern IndexScanDesc indexScanInit(Relation rel, int nkeys, int norderbys);
extern bool getNextIndex(IndexScanDesc scan, ScanDirection dir);
extern void scanMatchIndex(IndexScanDesc scan, ScanKey scankey, int nscankeys,
		 ScanKey orderbys, int norderbys);
extern void indexScanFree(IndexScanDesc scan);
extern void markScanPos(IndexScanDesc scan);
extern void retPrevPos(IndexScanDesc scan);
extern IndexBulkDeleteResult *deleteIndex(IndexVacuumInfo *info,
			 IndexBulkDeleteResult *stats,
			 IndexBulkDeleteCallback callback,
			 void *callback_state);
extern IndexBulkDeleteResult *vacuumCleanup(IndexVacuumInfo *info,
				IndexBulkDeleteResult *stats);
extern IndexBuildResult *buildIndexStructure(Relation heap, Relation index,
		struct IndexInfo *indexInfo);
extern void estimateCost(PlannerInfo *root, IndexPath *path, double loop_count,
			   Cost *indexStartupCost, Cost *indexTotalCost,
			   Selectivity *indexSelectivity, double *indexCorrelation,
			   double *indexPages);
extern bool canReturn(Relation index, int attno);


#endif
