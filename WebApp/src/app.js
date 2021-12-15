"use strict";
const http = require("http");
let assert = require("assert");
const express = require("express");
const app = express();
const mustache = require("mustache");
const filesystem = require("fs");
const url = require("url");
const port = Number(process.argv[2]);

const hbase = require("hbase");
let hclient = hbase({ host: process.argv[3], port: Number(process.argv[4]) });

// aux maps for rendering mustache
const monthNames = [
  "-",
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];
const dayNames = [
  "-",
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday",
  "Sunday",
];

function counterToNumber(c) {
  return Number(Buffer.from(c).readBigInt64BE());
}

function rowToMap(row) {
  let stats = {};
  row.forEach(function (item) {
    stats[item["column"]] = counterToNumber(item["$"]);
  });
  return stats;
}

function removePrefix(text, prefix) {
  if (text.indexOf(prefix) != 0) {
    throw "missing prefix";
  }
  return text.substr(prefix.length);
}

// load all the stations when loading the landing page
app.use(express.static("public"));
app.get("/stations.html", function (req, res) {
  hclient.table("adamzhang22_stations_hbase").scan((error, value) => {
    let template = filesystem
      .readFileSync("stationDropdown.mustache")
      .toString();
    let html = mustache.render(template, {
      stations: value,
    });
    res.send(html);
  });
});

// do query specified by user and render result in mustache
app.get("/station_info.html", function (req, res) {
  // query to get the station usage statistics
  const queryKey =
    req.query["station"] +
    " -month: " +
    req.query["month"] +
    " -hour: " +
    req.query["hour"] +
    " -dayofweek: " +
    req.query["dayofweek"];
  console.log(queryKey);
  const stationQueryKey = req.query["station"];

  // query and function to get station trip statistics
  function groupByRank(stationQueryKey, cells) {
    let ranksInfo = { 1: [], 2: [], 3: [], 4: [], 5: [] };
    cells.forEach((cell) => {
      let rank = Number(removePrefix(cell["key"], stationQueryKey));
      ranksInfo[rank].push(cell);
    });
    let results = [];
    Object.keys(ranksInfo).forEach((key) => {
      let rankInfo = ranksInfo[key];
      let rankStats = { rank: key };
      rankInfo.forEach((item) => {
        if (item["column"] === "routes:to_station_name") {
          rankStats[item["column"]] = String(item["$"]);
        } else {
          rankStats[item["column"]] = Number(item["$"]);
        }
      });
      rankStats["routes:avgDuration"] = Number(
        rankStats["routes:trip_duration"] / rankStats["routes:total_trips"]
      ).toFixed(1);
      results.push(rankStats);
    });
    return results;
  }

  hclient
    .table("adamzhang22_stations_stats_hbase")
    .row(queryKey)
    .get(function (err, cells) {
      if (cells == null) {
        let template = filesystem.readFileSync("error.mustache").toString();
        let html = mustache.render(template);
        res.send(html);
      } else {
        let stationInfo = rowToMap(cells);
        function getAvg(info) {
          let keys = [
            "stations:available_bikes",
            "stations:available_docks",
            "stations:docks_in_service",
            "stations:percent_full",
          ];
          let avgs = {};
          if (info["stations:record_counts"] === 0) {
            keys.forEach((key) => {
              avgs[key] = "-";
            });
          } else {
            keys.forEach((key) => {
              avgs[key] = (info[key] / info["stations:record_counts"]).toFixed(
                1
              );
            });
          }
          return avgs;
        }
        let avgInfo = getAvg(stationInfo);
        console.log(avgInfo);
        hclient.table("adamzhang22_divvy_trips_by_route_top5_hbase").scan(
          {
            filter: {
              type: "PrefixFilter",
              value: stationQueryKey,
            },
            maxVersions: 1,
          },
          function (err, cells) {
            let topDests = groupByRank(stationQueryKey, cells);
            let template = filesystem
              .readFileSync("result.mustache")
              .toString();
            let html = mustache.render(template, {
              station_name: req.query["station"],
              month: monthNames[req.query["month"]],
              hour: req.query["hour"],
              dayofweek: dayNames[req.query["dayofweek"]],
              docks_in_service: avgInfo["stations:docks_in_service"],
              available_bikes: avgInfo["stations:available_bikes"],
              available_docks: avgInfo["stations:available_docks"],
              percent_full: avgInfo["stations:percent_full"],
              top_dests: topDests,
            });
            res.send(html);
          }
        );
      }
    });
});

app.listen(port);
