package com.example.trangchu.service;
import android.content.AsyncQueryHandler;
import android.content.Context;

import com.loopj.android.http.*;

import java.util.Hashtable;
import java.util.Map;

import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpUtils {
    private static final String BASE_URL = "https://evening-tor-29508.herokuapp.com/";

    private static AsyncHttpClient client = new AsyncHttpClient(); //gui va nhan du lieu
    //param: tham số gửi kèm
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void get(String url, RequestParams params, Hashtable<String, String> headers, AsyncHttpResponseHandler responseHandler){
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            client.addHeader(k, v);
        }
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.delete(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.post(context,getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
