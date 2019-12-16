
package com.hwh.traffic.busEntity;
import java.io.Serializable;
import java.util.List;

public class NextBuses implements Serializable {

    private long routeId;
    private int seqNo;
    private long stopId;
    private long amapRouteId;
    private String stopName;
    private List<Buses> buses;
    private String noBusDesc;

    public String getNoBusDesc() {
        return noBusDesc;
    }

    public void setNoBusDesc(String noBusDesc) {
        this.noBusDesc = noBusDesc;
    }

    public void setRouteId(long routeId) {
         this.routeId = routeId;
     }
     public long getRouteId() {
         return routeId;
     }

    public void setSeqNo(int seqNo) {
         this.seqNo = seqNo;
     }
     public int getSeqNo() {
         return seqNo;
     }

    public void setStopId(long stopId) {
         this.stopId = stopId;
     }
     public long getStopId() {
         return stopId;
     }

    public void setAmapRouteId(long amapRouteId) {
         this.amapRouteId = amapRouteId;
     }
     public long getAmapRouteId() {
         return amapRouteId;
     }

    public void setStopName(String stopName) {
         this.stopName = stopName;
     }
     public String getStopName() {
         return stopName;
     }

    public void setBuses(List<Buses> buses) {
         this.buses = buses;
     }
     public List<Buses> getBuses() {
         return buses;
     }

}