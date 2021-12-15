import scala.reflect.runtime.universe._


case class KafkaStationRecord(
                              station_name: String,
                              record_month : Int,
                              record_hour : Int,
                              record_dayofweek : Int,
                              docks_in_service : Int,
                              available_docks : Int,
                              available_bikes: Int,
                              percent_full : Int)