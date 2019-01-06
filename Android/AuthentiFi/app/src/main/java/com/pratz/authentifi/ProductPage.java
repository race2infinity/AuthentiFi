package com.pratz.authentifi;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratz.authentifi.RetailerActivity.MainRetailerActivity;
import com.pratz.authentifi.SellActivity.SellActivity;
import com.pratz.authentifi.User.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class ProductPage extends AppCompatActivity {

	TextView productCode, productBrand, productName, productDescription, productManufacturer, productStatus, productStatusDescription;
	ImageButton productImage, productStatusImage, productSell, productStolen;
	ConstraintLayout productOwnerLayout;

	String prodCode;

	View view;
	//int i=3;
	//ImageButton testButton, testButton1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressbar_loading);

		view = View.inflate(ProductPage.this, R.layout.activity_product_page, null);

		//Get UI elements
		productCode = (TextView) view.findViewById(R.id.product_code);
		productBrand = (TextView) view.findViewById(R.id.product_brand);
		productName = (TextView) view.findViewById(R.id.product_name);
		productDescription = (TextView) view.findViewById(R.id.product_description);
		productManufacturer = (TextView) view.findViewById(R.id.product_manufacturer);
		productStatus = (TextView) view.findViewById(R.id.product_status);
		productStatusDescription = (TextView) view.findViewById(R.id.product_status_description);
		productImage = (ImageButton) view.findViewById(R.id.product_image);
		productStatusImage = (ImageButton) view.findViewById(R.id.product_status_image);
		productOwnerLayout = (ConstraintLayout) view.findViewById(R.id.owner_section);

		productStolen = (ImageButton) view.findViewById(R.id.product_stolen_image);
		productSell = (ImageButton) view.findViewById(R.id.product_sell_image);





		//Read code from previous activity
		Bundle bundle = getIntent().getExtras();
		if(bundle.getBoolean("isOwner"))
			productOwnerLayout.setVisibility(View.VISIBLE);

		prodCode = bundle.getString("code");
		productCode.setText(prodCode);

		productSell.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductPage.this, SellActivity.class);
				intent.putExtra("code", prodCode);
				intent.putExtra("retailer", getIntent().getExtras().getString("retailer"));
				startActivity(intent);
				finish();
			}
		});

		productStolen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				RequestQueue requestQueue = Volley.newRequestQueue(ProductPage.this);
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("code", prodCode);
					jsonObject.put("email", MainActivity.email);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				String URL = MainActivity.address+"/stolen";
				ConnectionManager.sendData(jsonObject.toString(), requestQueue, URL, new ConnectionManager.VolleyCallback() {
					@Override
					public void onSuccessResponse(String result) {
						Toast toast = Toast.makeText(ProductPage.this,
								"Product has been reported as stolen.",
								Toast.LENGTH_LONG);

						toast.show();

						finish();
					}

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
			}
		});


		//Get data from server
		try {
			getData();
		} catch (JSONException e) {
			e.printStackTrace();
		}


		//close button
		ImageButton closeButton = (ImageButton) view.findViewById(R.id.close);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});



		//set product state accordingly
		productValid();

		//Cycle between outputs (testing purposes only)

	}

	private void productValid() {
		productStatusImage.setImageResource(R.drawable.ic_check_circle_black);
		productStatus.setText(R.string.product_valid);
		productStatusDescription.setText(R.string.product_valid_description);
	}

	private void productStolen() {
		productStatusImage.setImageResource(R.drawable.ic_error_black);
		productStatus.setText(R.string.product_stolen);
		productStatusDescription.setText(R.string.product_stolen_description);
	}

	private void productInvalid() {
		productStatusImage.setImageResource(R.drawable.ic_cancel_black);
		productStatus.setText(R.string.product_unregistered);
		productStatusDescription.setText(R.string.product_unregistered_description);
	}

	private void getData() throws JSONException {

		RequestQueue requestQueue = Volley.newRequestQueue(ProductPage.this);
		String address;
		if(MainActivity.address==null)
			address=MainRetailerActivity.address;
		else
			address=MainActivity.address;
		String URL = address+"/scan";
		final JSONObject jsonBody = new JSONObject();
		jsonBody.put("code", prodCode);
		final String requestBody = jsonBody.toString();
		ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
			@Override
			public void onSuccessResponse(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);

					//Set data

					productName.setText(jsonObject.getString("model"));
					productBrand.setText(jsonObject.getString("name"));
					productDescription.setText(jsonObject.getString("description"));
					productManufacturer.setText(getString(R.string.product_manufacturer,
							jsonObject.getString("manufacturerName")+", "
									+jsonObject.getString("manufacturerLocation"),
							jsonObject.getString("manufacturerTimestamp")));

					switch(Integer.parseInt(jsonObject.getString("status"))) {
						case 0:
							productInvalid();
							break;

						case 1:
							productValid();;
							break;

						default:
							productStolen();
					}

					setContentView(view);

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("KALDONjs", "sad");
				}


			}

			@Override
			public void onErrorResponse(VolleyError error) {

				Toast toast = Toast.makeText(ProductPage.this,
						"Could not connect to server, please try again.",
						Toast.LENGTH_LONG);

				toast.show();
			}
		});
	}


}

//$2b$10$8LgfFIfInVNDvOQU0KZr0OGtB38ISPAT3pIdvhiTolBCS6zyvapYO