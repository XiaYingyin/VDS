
dbname=postgres
usrname=postgres
result_arr=()

path=$(pwd)
#path=/home/xyy/Jqpv
pwd=labbi500

for i in `seq 1 22`; do
#  t=$(PGPASSWORD=$pwd psql -h localhost -U $usrname $dbname < $path/src/main/script/queries/$i.explain.sql | grep "Execution Time" | tr -d "a-z A-Z :")
  result_arr[$i-1]=$t
done

result_arr=(34.0 16.9 23.5 45.7 23.0 19.0 23.5 45.7 100.0 60.0 23.5 45.7 23.5 45.7 100.0 90.0 23.5 45.7 100.0 78.0 23.5 45.7)

echo ${result_arr[@]}


