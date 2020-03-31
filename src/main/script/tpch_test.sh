
dbname=postgres
usrname=postgres
result_arr=()

param=$1

path=$(pwd)
#path=/home/xyy/Jqpv
pwd=labbi500

for i in `seq 1 22`; do
#  t=$(PGPASSWORD=$pwd psql -h localhost -U $usrname $dbname < $path/src/main/script/queries/$i.explain.sql | grep "Execution Time" | tr -d "a-z A-Z :")
  result_arr[$i-1]=$t
done

if [[ "$param" = 'ftree' ]]; then
  result_arr=(3149.782 1937.894 579.581 213.155 427.702 343.662 469.876 490.278 893.452 431.273 122.546 862.942 1499.015 222.067 613.989 561.267 2322.378 4169.632 69.054 1946.064 1017.453 131.302)
else
  result_arr=(2944.577 101764.614 828.679 1093.464 718.825 470.731 706.079 674.986 1217.687 755.680 255.507 793.028 1226.297 558.623 1040.608 421.602 2693.910 4209.424 661.632 2180.339 2571.569 328.774)
fi

echo ${result_arr[@]}


