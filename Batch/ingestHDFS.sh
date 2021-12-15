#!/bin/bash
# Download data from city of chicago and put them into hdfs file system

city_of_chicago_url=https://data.cityofchicago.org/api/views
declare -A divvy_data=( ["divvyTrips"]="fg6s-gzvg" ["stationUsage"]="eq45-8inv")
for data in "${!divvy_data[@]}"; 
do 
	curl ${city_of_chicago_url}/${divvy_data[$data]}/rows.csv | hdfs dfs -put -f - /home/hadoop/adamzhang22/final/$data/$data.csv
done