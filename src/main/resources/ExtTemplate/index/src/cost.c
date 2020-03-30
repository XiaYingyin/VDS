#include "postgres.h"
#include "header/exti.h"

void estimateCost(PlannerInfo *root, 
                  IndexPath *path, 
                  double loop_count,
                  Cost *indexStartupCost, 
                  Cost *indexTotalCost,
                  Selectivity *indexSelectivity, 
                  double *indexCorrelation,
                  double *indexPages)
{
    IndexOptInfo *index = path->indexinfo;
	  GenericCosts costs;

    /*
        write the code of computing cost
    */

	*indexStartupCost = costs.indexStartupCost;
	*indexTotalCost = costs.indexTotalCost;
	*indexSelectivity = costs.indexSelectivity;
	*indexCorrelation = costs.indexCorrelation;
	*indexPages = costs.numIndexPages;
}
