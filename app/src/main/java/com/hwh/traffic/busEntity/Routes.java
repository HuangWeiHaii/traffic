/**
  * Copyright 2019 bejson.com 
  */
package com.hwh.traffic.busEntity;

/**
 * Auto-generated: 2019-10-12 15:29:46
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Routes {
    public boolean isRegular() {
        return regular;
    }

    public boolean isHasGps() {
        return hasGps;
    }

    public boolean isFavorite() {
        return favorite;
    }

    private long routeId;
    private long oppositeId;
    private String routeName;
    private long amapId;
    private String amapName;
    private String cityCode;
    private String cityName;
    private int direction;
    private String origin;
    private String terminal;
    private int airPrice;
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

    public void setAmapId(long amapId) {
         this.amapId = amapId;
     }
     public long getAmapId() {
         return amapId;
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