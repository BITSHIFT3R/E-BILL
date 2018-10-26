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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sanjay on 14-Apr-16.
 */
public class Billing extends Fragment {
  Button b,b1;
    Uri imageUri;String message;
    Uri selectedImageUri= null;
    private Bitmap bitmap;String sp,data;
    private ProgressDialog pDialog,dialog;
    String jsonResult;
    String currentReading,mdivalue;
    Double p,q,reading;
    String prev;
    String totslkw,Catagory,customerName,meterNumber;
    double Temp,Ec,FC;
    double price,Temp1,Temp2,x;
    double CeilingAmmt,TexAmmt,ArrearLastBill,LPCAmt,SBMLPCAmt,regulatorySchg1,regulatorySchg2,SBMBillCurrAmt,SBMTotBillAmt,SBMBillArrearAmt;
    int SBMTotBillAmt1;
    String Lastbill,Lpc,mailadd;
    String email,supplytype,grossammount,Exchagedemandpanality;
    String[] mtrexcep = new String[] {
            "Normal",
            "Wrong Tariff",
            "Meter Not Advised",
            "House Locked",
            "Meter Defective",
            "Line Disconnected",
            "Temporary Disconnection",
            "Mechanical Meter"

    };
    String[] prmexcep = new String[] {
            "Normal",
            "Premises Exception",
            "Bank/LIC Office",
            "Hotel",
            "Nursing Home",
            "Resturant",
            "Guest House",
            "Banquet Hall",
            "Big Showrom",
            "GOVT Office"

    };
    String ims,prevdate;
    EditText MDI,catcmd,catkvah;Spinner sp1,sp2;String text1,text2;
    EditText et;String acno;HttpResponse response;
    GPSTracker gps;
    String location;
    double latitude =0.00;
    double longitude=0.00;
    double kwrate;
    String psupplytype;
    String ccmd,kvah;
    TableRow tablerowcmd,tablerowkvah,tablerowmdi,tablerowprerd;
    String totavgestunit;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.billing, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        meterNumber= bundle.getString("meterNumber");
        acno = bundle.getString("acno");
        customerName = bundle.getString("customerName");
        email = bundle.getString("email");
        psupplytype = bundle.getString("supplytype");
        System.out.println("ReceivingACNO"+acno+"\n"+"meterNumber"+meterNumber+"\n"+"customerName"+customerName+"-"+psupplytype);
        b = (Button)v.findViewById(R.id.buttonsbmit);
        b1 = (Button)v.findViewById(R.id.btnmtrcptr);
        et = (EditText)v.findViewById(R.id.etpresent);
        MDI=(EditText)v.findViewById(R.id.editText2);
       // sp1=(Spinner)v.findViewById(R.id.spinnermtrexc);
       // sp2=(Spinner)v.findViewById(R.id.spinnerexcept);
        tablerowcmd=(TableRow)v.findViewById(R.id.btrcmd);
        tablerowkvah=(TableRow)v.findViewById(R.id.btrkvah);
        tablerowmdi=(TableRow)v.findViewById(R.id.btrmdi);
        tablerowprerd=(TableRow)v.findViewById(R.id.btrpr);
        tablerowcmd.setVisibility(View.GONE);
        tablerowkvah.setVisibility(View.GONE);
        if(psupplytype.equals("11") || psupplytype.equals("22")|| psupplytype.equals("44")|| psupplytype.equals("47"))
        {   tablerowcmd.setVisibility(View.VISIBLE);
            tablerowkvah.setVisibility(View.VISIBLE);
            catcmd= (EditText)v.findViewById(R.id.betkvahcmd);
            catkvah= (EditText)v.findViewById(R.id.betkvah);
         //   catcmd.setVisibility(View.VISIBLE);
          //  catkvah.setVisibility(View.VISIBLE);
            ccmd=catcmd.getText().toString();
            ccmd=catkvah.getText().toString();

        }
        else
        {
            tablerowcmd.setVisibility(View.GONE);
            tablerowkvah.setVisibility(View.GONE);
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentReading=et.getText().toString();
                mdivalue=MDI.getText().toString();
                gps = new GPSTracker(getActivity());

                // check if GPS enabled
                if(gps.canGetLocation()){

                   latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude,
                            getActivity().getApplicationContext(), new GeocoderHandler());
                  //  latitude=20.18;
                  //  longitude=212.22;
                    if(latitude!=0 && longitude!=0)
                    {
                        if (bitmap == null) {
                            Toast.makeText(getActivity(),
                                    "Please Select Image ..", Toast.LENGTH_SHORT).show();
                        } else {

                            System.out.println("LOACATION");
                            if(currentReading != "") {
                                new Thread(new Runnable() {
                                    public void run() {
                                        calljson1();

                                    }
                                }).start();
                            }
                            else
                            {
                                et.setError("Enter Reading");
                            }
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "Please Wait Gps fetch the value....  " , Toast.LENGTH_LONG).show();
                        location();

                    }
                    Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


                }else{
                    buildAlertMessageNoGps();
                }

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName =acno+".jpg";
                //create parameters for Intent with filename
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                //create new Intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 1);

            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String filePath = null;
        if (true) {
            if (requestCode == 1) {

                selectedImageUri = imageUri;
            } else if (requestCode == 2) {

                selectedImageUri = data.getData();
//
            }

            if(selectedImageUri != null){
                try {
                    // OI FILE Manager
                    String filemanagerstring = selectedImageUri.getPath();

                    // MEDIA GALLERY
                    String selectedImagePath = getPath(selectedImageUri);
//                 txt9.setText(selectedImagePath);
                    if (selectedImagePath != null) {
                        filePath = selectedImagePath;
                    } else if (filemanagerstring != null) {
                        filePath = filemanagerstring;
                    } else {
                        Toast.makeText(getActivity(), "Unknown path",
                                Toast.LENGTH_LONG).show();
                        Log.e("Bitmap", "Unknown path");
                    }

                    if (filePath != null) {
                        decodeFile(filePath);
                    } else {
                        bitmap = null;
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Internal error",
                            Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }
        }



    }
    public void decodeFile(String filePath) {
        // Decode image size
        //txt9.setText(filePath);
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE =256;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        Toast.makeText(getActivity(),  "Image Captured....",
                Toast.LENGTH_LONG).show();

        //imgView.setImageBitmap(bitmap);
        BitmapFactory.Options bfo;

        ByteArrayOutputStream bao;

        bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 3;

        bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bao);
        byte [] ba = bao.toByteArray();
        ims = Base64.encodeToString(ba, Base64.DEFAULT);
        System.out.println("Image is" + ims);

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }



