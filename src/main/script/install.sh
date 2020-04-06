cd $1
make install
pwd=lcy123
dbname=postgres
usrname=postgres

# t=$(PGPASSWORD=$pwd psql -h localhost -U $usrname $dbname -c "create extension $2")
