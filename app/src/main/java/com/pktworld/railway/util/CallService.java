package com.pktworld.railway.util;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.Locale;


public class CallService {
    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
        client.setTimeout(15*10000);
    }

    public static void post(String url, RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        client.post(url, requestParams, responseHandler);
        client.setTimeout(15*10000);
    }

    public static String getResponse(String TAG,  String methodName,String url, RequestParams requestParams, byte[] response, Header[] headers, int statusCode, Throwable t) {

    	String serverResponse = null;
        Log.d(TAG, AsyncHttpClient.getUrlWithQueryString(false, url, requestParams));

        if (headers != null) {
            Log.e(TAG, methodName);
            Log.d(TAG, "Return Headers:");
            for (Header h : headers) {
                String _h = String.format(Locale.US, "%s : %s", h.getName(), h.getValue());
                Log.d(TAG, _h);
            }
            if (t != null) {
                Log.d(TAG, "Throwable:" + t);
            }
            Log.e(TAG, "StatusCode: " + statusCode);
            if (response != null) {
            	serverResponse = new String(response);
                Log.d(TAG, "Response: " + serverResponse);
            }

        }
        
        return serverResponse;
    }
}
