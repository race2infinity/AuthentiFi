package com.pratz.authentifi.User;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pratz.authentifi.ConnectionManager;
import com.pratz.authentifi.MainActivity;
import com.pratz.authentifi.RetailerActivity.MainRetailerActivity;
import com.pratz.authentifi.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LoginRetailerActivity extends AppCompatActivity {

	String filename = "logincredentials.aut";
	static String textAddress;
	User user;

	EditText email, password, address;
	Button submit;
	TextView signup, retailer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_login);

		email = (EditText) findViewById(R.id.editEmail);
		password = (EditText) findViewById(R.id.editPass);
		address = (EditText) findViewById(R.id.editAddress);
		submit = (Button) findViewById(R.id.submitbutton);
		signup = (TextView) findViewById(R.id.signupbutton);
		retailer = (TextView) findViewById(R.id.retailerbutton);

		signup.setVisibility(View.INVISIBLE);

		email.setHint("Email");
		password.setHint("Password");
		address.setHint("Server Address");

		retailer.setText(R.string.userlogin);

		try {
			FileInputStream fileInputStream = openFileInput(filename);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			email.setText(bufferedReader.readLine());
			password.setText(bufferedReader.readLine());
			address.setText(bufferedReader.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}



		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				/*
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				*/

				submit.setEnabled(false);
				Log.d("John", address.getText().toString());
				final String textEmail = email.getText().toString();
				final String textPass = password.getText().toString();
				textAddress = address.getText().toString();

				RequestQueue requestQueue = Volley.newRequestQueue(LoginRetailerActivity.this);
				String URL = textAddress+"/retailerLogin";
				Log.i("Kaldon-urllogin", URL);
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("email", textEmail);
					jsonObject.put("password", textPass);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				final String requestBody = jsonObject.toString();

				ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
					@Override
					public void onSuccessResponse(String result) {
						Log.i("KALDONi", result);
						Intent intent = new Intent(LoginRetailerActivity.this, MainRetailerActivity.class);
						intent.putExtra("email", textEmail);
						intent.putExtra("address", textAddress);
						startActivity(intent);


						try {
							user = new User(textEmail, textPass);
							FileOutputStream fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
							outputStreamWriter.write(user.getEmail());
							outputStreamWriter.append("\n");
							outputStreamWriter.append(user.getPassword());
							outputStreamWriter.append("\n");
							outputStreamWriter.append(address.getText());
							outputStreamWriter.flush();
							outputStreamWriter.close();

						} catch (IOException e) {
							e.printStackTrace();
						}


						finish();
					}

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast toast = Toast.makeText(LoginRetailerActivity.this,
								"Could not login, please try again.",
								Toast.LENGTH_LONG);

						toast.show();

						submit.setEnabled(true);
					}
				});
			}

		});

		signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginRetailerActivity.this, SignupActivity.class);
				startActivity(intent);

			}
		});

		retailer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginRetailerActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}


	});
	}

	void loginSuccessful() {
		Log.d("John", "Logged in!");
		Intent intent = new Intent(getApplicationContext(), MainRetailerActivity.class);
		startActivity(intent);
		finish();
	}

}

