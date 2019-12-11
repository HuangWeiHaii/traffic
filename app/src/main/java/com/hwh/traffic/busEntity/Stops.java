
package com.hwh.traffic.busEntity;
import java.util.List;

public class Stops {

    private long routeId;
    private long stopId;
    private String stopName;
    private int seqNo;
    private int distance;
    private List<Buses> buses;
    public void setRouteId(long routeId) {
         this.routeId = routeId;
     }
     public long getRouteId() {
         return routeId;
     }

    public void setStopId(long stopId) {
         this.stopId = stopId;
     }
     public long getStopId() {
         return stopId;
     }

    public void setStopName(String stopName) {
         this.stopName = stopName;
     }
     public String getStopName() {
         return stopName;
     }

    public void setSeqNo(int seqNo) {
         this.seqNo = seqNo;
     }
     public int getSeqNo() {
         return seqNo;
     }

    public void setDistance(int distance) {
         this.distance = distance;
     }
     public int getDistance() {
         return distance;
     }

    public List<Buses> getBuses() {
        return buses;
    }

    public void setBuses(List<Buses> buses) {
        this.buses = buses;
    }
}