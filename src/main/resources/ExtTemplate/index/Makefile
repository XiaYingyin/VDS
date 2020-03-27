EXTENSION = test
EXTVERSION   = $(shell grep default_version $(EXTENSION).control | sed -e "s/default_version[[:space:]]*=[[:space:]]*'\([^']*\)'/\1/")
REGRESS = test
TESTS        = $(wildcard test/sql/*.sql)

PG_CONFIG ?= pg_config

MODULE_big = test

OBJS = test.o insert.o .o scan.o delete.o cost.o compare.o 

DATA = $(wildcard *--*.sql)
PGXS := $(shell $(PG_CONFIG) --pgxs)
include $(PGXS)
