package com.pratz.authentifi.RetailerActivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pratz.authentifi.ConnectionManager;
import com.pratz.authentifi.ProductPage;
import com.pratz.authentifi.R;
import com.pratz.authentifi.SellActivity.SellActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class RetailerHomeFragment extends Fragment {


	Button claimButton, scanButton, sellButton;
	IntentIntegrator intentIntegrator;

	public RetailerHomeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onResume() {
		super.onResume();
		intentIntegrator = new IntentIntegrator(getActivity())
				.setOrientationLocked(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_retailer_home, container, false);

		claimButton = (Button) view.findViewById(R.id.claim_button);
		claimButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = intentIntegrator.setPrompt("Scan a Product")
						.createScanIntent();
				startActivityForResult(intent, 0);
			}
		});

		scanButton = (Button) view.findViewById(R.id.scan_button);
		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//	Intent intent = new Intent(MainActivity.this, Viewfind.class);
				//	startActivity(intent);

				Intent intent = intentIntegrator.setPrompt("Scan a Product")
						.createScanIntent();
				startActivityForResult(intent, 1);
			}
		});

		sellButton = (Button) view.findViewById(R.id.sell_button);
		sellButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = intentIntegrator.setPrompt("Scan a Product")
						.createScanIntent();
				startActivityForResult(intent, 2);

			}
		});

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, intent);
		switch(requestCode) {
			case 0:
				retailerClaim(scanResult.getContents());
				break;

			case 1:
				retailerScan(scanResult.getContents());
				break;

			default:
				retailerSell(scanResult.getContents());


		}
	}

	private void retailerClaim(String code) {
		String URL = MainRetailerActivity.address+"/addRetailerToCode";
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("code", code);
			jsonObject.put("email", MainRetailerActivity.email);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestQueue requestQueue = Volley.newRequestQueue(getContext());

		ConnectionManager.sendData(jsonObject.toString(), requestQueue, URL, new ConnectionManager.VolleyCallback() {
			@Override
			public void onSuccessResponse(String result) {
				Toast toast = Toast.makeText(getContext(),
						"Product claimed successfully.",
						Toast.LENGTH_LONG);

				toast.show();
			}

			@Override
			public void onErrorResponse(VolleyError error) {

				Toast toast = Toast.makeText(getContext(),
						"Could not connect to server, please try again.",
						Toast.LENGTH_LONG);

				toast.show();
			}
		});

	}

	private void retailerScan(String code) {
		Intent intent = new Intent(getContext(), ProductPage.class);
		intent.putExtra("isOwner", true);
		intent.putExtra("code", code);
		intent.putExtra("retailer", "1");
		startActivity(intent);

	}

	private void retailerSell(String code) {
		Intent intent = new Intent(getContext(), SellActivity.class);
		intent.putExtra("retailer", "1");
		intent.putExtra("code", code);
		startActivity(intent);

	}
}
