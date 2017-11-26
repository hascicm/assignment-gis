# Overview

This application let user choose right trail for they're trip. Application can show trails based on user input. 
User can choose one of the following features:
- main search is based on user preferences, user can choose which type of natural sights he want to see from trail, choose which general area he is interested in and desired trail difficulty.
- show all trails in TANAP
- color code trails by their length
- show trails and points of interest in specified location
- amenities in specified location

This is it in action(to be edited):

![Screenshot](screenshot.png)

The application has 2 separate parts, the client which is a [frontend web application](#frontend) using mapbox API and mapbox.js and the [backend application](#backend) written in [Java](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html), backed by PostGIS. The frontend application communicates with backend using a [REST API](#api).

# Frontend

The frontend application is a static HTML page (`index.html`), which shows a mapbox.js widget. It is displaying trails, which are mostly in High Tatras, thus the map style is based on the outdoors style. 

All relevant frontend code is in `index.html` in style part of the document Styling is located in separate file navigation.css. Responsibilities of front-end are:
- displaying the sidebar panel with all features calling the appropriate backend APIs
- displaying geo features by overlaying the map with a geojson layer, the geojson is provided directly by backend APIs
- getting user defined position based on dropped pin on map

# Backend

The backend application is written in Java and is responsible for querying geo data, formatting the geojson and data for the sidebar panel.
It is implemented as web aplication using jetty server. interfaces are implemented as rest web services. To speed up development, project was created using maven with web aplication archtype.

## Data

Trail data is coming directly from Open Street Maps. I downloaded an extent covering whole middle Slovakia and imported it using the `osm2pgsql` tool into the standard OSM schema. All deployed interfaces are located in class `Resources.java`. Database connector is located in class `Connector.java`. All queries are constructed and executed from class `QueryManager.java`. 


## Api

**Find all trails in TANAP with color coding based on length**

`http://localhost:8080/PDT/querry2`

### Response

{"geometry":
{"coordinates":
[[20.324003495502,49.2249381078786],
[20.3231145226969,49.2255244927382],
[20.3212436014546,49.226760014246],
[20.3172388220865,49.2294943610653]],
"type":"LineString"},
"type":"Feature",
"properties":
{"name":"neznáme dĺžka: 706.80976418901696",
"stroke-width":"10",
"stroke":"#12ec00"}}

```
This is partioan snippet of full response with multiple results.
