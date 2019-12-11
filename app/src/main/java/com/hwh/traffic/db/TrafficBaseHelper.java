package com.hwh.traffic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author 黄威海
 * @description traffic数据库 Helper
 */
public class TrafficBaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "traffic.db";
    public static final String DB_PATH = "schema";
    private Context mContext;
    public static final String ROUTE =
            "create table route ("
            + TrafficDbSchema.RouteTable.cols.ROUTE_ID + ", "
            + TrafficDbSchema.RouteTable.cols.ROUTE_NAME + ", "
            + TrafficDbSchema.RouteTable.cols.OPPOSITE_ID + ")";

    public static final String STOP =
            "create table route ("
            + TrafficDbSchema.StopTable.cols.SID + ", "
            + TrafficDbSchema.StopTable.cols.STOP_ID + ", "
            + TrafficDbSchema.StopTable.cols.STOP_NAME + ")";


    public TrafficBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(ROUTE);
        executeAssetsSQL(db, "traffic.sql");
        Log.d("db","创建表");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 读取数据库文件（.sql），并执行sql语句
     * */
    private void executeAssetsSQL(SQLiteDatabase db, String schemaName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(mContext.getAssets()
                    .open(DB_PATH + "/" + schemaName)));

            System.out.println("路径:"+DB_PATH + "/" + schemaName);
            String line;
            String buffer = "";
            while ((line = in.readLine()) != null) {
                buffer += line;
                if (line.trim().endsWith(";")) {
                    System.out.println("SQL SQLSQLSQLSQLSQLSQL" + buffer);
                    db.execSQL(buffer.replace(";", ""));
                    buffer = "";
                }
            }
        } catch (IOException e) {
            Log.e("db-error", e.toString());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                Log.e("db-error", e.toString());
            }
        }
    }
}
