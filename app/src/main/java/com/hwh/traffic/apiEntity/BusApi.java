package com.hwh.traffic.apiEntity;

/**
 * @author 黄威海
 * @description 实时公交API构造
 */
public class BusApi {

    private String city = "350100";
    private String stopId;//奥体路公交总站
    private String h5Platform = "7";
    private String routeId;
    private String appSource = "com.ibuscloud.fuzhou";
    private String radius = "1000";


    public BusApi(String routeId,String stopId){
        this.routeId = routeId;
        this.stopId = stopId;
    }

    @Override
    public String toString() {
        String param = "";
        param = "city="+city+"&routeId="+routeId+"&h5Platform="+h5Platform+ "&stopId=" + stopId + "&appSource=" + appSource +"&radius=" + radius;
        return param;
    }

}
