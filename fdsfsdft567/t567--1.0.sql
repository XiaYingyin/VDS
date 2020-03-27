\echo Use "CREATE EXTENSION t567" to load this file. \quit
LOAD '$libdir/t567';

CREATE OR REPLACE FUNCTION t567_handler(internal) RETURNS index_am_handler AS
'$libdir/t567' LANGUAGE C; 
CREATE ACCESS METHOD t567 TYPE INDEX HANDLER t567_handler;
