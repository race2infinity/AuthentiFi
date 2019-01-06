package com.pratz.authentifi.SellActivity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pratz.authentifi.ConnectionManager;
import com.pratz.authentifi.MainActivity;
import com.pratz.authentifi.ProductPage;
import com.pratz.authentifi.R;
import com.pratz.authentifi.Viewfind;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyStep1 extends Fragment {

	TextView textView;

	public BuyStep1() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		IntentIntegrator.forSupportFragment(this)
				.setPrompt("Scan the Seller's QR code")
				.setOrientationLocked(false)
				.initiateScan();


		View view= inflater.inflate(R.layout.fragment_buy_step1, container, false);

		textView = view.findViewById(R.id.nexttocontinue);



		return view;

	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		BuyActivity.qrcode=result.getContents();
		if(result != null) {
			RequestQueue requestQueue = Volley.newRequestQueue(getContext());
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("QRCode", result.getContents());
				jsonObject.put("email", MainActivity.email);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			String URL = MainActivity.address+"/buy";
			ConnectionManager.sendData(jsonObject.toString(), requestQueue, URL, new ConnectionManager.VolleyCallback() {
				@Override
				public void onSuccessResponse(String result) {
					textView.setVisibility(View.VISIBLE);
				}

				@Override
				public void onErrorResponse(VolleyError error) {

				}
			});
		}
	}
}
