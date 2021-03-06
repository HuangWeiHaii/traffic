package com.hwh.traffic.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwh.traffic.R;
import com.hwh.traffic.busEntity.BusDomJson;
import com.hwh.traffic.busEntity.Buses;
import com.hwh.traffic.busEntity.NextBuses;
import com.hwh.traffic.busEntity.Stops;
import com.hwh.traffic.utils.ButtonUtil;
import com.hwh.traffic.utils.DensityUtil;
import com.hwh.traffic.utils.HttpUtil;
import com.hwh.traffic.utils.TimeUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BusInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout bus_info_list;
    private List<Integer> linearLayout_list = new ArrayList<>();
    private List<Integer> imageView_point_list = new ArrayList<>();
    private List<Integer> imageView_bus_list = new ArrayList<>();
    private List<Integer> textView_list = new ArrayList<>();
    private BusDomJson busInfo = null;
    private String busApi[] = null;
    private static ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private String oppsite_api;
    private String forw_api;
    private String forw_uid;
    private String oppsite_uid;
    private boolean isForw = true;
    //构造一个 存储实时公交index的集合
    List<Integer> indexList = new ArrayList<>();
    //所有站点信息
    List<Stops> stopsList;
    private NextBuses nextBuses = null;
    //这是车辆距离信息 layout
    private LinearLayout bus_info_target;
    private ImageView flush_img;
    private ImageView bus_img_opposite;
    private ImageView bus_img_map;
    private String[] bus_uid = null;
    private BusLineSearch busLineSearch;
    private String now_bus_uid;
    private BusLineResult bus_line_result;
    private ArrayList<String> busUid;
    private PoiSearch mPoiSearch;
    private String now_bus_api;

    public void getBusUidByRouteName(String routeName) {
        new Thread(()->{
            busUid = new ArrayList<>();
            mPoiSearch = PoiSearch.newInstance();
            mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

                @Override
                public void onGetPoiResult(PoiResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        System.out.println("error");
                        return;
                    }
                    for (PoiInfo poi : result.getAllPoi()) {
                        System.out.println(" uid = " + poi.uid);
                        busUid.add(poi.uid);
                    }

                }

                @Override
                public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

                }

                @Override
                public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

                }

                @Override
                public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

                }
            });
            System.out.println(routeName);
            mPoiSearch.searchInCity(new PoiCitySearchOption().city("福州").keyword(routeName + "公交"));
            System.out.println("开始搜索");

            while (busUid.size() == 0){
                try {
                    Thread.sleep(200);
                    System.out.println("正在搜索" + routeName);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            bus_uid = new String[]{busUid.get(0),busUid.get(1)};
            getBusForw();
        }).start();





    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);

        busInfo = (BusDomJson) getIntent().getSerializableExtra("BUS_INFO");
        busApi = getIntent().getStringArrayExtra("BUS_API");
        String route_name = getIntent().getStringExtra("ROUTE_NAME");

        //POI搜索BUS路线的UID
        getBusUidByRouteName(route_name);
        //确认公交POI UID 的正确方向
        flush_img = findViewById(R.id.bus_img_flush);
        bus_img_opposite = findViewById(R.id.bus_img_opposite);
        bus_img_map = findViewById(R.id.bus_img_map);
        flush_img.setOnClickListener(v -> {
            if (ButtonUtil.isFastDoubleClick()) {
                return;
            }
            updataBusListInfo();
        });

        bus_img_opposite.setOnClickListener(v -> {
            if (ButtonUtil.isFastDoubleClick()) {
                return;
            }
            //反向
            oppositeBusListInfo();
        });
        bus_img_map.setOnClickListener(v -> {
            if (ButtonUtil.isFastDoubleClick()){
                return;
            }
            //转跳到地图
            toBusMapRouteActivity();
        });

        forw_api = busApi[0];
        now_bus_api = forw_api;
        oppsite_api = busApi[1];
        System.out.println(busInfo);
        System.out.println("正向API = " + forw_api);
        System.out.println("反向API = " + oppsite_api);

        //将实时公交信息渲染 并设置按钮监听
        getBusListUI(busInfo);

    }

    private void getBusForw() {
        busLineSearch = BusLineSearch.newInstance();
        busLineSearch.setOnGetBusLineSearchResultListener(new OnGetBusLineSearchResultListener() {
            @Override
            public void onGetBusLineResult(BusLineResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                bus_line_result = result;
                if (busInfo.getItems().get(0).getRoutes().get(0).getStops().get(0).getRouteStop().getStopName().contains(result.getStations().get(0).getTitle())) {
                    forw_uid = bus_uid[0];
                    oppsite_uid = bus_uid[1];
                    now_bus_uid = forw_uid;
                    System.out.println("正向 UID" + forw_uid);
                    System.out.println("反向 UID" + oppsite_uid);
                } else {
                    forw_uid = bus_uid[1];
                    oppsite_uid = bus_uid[0];
                    now_bus_uid = forw_uid;
                    System.out.println("正向 UID" + forw_uid);
                    System.out.println("反向 UID" + oppsite_uid);
                }

            }
        });
        busLineSearch.searchBusLine((new BusLineSearchOption()
                .city("福州") // 设置查询城市
                .uid(bus_uid[0])));// 设置公交路线uid
    }

    private void toBusMapRouteActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        if (bus_line_result != null){
            intent.putExtra("BUS_LINE_UID",now_bus_uid);
            intent.putExtra("BUS_NEXT",nextBuses.getStopName());
            intent.putExtra("BUS_API",now_bus_api);
            startActivity(intent);
        }

    }


    /**
     * @description 渲染实时公交信息 设置监听等等
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void getBusListUI(final BusDomJson busInfo) {

        bus_info_list = findViewById(R.id.bus_info_list);
        stopsList = busInfo.getItems().get(0).getRoutes().get(0).getStops();
        //获取 nextBuses 可获取当前站点名 与 所有公交所在站点信息 (新API)  用来确定距离与到达时间
        nextBuses = busInfo.getItems().get(0).getRoutes().get(0).getNextBuses();
        for (Stops stops : stopsList) {
            List<Buses> list = stops.getBuses();
            if (list != null && list.size() != 0) {
                indexList.add(stopsList.indexOf(stops)); //添加公交车在stopList中的索引值 到 indexList
            }
            //显示一条路线
            LinearLayout mLinearLayout = new LinearLayout(getApplicationContext());
            ImageView mImageView_point = new ImageView(getApplicationContext());
            ImageView imageView_small_bus = new ImageView(getApplicationContext());
            TextView mTextView_stop = new TextView(getApplicationContext());
            //设置10dp
            int dp_10 = DensityUtil.dip2px(getApplicationContext(), 10);

            //需要先addView
            bus_info_list.addView(mLinearLayout);
            mLinearLayout.addView(mImageView_point);
            mLinearLayout.addView(mTextView_stop);
            mLinearLayout.addView(imageView_small_bus);

            //设置LinearLayout 布局
            ViewGroup.LayoutParams lp_LinearLayout = mLinearLayout.getLayoutParams();
            lp_LinearLayout.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_LinearLayout.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            mLinearLayout.setId(View.generateViewId());
            int linearLayout_id = mLinearLayout.getId();
            linearLayout_list.add(linearLayout_id);
            mLinearLayout.setLayoutParams(lp_LinearLayout);


            //bus 站点名称点标记
            ViewGroup.LayoutParams lp_ImageView_point = mImageView_point.getLayoutParams();
            lp_ImageView_point.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp_ImageView_point.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mImageView_point.setImageResource(R.drawable.point);
            mImageView_point.setPadding(dp_10, dp_10, dp_10, dp_10);
            mImageView_point.setId(View.generateViewId());
            int imageView_point_id = mImageView_point.getId();
            imageView_point_list.add(imageView_point_id);
            mImageView_point.setLayoutParams(lp_ImageView_point);


            // bus 站点名称
            LinearLayout.LayoutParams lp_TextView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            lp_TextView.height = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_TextView.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mTextView_stop.setText(stops.getRouteStop().getStopName());
            mTextView_stop.setTextColor(getResources().getColor(R.color.black));
            //设置离我最近的站点为橘色
            if (stops.getRouteStop().getStopName().contains(nextBuses.getStopName())) {
                mTextView_stop.setTextColor(getResources().getColor(R.color.orange));
                mImageView_point.setImageResource(R.drawable.stop_select);
            }
            mTextView_stop.setTextSize(20);
            mTextView_stop.setPadding(dp_10, dp_10, dp_10, dp_10);
            mTextView_stop.setId(View.generateViewId());
            mTextView_stop.setOnClickListener(this);
            int textView_id = mTextView_stop.getId();
            textView_list.add(textView_id);//会留下一个索引 表示第几个被添加进来的TextView ID  等价于Stops 的索引
            mTextView_stop.setLayoutParams(lp_TextView);


            // bus 站点
            ViewGroup.LayoutParams lp_ImageView_bus = imageView_small_bus.getLayoutParams();
            lp_ImageView_bus.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp_ImageView_bus.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imageView_small_bus.setPadding(dp_10, dp_10, dp_10, dp_10);
            imageView_small_bus.setId(View.generateViewId());
            int imageView_bus_id = imageView_small_bus.getId();
            imageView_bus_list.add(imageView_bus_id);
            mImageView_point.setLayoutParams(lp_ImageView_bus);

        }

        //设置站点小公交UI图片
        for (Integer integer : indexList) {
            ImageView small_bus = findViewById(imageView_bus_list.get(integer));
            small_bus.setImageResource(R.drawable.small_bus);
        }

        //设置我的位置为橘色 (new Api)  通过 newBuses的seqNo定位索引

        //设置起点和终点的UI
        Integer start_id = imageView_point_list.get(0);
        Integer end_id = imageView_point_list.get(imageView_point_list.size() - 1);
        ImageView start_img = findViewById(start_id);
        ImageView end_img = findViewById(end_id);
        start_img.setImageResource(R.drawable.bus_start);
        end_img.setImageResource(R.drawable.bus_end);

        //设置距离几站UI提醒
        List<Buses> buses = nextBuses.getBuses();
        flushTargetInfo(buses);
    }


    private Map<String, Integer> targetIdMap = new HashMap<>();

    /**
     * @param buses
     * @description 更新距离几站UI
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void flushTargetInfo(List<Buses> buses) {


        //
        bus_info_target = findViewById(R.id.bus_info_target);
        int n = 0;

        if (buses.size() == 0) {
            //等待发车
            LinearLayout Linear_target = new LinearLayout(getApplicationContext());
            TextView textView_target = new TextView(getApplicationContext());
            bus_info_target.addView(Linear_target);
            Linear_target.addView(textView_target);

            ViewGroup.LayoutParams lp_textView_target = textView_target.getLayoutParams();
            lp_textView_target.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_textView_target.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            textView_target.setText("等待发车...");
            textView_target.setTextColor(getResources().getColor(R.color.black));
            textView_target.setGravity(Gravity.CENTER);
            textView_target.setTextSize(25);
            int dp_15 = DensityUtil.dip2px(getApplicationContext(), 15);
            textView_target.setPadding(dp_15, dp_15, dp_15, dp_15);
            textView_target.setBackgroundColor(getResources().getColor(R.color.white));
            textView_target.setLayoutParams(lp_textView_target);
            return;
        }

        for (Buses bus : buses) {
            String nextStation = bus.getNextStation();//公交车当前位置
            int targetStopCount = bus.getTargetStopCount();//距离当前位置几站
            int targetDistance = bus.getTargetDistance();//距离当前位置多少米
            int targetSeconds = bus.getTargetSeconds();//到达当前位置需要多少秒
            System.out.println("nextStation" + nextStation);
            System.out.println("targetStopCount" + targetStopCount);
            System.out.println("targetDistance" + targetDistance);
            System.out.println("targetSeconds" + targetSeconds);

            //定义视图
            LinearLayout Linear_target = new LinearLayout(getApplicationContext());
            TextView textView_target = new TextView(getApplicationContext());
            TextView textView_target2 = new TextView(getApplicationContext());

            //父视图添加子视图
            bus_info_target.addView(Linear_target);
            Linear_target.addView(textView_target);
            Linear_target.addView(textView_target2);


            //设置LinearLayout 布局
            LinearLayout.LayoutParams lp_LinearLayout_target = new LinearLayout.LayoutParams(Linear_target.getLayoutParams());
            lp_LinearLayout_target.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp_LinearLayout_target.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            if (n == 0) {
                int dp_50 = DensityUtil.dip2px(getApplicationContext(), 5);
                Linear_target.setPadding(0, 0, dp_50, 0);
            }
            Linear_target.setOrientation(LinearLayout.VERTICAL);
            Linear_target.setLayoutParams(lp_LinearLayout_target);


            ViewGroup.LayoutParams lp_textView_target = textView_target.getLayoutParams();
            lp_textView_target.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_textView_target.height = ViewGroup.LayoutParams.MATCH_PARENT;
            textView_target.setId(View.generateViewId());
            targetIdMap.put("target_text_count_" + n, textView_target.getId());
            textView_target.setTextSize(25);
            textView_target.setGravity(Gravity.CENTER);
            textView_target.setBackgroundColor(getResources().getColor(R.color.white));
            textView_target.setTextColor(getResources().getColor(R.color.black));
            if (targetStopCount == 0) {
                textView_target.setText("即将到站");
                textView_target.setTextColor(getResources().getColor(R.color.orange));
            } else {
                textView_target.setText(targetStopCount + "站");
            }
            textView_target.setLayoutParams(lp_textView_target);

            ViewGroup.LayoutParams lp_textView_target2 = textView_target2.getLayoutParams();
            lp_textView_target2.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_textView_target2.height = ViewGroup.LayoutParams.MATCH_PARENT;
            textView_target2.setTextSize(20);
            textView_target2.setGravity(Gravity.CENTER);
            textView_target2.setId(View.generateViewId());
            targetIdMap.put("target_text_distance_" + n, textView_target2.getId());
            textView_target2.setBackgroundColor(getResources().getColor(R.color.white));
            textView_target2.setTextColor(getResources().getColor(R.color.black));
            textView_target2.setText(TimeUtil.secToMin(targetSeconds) + "/" + targetDistance + "米");
            textView_target2.setLayoutParams(lp_textView_target2);

            n++;
            if (n >= 2) {
                break;
            }
        }
    }

    /**
     * @description 更新实时公交信息
     */
    public void updataBusListInfo() {
        new Thread(() -> {
            try {
                String str_businfo;
                if (isForw) {
                    str_businfo = HttpUtil.httpGet(forw_api);
                }else {
                    str_businfo = HttpUtil.httpGet(oppsite_api);
                }
                busInfo = MAPPER.readValue(str_businfo, BusDomJson.class);
                System.out.println(busApi);

                runOnUiThread(() -> {

                    stopsList = busInfo.getItems().get(0).getRoutes().get(0).getStops();
                    indexList.clear();
                    nextBuses = busInfo.getItems().get(0).getRoutes().get(0).getNextBuses();
                    List<Buses> buses = nextBuses.getBuses();
                    int n = 0;
                    for (Buses bus : buses) {
                        System.out.println(bus.getNextStation());
                        TextView t1 = findViewById(targetIdMap.get("target_text_count_" + n));
                        TextView t2 = findViewById(targetIdMap.get("target_text_distance_" + n));
                        int targetDistance = bus.getTargetDistance();
                        int targetStopCount = bus.getTargetStopCount();
                        int targetSeconds = bus.getTargetSeconds();
                        if (targetStopCount == 0) {
                            t1.setText("即将到站");
                            t1.setTextColor(getResources().getColor(R.color.orange));
                        } else {
                            t1.setText(targetStopCount + "站");
                        }
                        t2.setText(TimeUtil.secToMin(targetSeconds) + "/" + targetDistance + "米");
                        n++;
                        if (n == 2) {
                            break;
                        }

                    }

                    for (Stops stops : stopsList) {
                        List<Buses> list = stops.getBuses();
                        if (list != null && list.size() != 0) {
                            indexList.add(stopsList.indexOf(stops)); //添加公交车在stopList中的索引值 到 indexList
                        }
                    }

                    //取消掉small_bus的UI
                    for (Integer integer : imageView_bus_list) {
                        ImageView small_bus = findViewById(integer);
                        small_bus.setImageDrawable(null);
                    }

                    //将所有即将到来的公交设置UI (小公交UI)  刷新小公交UI
                    for (Integer integer : indexList) {
                        ImageView small_bus = findViewById(imageView_bus_list.get(integer));
                        small_bus.setImageResource(R.drawable.small_bus);
                    }


                });
                Log.d("updataBusListInfo", "更新实时公交数据成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * @description 反向列表
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void oppositeBusListInfo() {
        new Thread(() -> {
            try {
                String opposite_bus_json_info = "";
                if (isForw){
                    now_bus_uid = forw_uid;
                    now_bus_api = oppsite_api;
                    opposite_bus_json_info = HttpUtil.httpGet(oppsite_api);
                    isForw = !isForw;
                    System.out.println("点击反向后UID"+now_bus_uid);
                }
                else {
                    now_bus_uid = oppsite_uid;
                    now_bus_api = forw_api;
                    opposite_bus_json_info = HttpUtil.httpGet(forw_api);
                    isForw = !isForw;
                    System.out.println("点击反向后UID"+now_bus_uid);
                }
                busInfo = MAPPER.readValue(opposite_bus_json_info, BusDomJson.class);
                stopsList = busInfo.getItems().get(0).getRoutes().get(0).getStops();
                //修改列表UI
                //1 修改站点名称
                // 难点 1 正向站点数 和反向站点数 不一样 不可以用list直接拿ID改UI
                // 思路 先设置所有UI 不可见 隐藏 View.GONE  再开始展示UI
                // 如果正向站点数 大于反向站点数 显示小的方向站点数的UI 若小于 将TextViewList扩容 再动态添加几个TextView 进去
                runOnUiThread(() -> {
                    //清空之前保留的ID信息
                    imageView_bus_list.clear();
                    imageView_point_list.clear();
                    linearLayout_list.clear();
                    textView_list.clear();
                    indexList.clear();
                    bus_info_target.removeAllViews();
                    bus_info_list.removeAllViews();
                    linearLayout_list.clear();
                    //重新刷新公交列表UI
                    getBusListUI(busInfo);


                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //返回的时候需要clear 列表
        linearLayout_list.clear();
        //imageView_bus_list.clear();
        imageView_point_list.clear();
        textView_list.clear();
        Log.d("clearLists", "清楚列表ID");
    }

    @Override
    public void onClick(View v) {


        for (Integer integer : textView_list) {
            TextView no_select_view = findViewById(integer);
            no_select_view.setTextColor(getResources().getColor(R.color.black));
        }

        for (Integer integer : imageView_point_list) {
            ImageView no_select_point = findViewById(integer);
            no_select_point.setImageResource(R.drawable.point);
        }
        //设置起点和终点的UI
        Integer start_id = imageView_point_list.get(0);
        Integer end_id = imageView_point_list.get(imageView_point_list.size() - 1);
        ImageView start_img = findViewById(start_id);
        ImageView end_img = findViewById(end_id);
        start_img.setImageResource(R.drawable.bus_start);
        end_img.setImageResource(R.drawable.bus_end);


        int index = textView_list.indexOf(v.getId());
        Integer point_select_id = imageView_point_list.get(index);
        ImageView select_point_image = findViewById(point_select_id);
        select_point_image.setImageResource(R.drawable.stop_select);
        TextView select_view = findViewById(v.getId());
        select_view.setTextColor(getResources().getColor(R.color.orange));


    }
}