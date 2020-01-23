package com.hwh.traffic;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.hwh.traffic.db.TrafficLab;
import com.hwh.traffic.baiduLocation.LocateUtil;


/**
 * @author 黄威海
 * @date 2019年12月10日16:58:08
 * @description 百度地图初始化坐标
 */
public class MapApplication extends Application {

    public TrafficLab trafficLab = null;
    private LocateUtil locateUtil = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库 首次运行会自动创建数据库
        trafficLab = new TrafficLab(this);
        //开启定位
        locateUtil = new LocateUtil(this);

    }

    public TrafficLab getTrafficLab() {
        return trafficLab;
    }

    public LatLng getLatLng() {
        return locateUtil.getLatLng();
    }

    public BDLocation getBdLocation() {
        return locateUtil.getBdLocation();
    }



}
