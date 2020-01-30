package com.hwh.traffic.utils;




import java.io.*;
import java.net.*;

/**
 * 发送请求得到数据信息的接口类
 */
public class HttpUtil {

    //private static HttpURLConnection conn;
    //private static BufferedReader bfr;


    public static String httpPost(String url, String canshu) throws InterruptedException {
        try {

            HttpURLConnection conn = null;
            BufferedReader bfr = null;
//            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL ur = new URL(url);
            conn = (HttpURLConnection) ur.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setDoInput(true);
            conn.connect();
            DataOutputStream osw = new DataOutputStream(conn.getOutputStream());
            StringBuilder sb = new StringBuilder(canshu);
            osw.write(sb.toString().getBytes());
            osw.flush();
            osw.close();
            Thread.sleep(2000);
            InputStream i = conn.getInputStream();
            bfr = new BufferedReader(new InputStreamReader(i));
            String line = "";
            String data = "";
            while ((line = bfr.readLine()) != null) {
                data = data + line;
            }
            bfr.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }

    }


    public static String httpGet(String url) {
        try {
            HttpURLConnection conn = null;
            BufferedReader bfr = null;

            URL ur = new URL(url);
            conn = (HttpURLConnection) ur.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setDoInput(true);
            conn.connect();
            //读取返回数据
            Thread.sleep(20);
            InputStream i = conn.getInputStream();
            bfr = new BufferedReader(new InputStreamReader(i));
            String line;
            String data = "";
            while ((line = bfr.readLine()) != null) {
                data = data + line;
            }
            bfr.close();
            conn.disconnect();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
