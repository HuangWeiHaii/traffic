package com.hwh.traffic.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.List;


public class BusInfoActivity extends AppCompatActivity {


    private LinearLayout bus_info_list;
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
        int count = bus_info_list.getChildCount();
        System.out.println("bus_list----"+count);

        for (Stops stops : stopsList) {

            //显示一条路线
            LinearLayout mLinearLayout = new LinearLayout(getApplicationContext());
            ImageView mImageView = new ImageView(getApplicationContext());
            TextView mTextView = new TextView(getApplicationContext());
            int dp_10 = DensityUtil.dip2px(getApplicationContext(),10);

            bus_info_list.addView(mLinearLayout);
            mLinearLayout.addView(mImageView);
            mLinearLayout.addView(mTextView);

            ViewGroup.LayoutParams lp_LinearLayout = mLinearLayout.getLayoutParams();
            lp_LinearLayout.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_LinearLayout.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            mLinearLayout.setLayoutParams(lp_LinearLayout);

            ViewGroup.LayoutParams lp_ImageView = mImageView.getLayoutParams();
            lp_ImageView.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp_ImageView.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mImageView.setImageResource(R.drawable.point);
            mImageView.setPadding(dp_10,dp_10,dp_10,dp_10);
            mImageView.setLayoutParams(lp_ImageView);

            ViewGroup.LayoutParams lp_TextView = mTextView.getLayoutParams();
            lp_TextView.height = ViewGroup.LayoutParams.MATCH_PARENT;
            lp_TextView.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mTextView.setText(stops.getStopName());
            mTextView.setTextColor(getResources().getColor(R.color.black));
            mTextView.setTextSize(20);
            mTextView.setPadding(dp_10,dp_10,dp_10,dp_10);
            mTextView.setLayoutParams(lp_TextView);
            //显示一条路线
        }




    }
}
