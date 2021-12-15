drop table if exists adamzhang22_stations_stats;
create table adamzhang22_stations_stats (
  station_name string, 
  record_month tinyint,
  record_hour tinyint,
  record_dayofweek tinyint,
  docks_in_service bigint,
  available_docks bigint,
  available_bikes bigint,
  percent_full bigint,
  record_counts bigint);

insert overwrite table adamzhang22_stations_stats
  select 
  station_name, 
  record_month,
  record_hour,
  record_dayofweek,
  sum(docks_in_service),
  sum(available_docks),
  sum(available_bikes),
  sum(percent_full),
  count(1) as record_counts
  from adamzhang22_stationsusage
  group by station_name, record_month, record_hour, record_dayofweek