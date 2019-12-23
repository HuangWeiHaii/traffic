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
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.GCJ02);
        //初始化数据库 首次运行会自动创建数据库
        trafficLab = new TrafficLab(this);

        //开启定位
        locateUtil = new LocateUtil(getApplicationContext());

  /*      // 开启定位图层
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        //配置定位SDK参数
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        //开启定位
        mLocationClient.start();
        Log.d("百度地图定位application","Application开始定位");*/

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
