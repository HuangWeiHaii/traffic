package com.hwh.traffic.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.model.LatLng;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwh.traffic.MapApplication;
import com.hwh.traffic.busApiEntity.NewBusApi;
import com.hwh.traffic.R;
import com.hwh.traffic.busEntity.Record;
import com.hwh.traffic.db.TrafficLab;
import com.hwh.traffic.busEntity.BusDomJson;
import com.hwh.traffic.utils.ButtonUtil;
import com.hwh.traffic.utils.DensityUtil;
import com.hwh.traffic.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.util.List;

/**
 * @author 黄威海
 * @date 2019年12月10日16:56:09
 * @description 主页 Activity
 */
public class MainPageActivity extends AppCompatActivity {

    private BusDomJson busDomJson = null;
    private static ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private ImageView main_page_image;
    private ImageView main_like_image;
    private ImageView main_location_image;
    private ImageView main_user_image;
    private EditText main_page_edit;
    private Button main_page_search;
    private TextView main_page_text;
    private TextView main_like_text;
    private TextView main_location_text;
    private TextView main_user_text;
    private ImageView main_page_delete;
    private MapApplication mapApplication;
    private LatLng latLng;
    private String routeName;
    private Long route_id;  //正向路线ID
    private Long oppsite_id; //反向路线ID
    private String[] busApi; //实时公交API
    private TrafficLab trafficLab;
    private LinearLayout bus_search_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mapApplication = (MapApplication) getApplication();
        trafficLab = mapApplication.getTrafficLab();
        //更新 latlng 和 poiInfo 开启一条线程去执行 POI 搜索公交站
        initViews();
        updatePoiInfo();

    }

    /**
     * @param routeName 路线名称
     * @return 路线的正向ID和反向ID
     */
    public Long[] getRouteIdByRouteName(String routeName) {
        TrafficLab trafficLab = mapApplication.getTrafficLab();
        if (trafficLab != null) {
            Long[] routeIds = trafficLab.findRouteIdAndOppositeIdByRouteName(routeName);
            route_id = routeIds[0];
            System.out.println("正向路线ID" + route_id);
            oppsite_id = routeIds[1];
            System.out.println("反向路线ID" + oppsite_id);
        }
        return new Long[]{route_id, oppsite_id};

    }

    //后期打算将这个函数放进Application中 进行定期更新
    private void updatePoiInfo() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    while (mapApplication.getLatLng() == null) {
                        try {
                            Thread.sleep(500);
                            Log.d("百度地图定位MainPage", "正在定位");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("百度地图定位MainPage", "定位成功");
                    //这里对经纬度信息进行了更新
                    latLng = mapApplication.getLatLng();
                    try {
                        Thread.sleep(5000);
                        Log.d("百度地图定位MainPage", "更新latlng");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    /**
     * @param routeName
     * @return 获取实时公交的接口url
     */
    public String[] getBusApi(String routeName) {
        Long[] routeIds = getRouteIdByRouteName(routeName);
        Long routeId = routeIds[0];
        Long oppositeId = routeIds[1];
        String forw_busApi = "https://app.ibuscloud.com/v11/bus/getBusPositionByRouteId?" + new NewBusApi(String.valueOf(routeId), latLng).toString();
        String oppo_busApi = "https://app.ibuscloud.com/v11/bus/getBusPositionByRouteId?" + new NewBusApi(String.valueOf(oppositeId), latLng).toString();
        return new String[]{forw_busApi, oppo_busApi};

    }

    private void initViews() {
        main_page_image = findViewById(R.id.main_page_image);
        main_like_image = findViewById(R.id.main_like_image);
        main_location_image = findViewById(R.id.main_location_image);
        main_user_image = findViewById(R.id.main_user_image);

        main_page_text = findViewById(R.id.main_page_text);
        main_like_text = findViewById(R.id.main_like_text);
        main_location_text = findViewById(R.id.main_location_text);
        main_user_text = findViewById(R.id.main_user_text);

        main_page_edit = findViewById(R.id.main_page_edit);
        main_page_search = findViewById(R.id.main_page_search);

        main_page_search.setOnClickListener(v -> {

            if (ButtonUtil.isFastDoubleClick()) {
                return;
            }

            if (StringUtils.isEmpty(main_page_edit.getText().toString())) {
                Toast.makeText(MainPageActivity.this, "请输入路线", Toast.LENGTH_SHORT).show();
            } else {
                //updatePoiInfo();
                routeName = StringUtils.trim(main_page_edit.getText().toString());
                busApi = getBusApi(routeName);
                try {
                    //不可在主线程中使用HTTP请求 只能在异步请求
                    new Thread(()->{
                        try {
                            String busInfo = HttpUtil.httpGet(busApi[0]);
                            busDomJson = MAPPER.readValue(busInfo, BusDomJson.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();

                    while (busDomJson == null) {
                        Thread.sleep(200);
                        Log.d("MainpageActivity", "正在获取公交数据");
                    }
                    Log.d("MainpageActivity", "获取成功");
                    Intent intent = new Intent(MainPageActivity.this, BusInfoActivity.class);
                    intent.putExtra("BUS_INFO", busDomJson);
                    intent.putExtra("BUS_API", busApi);
                    intent.putExtra("ROUTE_NAME",routeName);

                    //将搜素记录插入SQLite
                    int index  = busDomJson.getItems().get(0).getRoutes().get(0).getStops().size() - 1;
                    String endName = busDomJson.getItems().get(0).getRoutes().get(0).getStops().get(index).getRouteStop().getStopName();
                    trafficLab.saveRecord(routeName,endName);
                    startActivity(intent);
                    showSearchList();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        main_page_image.setOnClickListener(v -> {
            main_page_image.setBackgroundResource(R.drawable.mainpage_select);
            main_like_image.setBackgroundResource(R.drawable.like);
            main_location_image.setBackgroundResource(R.drawable.location);
            main_user_image.setBackgroundResource(R.drawable.user);

            main_page_text.setTextColor(getResources().getColor(R.color.main_text_select));
            main_like_text.setTextColor(getResources().getColor(R.color.black));
            main_location_text.setTextColor(getResources().getColor(R.color.black));
            main_user_text.setTextColor(getResources().getColor(R.color.black));

        });

        main_like_image.setOnClickListener(v -> {
            main_like_image.setBackgroundResource(R.drawable.like_select);

            main_page_image.setBackgroundResource(R.drawable.mainpage);
            main_location_image.setBackgroundResource(R.drawable.location);
            main_user_image.setBackgroundResource(R.drawable.user);

            main_like_text.setTextColor(getResources().getColor(R.color.main_text_select));
            main_page_text.setTextColor(getResources().getColor(R.color.black));
            main_location_text.setTextColor(getResources().getColor(R.color.black));
            main_user_text.setTextColor(getResources().getColor(R.color.black));

        });

        main_location_image.setOnClickListener(v -> {
            main_location_image.setBackgroundResource(R.drawable.location_select);

            main_page_image.setBackgroundResource(R.drawable.mainpage);
            main_like_image.setBackgroundResource(R.drawable.like);
            main_user_image.setBackgroundResource(R.drawable.user);

            main_location_text.setTextColor(getResources().getColor(R.color.main_text_select));
            main_like_text.setTextColor(getResources().getColor(R.color.black));
            main_page_text.setTextColor(getResources().getColor(R.color.black));
            main_user_text.setTextColor(getResources().getColor(R.color.black));
        });

        main_user_image.setOnClickListener(v -> {
            main_user_image.setBackgroundResource(R.drawable.user_select);

            main_page_image.setBackgroundResource(R.drawable.mainpage);
            main_like_image.setBackgroundResource(R.drawable.like);
            main_location_image.setBackgroundResource(R.drawable.location);

            main_user_text.setTextColor(getResources().getColor(R.color.main_text_select));
            main_like_text.setTextColor(getResources().getColor(R.color.black));
            main_location_text.setTextColor(getResources().getColor(R.color.black));
            main_page_text.setTextColor(getResources().getColor(R.color.black));
        });


        showSearchList();

    }

    private void showSearchList() {
        bus_search_list = findViewById(R.id.main_page_searchlist);
        bus_search_list.removeAllViews();
        List<Record> recordList = trafficLab.findAllRecord();
        for (Record record : recordList) {
            //渲染搜索历史
            LinearLayout mLinearLayout = new LinearLayout(getApplicationContext());
            ImageView delete_img = new ImageView(getApplicationContext());
            TextView search_text = new TextView(getApplicationContext());

            bus_search_list.addView(mLinearLayout);
            mLinearLayout.addView(search_text);
            mLinearLayout.addView(delete_img);

            ViewGroup.LayoutParams lp_LinearLayout = mLinearLayout.getLayoutParams();
            lp_LinearLayout.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_LinearLayout.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLinearLayout.setGravity(Gravity.CENTER);
            mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            mLinearLayout.setLayoutParams(lp_LinearLayout);

            LinearLayout.LayoutParams lp_TextView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            lp_TextView.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp_TextView.width = DensityUtil.dip2px(getApplicationContext(), 340);
            int dp_20= DensityUtil.dip2px(getApplicationContext(), 20);
            int dp_10= DensityUtil.dip2px(getApplicationContext(), 10);
            search_text.setTextSize(20);
            search_text.setPadding(dp_20,dp_10,0,dp_10);
            search_text.setTextColor(Color.BLACK);
            search_text.setText(record.getSearch_route()+"路  方向  "+record.getSearch_end());
            search_text.setOnClickListener(v -> {
                busApi = getBusApi(record.getSearch_route());
                try {
                    //不可在主线程中使用HTTP请求 只能在异步请求
                    new Thread(()->{
                        try {
                            String busInfo = HttpUtil.httpGet(busApi[0]);
                            busDomJson = MAPPER.readValue(busInfo, BusDomJson.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    while (busDomJson == null) {
                        Thread.sleep(200);
                        Log.d("MainpageActivity", "正在获取公交数据");
                    }
                    Log.d("MainpageActivity", "获取成功");
                    Intent intent = new Intent(MainPageActivity.this, BusInfoActivity.class);
                    intent.putExtra("BUS_INFO", busDomJson);
                    intent.putExtra("BUS_API", busApi);
                    intent.putExtra("ROUTE_NAME",record.getSearch_route());
                    startActivity(intent);
                    } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            search_text.setLayoutParams(lp_TextView);

            ViewGroup.LayoutParams lp_ImageView_point = delete_img.getLayoutParams();
            lp_ImageView_point.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp_ImageView_point.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            delete_img.setImageResource(R.drawable.delete);
            delete_img.setPadding(0,0,dp_10,0);
            delete_img.setOnClickListener(v -> {
                trafficLab.deleteRecord(record.getRecord_id());
                LinearLayout linearLayout = (LinearLayout) delete_img.getParent();
                linearLayout.setVisibility(View.GONE);
            });

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        busDomJson = null;
    }
}