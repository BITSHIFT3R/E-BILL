package com.android4dev.navigationview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

import com.android4dev.navigationview.app.CheckNetwork;

/**
 * Created by Abhishek on 15-Jan-16.
 */
public class Upload  extends Fragment {
    Button b,add;
    EditText et,et1,et2,et3;
    TextView tv;
    HttpPost httppost;
    HttpPost httppost1;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;String email,div;
    ProgressDialog dialog = null;
    SQLiteDatabase db;
    GPSTracker gps;
    double latitude =0.00;
    double longitude=0.00;
    private String jsonResult;
    Button button;
    boolean count;
    String url="http://www.aditya.multifacet-software.com/php/check/upload.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.upload, container, false);
        button=(Button)v.findViewById(R.id.upb1);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        db=getActivity().openOrCreateDatabase("LESCO", Context.MODE_PRIVATE, null);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                isInternetOn();
                System.out.println("Calling Downloading");


            }

        });

        return v;
    }
    void  isInternetOn() {

        if(CheckNetwork.isInternetAvailable(getActivity()))  //if connection available
        {
            gps = new GPSTracker(getActivity());
            if(gps.canGetLocation()){
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if(latitude!=0 && longitude!=0)
                {
                    uploading();
                    System.out.println("nside longitude");

                }
                else{
                    Toast.makeText(getActivity(), "Please Wait Gps fetch the value....  " , Toast.LENGTH_LONG).show();
                    showAlert();
                    uploading();

                }

            }else{
                buildAlertMessageNoGps();
            }
        }
        else
        {
            Snackbar snack = Snackbar.make(getView().findViewById(android.R.id.content), "No INTERNET", Snackbar.LENGTH_LONG);
            View sview = snack.getView();
            TextView tv = (TextView) sview.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }
    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        private final String DIALOG_MESSAGE = "Uploading Data";

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
            this.mDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            String query= "select * from customer_transactions where flag='Y' AND sql_flag='Y'";
            Cursor cursor1= db .rawQuery(query, null);
            System.out.println("sql" + cursor1.getCount());
            if (cursor1.moveToNext()) {
                try {
                    do {
                        System.out.println("inside cursor");
                        String data1 = cursor1.getString(cursor1.getColumnIndex("Acctid"));
                        String data2 = cursor1.getString(cursor1.getColumnIndex("BILLNO"));
                        String data3 = cursor1.getString(cursor1.getColumnIndex("BILL_date"));
                        String data4 = cursor1.getString(cursor1.getColumnIndex("MF"));
                        String data5 = cursor1.getString(cursor1.getColumnIndex("CMD"));
                        String data6 = cursor1.getString(cursor1.getColumnIndex("CURRENT_KWH"));
                        String data7 = cursor1.getString(cursor1.getColumnIndex("PREVIOUS_KWH"));
                        String data8 = cursor1.getString(cursor1.getColumnIndex("CURRENT_KVWH"));
                        String data9 = cursor1.getString(cursor1.getColumnIndex("PREVIOUS_KVWH"));
                        String data10 = cursor1.getString(cursor1.getColumnIndex("BILL_PERIOD"));
                        String data11 = cursor1.getString(cursor1.getColumnIndex("ENERGY_CHARGES"));
                        String data12 = cursor1.getString(cursor1.getColumnIndex("Min_charge"));
                        String data13 = cursor1.getString(cursor1.getColumnIndex("ELECTRICITY_DUTY"));
                        String data14 = cursor1.getString(cursor1.getColumnIndex("REG_SURCHARGES"));
                        String data15 = cursor1.getString(cursor1.getColumnIndex("EX_DEMAND_PANALITY"));
                        String data16 = cursor1.getString(cursor1.getColumnIndex("AREAR_AMOUNT"));
                        String data17 = cursor1.getString(cursor1.getColumnIndex("AREAR_SURCHARGES"));
                        String data18 = cursor1.getString(cursor1.getColumnIndex("CURRENT_SURCHARGES"));
                        String data19 = cursor1.getString(cursor1.getColumnIndex("ADJUSTMENT_AMOUNT"));
                        String data20 = cursor1.getString(cursor1.getColumnIndex("NET_AMOUNT"));
                        String data21 = cursor1.getString(cursor1.getColumnIndex("Rebate"));
                        String data22 = cursor1.getString(cursor1.getColumnIndex("Gross_amount"));
                        String data23 = cursor1.getString(cursor1.getColumnIndex("ID"));
                        String data24 = cursor1.getString(cursor1.getColumnIndex("Previousdate"));
                        String data25 = cursor1.getString(cursor1.getColumnIndex("flag"));
                        String data26 = cursor1.getString(cursor1.getColumnIndex("SubmitBy"));
                        String data27 = cursor1.getString(cursor1.getColumnIndex("image"));
                        nameValuePairs.add(new BasicNameValuePair("acctid", data1));
                        nameValuePairs.add(new BasicNameValuePair("billno", data2));
                        nameValuePairs.add(new BasicNameValuePair("bill_date", data3));
                        nameValuePairs.add(new BasicNameValuePair("mf", data4));
                        nameValuePairs.add(new BasicNameValuePair("cmd", data5));
                        nameValuePairs.add(new BasicNameValuePair("current_kwh", data6));
                        nameValuePairs.add(new BasicNameValuePair("previous_kwh", data7));
                        nameValuePairs.add(new BasicNameValuePair("current_kvwh", data8));
                        nameValuePairs.add(new BasicNameValuePair("previous_kvwh", data9));
                        nameValuePairs.add(new BasicNameValuePair("bill_period", data10));
                        nameValuePairs.add(new BasicNameValuePair("energy_surcharges", data11));
                        nameValuePairs.add(new BasicNameValuePair("min_charges", data12));
                        nameValuePairs.add(new BasicNameValuePair("electricity_duty", data13));
                        nameValuePairs.add(new BasicNameValuePair("reg_surcharges", data14));
                        nameValuePairs.add(new BasicNameValuePair("ex_panality", data15));
                        nameValuePairs.add(new BasicNameValuePair("arrear_ammount", data16));
                        nameValuePairs.add(new BasicNameValuePair("arr_suchrgs", data17));
                        nameValuePairs.add(new BasicNameValuePair("current_surcharges", data18));
                        nameValuePairs.add(new BasicNameValuePair("adjust_amount", data19));
                        nameValuePairs.add(new BasicNameValuePair("net_amount", data20));
                        nameValuePairs.add(new BasicNameValuePair("rebate", data21));
                        nameValuePairs.add(new BasicNameValuePair("gross_ammount", data22));
                        nameValuePairs.add(new BasicNameValuePair("id", data23));
                        nameValuePairs.add(new BasicNameValuePair("previousdaste", data24));
                        nameValuePairs.add(new BasicNameValuePair("flag", data25));
                        nameValuePairs.add(new BasicNameValuePair("submitby", data26));
                        nameValuePairs.add(new BasicNameValuePair("image", data27));
                        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
                        nameValuePairs.add(new BasicNameValuePair("longi", String.valueOf(longitude)));

                        System.out.println("onphp" + data1 + data2 + data3 + data4 + data5 + data6 + data7 + data8 + data9 + data10 + data11 + data12 + data13
                                + data14 + data15 + data16 + data17 + data18 + data19 + data20 + data21 + data22 + data23 + data24 + data25 + data26 + data27);
                        System.out.println("calling php");
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        //  HttpResponse response = httpclient.execute(httppost);
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response1 = httpclient.execute(httppost, responseHandler);
                        System.out.println("Response : " + response1);
                        jsonResult = response1.substring(3);


                    } while (cursor1.moveToNext());

                }
                catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Snackbar snack = Snackbar.make(getActivity().findViewById(android.R.id.content), "No Data For Uploading", Snackbar.LENGTH_LONG);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.RED);
                snack.show();
            }
            return null;
        }



        @Override
        protected void onPostExecute(String result) {

            ListDrwaer();
            if(this.mDialog.isShowing())
            {
                this.mDialog.dismiss();
            }

        }
    }// end async task

    public void uploading() {
        JsonReadTask task = new JsonReadTask(getActivity());
        // passes values for the urls string array
        task.execute(new String[] { url });
    }

    // build hash set for list view
    public void ListDrwaer() {
        try {
            if(jsonResult.length()<14) {
                count=true;
                System.out.println("inside json result ");
                final String x=jsonResult;
                alert(x);
            }
            else if(jsonResult.length()>=14 && jsonResult.length()<18){
                count=false;
                final String x=jsonResult;
                alert(x);
            }
            else if(jsonResult.length()<18){
                final String x=jsonResult;
                alert(x);
            }

        }catch (Exception e) {
        e.printStackTrace();
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
            e.printStackTrace();
           // Toast.makeText(getActivity(),"Error..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return answer;
    }
    public void showAlert(){
        ((Activity) getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(" Error");
                builder.setMessage("GPS Loacation is not found")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Upload fragment = new Upload();
                                Bundle bundle = new Bundle();
                                fragment.setArguments(bundle);
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.offlineframe,fragment).addToBackStack("fragBack").commit();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("  Enable  GPS  ")
                .setCancelable(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public void alert(final String x){
        ((Activity) getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alert");
                builder.setMessage(x)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(x.length()<18)
                                {
                                db.execSQL("delete from "+ "users");
                                db.execSQL("delete from "+ "Customer_Details");
                                db.execSQL("delete from "+ "customer_transactions");
                                db.execSQL("delete from " + "Division_Master");
                                db.close();
                                dialog.cancel();
                                Intent intent=new Intent(getActivity(),SplashActivity.class);
                                getActivity().startActivity(intent);getActivity().finish();
                                }
                                dialog.cancel();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
