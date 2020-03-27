#ifndef CONFIG_H
#define CONFIG_H

/* Total number of strategies (operators) by which we can traverse/search this AM. Zero if AM does not have a fixed set of strategy assignments. */
#define OPERATOR_NUM      0 
/* total number of support functions that this AM uses */
#define FUNCTION_NUM      0
/* does AM support ORDER BY indexed column's value? */
#define CAN_ORDER         true
/* does AM support ORDER BY result of an operator on indexed column? */
#define CAN_ORDER_BY_OP   false
/* does AM support backward scanning? */
#define CAN_BACKWARD      true
/* does AM support UNIQUE indexes? */
#define CAN_UNIQUE        true
/* does AM support multi-column indexes? */
#define CAN_MULTICOL      true
/* does AM require scans to have a constraint on the first index column? */
#define OPTIONAL_KEY      true
/* does AM handle ScalarArrayOpExpr quals? */
#define SEARCH_ARRY       true
/* does AM handle IS NULL/IS NOT NULL quals? */
#define SEARCH_NULLS      true
/* can index storage data type differ from column data type? */
#define SAME_TYPE         true
/* can an index of this type be clustered on? */
#define CLUSTERABLE       false
/* does AM handle predicate locks? */
#define CAN_PARALLEL      false
/* does AM support columns included with clause INCLUDE? */
#define CAN_INCLUDE       true
/* if support index only? */
#define CAN_INDEX_ONLY    true

#endif


