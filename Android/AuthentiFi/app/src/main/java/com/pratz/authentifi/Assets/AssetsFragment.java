package com.pratz.authentifi.Assets;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pratz.authentifi.ConnectionManager;
import com.pratz.authentifi.MainActivity;
import com.pratz.authentifi.ProductPage;
import com.pratz.authentifi.R;
import com.pratz.authentifi.SellActivity.BuyActivity;
import com.pratz.authentifi.User.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Thread.sleep;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class AssetsFragment extends Fragment {

	View view;
	List<Asset> assetList = new ArrayList<Asset>();
	RecyclerView.Adapter mAdapter;

	RecyclerView mRecyclerView;
	ProgressBar progressBar;
	Button buyItem;

	String textAddress;

	public AssetsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(view==null)
			Log.d("yolo hi", "view is null create");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("Yolo", "Resumed!");


		progressBar.setVisibility(View.VISIBLE);
		mRecyclerView.setVisibility(View.INVISIBLE);

		RequestQueue requestQueue = Volley.newRequestQueue(getContext());


		String URL = MainActivity.address+"/myAssets";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("email", MainActivity.email);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		String requestBody = jsonObject.toString();
		Log.i("Kaldon-Asset", requestBody);

		ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
			@Override
			public void onSuccessResponse(String result) {
				try {
					progressBar.setVisibility(View.INVISIBLE);
					mRecyclerView.setVisibility(View.VISIBLE);
					JSONArray jsonArray = new JSONArray(result);
					jsonArray.remove(0);
					JSONObject tempObject;
					assetList.clear();
					for(int i=0; i<jsonArray.length(); i++) {

						tempObject = jsonArray.getJSONObject(i);
						assetList.add(i, new Asset(tempObject.getString("code"),
								tempObject.getString("model"),tempObject.getString("model")));

					}
					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
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

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


		Asset asset = new Asset("somecode", "Nike", "Cloudfoam");
		assetList.add(0, asset);

		Log.i("Yolo","Activity created");
		View view = getView();

		//RecyclerView
		mRecyclerView = (RecyclerView) view.findViewById(R.id.assetrecycler);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);

		// specify an adapter (see also next example)
		//RecyclerView.Adapter mAdapter = new MyAdapter(shit);

		//Lets hope clicking works
		mAdapter = new AssetAdapter(assetList);

		mRecyclerView.setAdapter(mAdapter);


		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
				((LinearLayoutManager) mLayoutManager).getOrientation());
		mRecyclerView.addItemDecoration(dividerItemDecoration);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {


		View view = inflater.inflate(R.layout.fragment_assets, container, false);


		mRecyclerView = (RecyclerView) view.findViewById(R.id.assetrecycler);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		buyItem = (Button) view.findViewById(R.id.buy);

		buyItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Kaldon_buyclick", "worked?");
				Intent intent = new Intent(getContext(), BuyActivity.class);
				ActivityOptions options =
						ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
				startActivity(intent, options.toBundle());
			}
		});

		Log.d("yolo hi", "Asset got createView()");


		return view;
	}


	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
	}


}


