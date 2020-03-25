#include <math.h>
#include "postgres.h"
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

#include "t567.h"
PG_MODULE_MAGIC;
PG_FUNCTION_INFO_V1(t567_handler);
Datum t567_handler(PG_FUNCTION_ARGS)
{IndexAmRoutine *amroutine = makeNode(IndexAmRoutine);

	amroutine->amstrategies = BTMaxStrategyNumber;
	amroutine->amsupport = BTNProcs;
	amroutine->amcanorder = true;
	amroutine->amcanorderbyop = false;
	amroutine->amcanbackward = true;
	amroutine->amcanunique = true;
	amroutine->amcanmulticol = true;
	amroutine->amoptionalkey = true;
	amroutine->amsearcharray = true;
	amroutine->amsearchnulls = true;
	amroutine->amstorage = false;
	amroutine->amclusterable = true;
	amroutine->ampredlocks = true;
	amroutine->amcanparallel = false;
	amroutine->amcaninclude = true;
	amroutine->amkeytype = InvalidOid;
amroutine->ambuild = t567_build;
	amroutine->ambuildempty = t567_buildempty;
	amroutine->aminsert = t567_insert;
	amroutine->ambulkdelete = t567_bulkdelete;
	amroutine->amcanreturn = t567_canreturn;
	amroutine->amcostestimate = t567_costestimate;
	PG_RETURN_POINTER(amroutine);
}
