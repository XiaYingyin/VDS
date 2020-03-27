#include "postgres.h"
#include "exti.h"

/*
 * Bulk deletion of all index entries pointing to a set of heap tuples.
 * The set of target tuples is specified via a callback routine that tells
 * whether any given heap tuple (identified by ItemPointer) is being deleted.
 *
 * Result: a palloc'd struct containing statistical info for VACUUM displays.
 */
IndexBulkDeleteResult *deleteIndex(IndexVacuumInfo *info, 
                                   IndexBulkDeleteResult *stats,
                                   IndexBulkDeleteCallback callback, 
                                   void *callback_state)
{
    /*
        write the code of delete index code
    */
	return stats;
}

/*
 * Post-VACUUM cleanup.
 *
 * Result: a palloc'd struct containing statistical info for VACUUM displays.
 */
IndexBulkDeleteResult *vacuumCleanup(IndexVacuumInfo *info, 
                                     IndexBulkDeleteResult *stats)
{
	return stats;
}
