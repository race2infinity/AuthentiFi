package com.pratz.authentifi.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pratz.authentifi.ConnectionManager;
import com.pratz.authentifi.R;

import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends AppCompatActivity {

	EditText name;
	EditText email;
	EditText phone;
	EditText pass;
	EditText passconfirm;
	EditText address;
	Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		name = (EditText) findViewById(R.id.editName);
		email = (EditText) findViewById(R.id.editEmail);
		phone = (EditText) findViewById(R.id.editPhone);
		pass = (EditText) findViewById(R.id.editPass);
		passconfirm = (EditText) findViewById(R.id.editPassConfirm);
		address = (EditText) findViewById(R.id.editAddress);
		submit = (Button) findViewById(R.id.submitbutton);

		name.setHint("Name");
		email.setHint("E-Mail Address");
		phone.setHint("Phone Number");
		pass.setHint("Password");
		passconfirm.setHint("Confirm Password");
		address.setHint("Server Address");

		address.setText(LoginActivity.textAddress);
		Log.i("kaldon-pass", LoginActivity.textAddress);

		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				submit.setEnabled(false);

				try {
					signupValidator();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});



	}

	void signupValidator() throws JSONException {
		String textEmail = email.getText().toString();
		String textName = name.getText().toString();
		String textPhone = phone.getText().toString();
		String textPass = pass.getText().toString();
		String textPassC = passconfirm.getText().toString();
		String textAddress = address.getText().toString();

		int flag=0;

		if(TextUtils.isEmpty(textEmail) || !Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {

			email.setError("Enter a valid E-mail address!");
			flag = 1;
		}

		if (TextUtils.isEmpty(textPass) || textPass.length()<8){
			flag=1;
			pass.setError("Password should be minimum 8 characters");
		}
		else if(!textPass.equals(textPassC))
		{
			pass.getText().clear();
			passconfirm.getText().clear();
			passconfirm.setError("Passwords do not match!");
			flag=1;
		}

		if(textPhone.length()!=10) {
			flag=1;
			phone.setError("Enter a valid phone number (10 digits)");
		}


		if(flag==0)
		{
			RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
			String URL = textAddress+"/signup";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", textName);
			jsonObject.put("email", textEmail);
			jsonObject.put("password", textPass);
			jsonObject.put("phone", textPhone);
			final String requestBody = jsonObject.toString();
			Log.i("KALDONreq", requestBody);

			ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
				@Override
				public void onSuccessResponse(String result) {
					finish();
				}

				@Override
				public void onErrorResponse(VolleyError error) {

					Toast toast = Toast.makeText(SignupActivity.this,
							"Could not connect to server, please try again.",
							Toast.LENGTH_LONG);

					toast.show();

					submit.setEnabled(true);
				}
			});
		}
	}


}
