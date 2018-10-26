package com.android4dev.navigationview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


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


import android.app.Activity;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by Abhishek on 15-Jan-16.
 */
public class OfflineAct extends Fragment {
    private String jsonResult,jsonloc,mtrtype;
    TextView et,et1,et2,et3,et4,et5,et6,et8,et9,mapsub,maptran,mappole;
    HttpResponse response;
    String[] countries = new String[] {
            "Shop",
            "Residence",
            "Ata Chakki",
            "Factory",
            "Others"

    };

    String[] type = new String[] {
            "Mechanical",
            "Digital",
            "Electronic",


    };
    String[] make = new String[] {
            "Secure",
            "Others"

    };
    String[] lati,longi;
    double latitude;
    double longitude ;
    String name,fname,area,mark,add,mtrno,no,nom,sarea,acno,smeter,sbook,sscno,email,lat,log,gis,subs,trans,supplytype;
    Spinner sp,sp1,sp2;	String text,text1,text2,set1="actmtr",set2="actbuild",set3="actdoc";
    Button b,cammtr,camdoc,cambld;
    SQLiteDatabase db;
    String sql1,ofaDiv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.offline_details, container, false);
        setHasOptionsMenu(true);
        db=getActivity().openOrCreateDatabase("LESCO", Context.MODE_PRIVATE, null);
        Bundle bundle = this.getArguments();
        email=  bundle.getString("email");
        acno =  bundle.getString("uname");
        sarea =  bundle.getString("area");
        smeter = bundle.getString("meter");
        sbook =  bundle.getString("book");
        sscno =  bundle.getString("scno");
        System.out.print("rece"+acno+"-"+sarea);
        sql1="select Code from Division_Master where Id='"+sarea+"'";
        Cursor q1=db.rawQuery("select Code from Division_Master where Id='"+sarea+"'",null);
        if( q1 != null && q1.moveToFirst() ){
            ofaDiv = q1.getString(q1.getColumnIndex("Code"));
        }

        System.out.println("offine Div"+ofaDiv);
        if(acno.length() > 0)
        {
          String  sql = "select PerName,MailAdd1,MtrSrlNo,gispoleid,ACCTID,CurrMtrStatus,SubDiv,DTCode,stcode from Customer_Details where  ACCTID='"+acno+"'  AND Div='"+ofaDiv+"'";
        System.out.print("sql"+sql);
            Cursor c =db.rawQuery(sql,null);
            if(c != null && c.moveToFirst())
            { System.out.print("IN Acc No");
                name=c.getString(0);
                add=c.getString(1);
                mtrno=c.getString(2);
                gis=c.getString(3);
                acno=c.getString(4);
                mtrtype=c.getString(5);
                subs=c.getString(6);
                trans=c.getString(7);
                supplytype=c.getString(8);
                //mtrtype=c.getString(8);

            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"Incorrect input details", Toast.LENGTH_LONG).show();
            }

        }
        else if(smeter.length() > 0)
        {
           String  sql = "select PerName,MailAdd1,MtrSrlNo,gispoleid,ACCTID,CurrMtrStatus,SubDiv,DTCode,gispoleid,stcode from Customer_Details where  MtrSrlNo='"+smeter+"'  AND Div=(select Code from Division_Master where Id='"+sarea+"')";
            Cursor c =db.rawQuery(sql,null);
            System.out.print("data"+c.moveToFirst());
            if(c != null && c.moveToFirst())
            {
                name=c.getString(0);
                add=c.getString(1);
                mtrno=c.getString(2);
                no=c.getString(3);
                gis=c.getString(4);
                subs=c.getString(5);
                trans=c.getString(6);
                acno=c.getString(7);
                mtrtype=c.getString(8);
                supplytype=c.getString(9);

            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"Incorrect input details", Toast.LENGTH_LONG).show();
            }


        }
        System.out.println(name+add+"-"+mtrno+"-"+no+"="+gis+"-"+subs+"-"+trans+"-"+acno+"-"+mtrtype);
        et1 = (TextView)v.findViewById(R.id.offlinemainname);
        et2 = (TextView)v.findViewById(R.id.offlinemainfather);
        et3 = (TextView)v.findViewById(R.id.offlinemainadd);
        et4 = (TextView)v.findViewById(R.id.offlinemainnumber);
        et5 = (TextView)v.findViewById(R.id.offlinemainnmr);
        et6= (TextView)v.findViewById(R.id.offlinemainmtrno);
        et8= (TextView)v.findViewById(R.id.offlinemainarea);
        et9= (TextView)v.findViewById(R.id.offlinemainmark);
        mapsub = (TextView)v.findViewById(R.id.offlineetactsub);
        maptran = (TextView)v.findViewById(R.id.offlineetacttrans);
        mappole = (TextView)v.findViewById(R.id.offlineetactpole);
        sp1= (Spinner)v.findViewById(R.id.offlineetmtrtype);
        cammtr=(Button)v.findViewById(R.id.offlinemainmter);
        camdoc=(Button)v.findViewById(R.id.offlinemaindocx);
        cambld=(Button)v.findViewById(R.id.offlinemainbuild);
        Toast.makeText(getActivity(),acno + sarea, Toast.LENGTH_SHORT).show();

        sp=(Spinner)v.findViewById(R.id.offlinespprocess);
        sp2=(Spinner)v.findViewById(R.id.offlinespmtrmake);
        b = (Button)v.findViewById(R.id.offlinemainsubmit);
        b.setVisibility(View.INVISIBLE);
        accessWebService();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, countries);

        // Setting the array adapter containing country list to the spinner widget
        sp.setAdapter(adapter);
        OnItemSelectedListener area = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {

                text1 = sp.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        sp.setOnItemSelectedListener(area);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, type);

        // Setting the array adapter containing country list to the spinner widget
        sp1.setAdapter(adapter1);
        OnItemSelectedListener area1 = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {

                text = sp1.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        sp1.setOnItemSelectedListener(area1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, make);

        // Setting the array adapter containing country list to the spinner widget
        sp2.setAdapter(adapter2);
        OnItemSelectedListener area2 = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {

                text2 = sp2.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        sp2.setOnItemSelectedListener(area2);
        b = (Button)v.findViewById(R.id.mainsubmit);

        accessWebService();

        cammtr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        camera fragment = new camera();
                        Bundle bundle = new Bundle();
                        bundle.putString("set", set1);
                        bundle.putString("acno", acno);
                        bundle.putString("email",email);
                        fragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                        fragmentTransaction.replace(R.id.offlineframe, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).start();
            }
        });
        camdoc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        camera fragment = new camera();
                        Bundle bundle = new Bundle();
                        bundle.putString("set", set3);
                        bundle.putString("acno", acno);
                        bundle.putString("email",email);
                        fragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                        fragmentTransaction.replace(R.id.offlineframe, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).start();
            }
        });
        cambld.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        camera fragment = new camera();
                        Bundle bundle = new Bundle();
                        bundle.putString("set", set2);
                        bundle.putString("acno", acno);
                        bundle.putString("email",email);
                        fragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                        fragmentTransaction.replace(R.id.offlineframe, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).start();
            }
        });
        return v;

    }
 public  void accessWebService(){
     et1.setText(name);
     et2.setText("-");
     et3.setText(add);
     et4.setText(no);
     et5.setText(nom);
     et6.setText(mtrno);
     et8.setText(area);
     et9.setText(mark);
     mappole.setText(gis);
     mapsub.setText(subs);
     maptran.setText(trans);
 }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.main, menu);

        MenuItem Import = menu.findItem(R.id.action_scan);
        Import.setVisible(false);
        MenuItem Import3 = menu.findItem(R.id.action_home);
        Import3.setVisible(false);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.action_settings1)  {
            new_connection fragment = new new_connection();
            Bundle bundle = new Bundle();
            bundle.putString("acno", acno);
            bundle.putString("div", sarea);
            bundle.putString("email", email);
            bundle.putString("meterNumber",mtrno);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.offlineframe, fragment).addToBackStack("fragBack").commit();





        }
        if (id == R.id.action_settings3)  {
            OfflineBilling fragment = new OfflineBilling();
            System.out.println("ReceivingACNO"+acno+"\n"+"email"+email+"\n"+"name"+name+"mtrno"+mtrno);
            Bundle bundle = new Bundle();
            bundle.putString("acno", acno);
            bundle.putString("div", sarea);
            bundle.putString("email", email);
            bundle.putString("customerName", name);
            bundle.putString("meterNumber", mtrno);
            bundle.putString("supplytype",supplytype);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.offlineframe, fragment).addToBackStack("fragBack").commit();



        }
        return true;
    }

}