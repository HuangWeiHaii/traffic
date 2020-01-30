package com.hwh.traffic.utils;

import android.graphics.Color;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.busline.BusLineResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于显示一条公交详情结果的Overlay
 */
public class BusLineOverlay extends OverlayManager {

    private BusLineResult mBusLineResult = null;
    private LatLng latLng = null;


    /**
     * 构造函数BusLineResult
     *
     * @param baiduMap 该BusLineOverlay所引用的 BaiduMap 对象
     */
    public BusLineOverlay(BaiduMap baiduMap, String stopName) {
        super(baiduMap, stopName);
    }

    /**
     * 设置公交线数据
     *
     * @param result 公交线路结果数据
     */
    public void setData(BusLineResult result) {
        this.mBusLineResult = result;
    }

    @Override
    public final List<OverlayOptions> getOverlayOptions() {

        if (mBusLineResult == null || mBusLineResult.getStations() == null) {
            return null;
        }
        List<OverlayOptions> overlayOptionses = new ArrayList<OverlayOptions>();
        for (BusLineResult.BusStation station : mBusLineResult.getStations()) {
            if (neary_stop.contains(station.getTitle())) {
                //读取最近站点的位置
                latLng = station.getLocation();
                //System.out.println("查询到地址 " + latLng.toString());
            }
            //System.out.println(neary_stop);
            //System.out.println("station title = " + station.getTitle());
            //System.out.println("station location = " + station.getLocation().toString());
            overlayOptionses.add(new MarkerOptions()
                    .position(station.getLocation())
                    .zIndex(10)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromAssetWithDpi("bus_station.png")));
        }
        overlayOptionses.add(new MarkerOptions()
                .position(latLng)
                .zIndex(10)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromAssetWithDpi("bus_station_nearby.png")));

        List<LatLng> points = new ArrayList<LatLng>();
        for (BusLineResult.BusStep step : mBusLineResult.getSteps()) {
            if (step.getWayPoints() != null) {
                points.addAll(step.getWayPoints());
            }
        }
        if (points.size() > 0) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromAssetWithDpi("Icon_road_green_arrow.png");
            overlayOptionses
                    .add(new PolylineOptions()
                            .width(16)
                            //.color(Color.argb(178, 0, 255, 0))
                            .zIndex(0)
                            .visible(true)
                            .points(points)
                            .customTexture(bitmapDescriptor)
                    );
        }
        return overlayOptionses;
    }


    /**
     * 缩放地图，使所有Overlay都在合适的视野内
     * <p>
     * 注： 该方法只对Marker类型的overlay有效
     * </p>
     */
    public void zoomToSpan() {
        if (mBaiduMap == null) {
            return;
        }
        if (mOverlayList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : mOverlayList) {
                // polyline 中的点可能太多，只按marker 缩放
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            MapStatus mapStatus = mBaiduMap.getMapStatus();
            if (null != mapStatus) {
                //int width = mapStatus.winRound.right - mBaiduMap.getMapStatus().winRound.left - 400;
                //int height = mapStatus.winRound.bottom - mBaiduMap.getMapStatus().winRound.top - 400;
                //mBaiduMap.setMapStatus(MapStatusUpdateFactory
                //        .newLatLngBounds(builder.build(), width, height));

                MapStatus.Builder build = new MapStatus.Builder();
                build.target(latLng).zoom(21.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(build.build()));
            }

        }
    }

    /**
     * 覆写此方法以改变默认点击行为
     *
     * @param index 被点击的站点在
     *              {@link com.baidu.mapapi.search.busline.BusLineResult#getStations()}
     *              中的索引
     * @return 是否处理了该点击事件
     */
    public boolean onBusStationClick(int index) {
        if (mBusLineResult.getStations() != null
                && mBusLineResult.getStations().get(index) != null) {
            Log.i("baidumapsdk", "BusLineOverlay onBusStationClick");
        }
        return false;
    }

    public final boolean onMarkerClick(Marker marker) {
        if (mOverlayList != null && mOverlayList.contains(marker)) {
            return onBusStationClick(mOverlayList.indexOf(marker));
        } else {
            return false;
        }

    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        // TODO Auto-generated method stub
        return false;
    }
}
