drop table if exists adamzhang22_stations;
create table adamzhang22_stations (
  station_name string);

insert overwrite table adamzhang22_stations
  select 
  distinct station_name as station_name 
  from adamzhang22_stations_stats;