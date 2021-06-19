package com.example.trangchu.service;
import com.loopj.android.http.*;
public class HttpUtils {
    private static final String BASE_URL = "https://evening-tor-29508.herokuapp.com/";

    private static AsyncHttpClient client = new AsyncHttpClient(); //gui va nhan du lieu
    //param: tham số gửi kèm
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
