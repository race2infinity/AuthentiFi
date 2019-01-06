package com.pratz.authentifi.SellActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.pratz.authentifi.R;

public class BuyActivity extends AppCompatActivity {

	Integer fragmentContainer;
	FragmentManager fragmentManager;
	Button nextButton;

	static String qrcode;

	int i=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);

		fragmentContainer = (R.id.fragment_container);
		fragmentManager = getSupportFragmentManager();

		nextButton = (Button) findViewById(R.id.button_next);
		ImageButton closeButton = (ImageButton) findViewById(R.id.close_button);

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nextScreen();
			}
		});

		nextScreen();



	}

	private void nextScreen() {
		Fragment fragment=null;

		switch(i) {
			case 0:
				//instructions
				fragment = new SellStep0();
				break;

			case 1:
				//nextButton.setVisibility(View.INVISIBLE);
				// /buy -> QRCode, email
				fragment = new BuyStep1();


				break;

			case 2:
				//buyerConfirm
				fragment = new BuyStep2();
				break;

			default:
				fragment = new BuyStep3();

				//getProductDetails QRCode prodCode

		}
		i++;

		fragmentManager.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
				.replace(fragmentContainer, fragment)
				.commit();
	}
}
