EXTENSION = demo
EXTVERSION   = $(shell grep default_version $(EXTENSION).control | sed -e "s/default_version[[:space:]]*=[[:space:]]*'\([^']*\)'/\1/")
REGRESS = test
TESTS        = $(wildcard test/sql/*.sql)

PG_CONFIG ?= pg_config

MODULE_big = demo

OBJS = src/demo.o src/insert.o src/scan.o src/delete.o src/cost.o src/compare.o src/build.o

DATA = $(wildcard *--*.sql)
PGXS := $(shell $(PG_CONFIG) --pgxs)
include $(PGXS)
