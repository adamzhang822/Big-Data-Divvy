#!/bin/bash
driver="jdbc:hive2://localhost:10000/default -n hadoop -d org.apache.hive.jdbc.HiveDriver"
declare -a hql_files=("divvyTrips.hql" "stationsUsage.hql")
for file in "${sql_files[@]}"; 
do
  beeline -u ${driver} -f $file
done