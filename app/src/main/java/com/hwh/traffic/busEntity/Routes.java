
package com.hwh.traffic.busEntity;
import java.io.Serializable;
import java.util.List;


public class Routes implements Serializable {

    private Route route;
    private NextBuses nextBuses;
    private List<Stops> stops;
    public void setRoute(Route route) {
         this.route = route;
     }
     public Route getRoute() {
         return route;
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