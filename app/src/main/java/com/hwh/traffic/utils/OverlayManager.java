package com.hwh.traffic.utils;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnPolylineClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwh.traffic.busEntity.BusDomJson;
import com.hwh.traffic.busEntity.Buses;
import com.hwh.traffic.busEntity.NextBuses;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;

/**
 * 该类提供一个能够显示和管理多个Overlay的基类
 * <p>
 * 复写{@link #getOverlayOptions()} 设置欲显示和管理的Overlay列表
 * </p>
 * <p>
 * 通过
 * {@link com.baidu.mapapi.map.BaiduMap#setOnMarkerClickListener(com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener)}
 * 将覆盖物点击事件传递给OverlayManager后，OverlayManager才能响应点击事件。
 * <p>
 * 复写{@link #onMarkerClick(com.baidu.mapapi.map.Marker)} 处理Marker点击事件
 * </p>
 */
public abstract class OverlayManager implements OnMarkerClickListener, OnPolylineClickListener {

    BaiduMap mBaiduMap = null;
    private List<OverlayOptions> mOverlayOptionList = null;
    private static ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    List<Overlay> mOverlayList = null;
    String neary_stop = null;
    private BusDomJson busInfo;
    private NextBuses nextBuses;
    private List<OverlayOptions> bus_option = new ArrayList<>();

    /**
     * 通过一个BaiduMap 对象构造
     *
     * @param baiduMap
     */
    public OverlayManager(BaiduMap baiduMap, String stopName) {
        mBaiduMap = baiduMap;
        neary_stop = stopName;
        // mBaiduMap.setOnMarkerClickListener(this);
        if (mOverlayOptionList == null) {
            mOverlayOptionList = new ArrayList<OverlayOptions>();
        }
        if (mOverlayList == null) {
            mOverlayList = new ArrayList<Overlay>();
        }
    }


    /**
     * 覆写此方法设置要管理的Overlay列表
     *
     * @return 管理的Overlay列表
     */
    public abstract List<OverlayOptions> getOverlayOptions();

    /**
     * 将所有Overlay 添加到地图上
     */
    public final void addToMap() {

        if (mBaiduMap == null) {
            return;
        }
        try {
            mBaiduMap.clear();
            removeFromMap();
            List<OverlayOptions> overlayOptions = getOverlayOptions();
            if (overlayOptions != null) {
                mOverlayOptionList.addAll(getOverlayOptions());
            }

            for (OverlayOptions option : mOverlayOptionList) {
                mOverlayList.add(mBaiduMap.addOverlay(option));
            }
            for (OverlayOptions options : bus_option) {
                mBaiduMap.addOverlay(options);

            }
            bus_option.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public final NextBuses updateBusImg(String bus_api) {

        String str_businfo;
        try {
            str_businfo = HttpUtil.httpGet(bus_api);
            busInfo = MAPPER.readValue(str_businfo, BusDomJson.class);
            nextBuses = busInfo.getItems().get(0).getRoutes().get(0).getNextBuses();
            return nextBuses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 将所有Overlay 从 地图上消除
     */
    public final void removeFromMap() {
        if (mBaiduMap == null) {
            return;
        }
        for (Overlay marker : mOverlayList) {
            marker.remove();
        }
        mOverlayOptionList.clear();
        mOverlayList.clear();

    }


    /**
     * 设置显示在规定宽高中的地图地理范围
     */
    public void zoomToSpanPaddingBounds(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
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

            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build(), paddingLeft, paddingTop, paddingRight, paddingBottom));
        }
    }

}
