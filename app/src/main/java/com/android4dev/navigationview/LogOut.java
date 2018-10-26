package com.android4dev.navigationview;

/**
 * Created by Abhishek on 14-Jan-16.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;



import com.android4dev.navigationview.app.*;
import com.android4dev.navigationview.help.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogOut extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    String uid,jsonResult;
    private SQLiteHandler db;
    private SessionManager session;
    SQLiteDatabase d;
    private String url ="http://www.aditya.multifacet-software.com/php/check/logout.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        Intent intent = getIntent();
        uid= intent.getStringExtra("uid");
        d=openOrCreateDatabase("Lesco_data", Context.MODE_PRIVATE, null);
        System.out.println(uid);
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        d.execSQL("delete from " + "emp");
        db.deleteUsers();
        accessWebService();
        // Launching the login activity
        Intent intent = new Intent(LogOut.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private class JsonReadTask extends AsyncTask<String, Void, String> {
        private final String DIALOG_MESSAGE = "Loading Data";

        private ProgressDialog mDialog = null;

        private void setDialog(Context context) {
            this.mDialog = new ProgressDialog(context);
            this.mDialog.setMessage(DIALOG_MESSAGE);
            this.mDialog.setCancelable(false);
        }

        public JsonReadTask(Context context) {
            this.setDialog(context);
        }

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            nameValuePairs.add(new BasicNameValuePair("Logout",uid));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                final String response1 = httpclient.execute(httppost, responseHandler);
                System.out.println("Response : " + response1);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(String result) {
            if(this.mDialog.isShowing())
            {
                this.mDialog.dismiss();
            }

        }
    }// end async task

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask(this);
        // passes values for the urls string array
        task.execute(new String[] { url });
    }

    // build hash set for list view

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
            Toast.makeText(this,"Error..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return answer;
    }


}
