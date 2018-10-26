package com.android4dev.navigationview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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

/**
 * Created by sanjay on 14-Apr-16.
 */
public class RateCard extends Fragment {
    String Catagory ;
    String supplytype;
    double CurrentReading;
    double PreviousReading;
    double Actualreading;
    double FC;
    double Temp;
    double Temp1;
    double Temp2;
    double kwrate;
    double Ec;
    double price;
    double billrate1;
    double billrate2;
    double billrate3;
    double billrate4;
    Spinner sp1,sp2;
    EditText et1,et2,et3,et4,et5,et6,et7;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rate_card, container, false);

        sp1=(Spinner)v.findViewById(R.id.rcsp1);
        sp2=(Spinner)v.findViewById(R.id.rcst);
        et1=(EditText)v.findViewById(R.id.rcar);
        et2=(EditText)v.findViewById(R.id.rcpr);
        et3=(EditText)v.findViewById(R.id.rckw);
        et4=(EditText)v.findViewById(R.id.rcb1);
        et5=(EditText)v.findViewById(R.id.rcb2);
        et6=(EditText)v.findViewById(R.id.rcb3);
        et7=(EditText)v.findViewById(R.id.rcb4);
        Actualreading = Double.parseDouble(et1.getText().toString());
        PreviousReading = Double.parseDouble(et2.getText().toString());
        kwrate = Double.parseDouble(et3.getText().toString());
        billrate1 = Double.parseDouble(et4.getText().toString());
        billrate2 = Double.parseDouble(et5.getText().toString());
        billrate3 = Double.parseDouble(et6.getText().toString());
        billrate4 = Double.parseDouble(et7.getText().toString());
        sp1.setOnClickListener((View.OnClickListener) area1);
        sp2.setOnClickListener((View.OnClickListener) area2);
        store();
        return v;
    }
    AdapterView.OnItemSelectedListener area1 = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> spinner, View container,
                                   int position, long id) {

            Catagory = sp1.getSelectedItem().toString();


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };
    AdapterView.OnItemSelectedListener area2 = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> spinner, View container,
                                   int position, long id) {

            supplytype = sp1.getSelectedItem().toString();


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };


    void store(){

        System.out.println("hello");
        CurrentReading=Actualreading-PreviousReading;
        if (Catagory != "abc")
        {

            if(Catagory =="LMV1")
            {
                if(supplytype !="xyz")
                {
                    if (supplytype == "10")
                    {

                        if (supplytype == "10" && kwrate == 1) {
                            FC=50.00;
                            FC=FC*kwrate;
                            if (CurrentReading <= 50) {
                                //FC=FC*kwrate;
                                Ec=CurrentReading*billrate1;
                                //  price=FC+Ec;
                            } else if (CurrentReading > 50 && CurrentReading <= 150) {

                                //FC=FC*kwrate;
                                Temp=50*billrate1;
                                Ec=(CurrentReading-50)*billrate2;
                                System.out.println("EC1"+Ec);
                                Ec=Ec+Temp;
                                System.out.println("EC2"+Ec);

                            }
                            else if (CurrentReading > 150 ) {

                                //FC=FC*kwrate;
                                Temp=50*billrate1;
                                Ec=(CurrentReading-50)*billrate2;
                                System.out.println("EC1"+Ec);
                                Ec=Ec+Temp;
                                System.out.println("EC2"+Ec);
                                showAlert();

                            }
                            price=FC+Ec;
                            System.out.println("price"+price);
                        }
                        else {
                            FC=90.00;
                            FC=FC*kwrate;
                            if (CurrentReading <= 150) {
                                Ec=CurrentReading*billrate1;
                                //price=FC+Ec;
                            } else if (CurrentReading > 150 && CurrentReading <= 300) {
                                Temp=150*billrate1;
                                Ec=(CurrentReading-150)*billrate2;
                                Ec=Ec+Temp;
                            } else if (CurrentReading > 300 && CurrentReading <= 500) {
                                Temp=150*billrate1;
                                Temp1=150*billrate2;
                                Ec=(CurrentReading-300)*billrate3;
                                Ec=Temp+Temp1+Ec;
                                //  price=FC+Ec;
                            } else if (CurrentReading > 500) {
                                Temp=150*billrate1;
                                Temp1=150*billrate2;
                                Temp2=200*billrate3;
                                Ec=(CurrentReading-300)*billrate4;
                                Ec=Temp+Temp1+Ec;
                            }
                            price=FC+Ec;
                        }
                    }
                    else  if (supplytype == "11") {
                        FC=90.00;
                        FC = FC*kwrate;

                        if (supplytype == "11") {
                            if (CurrentReading <= 150) {

                                Ec = CurrentReading * billrate1;
                                //  price=FC+Ec;
                            } else if (CurrentReading > 150 && CurrentReading <= 300) {
                                Temp = 150 * billrate1;
                                Ec = (CurrentReading - 150) * billrate2;
                                Ec = Temp + Ec;
                            } else if (CurrentReading > 300 && CurrentReading <= 500) {
                                Temp = 150 * billrate1;
                                Temp1= 150 * billrate2;
                                Ec = (CurrentReading - 300) * billrate3;
                                Ec = Temp+Temp1+ Ec;
                            } else if (CurrentReading > 500) {
                                Temp = 150 * billrate1;
                                Temp1= 150 * billrate2;
                                Temp2= 200 * billrate3;
                                Ec = (CurrentReading - 300) * billrate4;
                                Ec = Temp+Temp1+ Ec;
                            }

                        }
                        price = FC + Ec;
                    }
                    else  if (supplytype == "12")
                    {   FC=85.00;
                        FC=FC*kwrate;
                        if (supplytype == "12") {

                            Ec=CurrentReading*billrate1;

                        }
                        price=FC+Ec;
                    }
                }



            }
            else if(Catagory =="LMV2") {
                if (supplytype != "xyz") {
                    if (supplytype == "20") {

                        if (supplytype == "20" ) {
                            FC=225.00;
                            FC=FC*kwrate;
                            if (CurrentReading <= 300) {

                                Ec=CurrentReading*billrate1;

                            } else if (CurrentReading > 300 && CurrentReading <= 1000) {
                                Temp =300*billrate1;
                                Ec=(CurrentReading-300)*billrate2;
                                Ec=Temp+Ec;
                            }
                            else if (CurrentReading > 1000) {
                                Temp =300*billrate1;
                                Temp1=700*billrate2;
                                Ec=(CurrentReading-1000)*billrate3;
                                Ec=Temp+Temp1+Ec;
                            }
                            price=FC+Ec;
                        }
                    }
                    else if (supplytype == "22")
                    {   FC=225.00;
                        FC=FC*kwrate;
                        if (CurrentReading <= 300) {

                            Ec=CurrentReading*billrate1;

                        } else if (CurrentReading > 300 && CurrentReading <= 1000) {
                            Temp =300*billrate1;
                            Ec=(CurrentReading-300)*billrate2;
                            Ec=Temp+Ec;
                        }
                        else if (CurrentReading > 1000) {
                            Temp =300*billrate1;
                            Temp1 =300*billrate2;
                            Ec=(CurrentReading-1000)*billrate3;
                            Ec=Temp+Temp1+Ec;
                        }
                        price=FC+Ec;
                    }
                }
            }
            else if(Catagory =="LMV4A")
            {
                if (supplytype != "xyz")
                {
                    if (supplytype == "41" ||supplytype == "40" ||supplytype == "42" ||supplytype == "43" ||supplytype == "44")
                    {   FC=200.00;
                        FC=FC*kwrate;
                        if (CurrentReading <= 1000) {

                            Ec=CurrentReading*billrate1;

                        }
                        else if (CurrentReading > 1000) {
                            Temp=1000*billrate1;
                            Ec=(CurrentReading-1000)*billrate2;
                            Ec=Temp+Ec;
                        }
                        price=FC+Ec;

                    }
                }
            }
            else if(Catagory =="LMV4B")
            {
                if (supplytype != "xyz")
                {
                    if (supplytype == "46" ||supplytype == "47" )
                    {   FC=225.00;
                        FC=FC*kwrate;
                        if (CurrentReading <= 1000) {
                            Ec=CurrentReading*billrate1;

                        }
                        else if (CurrentReading > 1000) {
                            Temp=1000*billrate1;
                            Ec=(CurrentReading-1000)*billrate2;
                            Ec=Temp+Ec;

                        }
                        price=FC+Ec;

                    }
                }
            }
            else if(Catagory =="LMV5")
            {
                if (supplytype == "52")
                {   FC=75;
                    FC=FC*kwrate;
                    Ec=CurrentReading*billrate1;
                    price=FC+Ec;
                }
            }
            else if(Catagory =="LMV6")
            {
                if (supplytype == "60" ||supplytype == "62" ||supplytype == "64")
                {   FC=225;
                    FC=FC*kwrate;
                    if (CurrentReading <= 1000) {

                        Ec=CurrentReading*billrate1;

                    }
                    else if (CurrentReading > 1000) {
                        Temp=1000*billrate1;
                        Ec=(CurrentReading-1000)*billrate2;
                        Ec=Temp+Ec;
                    }
                    price=FC+Ec;

                }
            }
            else if(Catagory =="LMV7")
            {
                if (supplytype == "72" ||supplytype == "74")
                {   FC=225;
                    FC=FC+kwrate;
                    Ec=CurrentReading*billrate1;
                    price=FC+Ec;
                }
            }
            else if(Catagory =="LMV9")
            {
                if (supplytype == "92" )
                {
                    Ec=CurrentReading*billrate1;

                }
                else if (supplytype == "93")
                {
                    Ec=CurrentReading*billrate1;

                }
                price=Ec;
            }
        }
        System.out.println("Bill genrate is" + price);
        Toast.makeText(getActivity(), (int) price,Toast.LENGTH_SHORT).show();
    }

    public void showAlert(){
        ((Activity) getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("WARNING!Meter reading ");
                builder.setMessage("Press 'Ok to Continue'")
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
}
