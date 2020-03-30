#include "postgres.h"
#include "exti.h"
#include "config.h"
#include "xpg_storage.h"
#include "xpg_util.h"
#include "xpg_data.h"
#include "xpg_type.h"
#include "xpg_opt.h"

#include "access/relscan.h"
#include "access/xlog.h"
#include "commands/vacuum.h"
#include "miscadmin.h"
#include "nodes/execnodes.h"
#include "pgstat.h"
#include "postmaster/autovacuum.h"
#include "storage/condition_variable.h"
#include "storage/indexfsm.h"
#include "storage/ipc.h"
#include "storage/lmgr.h"
#include "storage/smgr.h"
#include "utils/builtins.h"
#include "utils/index_selfuncs.h"
#include "utils/memutils.h"
#include "nodes/relation.h"
#include "access/amapi.h"

#include <ctype.h>
#include <float.h>
#include <math.h>

#include "access/brin.h"
#include "access/gin.h"
#include "access/htup_details.h"
#include "access/sysattr.h"
#include "catalog/index.h"
#include "catalog/pg_am.h"
#include "catalog/pg_collation.h"
#include "catalog/pg_operator.h"
#include "catalog/pg_opfamily.h"
#include "catalog/pg_statistic.h"
#include "catalog/pg_statistic_ext.h"
#include "catalog/pg_type.h"
#include "executor/executor.h"
#include "mb/pg_wchar.h"
#include "miscadmin.h"
#include "nodes/makefuncs.h"
#include "nodes/nodeFuncs.h"
#include "optimizer/clauses.h"
#include "optimizer/cost.h"
#include "optimizer/pathnode.h"
#include "optimizer/paths.h"
#include "optimizer/plancat.h"
#include "optimizer/predtest.h"
#include "optimizer/restrictinfo.h"
#include "optimizer/var.h"
#include "parser/parse_clause.h"
#include "parser/parse_coerce.h"
#include "parser/parsetree.h"
#include "statistics/statistics.h"
#include "utils/acl.h"
#include "utils/builtins.h"
#include "utils/bytea.h"
#include "utils/date.h"
#include "utils/datum.h"
#include "utils/fmgroids.h"
#include "utils/index_selfuncs.h"
#include "utils/lsyscache.h"
#include "utils/nabstime.h"
#include "utils/pg_locale.h"
#include "utils/rel.h"
#include "utils/selfuncs.h"
#include "utils/snapmgr.h"
#include "utils/spccache.h"
#include "utils/syscache.h"
#include "utils/timestamp.h"
#include "utils/tqual.h"

PG_MODULE_MAGIC;

/*
 * index extension handler function: return IndexAmRoutine with access method parameters
 * and callbacks.
 */
PG_FUNCTION_INFO_V1(__handler__);

Datum
__handler__(PG_FUNCTION_ARGS)
{
	IndexAmRoutine *amroutine = makeNode(IndexAmRoutine);

	amroutine->amstrategies = OPERATOR_NUM;
	amroutine->amsupport = OPERATOR_NUM;
	amroutine->amcanorder = CAN_ORDER;
	amroutine->amcanorderbyop = CAN_ORDER_BY_OP;
	amroutine->amcanbackward = CAN_BACKWARD;
	amroutine->amcanunique = CAN_UNIQUE;
	amroutine->amcanmulticol = CAN_MULTICOL;
	amroutine->amoptionalkey = OPTIONAL_KEY;
	amroutine->amsearcharray = true;
	amroutine->amsearchnulls = true;
	amroutine->amstorage = false;
	amroutine->amclusterable = true;
	amroutine->ampredlocks = true;
	amroutine->amcanparallel = true;
	amroutine->amcaninclude = true;
	amroutine->amkeytype = InvalidOid;

	amroutine->ambuild = buildIndexStructure;
	amroutine->ambuildempty = initIndexMeta;
	amroutine->aminsert = insertIndex;
	amroutine->ambulkdelete = deleteIndex;
	amroutine->amvacuumcleanup = vacuumCleanup;
	amroutine->amcanreturn = canReturn;
	amroutine->amcostestimate = estimateCost;
	amroutine->ambeginscan = indexScanInit;
	amroutine->amrescan = scanMatchIndex;
	amroutine->amgettuple = getNextIndex;
	amroutine->amendscan = indexScanFree;
	amroutine->ammarkpos = markScanPos;
	amroutine->amrestrpos = retPrevPos;

	PG_RETURN_POINTER(amroutine);
}

/*
 *	canreturn() -- Check whether indexes support index-only scans.
 *
 */
bool canreturn(Relation index, int attno)
{
	return CAN_INDEX_ONLY;
}
