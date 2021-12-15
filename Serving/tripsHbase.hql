drop table if exists adamzhang22_divvy_trips_by_route_top5_hbase;
create external table adamzhang22_divvy_trips_by_route_top5_hbase (
  from_station_name_and_rank string, 
  to_station_name string, 
  total_trips bigint,
  trip_duration bigint)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,routes:to_station_name, routes:total_trips,routes:trip_duration')
TBLPROPERTIES ('hbase.table.name' = 'adamzhang22_divvy_trips_by_route_top5_hbase');


insert overwrite table adamzhang22_divvy_trips_by_route_top5_hbase
select 
    concat(from_station_name,rank),
    to_station_name,
    total_trips,
    trip_duration
 from adamzhang22_divvy_trips_by_route_top5;