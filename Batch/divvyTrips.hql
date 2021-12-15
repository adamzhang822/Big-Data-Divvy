-- First, map the CSV data we downloaded in Hive
drop table if exists adamzhang22_divvytrips_csv;
create external table adamzhang22_divvytrips_csv(
    trip_id string,
    start_time timestamp,
    stop_time timestamp,
    bike_id string,
    trip_duration int,
    from_station_id string,
    from_station_name string,
    to_station_id string,
    to_station_name string,
    user_type string,
    gender string,
    birth_year string,
    from_latitude string,
    from_longitude string,
    from_location string,
    to_latitude string,
    to_longitude string,
    to_location string)
    row format serde 'org.apache.hadoop.hive.serde2.OpenCSVSerde'

WITH SERDEPROPERTIES (
    "separatorChar" = "\,",
    "quoteChar"     = "\"",
    "timestamp.formats" = "MM/dd/yyyy hh:mm:ss a")
STORED AS TEXTFILE
location '/home/hadoop/adamzhang22/final/divvyTrips/'
TBLPROPERTIES("skip.header.line.count"="1");

-- Create an ORC table for divvytrips data
drop table if exists adamzhang22_divvytrips;
create external table adamzhang22_divvytrips(
    trip_id string,
    start_time timestamp,
    stop_time timestamp,
    bike_id string,
    trip_duration int,
    from_station_id string,
    from_station_name string,
    to_station_id string,
    to_station_name string,
    user_type string,
    gender string,
    birth_year string,
    from_latitude string,
    from_longitude string,
    from_location string,
    to_latitude string,
    to_longitude string,
    to_location string)
    stored as orc;
    
-- Copy the CSV table to the ORC table
insert overwrite table adamzhang22_divvytrips select 
    trip_id,
    start_time,
    stop_time,
    bike_id,
    cast(trip_duration as int),
    from_station_id,
    from_station_name,
    to_station_id,
    to_station_name,
    user_type,
    gender,
    birth_year,
    from_latitude,
    from_longitude,
    from_location,
    to_latitude,
    to_longitude,
    to_location
from adamzhang22_divvytrips_csv
where from_station_name is not null and to_station_name is not null
    and from_station_id is not null and to_station_id is not null
    and trip_duration is not null;