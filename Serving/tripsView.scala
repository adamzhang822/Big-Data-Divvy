// Opening hive tables as spark dataframes
val trips = spark.table("adamzhang22_divvytrips")
////////////////////////////////////////////////////
// Create batch views: 
// Top destination by origins 
// Top origins to destination
val trips_by_route = spark.sql("""select from_station_name, 
  to_station_name, count(1) as total_trips,
  sum(trip_duration) as trip_duration
  from adamzhang22_divvytrips
  group by from_station_name, to_station_name
  order by total_trips desc""")

trips_by_route.createOrReplaceTempView("adamzhang22_trips_by_route")

val trips_by_route_top5 = spark.sql("""select from_station_name, to_station_name, total_trips, trip_duration, rank
from (
  select from_station_name, to_station_name, rank() over (partition by from_station_name order by total_trips desc) as rank, total_trips, trip_duration from (
    select * from adamzhang22_trips_by_route
    distribute by from_station_name
    sort by from_station_name, total_trips desc
  ) a
) b
where rank <= 5
order by from_station_name, rank""")

// Save to Hive because bulk loading HBase from Spark is awkward
import org.apache.spark.sql.SaveMode
trips_by_route_top5.write.mode(SaveMode.Overwrite).saveAsTable("adamzhang22_divvy_trips_by_route_top5")