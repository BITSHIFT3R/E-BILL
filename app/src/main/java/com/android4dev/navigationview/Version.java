package com.android4dev.navigationview;

/**
 * Created by msspl on 6/9/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Version extends Activity {
    String myVersionName,userverification,apkversion;
    private String response1;
    String jsonResult;
    String saemail,div,surveystatus,loginemail;
    SQLiteDatabase db;
    String url="http://49.50.71.97:88/chinhat/verify.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Intent intent = getIntent();
        saemail= intent.getStringExtra("email");
        div= intent.getStringExtra("div");
        surveystatus= intent.getStringExtra("flag");
        loginemail= intent.getStringExtra("usermail");
        db=openOrCreateDatabase("Lesco_data", Context.MODE_PRIVATE, null);
        Cursor ldc = db.rawQuery("SELECT uid,Div,flag,loginemail FROM emp", null);
        System.out.println("sql" + ldc.moveToFirst());
        if(ldc.moveToFirst())
        {
            saemail=ldc.getString(0);
            div=ldc.getString(1);
            surveystatus=ldc.getString(2);
            loginemail=ldc.getString(3);
            System.out.println(" database division" + saemail + "-" + div+"="+loginemail);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error ", Toast.LENGTH_LONG)
                    .show();
        }
        System.out.println(" database" + saemail + "-" + div + "=" + loginemail);
        Context context = getApplicationContext(); // or activity.getApplicationContext()
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

         myVersionName = "not available"; // initialize String

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
            myVersionName = myVersionName.replace(" ", "");
            Toast.makeText(getApplicationContext(),myVersionName,Toast.LENGTH_LONG).show();
            System.out.println("version : " + myVersionName);
            verify();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
    private class JsonReadTask1 extends AsyncTask<String, Void, String> {
        private final String DIALOG_MESSAGE = "Please Wait Loading...";

        private ProgressDialog mDialog = null;

        private void setDialog(Context context) {
            this.mDialog = new ProgressDialog(context);
            this.mDialog.setMessage(DIALOG_MESSAGE);
            this.mDialog.setCancelable(false);
        }

        public JsonReadTask1(Context context) {
            this.setDialog(context);
        }

        @Override
        protected void onPreExecute() {
            this.mDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            System.out.println("email123" + loginemail);
            nameValuePairs.add(new BasicNameValuePair("div", div));
            nameValuePairs.add(new BasicNameValuePair("user",loginemail));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response1 = httpclient.execute(httppost, responseHandler);
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
            ListDrwaer1();
            System.out.println("calling list");

            if(this.mDialog.isShowing())
            {
                this.mDialog.dismiss();
            }
            System.out.println("Calling Activity");
            callActivity();

        }
    }// end async task
    public void verify() {
        System.out.println("Called json");
        JsonReadTask1 task = new JsonReadTask1(this);
        // passes values for the urls string array
        task.execute(new String[]{url});
    }

    // build hash set for list view
    public void ListDrwaer1() {


        try {
            response1 = response1.substring(3);
            JSONObject jsonResponse = new JSONObject(response1);
            System.out.println("JSON IN LIST" + jsonResponse);
            userverification =jsonResponse.getString("verification");
            System.out.println("JSON IN LIST121341" + userverification);
            JSONArray jsonMainNode1 = jsonResponse.optJSONArray("version");
            System.out.println("uhyg" + jsonMainNode1.length());
            for (int i = 0; i < jsonMainNode1.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode1.getJSONObject(i);
                apkversion=jsonChildNode.optString("apk_version");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void callActivity() {
        System.out.println("called Activity");

        if(userverification.equalsIgnoreCase("[\"Found\"]") && apkversion.equalsIgnoreCase(myVersionName))
        {
            System.out.println("if 1 Activity");
            Intent intent=new Intent(Version.this,SplashActivity.class);
           intent.putExtra("email", saemail);
            intent.putExtra("div",div);
            startActivity(intent);
            finish();
        }
        else if(userverification.equals("[\"Not Found\"]"))
        {
            System.out.println("if 2 Activity");
            Intent intent=new Intent(Version.this,LogOut.class);
            intent.putExtra("uid", saemail);
            startActivity(intent);
        }
        else if(!apkversion.equals(myVersionName))
        {
            System.out.println("if 3 Activity");
             AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                    alertDialog.setTitle("Title");
                    alertDialog.setMessage("Update Required");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "well come",Toast.LENGTH_LONG).show();
                        }
                    });
                    alertDialog.setButton2("cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "yoy have pressed cancel", Toast.LENGTH_LONG).show();
                        }
                    });
                    // Set the Icon for the Dialog
                    alertDialog.setIcon(R.drawable.alert);
                    alertDialog.show();
                    // see http://androidsnippets.com/simple-alert-dialog-popup-with-title-message-icon-and-button

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
            e.printStackTrace();
            System.out.println("Exception"+e);
        }
        return answer;
    }
    public Dialog onCreateDialog(Context savedInstanceState) {

        AlertDialog.Builder b=  new  AlertDialog.Builder(getApplicationContext())
                .setTitle("Update Required")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                               Toast.makeText(getApplicationContext(),"Ok Press",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getApplicationContext(),"Cancel Press",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }

                );

        return b.create();
    }
}
