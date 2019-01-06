package com.pratz.authentifi;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

public class ConnectionManager {

	public interface VolleyCallback {
		void onSuccessResponse(String result);

		void onErrorResponse(VolleyError error);
	}

	public static void sendData(final String requestBody, RequestQueue requestQueue, String URL, final VolleyCallback volleyCallback) {


		Log.i("Kaldon-request", requestBody);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.i("KALDON-response", response);
				volleyCallback.onSuccessResponse(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("KALDON-error", error.toString());
				volleyCallback.onErrorResponse(error);
			}
		}) {
			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				Log.i("Kaldon-net", Integer.toString(response.statusCode));
				String parsed;
				try {
					parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
				} catch (UnsupportedEncodingException e) {
					parsed = new String(response.data);
				}
				return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));

			}

			@Override
			public String getBodyContentType() {
				return "application/json; charset=utf-8";
			}

			@Override
			public byte[] getBody() throws AuthFailureError {
				try {
					return requestBody == null ? null : requestBody.getBytes("utf-8");
				} catch (UnsupportedEncodingException e) {
					return null;
				}
			}
		};


		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				10000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		requestQueue.add(stringRequest);
	}

}
