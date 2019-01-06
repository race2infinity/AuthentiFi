package com.pratz.authentifi.SellActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.pratz.authentifi.R;

public class SellActivity extends AppCompatActivity {

	int i=0;

	static String qrcode;
	Integer fragmentContainer;
	FragmentManager fragmentManager;
	Button nextButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);

		fragmentContainer = (R.id.fragment_container);
		fragmentManager = getSupportFragmentManager();

		nextScreen();

		nextButton = (Button) findViewById(R.id.button_next);
		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nextScreen();
			}
		});

		ImageButton closeButton = (ImageButton) findViewById(R.id.close_button);

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


	}

	private void nextScreen() {

		Fragment fragment;

		switch(i) {
			case 0:
				fragment = new SellStep0();
				break;

			case 1:
				fragment = new SellStep1();
				break;

			default:
				nextButton.setVisibility(View.INVISIBLE);
				Bundle bundle = new Bundle();
				bundle.putString("retailer", getIntent().getExtras().getString("retailer"));
				fragment = new SellStep2();
				fragment.setArguments(bundle);
		}

		i++;

		fragmentManager.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
				.replace(fragmentContainer, fragment)
				.commit();
	}
}
