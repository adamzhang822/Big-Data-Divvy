package edu.uchicago.adamzhang22;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "station_name",
        "record_month",
        "record_hour",
        "record_dayofweek",
        "docks_in_service",
        "available_docks",
        "available_bikes",
        "percent_full"
})
@Generated("jsonschema2pojo")
public class StationResponse {

    @JsonProperty("station_name")
    private String stationName;
    @JsonProperty("record_month")
    private String recordMonth;
    @JsonProperty("record_hour")
    private String recordHour;
    @JsonProperty("record_dayofweek")
    private String recordDayofweek;
    @JsonProperty("docks_in_service")
    private String docksInService;
    @JsonProperty("available_docks")
    private String availableDocks;
    @JsonProperty("available_bikes")
    private String availableBikes;
    @JsonProperty("percent_full")
    private String percentFull;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("station_name")
    public String getStationName() {
        return stationName;
    }

    @JsonProperty("station_name")
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @JsonProperty("record_month")
    public String getRecordMonth() {
        return recordMonth;
    }

    @JsonProperty("record_month")
    public void setRecordMonth(String recordMonth) {
        this.recordMonth = recordMonth;
    }

    @JsonProperty("record_hour")
    public String getRecordHour() {
        return recordHour;
    }

    @JsonProperty("record_hour")
    public void setRecordHour(String recordHour) {
        this.recordHour = recordHour;
    }

    @JsonProperty("record_dayofweek")
    public String getRecordDayofweek() {
        return recordDayofweek;
    }

    @JsonProperty("record_dayofweek")
    public void setRecordDayofweek(String recordDayofweek) {
        this.recordDayofweek = recordDayofweek;
    }

    @JsonProperty("docks_in_service")
    public String getDocksInService() {
        return docksInService;
    }

    @JsonProperty("docks_in_service")
    public void setDocksInService(String docksInService) {
        this.docksInService = docksInService;
    }

    @JsonProperty("available_docks")
    public String getAvailableDocks() {
        return availableDocks;
    }

    @JsonProperty("available_docks")
    public void setAvailableDocks(String availableDocks) {
        this.availableDocks = availableDocks;
    }

    @JsonProperty("available_bikes")
    public String getAvailableBikes() {
        return availableBikes;
    }

    @JsonProperty("available_bikes")
    public void setAvailableBikes(String availableBikes) {
        this.availableBikes = availableBikes;
    }

    @JsonProperty("percent_full")
    public String getPercentFull() {
        return percentFull;
    }

    @JsonProperty("percent_full")
    public void setPercentFull(String percentFull) {
        this.percentFull = percentFull;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}