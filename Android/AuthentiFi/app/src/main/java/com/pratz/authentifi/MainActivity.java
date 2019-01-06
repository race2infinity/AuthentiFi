package com.pratz.authentifi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pratz.authentifi.Assets.AssetsFragment;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MainActivity extends AppCompatActivity {

	FragmentManager fragmentManager;
	public static String email, address;

	TextView title;
	ImageButton profileButton, closeButton;
	FloatingActionButton scanButton;

	private void askForPermission(String permission, Integer requestCode) {
		if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

				//This is called if user has denied the permission before
				//In this case I am just asking the permission again
				ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

			}
			else {

				ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
			}
		} /*else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }*/
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		askForPermission(Manifest.permission.CAMERA, CAMERA);

		email = getIntent().getExtras().getString("email");
		address = getIntent().getExtras().getString("address");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);

		final Integer fragmentContainer = R.id.fragment_container;
		fragmentManager = getSupportFragmentManager();
		final Fragment assetsFragment = new AssetsFragment();
		final Fragment profileFragment = new MyProfileFragment();
		fragmentManager.beginTransaction().add(fragmentContainer, assetsFragment).commit();

		title = (TextView) findViewById(R.id.title_head);
		title.setText(R.string.assets_head);

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
						.replace(fragmentContainer, assetsFragment)
						.commit();

				title.setText(R.string.assets_head);

				profileButton.setVisibility(View.VISIBLE);
			}
		});

		scanButton = (FloatingActionButton)findViewById(R.id.scan_button);
		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//	Intent intent = new Intent(MainActivity.this, Viewfind.class);
			//	startActivity(intent);

				Intent intent = new IntentIntegrator(MainActivity.this)
						.setPrompt("Scan a product")
						.setOrientationLocked(false)
						.createScanIntent();

				intent.putExtra("retailer", "0");

				startActivityForResult(intent, 0);


			}
		});




		/*BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

				Fragment fragment = null;

				switch (menuItem.getItemId()) {
					case R.id.navigation_assets:
						fragment = Assets;
						break;

					case R.id.navigation_scan:
						openCam();
						return false;

					case R.id.navigation_profile:
						fragment = Profile;
						break;

				}

				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.replace(fragmentContainer, fragment)
					//	.show(fragment)
					//	.hide(fragmentManager.findFragmentById(fragmentContainer))
						.commit();
				return true;
			}
		});*/

		//set screen at boot
		//fragmentManager.beginTransaction().add(fragmentContainer, Profile).hide(Profile).commit();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator
				.parseActivityResult(requestCode, resultCode, intent);

		if (scanResult != null) {
			Log.i("QRcode", scanResult.getContents());
			// handle scan result
			intent = new Intent(MainActivity.this, ProductPage.class);
			Bundle bundle = new Bundle();
			bundle.putString("code", scanResult.getContents());

			if(false)
				bundle.putBoolean("isOwner", true);
			else
				bundle.putBoolean("isOwner", false);

			intent.putExtras(bundle);
			startActivity(intent);
		}
		// else continue with any other code you need in the method

	}


	void pushlogs(Fragment fragment) {

		Log.d("yolo added", Boolean.toString(fragment.isAdded()));
		Log.d("yolo stsvd", Boolean.toString(fragment.isStateSaved()));
		Log.d("yolo detac", Boolean.toString(fragment.isDetached()));
		Log.d("yolo hidde", Boolean.toString(fragment.isHidden()));
		Log.d("yolo visib", Boolean.toString(fragment.isVisible()));
		Log.d("yolo resum", Boolean.toString(fragment.isResumed()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}





}


// Code that I used temporarily until I did it the right way:

/*
class SwipelessViewPager extends ViewPager {

	private boolean isPagingEnabled;

	public SwipelessViewPager(Context context) {
		super(context);
		this.isPagingEnabled = true;
	}

	public SwipelessViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.isPagingEnabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.isPagingEnabled && super.onTouchEvent(event);
	}

	//for samsung phones to prevent tab switching keys to show on keyboard
	@Override
	public boolean executeKeyEvent(KeyEvent event) {
		return isPagingEnabled && super.executeKeyEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return this.isPagingEnabled && super.onInterceptTouchEvent(event);
	}

	public void setPagingEnabled(boolean enabled) {
		this.isPagingEnabled = enabled;
	}
}


class BottomNavigationAdapter extends FragmentPagerAdapter {

	private final List<Fragment> fragmentList = new ArrayList<>();

	BottomNavigationAdapter(FragmentManager manager) {
		super(manager);
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	public void addFragment(Fragment fragment){
		fragmentList.add(fragment);
	}

}*/


/** TODO:
Android App:

 Enviroment 1(Customer):

 Login(username, password)

 Signup(email,password,name,photo(*),phone)

 MyAssets(List of assets, when you click on the assets it takes you to ProductPage)

 ProductPage(Brand name, model no,image,desc,status & Manufacturer(location,timestamp) & ++PreviousOwner(name,timestamp))
 (Button for SELL -> which goes to SellActivity with the code)
 (Button for Report Stolen -> and response)

 Scanner (Can scan NFC code, once scanned goes to ProductPage
 (Brand name, model no,image,desc,status & Manufacturer(location,timestamp))

 MyProfile(Details, and listview of transaction history)
		buyer    (Scanner for scanning sellers QR Code -> Scanner for scanning NFC code -> Confirm)


 Enviroment 2(Retailer):

 Login(username, password)

 Scanner(scans the NFC code, then the same process of second hand)
*/
