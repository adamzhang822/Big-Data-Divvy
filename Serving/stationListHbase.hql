drop table if exists adamzhang22_stations_hbase;
create external table adamzhang22_stations_hbase (
  station_name string,
  station_name_val string
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key, stations:station_name_val')
TBLPROPERTIES ('hbase.table.name' = 'adamzhang22_stations_hbase');


insert overwrite table adamzhang22_stations_hbase
select station_name, station_name from adamzhang22_stations;