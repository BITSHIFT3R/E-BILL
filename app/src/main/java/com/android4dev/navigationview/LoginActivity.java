/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.android4dev.navigationview;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import com.android4dev.navigationview.app.AppConfig;
import com.android4dev.navigationview.app.AppController;
import com.android4dev.navigationview.app.CheckNetwork;
import com.android4dev.navigationview.help.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class LoginActivity extends Activity {
	// LogCat tag
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnLogin;
	private Button btnLinkToRegister;String jsonResult;
	private EditText inputEmail;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private SessionManager session;
	SharedPreferences sf=null;
	SQLiteDatabase db;
	String surveystatus;
	String imei;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		db=openOrCreateDatabase("Lesco_data", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS emp(uid varchar,Div VARCHAR,flag varchar(2),loginemail varchar(50));");
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Session manager
		session = new SessionManager(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(LoginActivity.this,Version.class);

			startActivity(intent);
			finish();
		}

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();

				// Check for empty data in the form
				if (email.trim().length() > 0 && password.trim().length() > 0) {
					// login user
					if(CheckNetwork.isInternetAvailable(LoginActivity.this))  //if connection available
					{
						checkLogin(email, password);
					}
					else
					{
						Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "No INTERNET", Snackbar.LENGTH_LONG);
						View sview = snack.getView();
						TextView tv = (TextView) sview.findViewById(android.support.design.R.id.snackbar_text);
						tv.setTextColor(Color.WHITE);
						snack.show();
					}

				} else {
					// Prompt user to enter credentials
					Toast.makeText(getApplicationContext(),
							"Please enter the credentials!", Toast.LENGTH_LONG)
							.show();
				}
			}

		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	/**
	 * function to verify login details in mysql db
	 * */
	private void checkLogin(final String email, final String password) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		pDialog.setMessage("Logging in ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_REGISTER, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				//response=response.replaceAll("﻿﻿","");
				response=response.substring(9);

				Log.d(TAG, "Login Response: "+  response.toString());
				hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");
					String id=jObj.getString("uid");
					System.out.println(id);
					// Check for error node in json
					if (!error) {
						// user successfully logged in
						// Create login session
						session.setLogin(true);
						String s=jObj.getString("division");
						System.out.println("div"+s);
						String mid=jObj.getString("uid");
						surveystatus=jObj.getString("survey_status");
						String loginemail=jObj.getString("email");
						// Launch main activity
						System.out.println("login"+surveystatus);
						//db.execSQL("DROP TABLE "+"transa");
						//db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
						db.execSQL("INSERT INTO emp VALUES('" + id + "','" + s + "','" + surveystatus + "','" + loginemail + "');");
						Intent intent1 = new Intent(LoginActivity.this,
								Version.class);
						intent1.putExtra("email",id);
						intent1.putExtra("div", s);
						intent1.putExtra("flag", surveystatus);
						intent1.putExtra("usermail", loginemail);
						startActivity(intent1);

						finish();
					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
						Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "INVALID EMAIL OR PASSWORD", Snackbar.LENGTH_LONG);
						View view = snack.getView();
						TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
						tv.setTextColor(Color.RED);
						snack.show();
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),"Msg"+e, Toast.LENGTH_LONG).show();

				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Login Error: " + error.getMessage());
				//Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();
				hideDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "login");
				params.put("email", email);
				params.put("password", password);
				params.put("imei", imei);

				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mode, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.action_settings1:{
				MyAsyncTask<Void, Void, Void> updateTask = new MyAsyncTask<Void, Void, Void>(LoginActivity.this);
				updateTask.execute();
				return true;
			}

		}
		return true;



	}
	public class MyAsyncTask<Params, Progress, Result> extends
			AsyncTask<Params, Progress, Result> {

		private final String DIALOG_MESSAGE = "Downloading Data..";

		private ProgressDialog mDialog = null;

		private void setDialog(Context context) {
			this.mDialog = new ProgressDialog(context);
			this.mDialog.setMessage(DIALOG_MESSAGE);
			this.mDialog.setCancelable(false);
		}

		public MyAsyncTask(Context context) {
			this.setDialog(context);
		}

		@Override
		protected void onPreExecute() {
			this.mDialog.show();
		}

		@Override
		protected Result doInBackground(Params... arg0) {
			// Place your background executed method here
			try {
				Thread.sleep(2000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Result result) {
			// Update the UI if u need to

			// And then dismiss the dialog
			if (this.mDialog.isShowing()) {
				this.mDialog.dismiss();
			}
		}
	}

	public StringBuilder inputStreamToString(InputStream is) {
		String rLine = "";
		StringBuilder answer = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((rLine = rd.readLine()) != null) {
				answer.append(rLine);
			}
		}

		catch (IOException e) {
			// e.printStackTrace();
			Toast.makeText(LoginActivity.this,"Error..." + e.toString(), Toast.LENGTH_LONG).show();
		}
		return answer;
	}
}
