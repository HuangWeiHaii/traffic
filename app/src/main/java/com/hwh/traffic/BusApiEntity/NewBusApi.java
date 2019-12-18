package com.hwh.traffic.BusApiEntity;

import com.baidu.mapapi.model.LatLng;

public class NewBusApi {

    private String city = "350100";
    private String h5Platform = "7";
    private String routeId;
    private String radius = "1000";
    private String userLat;
    private String userLng;
    private String lat;
    private String lng;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getH5Platform() {
        return h5Platform;
    }

    public void setH5Platform(String h5Platform) {
        this.h5Platform = h5Platform;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserLng() {
        return userLng;
    }

    public void setUserLng(String userLng) {
        this.userLng = userLng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public NewBusApi(String routeId, LatLng latLng){
        this.routeId = routeId;
        this.lat = String.valueOf(latLng.latitude);
        this.lng = String.valueOf(latLng.longitude);
        this.userLat = String.valueOf(latLng.latitude);
        this.userLng = String.valueOf(latLng.longitude);

    }

    @Override
    public String toString() {
        String param = "";
        param = "city="+city+"&routeId="+routeId+"&h5Platform="+h5Platform +"&radius=" + radius +"&userLat=" + userLat +"&userLng=" + userLng  +"&lat=" + lat  +"&lng=" + lng;
        return param;
    }
}

