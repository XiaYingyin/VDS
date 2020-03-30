\echo Use "CREATE EXTENSION Demo" to load this file. \quit
LOAD '$libdir/demo';

CREATE OR REPLACE FUNCTION demo_handler(internal) RETURNS index_am_handler AS
'$libdir/demo' LANGUAGE C; 
CREATE ACCESS METHOD Demo TYPE INDEX HANDLER Demo_handler;
-- Add new operators --
