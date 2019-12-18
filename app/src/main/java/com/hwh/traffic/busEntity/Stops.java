
package com.hwh.traffic.busEntity;
import java.io.Serializable;
import java.util.List;

public class Stops  implements Serializable {

    private RouteStop routeStop;
    private List<Buses> buses;
    public void setRouteStop(RouteStop routeStop) {
         this.routeStop = routeStop;
     }
     public RouteStop getRouteStop() {
         return routeStop;
     }

    public List<Buses> getBuses() {
        return buses;
    }

    public void setBuses(List<Buses> buses) {
        this.buses = buses;
    }
}