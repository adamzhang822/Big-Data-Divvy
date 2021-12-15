package edu.uchicago.adamzhang22;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// Inspired by http://stackoverflow.com/questions/14458450/what-to-use-instead-of-org-jboss-resteasy-client-clientrequest
public class StationUpdates {
    static class Task extends TimerTask {
        private Client client;

        public StationResponse[] getStationResponse() {
            Invocation.Builder bldr
                    = client.target("https://data.cityofchicago.org/resource/eq45-8inv.json?$select=station_name,date_extract_m(timestamp)%20as%20record_month,%20date_extract_hh(timestamp)%20as%20record_hour,%20date_extract_dow(timestamp)%20as%20record_dayofweek,%20docks_in_service,%20available_docks,%20available_bikes,%20percent_full&$order=timestamp%20desc&$limit=100")
                    .request("application/json");
            try {
                return bldr.get(StationResponse[].class);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return null;
        }

        // Adapted from http://hortonworks.com/hadoop-tutorial/simulating-transporting-realtime-events-stream-apache-kafka/
        Properties props = new Properties();
        String TOPIC = "adamzhang22-final";
        KafkaProducer<String, String> producer;

        public Task() {
            client = ClientBuilder.newClient();
            // enable POJO mapping using Jackson - see
            // https://jersey.java.net/documentation/latest/user-guide.html#json.jackson
            client.register(JacksonFeature.class);
            props.put("bootstrap.servers", bootstrapServers);
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("batch.size", 16384);
            props.put("linger.ms", 1);
            props.put("buffer.memory", 33554432);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            producer = new KafkaProducer<>(props);
        }

        @Override
        public void run() {
            StationResponse[] response = getStationResponse();
            if(response == null)
                return;
            ObjectMapper mapper = new ObjectMapper();
            // Process API response
            for (StationResponse record : response) {
                ProducerRecord<String, String> data;
                try {
                    KafkaStationRecord newRecord = new KafkaStationRecord(
                            record.getStationName(),
                            Integer.parseInt(record.getRecordMonth()),
                            Integer.parseInt(record.getRecordHour()),
                            Integer.parseInt(record.getRecordDayofweek()) + 1,
                            Integer.parseInt(record.getDocksInService()),
                            Integer.parseInt(record.getAvailableDocks()),
                            Integer.parseInt(record.getAvailableBikes()),
                            Integer.parseInt(record.getPercentFull()));
                    data = new ProducerRecord<String, String> (
                            TOPIC,
                            mapper.writeValueAsString(newRecord));
                    producer.send(data);
                } catch (JsonProcessingException e) {
                    // System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    static String bootstrapServers = new String("localhost:9092");

    public static void main(String[] args) {
        if(args.length > 0)  // run on the cluster with a different kafka
            bootstrapServers = args[0];
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Task(), 0, 60*1000);
    }
}