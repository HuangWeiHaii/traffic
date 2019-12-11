package com.hwh.traffic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwh.traffic.MapApplication;
import com.hwh.traffic.R;
import com.hwh.traffic.busEntity.BusDomJson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author 黄威海
 * @date 2019年12月10日16:56:09
 * @description 主页 Activity
 */
public class MainPageActivity extends AppCompatActivity{

    private List<PoiInfo> poiInfos;
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
    private MapApplication mapApplication;
    private LatLng latLng;
    private String route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
      /*  getDatasync("https://app.ibuscloud.com/v11/bus/getNextBusByRouteStopId?city=350100\n" +
                "&userLat=26.074268\n" +
                "&userLng=119.296389\n" +
                "&stopId=3501000100105557\n" +
                "&routeId=3501000100097601\n" +
                "&h5Platform=7\n" +
                "&radius=1000\n" +
                "&lat=26.074268\n" +
                "&lng=119.296389");//输入URL*/


        mapApplication = (MapApplication) getApplication();
        //开启一条线程去执行 POI 搜索公交站

        new Thread(){
            @Override
            public void run() {
                while (mapApplication.getLatLng() == null){
                    try {
                        Thread.sleep(1000);
                        Log.d("百度地图定位MainPage","正在定位");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("百度地图定位MainPage","定位成功");
                latLng = mapApplication.getLatLng();
                if (latLng != null) {
                    getStopInfoByLatLng(latLng);
                    while (poiInfos == null){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //


                }
                else {
                    //错误处理
                }

            }
        }.start();
        initViews();
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

        main_page_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (main_page_edit.getText().toString().equals("")){
                    Toast.makeText(MainPageActivity.this,"请输入路线",Toast.LENGTH_SHORT).show();
                }
                else {
                    route = main_page_edit.getText().toString();
                    if (poiInfos != null) {
                        //对POI公交信息进行遍历
                        for (PoiInfo poiInfo : poiInfos) {
                            System.out.println(poiInfo.getAddress());
                            System.out.println(poiInfo.getName());

                            //获取 路线信息 (15路;22路;77路;85路;312路;329路)
                            String routes = poiInfo.getAddress();
                            if (routes.contains(main_page_edit.getText().toString())){//85路
                                //直接获取第一个 公交站点 (福建理工学校)
                                System.out.println(poiInfo.getName());
                                break;
                            }

                        }

                    }
                }
            }
        });

        main_page_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_page_image.setBackgroundResource(R.drawable.mainpage_select);
                main_like_image.setBackgroundResource(R.drawable.like);
                main_location_image.setBackgroundResource(R.drawable.location);
                main_user_image.setBackgroundResource(R.drawable.user);

                main_page_text.setTextColor(getResources().getColor(R.color.main_text_select));
                main_like_text.setTextColor(getResources().getColor(R.color.black));
                main_location_text.setTextColor(getResources().getColor(R.color.black));
                main_user_text.setTextColor(getResources().getColor(R.color.black));

            }
        });

        main_like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_like_image.setBackgroundResource(R.drawable.like_select);

                main_page_image.setBackgroundResource(R.drawable.mainpage);
                main_location_image.setBackgroundResource(R.drawable.location);
                main_user_image.setBackgroundResource(R.drawable.user);

                main_like_text.setTextColor(getResources().getColor(R.color.main_text_select));
                main_page_text.setTextColor(getResources().getColor(R.color.black));
                main_location_text.setTextColor(getResources().getColor(R.color.black));
                main_user_text.setTextColor(getResources().getColor(R.color.black));

            }
        });

        main_location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_location_image.setBackgroundResource(R.drawable.location_select);

                main_page_image.setBackgroundResource(R.drawable.mainpage);
                main_like_image.setBackgroundResource(R.drawable.like);
                main_user_image.setBackgroundResource(R.drawable.user);

                main_location_text.setTextColor(getResources().getColor(R.color.main_text_select));
                main_like_text.setTextColor(getResources().getColor(R.color.black));
                main_page_text.setTextColor(getResources().getColor(R.color.black));
                main_user_text.setTextColor(getResources().getColor(R.color.black));
            }
        });

        main_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_user_image.setBackgroundResource(R.drawable.user_select);

                main_page_image.setBackgroundResource(R.drawable.mainpage);
                main_like_image.setBackgroundResource(R.drawable.like);
                main_location_image.setBackgroundResource(R.drawable.location);

                main_user_text.setTextColor(getResources().getColor(R.color.main_text_select));
                main_like_text.setTextColor(getResources().getColor(R.color.black));
                main_location_text.setTextColor(getResources().getColor(R.color.black));
                main_page_text.setTextColor(getResources().getColor(R.color.black));
            }
        });
    }


    /**
     * @description 通过经纬度得到 公交站点信息
     * @param latLng
     */
    public void getStopInfoByLatLng(LatLng latLng) {

        Log.d("百度地图POI","开始POI搜索");
        PoiSearch mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                Log.d("百度地图POI","获取到poiResult");
                List<PoiInfo> allPoi = poiResult.getAllPoi();
                poiInfos = allPoi;
                Log.d("百度地图POI", String.valueOf(poiResult.getTotalPoiNum()));
             /*   if (allPoi != null) {
                    for (PoiInfo poiInfo : allPoi) {
                        Log.d("百度地图POI", poiInfo.address);
                        Log.d("百度地图POI", poiInfo.name);
                        Log.d("百度地图POI", "-----------------------");

                    }
                }*/
            }
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
                Log.d("百度地图POI","poiDetailSearchResult");
                List<PoiDetailInfo> infoList = poiDetailSearchResult.getPoiDetailInfoList();
                for (PoiDetailInfo poiDetailInfo : infoList) {
                    System.out.println(poiDetailInfo.getName());
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .radius(1000)
                .location(latLng)
                .keyword("公交")
                .scope(2)
                .pageCapacity(20)
                .radiusLimit(true)
        );

    }

    /**
     * @description 异步获取Json数据
     * @param url
     */
    public void getDatasync(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;

                    response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        Log.d("实时公交信息","response.code()=="+response.code());
                        Log.d("实时公交信息","response.message()=="+response.message());
//                        Log.d("实时公交信息","res=="+response.body().string());
                        String busInfo = response.body().string();
                        Log.d("businfo",busInfo);
                        busDomJson = MAPPER.readValue(busInfo, BusDomJson.class);
                        if (busDomJson != null){
                            Log.d("实时公交信息","读取实时公交成功");
                        }
                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getStopName(List<PoiInfo> poiInfos){
        if (poiInfos != null) {
            for (PoiInfo poiInfo : poiInfos) {
//                Log.d("百度地图POI", poiInfo.address);
//                Log.d("百度地图POI", poiInfo.name);
                Log.d("百度地图POI", "-----------------------stopname");
            }
        }


    }

}
