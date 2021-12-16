#This script edit by zq_whale
#This script used by repair tables
mysql_host=127.0.0.1
mysql_user=$1
mysql_pass=$2
database=$3

tables=$(mysql -h$mysql_host -u$mysql_user -p$mysql_pass $database -A -Bse "show tables")
for arg in $tables
do
check_status=$(mysql -h$mysql_host -u$mysql_user -p$mysql_pass $database -A -Bse "check table $arg" | awk '{ print $4 }')
if [ "$check_status" = "OK" ]
then
echo "$arg is ok"
else
echo $(mysql -h$mysql_host -u$mysql_user -p$mysql_pass $database -A -Bse "repair table $arg")

fi
echo $(mysql -h$mysql_host -u$mysql_user -p$mysql_pass $database -A -Bse "optimize table $arg")
done
