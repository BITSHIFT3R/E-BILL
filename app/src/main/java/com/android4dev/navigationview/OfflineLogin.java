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


import com.android4dev.navigationview.app.*;
import com.android4dev.navigationview.help.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class OfflineLogin extends Activity {
    // LogCat tag
    SQLiteDatabase db;
    String div;
Button submit;
    EditText inputemail,inputPassword;
    private ProgressDialog pDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offlinelogin);
        inputemail=(EditText)findViewById(R.id.offlineemail);
        inputPassword=(EditText)findViewById(R.id.offlinepassword);
        submit=(Button)findViewById(R.id.offlinebtnLogin);
        pDialog = new ProgressDialog(this);
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputemail.getText().toString();
                String password = inputPassword.getText().toString();

                // Check for empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(email, password);
                } else if(email.trim().length()== 0) {
                    // Prompt user to enter credentials
                    inputemail.setError("Enter Registered Email");
                } else if (password.trim().length()== 0) {
                    // Prompt user to enter credentials
                    inputemail.setError("Enter Password");
                }
            }

        });
    }
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        System.out.println("email is"+email);
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");

        showDialog();
            db=openOrCreateDatabase("LESCO", Context.MODE_PRIVATE, null);

            String query = "SELECT * FROM users where email ='"+email+"'";

            // get reference of the BookDB database

            Cursor c = db.rawQuery(query, null);
        System.out.println("cursor is"+email);
            if(c.getCount()==0)
            {
                Toast.makeText(getApplicationContext(),
                        "Incorrect Email or Password", Toast.LENGTH_LONG).show();
                hideDialog();
                return;
            }
            else
            {      int count=0;
                Cursor q=db.rawQuery("select division from users where email='" + email + "'", null);

                if(q.moveToFirst())
                {
                    div=q.getString(0);
                    count+=1;
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Incorrect input details", Toast.LENGTH_LONG).show();
                }
                if(count == 1)
                {
                    Intent intent = new Intent(OfflineLogin.this,
                            OfflineMain.class);
                    intent.putExtra("email", email);
                    intent.putExtra("div",div);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"NO Data Found", Toast.LENGTH_LONG).show();
                }

            }
            db.close();
        }

    private void showDialog() {
        if (!pDialog.isShowing())
        pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
