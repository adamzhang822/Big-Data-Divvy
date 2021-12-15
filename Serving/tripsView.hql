drop table if exists adamzhang22_trips_by_route;
create table adamzhang22_trips_by_route (
  from_station_name string, 
  to_station_name string, 
  total_trips bigint,
  trip_duration bigint);

insert overwrite table adamzhang22_trips_by_route
  select 
  from_station_name, 
  to_station_name, 
  count(1) as total_trips,
  sum(trip_duration) as trip_duration
  from adamzhang22_divvytrips
  group by from_station_name, to_station_name
  order by total_trips desc;

drop table if exists adamzhang22_trips_by_route_top5;
create table adamzhang22_trips_by_route_top5 (
  from_station_name string, 
  to_station_name string, 
  total_trips bigint,
  trip_duration bigint,
  rank tinyint);

insert overwrite table adamzhang22_trips_by_route_top5
select from_station_name, to_station_name, total_trips, trip_duration, rank
from (
  select from_station_name, to_station_name, rank() over (partition by from_station_name order by total_trips desc) as rank, total_trips, trip_duration from (
    select * from adamzhang22_trips_by_route
    distribute by from_station_name
    sort by from_station_name, total_trips desc
  ) a
) b
where rank <= 5
order by from_station_name, rank;

