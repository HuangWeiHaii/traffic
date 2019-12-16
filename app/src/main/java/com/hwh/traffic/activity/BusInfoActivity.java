package com.hwh.traffic.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwh.traffic.R;
import com.hwh.traffic.busEntity.BusDomJson;
import com.hwh.traffic.busEntity.Stops;
import com.hwh.traffic.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;


public class BusInfoActivity extends AppCompatActivity implements View.OnClickListener{


    private LinearLayout bus_info_list;
    private List<Integer> linearLayout_list = new ArrayList<>();
    private List<Integer> imageView_point_list = new ArrayList<>();
    private List<Integer> imageView_bus_list = new ArrayList<>();
    private List<Integer> textView_list = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);
        BusDomJson busInfo = (BusDomJson) getIntent().getSerializableExtra("busInfo");
        System.out.println(busInfo);

        //所有站点信息
        List<Stops> stopsList = busInfo.getItem().getStops();
        //站点数
        int stop_num = busInfo.getItem().getStops().size();
        System.out.println("站点数："+stop_num);

        bus_info_list = findViewById(R.id.bus_info_list);

        for (Stops stops : stopsList) {

            //显示一条路线
            LinearLayout mLinearLayout = new LinearLayout(getApplicationContext());
            ImageView mImageView_point = new ImageView(getApplicationContext());
            ImageView imageView_small_bus = new ImageView(getApplicationContext());

            TextView mTextView = new TextView(getApplicationContext());
            int dp_10 = DensityUtil.dip2px(getApplicationContext(),10);

            bus_info_list.addView(mLinearLayout);
            mLinearLayout.addView(mImageView_point);
            mLinearLayout.addView(mTextView);
            mLinearLayout.addView(imageView_small_bus);

            ViewGroup.LayoutParams lp_LinearLayout = mLinearLayout.getLayoutParams();
            lp_LinearLayout.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_LinearLayout.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            mLinearLayout.setId(View.generateViewId());
            int linearLayout_id = mLinearLayout.getId();
            linearLayout_list.add(linearLayout_id);
            mLinearLayout.setLayoutParams(lp_LinearLayout);

            ViewGroup.LayoutParams lp_ImageView_point = mImageView_point.getLayoutParams();
            lp_ImageView_point.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp_ImageView_point.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mImageView_point.setImageResource(R.drawable.point);
            mImageView_point.setPadding(dp_10,dp_10,dp_10,dp_10);
            mImageView_point.setId(View.generateViewId());
            int imageView_point_id = mImageView_point.getId();
            imageView_point_list.add(imageView_point_id);
            mImageView_point.setLayoutParams(lp_ImageView_point);


            LinearLayout.LayoutParams lp_TextView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
//            ViewGroup.LayoutParams lp_TextView = mTextView.getLayoutParams();
            lp_TextView.height = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_TextView.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mTextView.setText(stops.getStopName());
            mTextView.setTextColor(getResources().getColor(R.color.black));
            mTextView.setTextSize(20);
            mTextView.setPadding(dp_10,dp_10,dp_10,dp_10);
            mTextView.setId(View.generateViewId());
            mTextView.setOnClickListener(this);
            int textView_id = mTextView.getId();
            textView_list.add(textView_id);
            mTextView.setLayoutParams(lp_TextView);



            // bus 站点
            ViewGroup.LayoutParams lp_ImageView_bus = imageView_small_bus.getLayoutParams();
            lp_ImageView_bus.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp_ImageView_bus.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            imageView_small_bus.setImageResource(R.drawable.small_bus);
            imageView_small_bus.setPadding(dp_10,dp_10,dp_10,dp_10);
            imageView_small_bus.setId(View.generateViewId());
            int imageView_bus_id = imageView_small_bus.getId();
            imageView_bus_list.add(imageView_bus_id);
            mImageView_point.setLayoutParams(lp_ImageView_bus);

            //显示一条路线
        }

       /* for (Integer integer : linearLayout_list) {
            System.out.println("linearLayout_list id = " + integer);
        }

        for (Integer integer : imageView_list) {
            System.out.println("imageView_list id = " + integer);
        }

        for (Integer integer : textView_list) {
            System.out.println("imageView_list id = " + integer);
        }*/


       //假如第3  5  7  9个站点有公交车
        ImageView small_bus_1 = findViewById(imageView_bus_list.get(3));
        ImageView small_bus_2 = findViewById(imageView_bus_list.get(5));
        ImageView small_bus_3 = findViewById(imageView_bus_list.get(7));
        ImageView small_bus_4 = findViewById(imageView_bus_list.get(9));
        small_bus_1.setImageResource(R.drawable.small_bus);
        small_bus_2.setImageResource(R.drawable.small_bus);
        small_bus_3.setImageResource(R.drawable.small_bus);
        small_bus_4.setImageResource(R.drawable.small_bus);


        Integer start_id = imageView_point_list.get(0);
        Integer end_id = imageView_point_list.get(imageView_point_list.size()-1);
        ImageView start_img = findViewById(start_id);
        ImageView end_img = findViewById(end_id);
        start_img.setImageResource(R.drawable.bus_start);
        end_img.setImageResource(R.drawable.bus_end);

    }


    @Override
    protected void onPause() {
        super.onPause();
        //返回的时候需要clear 三个列表
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
