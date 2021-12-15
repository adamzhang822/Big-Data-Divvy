drop table if exists adamzhang22_stations_stats_hbase;
create external table adamzhang22_stations_stats_hbase (
  station_record string, 
  docks_in_service bigint,
  available_docks bigint,
  available_bikes bigint,
  percent_full bigint,
  record_counts bigint
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,stations:docks_in_service#b, stations:available_docks#b, stations:available_bikes#b,stations:percent_full#b,stations:record_counts#b')
TBLPROPERTIES ('hbase.table.name' = 'adamzhang22_stations_stats_hbase');


insert overwrite table adamzhang22_stations_stats_hbase
select 
    concat(station_name, " -month: ", record_month, " -hour: ", record_hour, " -dayofweek: ", record_dayofweek),
    docks_in_service,
    available_docks,
    available_bikes,
    percent_full,
    record_counts
 from adamzhang22_stations_stats;