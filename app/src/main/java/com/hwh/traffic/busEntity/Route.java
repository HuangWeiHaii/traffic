
package com.hwh.traffic.busEntity;

import java.io.Serializable;

public class Route implements Serializable {

    private long routeId;
    private long oppositeId;
    private String routeName;
    private long oppositeAmapId;
    private String amapName;
    private String cityCode;
    private String cityName;
    private int direction;
    private String origin;
    private String terminal;
    private int airPrice;
    private String carfare;
    private String firstBus;
    private String lastBus;
    private int stationCnt;
    private double distance;
    private boolean regular;
    private boolean hasGps;
    private boolean favorite;
    private long routeNo;
    public void setRouteId(long routeId) {
         this.routeId = routeId;
     }
     public long getRouteId() {
         return routeId;
     }

    public void setOppositeId(long oppositeId) {
         this.oppositeId = oppositeId;
     }
     public long getOppositeId() {
         return oppositeId;
     }

    public void setRouteName(String routeName) {
         this.routeName = routeName;
     }
     public String getRouteName() {
         return routeName;
     }

    public void setOppositeAmapId(long oppositeAmapId) {
         this.oppositeAmapId = oppositeAmapId;
     }
     public long getOppositeAmapId() {
         return oppositeAmapId;
     }

    public void setAmapName(String amapName) {
         this.amapName = amapName;
     }
     public String getAmapName() {
         return amapName;
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

    public void setDirection(int direction) {
         this.direction = direction;
     }
     public int getDirection() {
         return direction;
     }

    public void setOrigin(String origin) {
         this.origin = origin;
     }
     public String getOrigin() {
         return origin;
     }

    public void setTerminal(String terminal) {
         this.terminal = terminal;
     }
     public String getTerminal() {
         return terminal;
     }

    public void setAirPrice(int airPrice) {
         this.airPrice = airPrice;
     }
     public int getAirPrice() {
         return airPrice;
     }

    public void setCarfare(String carfare) {
         this.carfare = carfare;
     }
     public String getCarfare() {
         return carfare;
     }

    public void setFirstBus(String firstBus) {
         this.firstBus = firstBus;
     }
     public String getFirstBus() {
         return firstBus;
     }

    public void setLastBus(String lastBus) {
         this.lastBus = lastBus;
     }
     public String getLastBus() {
         return lastBus;
     }

    public void setStationCnt(int stationCnt) {
         this.stationCnt = stationCnt;
     }
     public int getStationCnt() {
         return stationCnt;
     }

    public void setDistance(double distance) {
         this.distance = distance;
     }
     public double getDistance() {
         return distance;
     }

    public void setRegular(boolean regular) {
         this.regular = regular;
     }
     public boolean getRegular() {
         return regular;
     }

    public void setHasGps(boolean hasGps) {
         this.hasGps = hasGps;
     }
     public boolean getHasGps() {
         return hasGps;
     }

    public void setFavorite(boolean favorite) {
         this.favorite = favorite;
     }
     public boolean getFavorite() {
         return favorite;
     }

    public void setRouteNo(long routeNo) {
         this.routeNo = routeNo;
     }
     public long getRouteNo() {
         return routeNo;
     }

}