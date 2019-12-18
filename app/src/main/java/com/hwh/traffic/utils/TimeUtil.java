package com.hwh.traffic.utils;

public class TimeUtil {

    public static String secToMin(int sec){
        int minutes = sec / 60;
        int remainingSeconds = sec % 60;
        return minutes+"分钟";
    }

}
