package edu.uchicago.adamzhang22;

public class KafkaStationRecord {
    public KafkaStationRecord(String station_name, int record_month, int record_hour, int record_dayofweek, int docks_in_service, int available_docks, int available_bikes, int percent_full) {
        super();
        this.station_name = station_name;
        this.record_month = record_month;
        this.record_hour = record_hour;
        this.record_dayofweek = record_dayofweek;
        this.docks_in_service = docks_in_service;
        this.available_docks = available_docks;
        this.available_bikes = available_bikes;
        this.percent_full = percent_full;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public int getRecord_month() {
        return record_month;
    }

    public void setRecord_month(int record_month) {
        this.record_month = record_month;
    }

    public int getRecord_hour() {
        return record_hour;
    }

    public void setRecord_hour(int record_hour) {
        this.record_hour = record_hour;
    }

    public int getRecord_dayofweek() {
        return record_dayofweek;
    }

    public void setRecord_dayofweek(int record_dayofweek) {
        this.record_dayofweek = record_dayofweek;
    }

    public int getDocks_in_service() {
        return docks_in_service;
    }

    public void setDocks_in_service(int docks_in_service) {
        this.docks_in_service = docks_in_service;
    }

    public int getAvailable_docks() {
        return available_docks;
    }

    public void setAvailable_docks(int available_docks) {
        this.available_docks = available_docks;
    }

    public int getAvailable_bikes() {
        return available_bikes;
    }

    public void setAvailable_bikes(int available_bikes) {
        this.available_bikes = available_bikes;
    }

    public int getPercent_full() {
        return percent_full;
    }

    public void setPercent_full(int percent_full) {
        this.percent_full = percent_full;
    }

    String station_name;
    int record_month;
    int record_hour;
    int record_dayofweek;
    int docks_in_service;
    int available_docks;
    int available_bikes;
    int percent_full;
}

