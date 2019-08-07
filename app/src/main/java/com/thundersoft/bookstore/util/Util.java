package com.thundersoft.bookstore.util;

import android.text.TextUtils;
import android.util.Log;

import com.thundersoft.bookstore.model.Book;
import com.thundersoft.bookstore.model.BookCategory;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class Util {

    private static final String TAG = "Util";

    private static final String KEY = "006e8b17f75158a969f56e37f4979d41";

    private static final String PN = "0";//起始位置

    private static final String RN = "20";//数据最大返回条数


    /*
     * response 返回的JSON格式的数组
     * 处理Book类型JSON数组
     * 存入LitePal数据库
     * */
    public static boolean handleBookResponse(String response, String categoryId) {

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject json = new JSONObject(response);
                String resultCode = json.getString("resultcode");
                if (resultCode.equals("200")) {
                    JSONObject result = json.getJSONObject("result");
                    JSONArray data = result.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject book_json = data.getJSONObject(i);
                        String title = book_json.getString("title");
                        String catalog = book_json.getString("catalog");
                        String tags = book_json.getString("tags");
                        String sub1 = book_json.getString("sub1");
                        String sub2 = book_json.getString("sub2");
                        String imageUrl = book_json.getString("img");
                        String reading = book_json.getString("reading");
                        String online = book_json.getString("online");
                        String byTime = book_json.getString("bytime");

                        Book book = new Book(title, catalog, tags,
                                sub1, sub2, imageUrl, reading, online,
                                byTime, categoryId, 1, 0);
                        book.save();
                        Log.i(TAG, "handleResponse: 书籍数据存储成功!");
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
     * response 返回的JSON格式的数组
     * 处理BookCategory类型JSON数组
     * 存入LitePal数据库
     * */
    public static boolean handleCategoryResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("resultcode");
                if (resultCode.equals("200")) {
                    JSONArray categorys = jsonObject.getJSONArray("result");
                    for (int i = 0; i < categorys.length(); i++) {
                        JSONObject book_category = categorys.getJSONObject(i);
                        String id = book_category.getString("id");
                        String catalog = book_category.getString("catalog");
                        BookCategory category = new BookCategory(catalog, Integer.parseInt(id), true);
                        category.save();
                        Log.i(TAG, "handleCategoryResponse: id " + id);
                        Log.i(TAG, "handleCategoryResponse: category " + catalog);
                    }
                    return true;
                } else {
                    Log.i(TAG, "handleBookResponse: 数据库存储失败！ resultCode " + resultCode);
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean downloadCategoryFromServer() {
        String address = "http://apis.juhe.cn/goodbook/catalog?key=" + KEY +
                "&dtype=json";
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                handleCategoryResponse(responseText);
            }
        });
        return false;
    }

    public static boolean downloadBookFromServer(String catalogId) {
        String address = "http://apis.juhe.cn/goodbook/query?key=" + KEY +
                "&catalog_id=" + catalogId +
                "&pn=" + PN +
                "&rn=" + RN;
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "onFailure: HttpOnFailed!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                handleBookResponse(responseText,catalogId);
            }
        });
        return false;
    }

}
