\echo Use "CREATE EXTENSION ftree" to load this file. \quit
LOAD '$libdir/ftree';

CREATE OR REPLACE FUNCTION fthandler(internal) RETURNS index_am_handler AS
'$libdir/ftree' LANGUAGE C; 
CREATE ACCESS METHOD ftree TYPE INDEX HANDLER fthandler;


CREATE FUNCTION ftint8cmp(int8, int8) RETURNS int32 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftboolcmp(bool, bool) RETURNS int32 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint2cmp(int2, int2) RETURNS int32 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint2sortsupport(internal) RETURNS void AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint4cmp(int4, int4) RETURNS int4 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint4sortsupport(internal) RETURNS void AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint8sortsupport(internal) RETURNS void AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

-- CREATE FUNCTION ftfloat4cmp(float4, float4) RETURNS int4 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint28cmp(int2, int8) RETURNS int4 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint82cmp(int8, int2) RETURNS int4 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint24cmp(int2, int4) RETURNS int4 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint42cmp(int4, int2) RETURNS int4 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint48cmp(int4, int8) RETURNS int4 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE FUNCTION ftint84cmp(int8, int4) RETURNS int4 AS '$libdir/ftree' LANGUAGE C IMMUTABLE STRICT;

CREATE OPERATOR FAMILY integer_ops USING ftree;

CREATE OPERATOR CLASS int8_ops
DEFAULT FOR TYPE int8 USING ftree FAMILY integer_ops AS
  -- standard int8 comparisons
  OPERATOR 1 < ,
  OPERATOR 2 <= ,
  OPERATOR 3 = ,
  OPERATOR 4 >= ,
  OPERATOR 5 > ,
  FUNCTION 1 ftint8cmp(int8, int8),
  FUNCTION 2 ftint8sortsupport(internal) ,
  FUNCTION 3 in_range(int8, int8, int8, boolean, boolean) ;

CREATE OPERATOR CLASS int4_ops
DEFAULT FOR TYPE int4 USING ftree FAMILY integer_ops AS
  -- standard int4 comparisons
  OPERATOR 1 < ,
  OPERATOR 2 <= ,
  OPERATOR 3 = ,
  OPERATOR 4 >= ,
  OPERATOR 5 > ,
  FUNCTION 1 ftint4cmp(int4, int4) ,
  FUNCTION 2 ftint4sortsupport(internal) ,
  FUNCTION 3 in_range(int4, int4, int4, boolean, boolean) ;

CREATE OPERATOR CLASS int2_ops
DEFAULT FOR TYPE int2 USING ftree FAMILY integer_ops AS
  -- standard int2 comparisons
  OPERATOR 1 < ,
  OPERATOR 2 <= ,
  OPERATOR 3 = ,
  OPERATOR 4 >= ,
  OPERATOR 5 > ,
  FUNCTION 1 ftint2cmp(int2, int2) ,
  FUNCTION 2 ftint2sortsupport(internal) ,
  FUNCTION 3 in_range(int2, int2, int2, boolean, boolean) ;

ALTER OPERATOR FAMILY integer_ops USING ftree ADD
  -- cross-type comparisons int8 vs int2
  OPERATOR 1 < (int8, int2) ,
  OPERATOR 2 <= (int8, int2) ,
  OPERATOR 3 = (int8, int2) ,
  OPERATOR 4 >= (int8, int2) ,
  OPERATOR 5 > (int8, int2) ,
  FUNCTION 1 (int8, int2) ftint82cmp(int8, int2) ;

ALTER OPERATOR FAMILY integer_ops USING ftree ADD
  -- cross-type comparisons int8 vs int2
  OPERATOR 1 < (int2, int8) ,
  OPERATOR 2 <= (int2, int8) ,
  OPERATOR 3 = (int2, int8) ,
  OPERATOR 4 >= (int2, int8) ,
  OPERATOR 5 > (int2, int8) ,
  FUNCTION 1 (int2, int8) ftint28cmp(int2, int8) ;

ALTER OPERATOR FAMILY integer_ops USING ftree ADD
  -- cross-type comparisons int8 vs int2
  OPERATOR 1 < (int8, int4) ,
  OPERATOR 2 <= (int8, int4) ,
  OPERATOR 3 = (int8, int4) ,
  OPERATOR 4 >= (int8, int4) ,
  OPERATOR 5 > (int8, int4) ,
  FUNCTION 1 (int8, int4) ftint84cmp(int8, int4) ;

ALTER OPERATOR FAMILY integer_ops USING ftree ADD
  -- cross-type comparisons int8 vs int2
  OPERATOR 1 < (int4, int8) ,
  OPERATOR 2 <= (int4, int8) ,
  OPERATOR 3 = (int4, int8) ,
  OPERATOR 4 >= (int4, int8) ,
  OPERATOR 5 > (int4, int8) ,
  FUNCTION 1 (int4, int8) ftint48cmp(int4, int8) ;
