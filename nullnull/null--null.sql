\echo Use "CREATE EXTENSION null" to load this file. \quit
LOAD '$libdir/null';

CREATE OR REPLACE FUNCTION null_handler(internal) RETURNS index_am_handler AS
'$libdir/null' LANGUAGE C; 
CREATE ACCESS METHOD null TYPE INDEX HANDLER null_handler;