    private class JsonReadTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("empid",acno));

            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
                System.out.println("Routes"+jsonResult);
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
            store();
        }
    }// end async task

    public void calljson1() {
        JsonReadTask1 task1 = new JsonReadTask1();
        // passes values for the urls string array
        task1.execute(new String[] { "http://49.50.71.97:88/chinhat/reading.php" });
    }

    void store(){

            try {
                System.out.println("Store json"+jsonResult);
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("trans");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                     prev=jsonChildNode.optString("prevkwh");
                     totslkw=jsonChildNode.optString("totslkw");
                    prevdate=jsonChildNode.optString("prevdt");
                    supplytype=jsonChildNode.optString("stcode");
                   // prevdate=prevdate.substring(9,19);
                    Lastbill=jsonChildNode.optString("ArrearLastBill");
                    Lpc=jsonChildNode.optString("lpcamt");
                    totavgestunit=jsonChildNode.optString("totavgestunits");
                    mailadd=jsonChildNode.optString("MailAdd1");
                    System.out.println("Catagory"+Catagory+"\n"+"prev"+prev+"\n"+
                            "totslkw"+totslkw+"\n"+"acno"+acno+"\n"+
                            "\n"+"Lastbill"+Lastbill+"\n"+"\n"+"Lpc"+Lpc+"-"+totavgestunit);

                }

                JSONArray jsonMainNode2 = jsonResponse.optJSONArray("data2");
                Double[] billrate=new Double[jsonMainNode2.length()];
                for (int i = 0; i < jsonMainNode2.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode2.getJSONObject(i);
                    billrate[i]=Double.parseDouble(jsonChildNode.optString("rate"));
                    System.out.println("rateis"+billrate[i]);
                }
                JSONArray jsonMainNode3 = jsonResponse.optJSONArray("lmv");
                for (int i = 0; i < jsonMainNode3.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode3.getJSONObject(i);
                    Catagory=jsonChildNode.optString("tariffcateg");
                }


            //   if(ccmd.length()>0 && kvah.length()>0)
            //   {
            //       System.out.print("kvah"+ccmd+"-"+kvah);
           //    }
           //     else
           //    {
           //        System.out.print("kvahelse"+ccmd+"-"+kvah);
           //    }
                ArrearLastBill=Double.parseDouble(Lastbill);
                LPCAmt=Double.parseDouble(Lpc);
                System.out.println("ArrearLastBill"+ArrearLastBill+"LPCAmt"+LPCAmt);
                p = Double.parseDouble(prev);
                q =Double.parseDouble(currentReading);
                if(mdivalue.length()>0) {
                    x = Double.parseDouble(mdivalue);
                    if(x < 0.75)
                    {
                        x=0.75;

                    }
                    else
                    {
                        x=1.00;
                    }
                }
                else{

                    x=1.00;
                }
                kwrate =Double.parseDouble(totslkw);
                int i=0;
                reading=q-p;

                if(reading <= 0)
                {

                        reading=Double.parseDouble(totavgestunit);
                }
                else if(reading>1500)
                {
                        reading=1500.00;
                }

                        if (Catagory != "abc") {
                        System.out.println("in categ1");
                            if (Catagory.equals("LMV1")) {
                                System.out.println("in lmv");
                                if (supplytype != "xyz") {
                                    System.out.println("in supply1");
                                    if (supplytype.equals("10")) {
                                        System.out.println("in supply2");
                                        if (supplytype.equals("10") && kwrate == 1) {
                                            System.out.println("in condition");
                                            FC = 50.00;
                                            FC = FC * kwrate*x;
                                            if (reading <= 50) {
                                                //FC=FC*kwrate;
                                                Ec = reading * billrate[i];
                                                //  price=FC+Ec;
                                            } else if (reading > 50 && reading <= 150) {

                                                //FC=FC*kwrate;
                                                Temp = 50 * billrate[i];
                                                Ec = (reading - 50) * billrate[i + 1];
                                                System.out.println("EC1" + Ec);
                                                Ec = Ec + Temp;
                                                System.out.println("EC2" + Ec);

                                            } else if (reading > 150) {
                                               // showAlert();
                                                FC = 90.00;
                                                Temp = 50 * billrate[i];
                                                Ec = (reading - 150) * billrate[i + 1];
                                                System.out.println("EC1" + Ec);
                                                Ec = Ec + Temp;
                                                System.out.println("EC2" + Ec);


                                            }
                                            price = FC + Ec;
                                            System.out.println("price" + price);
                                        } else {
                                            System.out.println("in 10 2");
                                            FC = 90.00;
                                            FC = FC * kwrate*x;
                                            if (reading <= 150) {
                                                Ec = reading * billrate[i];
                                                //price=FC+Ec;
                                            } else if (reading > 150 && reading <= 300) {
                                                Temp = 150 * billrate[i];
                                                Ec = (reading - 150) * billrate[i + 1];
                                                Ec = Ec + Temp;
                                            } else if (reading > 300 && reading <= 500) {
                                                Temp = 150 * billrate[i];
                                                Temp1 = 150 * billrate[i + 1];
                                                Ec = (reading - 300) * billrate[i + 2];
                                                Ec = Temp + Temp1 + Ec;
                                                //  price=FC+Ec;
                                            } else if (reading > 500) {
                                                Temp = 150 * billrate[i];
                                                Temp1 = 150 * billrate[i + 1];
                                                Temp2 = 200 * billrate[i + 2];
                                                Ec = (reading - 500) * billrate[i + 3];
                                                System.out.println("EC1" + Ec);
                                                Ec = Temp + Temp1+Temp2 + Ec;
                                                System.out.println("EC2" + Ec);
                                            }
                                            price = FC + Ec;
                                        }
                                    } else if (supplytype.equals("11")) {
                                        FC = 90.00;
                                        FC = FC * kwrate*x;

                                        if (supplytype.equals("11")) {
                                            System.out.println("in 11");
                                            if (reading <= 150) {

                                                Ec = reading * billrate[i];
                                                //  price=FC+Ec;
                                            } else if (reading > 150 && reading <= 300) {
                                                Temp = 150 * billrate[i];
                                                Ec = (reading - 150) * billrate[i + 1];
                                                Ec = Temp + Ec;
                                            } else if (reading > 300 && reading <= 500) {
                                                Temp = 150 * billrate[i];
                                                Temp1 = 150 * billrate[i + 1];
                                                Ec = (reading - 300) * billrate[i + 2];
                                                Ec = Temp + Temp1 + Ec;
                                            } else if (reading > 500) {
                                                Temp = 150 * billrate[i];
                                                Temp1 = 150 * billrate[i + 1];
                                                Temp2 = 200 * billrate[i + 2];
                                                Ec = (reading - 500) * billrate[i + 3];
                                                Ec = Temp + Temp1+Temp2 + Ec;
                                            }

                                        }
                                        price = FC + Ec;
                                    } else if (supplytype.equals("12")) {
                                        System.out.println("in 12");
                                        FC = 85.00;
                                        FC = FC * kwrate*x;
                                        if (supplytype == "12") {

                                            Ec = reading * billrate[i];

                                        }
                                        price = FC + Ec;
                                    }
                                }


                            } else if (Catagory.equals("LMV2")) {
                                if (supplytype != "xyz") {
                                    if (supplytype.equals("20")) {

                                        if (supplytype.equals("20")) {
                                            FC = 225.00;
                                            FC = FC * kwrate*x;
                                            if (reading <= 300) {

                                                Ec = reading * billrate[i];

                                            } else if (reading > 300 && reading <= 1000) {
                                                Temp = 300 * billrate[i];
                                                Ec = (reading - 300) * billrate[i + 1];
                                                Ec = Temp + Ec;
                                            } else if (reading > 1000) {
                                                Temp = 300 * billrate[i];
                                                Temp1 = 700 * billrate[i + 1];
                                                Ec = (reading - 1000) * billrate[i + 2];
                                                Ec = Temp + Temp1 + Ec;
                                            }
                                            price = FC + Ec;
                                        }
                                    } else if (supplytype.equals("22")) {
                                        FC = 225.00;
                                        FC = FC * kwrate*x;
                                        if (reading <= 300) {

                                            Ec = reading * billrate[i];

                                        } else if (reading > 300 && reading <= 1000) {
                                            Temp = 300 * billrate[i];
                                            Ec = (reading - 300) * billrate[i + 1];
                                            Ec = Temp + Ec;
                                        } else if (reading > 1000) {
                                            Temp = 300 * billrate[i];
                                            Temp1 = 300 * billrate[i + 1];
                                            Ec = (reading - 6000) * billrate[i + 2];
                                            Ec = Temp + Temp1 + Ec;
                                        }
                                        price = FC + Ec;
                                    }
                                }
                            } else if (Catagory.equals("LMV4A")) {
                                if (supplytype != "xyz") {
                                    if (supplytype.equals("41") || supplytype.equals("40") || supplytype.equals("42") || supplytype.equals("43") || supplytype.equals("44")) {
                                        FC = 200.00;
                                        FC = FC * kwrate*x;
                                        if (reading <= 1000) {

                                            Ec = reading * billrate[i];

                                        } else if (reading > 1000) {
                                            Temp = 1000 * billrate[i];
                                            Ec = (reading - 1000) * billrate[i + 1];
                                            Ec = Temp + Ec;
                                        }
                                        price = FC + Ec;

                                    }
                                }
                            } else if (Catagory.equals("LMV4B")) {
                                if (supplytype != "xyz") {
                                    if (supplytype.equals("46") || supplytype.equals("47")) {
                                        FC = 225.00;
                                        FC = FC * kwrate*x;
                                        if (reading <= 1000) {
                                            Ec = reading * billrate[i];

                                        } else if (reading > 1000) {
                                            Temp = 1000 * billrate[i];
                                            Ec = (reading - 1000) * billrate[i + 1];
                                            Ec = Temp + Ec;

                                        }
                                        price = FC + Ec;

                                    }
                                }
                            } else if (Catagory.equals("LMV5")) {
                                if (supplytype.equals("52")) {
                                    FC = 75;
                                    FC = FC * kwrate*x;
                                    Ec = reading * billrate[i];
                                    price = FC + Ec;
                                }
                            } else if (Catagory.equals("LMV6")) {
                                System.out.println("in LMV6");
                                if (supplytype.equals("60") || supplytype.equals("62") || supplytype.equals("64")) {
                                    FC = 225;
                                    FC = FC * kwrate*x;
                                    if (reading <= 1000) {
                                        Ec = reading * billrate[i];
                                    } else if (reading > 1000) {
                                        Temp = 1000 * billrate[i];
                                        Ec = (reading - 1000) * billrate[i + 1];
                                        Ec = Temp + Ec;
                                    }
                                    price = FC + Ec;

                                }
                            } else if (Catagory.equals("LMV7")) {
                                if (supplytype == "72" || supplytype == "74") {
                                    FC = 225;
                                    FC = FC * kwrate*x;
                                    Ec = reading * billrate[i];
                                    price = FC + Ec;
                                }
                            } else if (Catagory.equals("LMV9")) {
                                if (supplytype.equals("92")) {
                                    Ec = reading * billrate[i];

                                } else if (supplytype == "93") {
                                    Ec = reading * billrate[i];

                                }
                                price = Ec;
                            }

                    }
                double exdp,y;
                if(Double.parseDouble(mdivalue)>kwrate)
                {
                    exdp=(Double.parseDouble(mdivalue)-kwrate)*2.0*FC;
                }
                else
                {
                    exdp=0.0;
                }
                Exchagedemandpanality=Double.toString(exdp);
                CeilingAmmt=0.0;
                if(reading >1500) {
                    CeilingAmmt = (5.01 * price) / 100;
                }
                System.out.println("helloo"+Exchagedemandpanality+"-"+CeilingAmmt);
                    TexAmmt=(5.00*price)/100;
              //  price=CeilingAmmt+TexAmmt+price;

                SBMLPCAmt=(0.1*ArrearLastBill)/100 ;
                System.out.println("SBMLPCAmt"+SBMLPCAmt);
                if(Catagory.equals("LMV2"))
                {
                    regulatorySchg1 = ((2.84)*(Ec+FC))/100;

                    regulatorySchg2=0.0;
                    System.out.println("1"+"regulatorySchg1"+regulatorySchg1+"regulatorySchg2"+regulatorySchg2);
                }
                else
                {     regulatorySchg2=0.0;
                        regulatorySchg1 = ((4.28)*(Ec+FC))/100;
                    System.out.println("2"+"regulatorySchg1"+regulatorySchg1+"regulatorySchg2"+regulatorySchg2);
                }

                SBMBillArrearAmt = Math.round(ArrearLastBill * 1.5)/100 ; // i.e. (3680.05 * 0.015) = 3680.06

                // EnergyDuty = ((Ec + FC)*5)*100; // (26057.7 * 0.05) = 1316.39
                System.out.println("fbaswbdefab"+SBMBillArrearAmt);
                SBMBillCurrAmt =( FC+ Ec + CeilingAmmt +TexAmmt + SBMLPCAmt + LPCAmt + regulatorySchg1 + regulatorySchg2+exdp);
                System.out.println("SBMBillCurrAmt"+SBMBillCurrAmt);
                 SBMTotBillAmt = (SBMBillCurrAmt + SBMBillArrearAmt+ArrearLastBill);
                System.out.println("SBMTotBillAmt"+SBMTotBillAmt);
                y=Math.round(Double.valueOf(SBMTotBillAmt)*100.0)/100.0;
                SBMTotBillAmt1 = (int) Math.round(y);
                System.out.println("locationbefor"+location);
                location=  location.replaceAll("\\s+", "");
                System.out.println("location"+location);

                double rebate;
                if(ArrearLastBill <= 0.0)
                {
                    System.out.println("rebate1");
                    rebate=(SBMTotBillAmt1*0.25)/100;
                    rebate = (int) Math.round(rebate);
                }
                else
                {System.out.println("rebate2");
                    rebate=0.0;
                }
                System.out.println("rebate"+rebate);
                int a =(int)Math.round(SBMTotBillAmt1-rebate);
                grossammount=String.valueOf(a);
                System.out.println("grossAmont"+grossammount+"-"+a);
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c1 = Calendar.getInstance();
                c1.setTime(new java.util.Date()); // Now use today date.
                c1.add(Calendar.DATE, 7); // Adding 7 days
                String output = sdf.format(c1.getTime());
                Bill_Preview fragment1 = new Bill_Preview();
                Bundle b1 = new Bundle();
                double reg=regulatorySchg1+regulatorySchg2;
                b1.putString("ddate", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(System.currentTimeMillis() * 1000L));
                b1.putString("acno", acno);
                b1.putString("currentreading",String.valueOf(currentReading));
                b1.putString("pre", String.valueOf(reading));
                b1.putString("CeilingAmmt", String.valueOf(CeilingAmmt));
                b1.putString("TexAmmt", String.valueOf(TexAmmt));
                b1.putString("LPCAmt", String.valueOf(LPCAmt));
                b1.putString("FC", String.valueOf(FC));
                b1.putString("EC", String.valueOf(Ec));
                b1.putString("SBMBillArrearAmt", String.valueOf(ArrearLastBill));
                b1.putString("reg", String.valueOf(reg));
                b1.putString("total", String.valueOf(SBMTotBillAmt1));
               //more datta required
                b1.putString("oficeAddress","LESCO Headquarter,Kesa House ALAMBAG,LUCKNOW" );
                b1.putString("bookNumber","R23C" );
                b1.putString("SCNumber","0105604" );
                b1.putString("meterNuber",meterNumber);
                b1.putString("consumerName",customerName );
                b1.putString("consumerAddress",mailadd);
                b1.putString("OverALLReading",currentReading);
                b1.putString("PreviousReadin",prev);
                b1.putString("currentKVWH","0.00" );
                b1.putString("MeteredKVWH","0.00" );
                b1.putString("LastPayment","0.00" );
                b1.putString("LastPaymentDate","-" );
                b1.putString("ArrSurChrgs",String.valueOf(SBMBillArrearAmt));
                b1.putString("CurSurChrgs","0.00" );
                b1.putString("AdjstAmt","0.00" );
                b1.putString("REBATE",String.valueOf(rebate));
                b1.putString("BILLNO","3015697" );
                b1.putString("Previousdate",prevdate);
                b1.putString("DiscountDate",output);
                b1.putString("PreviousKVWH","0.00" );
                b1.putString("BillBasis","MU" );
                b1.putString("EXDamagePanality",Exchagedemandpanality);
                b1.putString("ElecticityDuty", String.valueOf(TexAmmt));
                b1.putString("cmd",String.valueOf(x));
                b1.putString("Image",ims);
                b1.putString("email",email);
                b1.putString("supplytype",supplytype);
                b1.putString("location",location);
               b1.putString("latitude", String.valueOf(latitude));
               b1.putString("longitude",String.valueOf(longitude));
                    b1.putString("arfcode",Catagory);
                    b1.putString("conload",totslkw);
                b1.putString("GrossAmount",grossammount);

               // b1.putString("latitude", String.valueOf(20.12));
              //  b1.putString("longitude",String.valueOf(20.12));
                fragment1.setArguments(b1);

                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
                catch (JSONException e) {
                Toast.makeText(getActivity(), "Error" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }

        catch(Exception e){
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
          //  Toast.makeText(getActivity(),"Error..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return answer;
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
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
           location= locationAddress;

        }
    }
    public void alert(final String x){
        ((Activity) getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alert");
                builder.setMessage(x+prev)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    public void location(){
        ((Activity) getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("NO GPS LOCATION FOUND");
                builder.setMessage("Press OK to continue")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                calljson1();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
