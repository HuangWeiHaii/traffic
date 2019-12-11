package com.hwh.traffic.db;

import android.content.Context;
import android.database.Cursor;
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
     * @return 站点ID
     * @date 2019年12月11日16:45:24
     */
    public Long findStopIdByName(String stopName){

        String sql = "SELECT stop_id FROM stop WHERE stop_name LIKE '%"+stopName+"%'";
        //执行查询
        Cursor cursor = mDatabase.rawQuery(sql,null);
        Long stop_id = 0l;
        cursor.moveToNext();
        stop_id = cursor.getLong(0);
        cursor.close();
        return stop_id;
    }

    /**
     *
     * @param routeName 路线名称
     * @return  站点正向ID 和 反向ID
     */
    public Long[] findRouteIdAndOppositeIdByRouteName(String routeName){
        String sql = "SELECT route_id,opposite_id FROM route WHERE route_name LIKE '%"+routeName+"%'";

        Cursor cursor = mDatabase.rawQuery(sql,null);
        cursor.moveToNext();
        Long route_id = cursor.getLong(0);
        Long oppsite_id = cursor.getLong(1);
        cursor.close();
        return new Long[]{route_id,oppsite_id};
    }

}
