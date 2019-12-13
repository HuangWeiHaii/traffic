package com.hwh.traffic.apiEntity;

/**
 * @author 黄威海
 * @description 实时公交API构造
 */
public class BusApi {

    private String city = "350100";
    private String userLat= "26.074268";
    private String userLng = "119.296389";
    private String stopId = "111";//奥体路公交总站
    private String h5Platform = "7";
    private String routeId;
    private String appSource = "com.ibuscloud.fuzhou";
    private String token = "A90D8DAFD7A63935BC56F082D31995777BC5AA9C5B5E33C4BCF5401055D599B47E28C0F77CAECA9C39F41B2BC7AFA279";
    private String radius = "1000";
    private String lat = "26.074268";
    private String lng = "119.296389";
    private String uuid = "fbdb87b5-1ea9-3259-87f3-69ceeefdde31";

    public BusApi(String routeId) {
        this.routeId = routeId;
    }

    public BusApi(String routeId,String stopId){
        this.routeId = routeId;
        this.stopId = stopId;
    }

    @Override
    public String toString() {
        String param = "";
        param = "city="+city+"&routeId="+routeId+"&h5Platform="+h5Platform+
                "&userLat=" + userLat + "&userLng=" + userLng + "&stopId=" + stopId +
                "&appSource=" + appSource +"&token=" +token+"&radius=" + radius +
                "&lat=" + lat + "&lng=" + lng + "&uuid=" + uuid;
        return param;
    }

}
