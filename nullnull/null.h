#include "access/amapi.h"
#include "access/itup.h"
#include "access/sdir.h"
#include "access/xlogreader.h"
#include "catalog/pg_index.h"
#include "catalog/pg_type.h"
#include "lib/stringinfo.h"
#include "storage/bufmgr.h"
#include "storage/shm_toc.h"
//#include "nodes/relation.h"
#include "utils/selfuncs.h"
#include "utils/lsyscache.h"
#include "utils/syscache.h"
#include "optimizer/cost.h"
#include "parser/parsetree.h"extern IndexBuildResult *null_build(Relation heap, Relation index,
		struct IndexInfo *indexInfo);
extern bool null_doinsert(Relation rel, IndexTuple itup,
			 IndexUniqueCheck checkUnique, Relation heapRel);
extern Stack null_search(Relation rel,
		   int keysz, ScanKey scankey, bool nextkey,
		   Buffer *bufP, int access, Snapshot snapshot);
extern void null_costestimate(struct PlannerInfo *root,
			   struct IndexPath *path,
			   double loop_count,
			   Cost *indexStartupCost,
			   Cost *indexTotalCost,
			   Selectivity *indexSelectivity,
			   double *indexCorrelation,
			   double *indexPages);
