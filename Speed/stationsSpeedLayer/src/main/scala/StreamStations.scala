import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import com.fasterxml.jackson.databind.{ DeserializationFeature, ObjectMapper }
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.Increment
import org.apache.hadoop.hbase.util.Bytes

object StreamStations {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  val hbaseConf: Configuration = HBaseConfiguration.create()
  hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
  hbaseConf.set("hbase.zookeeper.quorum", "localhost")

  val hbaseConnection = ConnectionFactory.createConnection(hbaseConf)
  val stationsStats = hbaseConnection.getTable(TableName.valueOf("adamzhang22_stations_stats_hbase"))


  def incrementRecordByStationTime(ksr : KafkaStationRecord) : String = {
    val inc = new Increment(Bytes.toBytes(ksr.station_name + " -month: " + ksr.record_month + " -hour: " + ksr.record_hour + " -dayofweek: " + ksr.record_dayofweek ))
    inc.addColumn(Bytes.toBytes("stations"), Bytes.toBytes("docks_in_service"), ksr.docks_in_service)
    inc.addColumn(Bytes.toBytes("stations"), Bytes.toBytes("available_docks"), ksr.available_docks)
    inc.addColumn(Bytes.toBytes("stations"), Bytes.toBytes("available_bikes"), ksr.available_bikes)
    inc.addColumn(Bytes.toBytes("stations"), Bytes.toBytes("percent_full"), ksr.percent_full)
    inc.addColumn(Bytes.toBytes("stations"), Bytes.toBytes("record_counts"), 1)
    stationsStats.increment(inc)
    return "Updated speed layer for record at " + ksr.station_name + " -month: " + ksr.record_month + " -hour: " + ksr.record_hour + " -dayofweek: " + ksr.record_dayofweek
  }

  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.println(s"""
                            |Usage: StreamFlights <brokers>
                            |  <brokers> is a list of one or more Kafka brokers
                            |
        """.stripMargin)
      System.exit(1)
    }

    val Array(brokers) = args

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("StreamStations")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    // Create direct kafka stream with brokers and topics
    val topicsSet = Set("adamzhang22-final")
    // Create direct kafka stream with brokers and topics
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc, PreferConsistent,
      Subscribe[String, String](topicsSet, kafkaParams)
    )

    // Get the lines, split them into words, count the words and print
    val serializedRecords = stream.map(_.value);

    val ksrs = serializedRecords.map(rec => mapper.readValue(rec, classOf[KafkaStationRecord]))

    // Update speed table    
    val processedRecords = ksrs.map(incrementRecordByStationTime)
    processedRecords.print()
    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}
