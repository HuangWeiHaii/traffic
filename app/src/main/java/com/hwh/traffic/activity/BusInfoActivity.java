package com.hwh.traffic.activity;

import androidx.annotation.LongDef;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwh.traffic.R;
import com.hwh.traffic.busEntity.BusDomJson;
import com.hwh.traffic.busEntity.Buses;
import com.hwh.traffic.busEntity.NextBuses;
import com.hwh.traffic.busEntity.Stops;
import com.hwh.traffic.utils.DensityUtil;
import com.hwh.traffic.utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BusInfoActivity extends AppCompatActivity implements View.OnClickListener{


    private LinearLayout bus_info_list;
    private List<Integer> linearLayout_list = new ArrayList<>();
    private List<Integer> imageView_point_list = new ArrayList<>();
    private List<Integer> imageView_bus_list = new ArrayList<>();
    private List<Integer> textView_list = new ArrayList<>();
    private BusDomJson busInfo = null;
    private String busApi = null;
    private static ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);


        busInfo = (BusDomJson) getIntent().getSerializableExtra("BUS_INFO");
        busApi = getIntent().getStringExtra("BUS_API");
        System.out.println(busInfo);
        System.out.println(busApi);



        //将实时公交信息渲染
        getBusListUI(busInfo);

    }

    /**
     * @description 渲染实时公交信息 设置监听等等
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void getBusListUI(final BusDomJson busInfo) {


        ImageView flush_img = findViewById(R.id.bus_img_flush);
        flush_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updataBusListInfo();
            }
        });

        //所有站点信息
        List<Stops> stopsList = busInfo.getItem().getStops();
        //所有正在前往目的地的公交信息
        NextBuses nextBuses = busInfo.getItem().getNextBuses();
        //所有正在前往目的地的公交信息
        List<Buses> buses = nextBuses.getBuses();

        //站点数
        int stop_num = busInfo.getItem().getStops().size();
        System.out.println("站点数："+stop_num);

        bus_info_list = findViewById(R.id.bus_info_list);

        for (Stops stops : stopsList) {

            //显示一条路线
            LinearLayout mLinearLayout = new LinearLayout(getApplicationContext());
            ImageView mImageView_point = new ImageView(getApplicationContext());
            ImageView imageView_small_bus = new ImageView(getApplicationContext());
            TextView mTextView_stop = new TextView(getApplicationContext());
            //设置10dp
            int dp_10 = DensityUtil.dip2px(getApplicationContext(),10);

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
            mImageView_point.setPadding(dp_10,dp_10,dp_10,dp_10);
            mImageView_point.setId(View.generateViewId());
            int imageView_point_id = mImageView_point.getId();
            imageView_point_list.add(imageView_point_id);
            mImageView_point.setLayoutParams(lp_ImageView_point);


            // bus 站点名称
            LinearLayout.LayoutParams lp_TextView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            lp_TextView.height = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_TextView.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mTextView_stop.setText(stops.getStopName());
            mTextView_stop.setTextColor(getResources().getColor(R.color.black));
            mTextView_stop.setTextSize(20);
            mTextView_stop.setPadding(dp_10,dp_10,dp_10,dp_10);
            mTextView_stop.setId(View.generateViewId());
            mTextView_stop.setOnClickListener(this);
            int textView_id = mTextView_stop.getId();
            textView_list.add(textView_id);
            mTextView_stop.setLayoutParams(lp_TextView);


            // bus 站点
            ViewGroup.LayoutParams lp_ImageView_bus = imageView_small_bus.getLayoutParams();
            lp_ImageView_bus.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp_ImageView_bus.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imageView_small_bus.setPadding(dp_10,dp_10,dp_10,dp_10);
            imageView_small_bus.setId(View.generateViewId());
            int imageView_bus_id = imageView_small_bus.getId();
            imageView_bus_list.add(imageView_bus_id);
            mImageView_point.setLayoutParams(lp_ImageView_bus);

        }

        //将所有即将到来的公交设置UI (小公交UI)
        for (Buses bus : buses) {
            //公交现在的位置
            int passedSeqNo = bus.getPassedSeqNo();
            ImageView small_bus = findViewById(imageView_bus_list.get(passedSeqNo));
            small_bus.setImageResource(R.drawable.small_bus);
        }


        //设置起点和终点的UI
        Integer start_id = imageView_point_list.get(0);
        Integer end_id = imageView_point_list.get(imageView_point_list.size()-1);
        ImageView start_img = findViewById(start_id);
        ImageView end_img = findViewById(end_id);
        start_img.setImageResource(R.drawable.bus_start);
        end_img.setImageResource(R.drawable.bus_end);
    }

    /**
     * @description 更新实时公交信息
     */
    public void updataBusListInfo(){
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                try {
                    String str_businfo = HttpUtils.httpGet(busApi);
                    busInfo = MAPPER.readValue(str_businfo, BusDomJson.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //所有站点信息
                            List<Stops> stopsList = busInfo.getItem().getStops();
                            //所有正在前往目的地的公交信息
                            NextBuses nextBuses = busInfo.getItem().getNextBuses();
                            //所有正在前往目的地的公交信息
                            List<Buses> buses = nextBuses.getBuses();

                            //站点数
                            int stop_num = busInfo.getItem().getStops().size();
                            System.out.println("站点数："+stop_num);

                            //取消掉small_bus的UI
                            //。。。。。


                            //将所有即将到来的公交设置UI (小公交UI)
                            for (Buses bus : buses) {
                                //公交现在的位置
                                int passedSeqNo = bus.getPassedSeqNo();
                                ImageView small_bus = findViewById(imageView_bus_list.get(passedSeqNo));
                                small_bus.setImageResource(R.drawable.small_bus);
                            }

                        }
                    });
                    Log.d("updataBusListInfo","更新实时公交数据成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    @Override
    protected void onPause() {
        super.onPause();
        //返回的时候需要clear 列表
        linearLayout_list.clear();
        imageView_bus_list.clear();
        imageView_point_list.clear();
        textView_list.clear();
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
        int index = textView_list.indexOf(v.getId());
        Integer point_select_id = imageView_point_list.get(index);
        ImageView select_point_image = findViewById(point_select_id);
        select_point_image.setImageResource(R.drawable.stop_select);
        TextView select_view = findViewById(v.getId());
        select_view.setTextColor(getResources().getColor(R.color.orange));
    }
}
