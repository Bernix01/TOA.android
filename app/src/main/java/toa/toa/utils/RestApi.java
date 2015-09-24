/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;

import cz.msebera.android.httpclient.entity.BasicHttpEntity;

/**
 * Created by Guillermo on 5/27/2015.
 */
public class RestApi {
    public static final String EVENTRELTYPE = "isGoing";
    private static final String BASE_URL = "http://toa-neo4j.cloudapp.net:7474/db/data";
    private static final String BASE_URL_PW = "http://mundotoa.co/api/lost.php";
    private static String _authheader = "bmVvNGo6RGVwb3J0aXN0YXMx";
    private static String _recoverAuth = "cmFkbWluOlRPQQ==";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Basic " + _authheader);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void post(String url, final JSONObject data, AsyncHttpResponseHandler responseHandler) {

        client.addHeader("Authorization", "Basic " + _authheader);

        BasicHttpEntity se = new BasicHttpEntity();
        se.setContent(new ByteArrayInputStream((data.toString()).getBytes()));
        client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, "application/json");
        client.post(null, getAbsoluteUrl(url), se, "aplication/json", responseHandler);
    }

    public static void _delete(int id, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Basic " + _authheader);
        client.delete(null, getAbsoluteUrl("/node/" + id), responseHandler);
    }

    public static void put(String url, JSONObject data, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Basic " + _authheader);

        BasicHttpEntity se = new BasicHttpEntity();
        se.setContent(new ByteArrayInputStream((data.toString()).getBytes()));
        client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, "application/json");
        client.put(null, getAbsoluteUrl(url), se, "aplication/json", responseHandler);
    }

    public static void post(String url, String data, AsyncHttpResponseHandler responseHandler) {

        client.addHeader("Authorization", "Basic " + _authheader);

        BasicHttpEntity se = new BasicHttpEntity();
        se.setContent(new ByteArrayInputStream(data.getBytes()));
        client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, "application/json");
        Log.e("done", "done");
        client.post(null, getAbsoluteUrl(url), se, "aplication/json", responseHandler);
    }

    protected static void recoverPw(int Key, String email, AsyncHttpResponseHandler handler) {
        client.addHeader("Authorization", "Basic " + _recoverAuth);
        RequestParams params = new RequestParams();
        params.put("key", Key);
        params.put("email", email);
        Log.e("params", params.toString());
        client.get(null, BASE_URL_PW, params, handler);
    }

}