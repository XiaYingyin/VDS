
dbname=postgres
usrname=postgres
result_arr=()

path=$(pwd)

for i in `seq 1 22`; do
  t=$(PGPASSWORD=lcy123 psql -h localhost -U $usrname $dbname < $path/src/main/script/queries/$i.explain.sql | grep "Execution Time" | tr -d "a-z A-Z :")
  result_arr[$i-1]=$t
done

echo ${result_arr[@]}


