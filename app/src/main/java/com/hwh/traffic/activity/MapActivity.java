package com.hwh.traffic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapCustomStyleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.hwh.traffic.MapApplication;
import com.hwh.traffic.R;
import com.hwh.traffic.busEntity.BusDomJson;
import com.hwh.traffic.busEntity.Buses;
import com.hwh.traffic.busEntity.NextBuses;
import com.hwh.traffic.utils.BusLineOverlay;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author 黄威海
 * @date 2019年12月10日16:57:22
 * @description 百度地图 Activity
 */
public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button bt;
    private Button button;
    private Button buttons;
    private LatLng latLng;
    private MapApplication mapApplication;
    private BusLineSearch busLineSearch;
    private BusLineResult mBusLineResult = null; // 保存驾车/步行路线数据的变量，供浏览节点时使用
    private BusLineOverlay mBusLineOverlay; // 公交路线绘制对象
    private String bus_api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
        initMap();

        String neary_stop = getIntent().getStringExtra("BUS_NEXT");
        mBusLineOverlay = new BusLineOverlay(mBaiduMap, neary_stop);
        String bus_line_uid = getIntent().getStringExtra("BUS_LINE_UID");
        bus_api = getIntent().getStringExtra("BUS_API");
        System.out.println("map --------" + bus_line_uid);
        busLineSearch = BusLineSearch.newInstance();

        //确认公交POI UID 的正确方向
        busLineSearch.setOnGetBusLineSearchResultListener(new OnGetBusLineSearchResultListener() {
            @Override
            public void onGetBusLineResult(BusLineResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                mBaiduMap.clear();
                mBusLineResult = result;
                mBusLineOverlay.removeFromMap();
                mBusLineOverlay.setData(result);
                mBusLineOverlay.addToMap();
                new Thread(() -> {
                    int temp = 1;
                    while (true) {
                        if (mBaiduMap != null) {
                            NextBuses nextBuses = mBusLineOverlay.updateBusImg(bus_api);
                            mBusLineOverlay.addToMap();
                            for (Buses bus : nextBuses.getBuses()) {
                                LatLng latLng = new LatLng(bus.getLat(), bus.getLng());
                                //mBaiduMap.clear();
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromAssetWithDpi("behind_bus.png");
                                OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmapDescriptor).anchor(0.5f, 0.5f);
                                mBaiduMap.addOverlay(option);
                            }
                            if (nextBuses.getBuses().size() != 0) {
                                Buses bus = nextBuses.getBuses().get(0);
                                LatLng latLng = new LatLng(bus.getLat(), bus.getLng());
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromAssetWithDpi("next_bus.png");
                                OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmapDescriptor).zIndex(10).anchor(0.5f, 0.5f);
                                mBaiduMap.addOverlay(option);
                            }
                            try {
                                if (temp == 1) {
                                    mBusLineOverlay.zoomToSpan();
                                    temp++;
                                }
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                mBusLineOverlay.zoomToSpan();
            }
        });
        busLineSearch.searchBusLine((new BusLineSearchOption()
                .city("福州") // 设置查询城市
                .uid(bus_line_uid)));// 设置公交路线uid

    }


    private void initMap() {
        //获取地图控件引用
        mMapView.setMapCustomStyleEnable(true);
        MapCustomStyleOptions mapCustomStyleOptions = new MapCustomStyleOptions();
        mapCustomStyleOptions.customStyleId("729d1b655dea5a678081468b99c1293e");
        mMapView.setMapCustomStyle(mapCustomStyleOptions, new MapView.CustomMapStyleCallBack() {
            @Override
            public boolean onPreLoadLastCustomMapStyle(String s) {
                return false;
            }

            @Override
            public boolean onCustomMapStyleLoadSuccess(boolean b, String s) {
                return false;
            }

            @Override
            public boolean onCustomMapStyleLoadFailed(int i, String s, String s1) {
                return false;
            }
        });
        mMapView.setMapCustomStyleEnable(true);
        mBaiduMap = mMapView.getMap();


        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setBuildingsEnabled(false);
        mBaiduMap.setCompassEnable(true);


        mapApplication = (MapApplication) getApplication();

        //通过异步执行,防止线程阻塞 因为Application会等待Activity的onCreate执行完后才开始定位
        new Thread() {
            @Override
            public void run() {
                while (mapApplication.getLatLng() == null) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("正在定位");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("定位成功！！");
                latLng = mapApplication.getLatLng();
                BDLocation bdLocation = mapApplication.getBdLocation();
//                LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                System.out.println("获取BDlcation成功" + latLng.toString());
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);

                //设置地图缩放至定位点
                //MapStatus.Builder builder = new MapStatus.Builder();
                //builder.target(latLng).zoom(21.0f);
                //mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            }
        }.start();
//
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(this);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        buttons = (Button) findViewById(R.id.buttons);
        buttons.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        //mBaiduMap = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                //把定位点再次显现出来
                System.out.println("复位:" + latLng.toString());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                break;
            case R.id.button:
                //卫星地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                //mBaiduMap.setMyLocationEnabled(true);
                //mBaiduMap.setTrafficEnabled(true);

                break;
            case R.id.buttons:
                //普通地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
        }
    }

}
