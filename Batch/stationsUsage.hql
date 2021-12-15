-- First, map the CSV data we downloaded in Hive
drop table if exists adamzhang22_stationsusage_csv;
create external table adamzhang22_stationsusage_csv(
    divvy_station_id string,
    record_timestamp timestamp,
    station_name string,
    address string,
    total_docks tinyint,
    docks_in_service tinyint,
    available_docks tinyint,
    available_bikes tinyint,
    percent_full tinyint,
    status string,
    latitude string,
    longitude string,
    station_location_map string,
    record_id string)
    row format serde 'org.apache.hadoop.hive.serde2.OpenCSVSerde'

WITH SERDEPROPERTIES (
    "separatorChar" = "\,",
    "quoteChar"     = "\"",
    "timestamp.formats" = "MM/dd/yyyy hh:mm:ss a")
STORED AS TEXTFILE
location '/home/hadoop/adamzhang22/final/stationsUsage/'
TBLPROPERTIES("skip.header.line.count"="1");

-- Create an ORC table for stations usage data
drop table if exists adamzhang22_stationsusage;
create external table adamzhang22_stationsusage(
    divvy_station_id string,
    record_timestamp timestamp,
    record_month tinyint,
    record_hour tinyint,
    record_dayofweek tinyint,
    station_name string,
    address string,
    total_docks tinyint,
    docks_in_service tinyint,
    available_docks tinyint,
    available_bikes tinyint,
    percent_full tinyint,
    status string,
    latitude string,
    longitude string,
    station_location_map string,
    record_id string)
    stored as orc;
    
-- Copy the CSV table to the ORC table
insert overwrite table adamzhang22_stationsusage
select
    divvy_station_id,
    from_unixtime(unix_timestamp(record_timestamp,"MM/dd/yyyy hh:mm:ss a")),
    MONTH(from_unixtime(unix_timestamp(record_timestamp,"MM/dd/yyyy hh:mm:ss a"))),
    HOUR(from_unixtime(unix_timestamp(record_timestamp,"MM/dd/yyyy hh:mm:ss a"))),
    date_format(from_unixtime(unix_timestamp(record_timestamp,"MM/dd/yyyy hh:mm:ss a")),'u'),
    station_name,
    address,
    cast(total_docks as tinyint),
    cast(docks_in_service as tinyint),
    cast(available_docks as tinyint),
    cast(available_bikes as tinyint),
    cast(percent_full as tinyint),
    status,
    latitude,
    longitude,
    station_location_map,
    record_id
FROM adamzhang22_stationsusage_csv
WHERE record_timestamp is not null and station_name is not null
    and total_docks is not null and docks_in_service is not null
    and available_docks is not null and available_bikes is not null
    and percent_full is not null and status is not null;