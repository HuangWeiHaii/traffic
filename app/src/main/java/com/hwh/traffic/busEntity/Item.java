
package com.hwh.traffic.busEntity;
import java.io.Serializable;
import java.util.List;


public class Item implements Serializable {

    private long routeId;
    private NextBuses nextBuses;
    private List<Stops> stops;
    private String name;
    private List<Routes> routes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }

    public void setRouteId(long routeId) {
         this.routeId = routeId;
     }
     public long getRouteId() {
         return routeId;
     }

    public void setNextBuses(NextBuses nextBuses) {
         this.nextBuses = nextBuses;
     }
     public NextBuses getNextBuses() {
         return nextBuses;
     }

    public void setStops(List<Stops> stops) {
         this.stops = stops;
     }
     public List<Stops> getStops() {
         return stops;
     }

}