package toa.toa.utils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Guillermo on 5/27/2015.
 */
public class RestApi {
    private static final String BASE_URL = "http://toa-neo4j.cloudapp.net:7474/db/data";
    private static String _authheader = "bmVvNGo6RGVwb3J0aXN0YXMx";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Basic " + _authheader);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void post(String url, JSONObject data, AsyncHttpResponseHandler responseHandler) {

        client.addHeader("Authorization", "Basic " + _authheader);
        StringEntity se = null;
        try {
            se = new StringEntity(data.toString(), "UTF-8");
            Log.e("json", se.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("error stringEntity", e.getMessage());
            return;
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.post(null, getAbsoluteUrl(url), se, "aplication/json", responseHandler);
    }

    public static void _delete(int id, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Basic " + _authheader);
        client.delete(null, getAbsoluteUrl("/node/" + id), responseHandler);
    }

    public static void put(String url, JSONObject data, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Basic " + _authheader);
        StringEntity se = null;
        try {
            se = new StringEntity(data.toString(), "UTF-8");
            Log.e("json", se.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("error stringEntity", e.getMessage());
            return;
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.put(null, getAbsoluteUrl(url), se, "aplication/json", responseHandler);
    }

    public static void post(String url, String data, AsyncHttpResponseHandler responseHandler) {

        client.addHeader("Authorization", "Basic " + _authheader);
        StringEntity se;
        try {
            se = new StringEntity(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("error stringEntity", e.getMessage());
            return;
        }
        Log.e("set content", "typw");
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        Log.e("done", "done");
        client.post(null, getAbsoluteUrl(url), se, "aplication/json", responseHandler);
    }
}