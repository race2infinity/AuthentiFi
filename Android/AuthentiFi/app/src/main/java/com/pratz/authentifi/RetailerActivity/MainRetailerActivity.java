package com.pratz.authentifi.RetailerActivity;

import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.pratz.authentifi.Assets.AssetsFragment;
import com.pratz.authentifi.MainActivity;
import com.pratz.authentifi.MyProfileFragment;
import com.pratz.authentifi.R;

public class MainRetailerActivity extends AppCompatActivity {

	TextView title;
	ImageButton profileButton, closeButton;
	public static String email, address;
	FragmentManager fragmentManager;



	private void askForPermission(String permission, Integer requestCode) {
		if (ContextCompat.checkSelfPermission(MainRetailerActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(MainRetailerActivity.this, permission)) {

				//This is called if user has denied the permission before
				//In this case I am just asking the permission again
				ActivityCompat.requestPermissions(MainRetailerActivity.this, new String[]{permission}, requestCode);

			}
			else {

				ActivityCompat.requestPermissions(MainRetailerActivity.this, new String[]{permission}, requestCode);
			}
		} /*else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }*/
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_retailer);
		email = getIntent().getExtras().getString("email");
		address = getIntent().getExtras().getString("address");

		title = (TextView) findViewById(R.id.title_head);
		title.setText(R.string.assets_head);


		final Integer fragmentContainer = R.id.fragment_container;
		fragmentManager = getSupportFragmentManager();
		final Fragment homeFragment = new RetailerHomeFragment();
		final Fragment profileFragment = new MyProfileFragment();
		fragmentManager.beginTransaction().add(fragmentContainer, homeFragment).commit();


		profileButton = (ImageButton) findViewById(R.id.profile_button);
		profileButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				profileButton.setVisibility(View.INVISIBLE);

				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.replace(fragmentContainer, profileFragment)
						.commit();

				title.setText(R.string.profile_head);
				closeButton.setVisibility(View.VISIBLE);
			}
		});

		closeButton = (ImageButton) findViewById(R.id.close_button);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeButton.setVisibility(View.INVISIBLE);

				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.replace(fragmentContainer, homeFragment)
						.commit();

				title.setText(R.string.assets_head);

				profileButton.setVisibility(View.VISIBLE);
			}
		});



	}
}
