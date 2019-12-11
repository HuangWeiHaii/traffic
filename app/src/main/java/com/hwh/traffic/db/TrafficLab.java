package com.hwh.traffic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class TrafficLab {

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public TrafficLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new TrafficBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * @param stopName
     * @return
     * @date 2019年12月11日16:45:24
     */
    public List<Long> findStopIdByName(String stopName){

        String sql = "SELECT stop_id FROM STOP WHERE stop_name LIKE CONCAT('%',"+stopName+",'%')";
        //执行查询


        return null;
    }

}
