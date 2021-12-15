# Big-Data-Divvy
Big Data webapp using Chicago divvy bike stations data

# Application Overview
This is a big data webapp that tracks the usage statistics of Divvy bike stations in Chicago. 
It shows historical Divvy bike stations usage statistics (availability of docks and bikes at a particular hour of the day, month of the year, and day of the week) and the most frequent destinations from each station.

http://adamzhang22-final-lb-391449449.us-east-2.elb.amazonaws.com/stations.html

# Data

This application ingests and processes Divvy bike stations and trips data from City of Chicago Data Portal to show users the usage statistics of 
each Divvy bike stations since 2016. The datasets used are :

- Divvy Bicycle Stations - Historical (https://data.cityofchicago.org/Transportation/Divvy-Bicycle-Stations-Historical/eq45- 8inv)
- Divvy Trips (https://data.cityofchicago.org/Transportation/Divvy-Trips/fg6s-gzvg)
- Divvy Bicycle Stations - Historical is updated hourly, so the speed layer will stream newest data each hour and update views in serving layer stored in HBase. Details will be provided in the later sections

# Structure

The project adheres to the Lambda Architecture (https://en.wikipedia.org/wiki/Lambda_architecture) which consists of 3 layers:

- Batch layer - Manages and stores raw data on HDFS and precomputes results using a distributed processing system such as Spark to handle large volume data
- Serving layer - Where precomputed views from Batch layer are indexed so that to serve ad-hoc queries with low-latency
- Speed layer - Streams real-time, up-to-date data to complement the information in Batch Layer

The project also has a web application which gives users a graphical interface to make queries and receive query results.

### Batch Layer

The batch layer ingests raw data and stores them on HDFS hosted on AWS EMR cluster. Raw data from csv files are stored on Hive in ORC format.
Views including historical average station usage statistics by time of the day and year are computed in Spark. 

### Serving Layer

The serving layer takes pre-computed views from Batch layer and puts them on HBase for low-latency, ad-hoc queries.

### Speed Layer

The speed layer uses Kafka to ingest real time data from Socrata Open Data API from City of Chicago portal and make incremental udpates to HBase table each hour to reflect the newest station usage statistics.

### Webapp

The webapp is built using Node.js and renders results of queries dynamically using mustache templates

# Demo

![Alt Text](https://github.com/adamzhang822/Big-Data-Divvy/blob/main/ezgif.com-gif-maker.gif)



