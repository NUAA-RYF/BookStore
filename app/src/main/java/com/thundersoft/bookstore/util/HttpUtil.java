package com.thundersoft.bookstore.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    //需要在线程中运行
    public static void sendOkhttpRequest(String address,okhttp3.Callback callback) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(address).build();
                client.newCall(request).enqueue(callback);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
