package com.android4dev.navigationview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import android.app.Activity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.widget.Toast;
/**
 * Created by Abhishek on 15-Jan-16.
 */
public class Details extends Fragment {
    Button bfind;
    EditText etemp,etorg;
    HttpPost httppost;CheckBox unmtrd;
    HttpPost httppost1;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;String email;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        email= bundle.getString("email");
        bfind = (Button)v.findViewById(R.id.btnfind);
        etemp = (EditText)v.findViewById(R.id.etfind);
        etorg=(EditText)v.findViewById(R.id.etorg);
        unmtrd=(CheckBox)v.findViewById(R.id.checkunmtrd);
        bfind.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Validating user...", true);
                login();

            }


        });
        unmtrd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent obj1=new Intent(Details.this,Unmetered.class);

                startActivity(obj1);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            }
        });
        return v;
    }
    void login(){
        try{
            //Toast.makeText(Details.this,"login", Toast.LENGTH_SHORT).show();
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://www.aditya.multifacet-software.com/railway/find.php");

            // make sure the url is correct.
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("empcode",etemp.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("orgcode",etorg.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];

            nameValuePairs.add(new BasicNameValuePair("email",email));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);

            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            String s=response;
            s=s.replaceAll("ï»¿","");
            System.out.println("Response : " + response);
           getActivity().runOnUiThread(new Runnable() {
               public void run() {

                   dialog.dismiss();
               }
           });

            if(s.equals("User Found")){
               getActivity().runOnUiThread(new Runnable() {
                   public void run() {
                       Toast.makeText(getActivity(), "Got Hit", Toast.LENGTH_SHORT).show();
                   }
               });


                if(etorg.getText().toString().length()>0)
                {
                    Intent intent1 = new Intent(this, OrgDetails.class);
                    intent1.putExtra("orgcode",etorg.getText().toString());

                    intent1.putExtra("email", email);

                    startActivity(intent1);
                }
                else
                {
                    Intent intent = new Intent(Details.this, Details.class);
                    intent.putExtra("empid", etemp.getText().toString());

                    intent.putExtra("email", email);

                    startActivity(intent);
                }
            }else{
                showAlert();
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }



    public void showAlert(){
        ((Activity) getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error");
                builder.setMessage("User not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item2 = menu.findItem(R.id.action_settings3);
        item2.setVisible(false);
        MenuItem item3 = menu.findItem(R.id.action_scan);
        item3.setVisible(false);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {


            // Launching the login activity
            getActivity().finish();
            System.exit(0);
        }


        return super.onOptionsItemSelected(item);
    }

}
