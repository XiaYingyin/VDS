EXTENSION = null
EXTVERSION = null
PG_CONFIG ?= pg_config
MODULE_big = null
OBJS = name.o
DATA = $(wildcard *--*.sql)
PGXS := $(shell $(PG_CONFIG) --pgxs)
include $(PGXS)

DEBUILD_ROOT = /tmp/$(EXTENSION)

deb: release-zip
	mkdir -p $(DEBUILD_ROOT) && rm -rf $(DEBUILD_ROOT)/*
	unzip ./${EXTENSION}-$(EXTVERSION).zip -d $(DEBUILD_ROOT)
	cd $(DEBUILD_ROOT)/${EXTENSION}-$(EXTVERSION) && make -f debian/rules orig
	cd $(DEBUILD_ROOT)/${EXTENSION}-$(EXTVERSION) && debuild -us -uc -sa