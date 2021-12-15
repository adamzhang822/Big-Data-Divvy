////////////////////////////////////////////////////
// Create batch views: 
// stations usage by month, hour, and day of the week
val stations_stats = spark.sql("""select 
  station_name, 
  record_month,
  record_hour,
  record_dayofweek,
  sum(docks_in_service) as docks_in_service,
  sum(available_docks) as available_docks,
  sum(available_bikes) as available_bikes,
  sum(percent_full) as percent_full,
  count(1) as record_counts
  from adamzhang22_stationsusage
  group by station_name, record_month, record_hour, record_dayofweek""")

// Save to Hive because bulk loading HBase from Spark is awkward
import org.apache.spark.sql.SaveMode
stations_stats.write.mode(SaveMode.Overwrite).saveAsTable("adamzhang22_stations_stats")