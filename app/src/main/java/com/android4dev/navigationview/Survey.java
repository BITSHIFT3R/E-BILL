package com.android4dev.navigationview;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by Abhishek on 15-Jan-16.
 */
public class Survey extends Fragment {
    private NotificationManager mNotifyManager;
    private Builder mBuilder;
    int id = 1;
    Uri imageUri;String message;
    Uri selectedImageUri= null;
    String response1,ims="";
    private  String jsonResult;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;String email,div;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    EditText gp,bn,conno,name,mobno,add,metersrno,deptno,redg,demad,gpstyp;
    Button image,save,print;
    String group,bookno,connno,cname,address,mobileno,mseno,departmentno,reading,demand,gpstype;
    private Bitmap bitmap;
    String Supplytype,meterexcep;
    String url="http://www.aditya.multifacet-software.com/php/check/survey.php";
    Spinner sp1,sp2;
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
    String oficeAddress="LESCO Headquarter,Kesa House ALAMBAG,LUCKNOW";
    String[] sptype = new String[] {
            "10","11","12","13","17",
            "20","22","23","24","28",
            "31","32","33","34","35","36",
            "40","41","42","43","44",
            "46","47",
            "50","51","52",
            "60","61","62","63","64",
            "72","74",
            "92","93"
    };
    File myDir;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        email = bundle.getString("email");
        div = bundle.getString("div");
        System.out.println("details div" + div);
        gp = (EditText) v.findViewById(R.id.surgrp);
        bn = (EditText) v.findViewById(R.id.surbookno);
        conno = (EditText) v.findViewById(R.id.surconnno);
        name = (EditText) v.findViewById(R.id.surname);
        add = (EditText) v.findViewById(R.id.suraddress);
        mobno = (EditText) v.findViewById(R.id.surmobno);
        metersrno = (EditText) v.findViewById(R.id.surmetersrno);
        deptno = (EditText) v.findViewById(R.id.surmeterdepartmentno);
        redg = (EditText) v.findViewById(R.id.surred);
        demad = (EditText) v.findViewById(R.id.surdemand);
        gpstyp = (EditText) v.findViewById(R.id.surgpstype);
        image = (Button) v.findViewById(R.id.surimage);
        save = (Button) v.findViewById(R.id.sursave);
        print = (Button) v.findViewById(R.id.surprint);
        sp1=(Spinner)v.findViewById(R.id.sursupplytype);
        sp2=(Spinner)v.findViewById(R.id.surmeterexp);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,sptype);
        sp2.setAdapter(adapter1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mtrexcep);

        // Setting the array adapter containing country list to the spinner widget
        sp1.setAdapter(adapter);
        AdapterView.OnItemSelectedListener area1 = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {

                Supplytype= sp2.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };

        AdapterView.OnItemSelectedListener area = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {

                 meterexcep= sp1.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        sp2.setOnItemSelectedListener(area1);
        sp1.setOnItemSelectedListener(area);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = connno + ".jpg";
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // int fCount = 0;
                group=gp.getText().toString();
                bookno=bn.getText().toString();
                connno=conno.getText().toString();
                cname=name.getText().toString();
                address=add.getText().toString();
                mobileno=mobno.getText().toString();
                mseno=metersrno.getText().toString();
                departmentno=deptno.getText().toString();
                reading=redg.getText().toString();
                demand=demad.getText().toString();
                gpstype=gpstyp.getText().toString();
                if(group.length()>0 || bookno.length()>0 || connno.length()>0 ||cname.length()>0 || address.length()>0 || mobileno.length()>0
                        || mseno.length()>0 || departmentno.length()>0 || reading.length()>0 ||demand.length()>0 || gpstype.length()>0)
                {
                String FILE = Environment.getExternalStorageDirectory().toString()
                        + "/LESCO/" +bookno + ".pdf";

                Document document = new Document(PageSize.A4);

                // Create Directory in External Storage
                String root = Environment.getExternalStorageDirectory().toString();
                myDir = new File(root + "/LESCO");

                if (!myDir.exists()) {

                    myDir.mkdir();
                }
                if(myDir.exists()){
                    // Create Pdf Writer for Writting into New Created Document
                    try {



                            mNotifyManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            mBuilder = new NotificationCompat.Builder(getActivity());
                            mBuilder.setContentTitle(bookno)
                                    .setContentText("Download in progress")
                                    .setSmallIcon(R.drawable.lescologo);

                           new Downloader().execute();
                            PdfWriter.getInstance(document, new FileOutputStream(FILE));

                            // Open Document for Writting into document
                            document.open();

                            // User Define Method
                            addMetaData(document);
                            addTitlePage(document);


                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                    document.close();

                    Toast.makeText(getActivity(), "PDF File is Created. Location : " + FILE,
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Snackbar snack = Snackbar.make(getActivity().findViewById(android.R.id.content), "FIELD REQUIRED", Snackbar.LENGTH_LONG);
                    View view1 = snack.getView();
                    TextView tv = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }

                // Close Document after writting all content

            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group=gp.getText().toString();
                bookno=bn.getText().toString();
                connno=conno.getText().toString();
                cname=name.getText().toString();
                address=add.getText().toString();
                mobileno=mobno.getText().toString();
                mseno=metersrno.getText().toString();
                departmentno=deptno.getText().toString();
                reading=redg.getText().toString();
                demand=demad.getText().toString();
                gpstype=gpstyp.getText().toString();
                if(group.length()>0 || bookno.length()>0 || connno.length()>0 ||cname.length()>0 || address.length()>0 || mobileno.length()>0
                        || mseno.length()>0 || departmentno.length()>0 || reading.length()>0 ||demand.length()>0 || gpstype.length()>0) {

                    if (bitmap != null) {
                        imageToStrig();
                        String FILE = Environment.getExternalStorageDirectory().toString()
                                + "/LESCO/" + bookno + ".pdf";

                        Document document = new Document(PageSize.A4);

                        // Create Directory in External Storage
                        String root = Environment.getExternalStorageDirectory().toString();
                        myDir = new File(root + "/LESCO");

                        if (!myDir.exists()) {

                            myDir.mkdir();
                        }
                        if (myDir.exists()) {
                            // Create Pdf Writer for Writting into New Created Document
                            try {

                                PdfWriter.getInstance(document, new FileOutputStream(FILE));

                                // Open Document for Writting into document
                                document.open();

                                // User Define Method
                                addMetaData(document);
                                addTitlePage(document);
                                calljason();


                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (DocumentException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        document.close();

                        Toast.makeText(getActivity(), "PDF File is Created. Location : " + FILE,
                                Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Snackbar snack = Snackbar.make(getActivity().findViewById(android.R.id.content), "Image is not captured", Snackbar.LENGTH_LONG);
                        View view1 = snack.getView();
                        TextView tv = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);
                        snack.show();
                    }
                }
                else
                {
                    Snackbar snack = Snackbar.make(getActivity().findViewById(android.R.id.content), "FIELD REQUIRED", Snackbar.LENGTH_LONG);
                    View view1 = snack.getView();
                    TextView tv = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }
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
            this.mDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            System.out.println("SUpply type"+Supplytype+"-"+meterexcep);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            nameValuePairs.add(new BasicNameValuePair("group",group));
            nameValuePairs.add(new BasicNameValuePair("bookno",bookno));
            nameValuePairs.add(new BasicNameValuePair("connno",connno));
            nameValuePairs.add(new BasicNameValuePair("cname",cname));
            nameValuePairs.add(new BasicNameValuePair("address",address));
            nameValuePairs.add(new BasicNameValuePair("mobileno",mobileno));
            nameValuePairs.add(new BasicNameValuePair("mseno",mseno));
            nameValuePairs.add(new BasicNameValuePair("departmentno",departmentno));
            nameValuePairs.add(new BasicNameValuePair("reading",reading));
            nameValuePairs.add(new BasicNameValuePair("demand",demand));
            nameValuePairs.add(new BasicNameValuePair("gpstype",gpstype));
            nameValuePairs.add(new BasicNameValuePair("ims",ims));
            nameValuePairs.add(new BasicNameValuePair("Supplytype",Supplytype));
            nameValuePairs.add(new BasicNameValuePair("meterexcep",meterexcep));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response1 = httpclient.execute(httppost, responseHandler);
                System.out.println("Response : " + response1);

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
            ListDrwaer();
            System.out.println("calling list");

            if(this.mDialog.isShowing())
            {
                this.mDialog.dismiss();
            }

        }
    }// end async task

    public void calljason() {
        System.out.println("Called json");
        JsonReadTask task = new JsonReadTask(getActivity());
        // passes values for the urls string array
        task.execute(new String[] { url });
    }

    // build hash set for list view
    public void ListDrwaer() {
            response1=response1.substring(3);
            if(response1.length()<10)
            {
                Toast.makeText(getActivity().getApplicationContext(),"DATA INSERTED",Toast.LENGTH_SHORT).show();
                Survey sfragment=new Survey();
                Bundle dbundle = new Bundle();
                sfragment.setArguments(dbundle);
                dbundle.putString("div", div);
                dbundle.putString("email", email);
                android.support.v4.app.FragmentTransaction sfragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                sfragmentTransaction.replace(R.id.frame,sfragment).addToBackStack("fragBack").commit();
                getActivity().finish();

            }
             else
            {
                Snackbar snack = Snackbar.make(getActivity().findViewById(android.R.id.content), "Try Again", Snackbar.LENGTH_LONG);
                View view1 = snack.getView();
                TextView tv = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.show();
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
    public void addMetaData(Document document)

    {
        document.addTitle(oficeAddress);
        document.addSubject("BILL");
        document.addKeywords(bookno);
        document.addAuthor(cname);
        document.addCreator("SandeepSingh");
    }

    public void addTitlePage(Document document) throws DocumentException {
        int cols = 9;
        Date now = new Date();
        // Date alsoNow = Calendar.getInstance().getTime();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(now);
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD
                , BaseColor.BLACK);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Paragraph newline = new Paragraph();
        newline.add("\n");

        Paragraph prHead = new Paragraph();
        // Set Font in this Paragraph
        prHead.setFont(titleFont);
        // Add item into Paragraph
        prHead.add("LESCO");
        prHead.setAlignment(Element.ALIGN_CENTER);
        PdfPTable table0 = new PdfPTable(2);
        table0.getWidthPercentage();
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell = new PdfPCell(new Phrase(oficeAddress,new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setRowspan(2);
        cell.setColspan(15);
        table.addCell(cell);
        table.addCell("Group");
        table.addCell(group);
        table.addCell("BOOK NO: ");
        table.addCell(bookno);
        table.addCell("CONNECTION NUMBER");
        table.addCell(connno);
        table.addCell("SUPPLY TYPE");
        table.addCell(Supplytype);
        table.addCell("NAME");
        table.addCell(cname);
        table.addCell("ADDRESS ");
        table.addCell(address);
        table.addCell("MOBILE NUMBER");
        table.addCell(mobileno);
        table.addCell("METER SERIAL NUMBER");
        table.addCell(mseno);
        table.addCell("DEPARTMENT NUMBER");
        table.addCell(departmentno);
        table.addCell("METER EXCEPTION");
        table.addCell(meterexcep);
        table.addCell("READING");
        table.addCell(reading);
        table.addCell("DEMAND");
        table.addCell(demand);
        table.addCell("GPS TYPE");
        table.addCell(gpstype);
        table.addCell("BILL BASIS");
        document.add(prHead);
        document.add(newline);
        document.add(table);
        document.newPage();
        // Create new Page in PDF
        document.newPage();
    }
    void imageToStrig()
    {
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
    private class Downloader extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            mBuilder.setProgress(100, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update progress
            mBuilder.setProgress(100, 0, true);
            mNotifyManager.notify(id, mBuilder.build());
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int i;
            for (i = 0; i <= 100; i += 50) {
                // Sets the progress indicator completion percentage
                publishProgress(Math.min(i, 100));
                try {
                    // Sleep for 5 seconds
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    Log.d("TAG", "sleep failure");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mBuilder.setContentText("Download complete"+"\n"+"LOCATION"+myDir);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(Uri.fromFile(myDir), "file/*");

            try{
                startActivity(intent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(getActivity(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }
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
