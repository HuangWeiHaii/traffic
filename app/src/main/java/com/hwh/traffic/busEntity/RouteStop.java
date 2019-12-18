
package com.hwh.traffic.busEntity;

import java.io.Serializable;

public class RouteStop  implements Serializable {

    private long staId;
    private long routeId;
    private int seqNo;
    private int type;
    private int direction;
    private long stopId;
    private String stopName;
    private String cityCode;
    private String cityName;
    private double lng;
    private double lat;
    private boolean metroTrans;
    private boolean favorite;
    public void setStaId(long staId) {
         this.staId = staId;
     }
     public long getStaId() {
         return staId;
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

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

    public void setDirection(int direction) {
         this.direction = direction;
     }
     public int getDirection() {
         return direction;
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

    public void setCityCode(String cityCode) {
         this.cityCode = cityCode;
     }
     public String getCityCode() {
         return cityCode;
     }

    public void setCityName(String cityName) {
         this.cityName = cityName;
     }
     public String getCityName() {
         return cityName;
     }

    public void setLng(double lng) {
         this.lng = lng;
     }
     public double getLng() {
         return lng;
     }

    public void setLat(double lat) {
         this.lat = lat;
     }
     public double getLat() {
         return lat;
     }

    public void setMetroTrans(boolean metroTrans) {
         this.metroTrans = metroTrans;
     }
     public boolean getMetroTrans() {
         return metroTrans;
     }

    public void setFavorite(boolean favorite) {
         this.favorite = favorite;
     }
     public boolean getFavorite() {
         return favorite;
     }

}