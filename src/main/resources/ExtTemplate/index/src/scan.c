#include "postgres.h"
#include "header/exti.h"

/*
 *	indexScanInit() -- start a scan on a index
 */
IndexScanDesc indexScanInit(Relation rel, 
                        int nkeys, 
                        int norderbys)
{
	IndexScanDesc scan = NULL;

	return scan;
}

/*
 *	scanMatchIndex() -- rescan an index relation
 */
void scanMatchIndex(IndexScanDesc scan, 
          ScanKey scankey, 
          int nscankeys, 
          ScanKey orderbys, 
          int norderbys)
{
	
}

/*
 *	indexScanFree() -- close down a scan
 */
void indexScanFree(IndexScanDesc scan)
{
	
}

/*
 *	markpos() -- save current scan position
 */
void markScanPos(IndexScanDesc scan)
{
	
}

/*
 *	restrpos() -- restore scan to last saved position
 */
void
retPrevPos(IndexScanDesc scan)
{
	
}

/*
 *	getNextIndex() -- Get the next tuple in the scan.
 */
bool getNextIndex(IndexScanDesc scan, ScanDirection dir)
{
	bool res = true;

    // if find success, return true
	return res;
}
