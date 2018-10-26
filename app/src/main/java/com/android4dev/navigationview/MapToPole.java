package com.android4dev.navigationview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sanjay on 20-Jan-16.
 */
public class MapToPole extends Fragment {
    private String jsonResult,jsonResult2;

    private String url = "http://www.aditya.multifacet-software.com/php/store/getspinner.php",outPut,text,file;
    private ListView listView,acclistview;
    Button b,b1,reset;
    HttpPost httppost;
    HttpPost httppost1;
    StringBuffer buffer;
    HttpResponse response;
    String value;ArrayAdapter<String> adapter,adapter1;
    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    ArrayList<String> ar = new ArrayList<String>();
    ArrayList<String> ar1 = new ArrayList<String>();
    ArrayList<String> ar2 = new ArrayList<String>();
    ArrayList<String> ar3 = new ArrayList<String>();
    Spinner sp2,sp1;String div,text1,email;
    EditText polecode,ecode,ptype;
    List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.maptopole, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle bundle = this.getArguments();
        div = bundle.getString("div");
        email = bundle.getString("email");
        sp1=(Spinner)v.findViewById(R.id.msptrans);
        sp2=(Spinner)v.findViewById(R.id.mspsub);
        b=(Button)v.findViewById(R.id.mbtnsbmt);
        reset=(Button)v.findViewById(R.id.mapreset);
        tv1=(TextView)v.findViewById(R.id.msetname);
        tv2=(TextView)v.findViewById(R.id.macctvname);
        tv3=(TextView)v.findViewById(R.id.msetmtr);
        tv4=(TextView)v.findViewById(R.id.mmtrtvno);
        tv5=(TextView)v.findViewById(R.id.maddtv);
        tv6=(TextView)v.findViewById(R.id.msetaddtv);
        listView= (ListView) v.findViewById(R.id.mlvpolesrch);
        acclistview= (ListView) v.findViewById(R.id.macctlistView);
        polecode= (EditText)v.findViewById(R.id.metpolecode);
       ecode= (EditText)v.findViewById(R.id.metacccode);
        try {
            getccno();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setVisibility(View.INVISIBLE);
        acclistview.setVisibility(View.INVISIBLE);
        try {
            getspinner();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {

                        login();
                        getFragmentManager().popBackStack();
                    }
                }).start();
            }
        });
       reset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               new Thread(new Runnable() {
                   public void run() {
MapToPole a=new MapToPole();
                      Bundle b=new Bundle();

                      b.putString("email", email);
                        b.putString("div", div);
                       a.setArguments(b);
                       FragmentTransaction tr = getFragmentManager().beginTransaction();
                       tr.replace(R.id.frame, a);
                       tr.commit();
                   }
                }).start();
            }
        });
        return v;
    }
void getccno() throws JSONException {
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/getaccno.php");

    try {
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        final String response1 = httpclient.execute(httppost, responseHandler);
        System.out.println("Response  of post : " + response1);
        jsonResult = response1.substring(3);
        JSONObject jsonResponse = new JSONObject(jsonResult);
        JSONArray jsonMainNode = jsonResponse.optJSONArray("trans");
        for (int i = 0; i < jsonMainNode.length(); i++) {
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
            String name1 = jsonChildNode.optString("ACCTID");

            ar.add(name1);
        }
        adapter1=new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                ar);
        acclistview.setAdapter(adapter1);
        acclistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String selectedFromList =(String) (acclistview.getItemAtPosition(myItemInt));
            ecode.setText(selectedFromList);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/getaccno.php");
                nameValuePairs.add(new BasicNameValuePair("acc",selectedFromList));
                nameValuePairs.add(new BasicNameValuePair("tag", "data"));
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response  of post : " + response1);
                    jsonResult = response1.substring(3);
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(jsonResult);

                    JSONArray jsonMainNode = jsonResponse.optJSONArray("trans");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        String name1 = jsonChildNode.optString("PerName");
                        String mtr = jsonChildNode.optString("MtrSrlNo");
                        String add = jsonChildNode.optString("MailAdd1");
tv1.setText("Name");
                        tv2.setText(name1);
                        tv3.setText("Meter No");
                        tv4.setText(mtr);
                        tv5.setText("Address");
                        tv6.setText(add);

                    }
                        acclistview.setVisibility(View.INVISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            }

        );

        acclistview.setTextFilterEnabled(true);
        ecode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                MapToPole.this.adapter1.getFilter().filter(cs);
                acclistview.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub


            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

        });

    }

    catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}
    void getspinner1() throws JSONException
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/getnewspinner.php");
        nameValuePairs.add(new BasicNameValuePair("tag", "pole"));
        nameValuePairs.add(new BasicNameValuePair("text", text1));
        nameValuePairs.add(new BasicNameValuePair("div", div));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response1 = httpclient.execute(httppost, responseHandler);
            System.out.println("Response  of post : " + response1);
            jsonResult = response1.substring(3);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("trans");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name1 = jsonChildNode.optString("gispoleid");

                ar.add(name1);
            }
            adapter=new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    ar);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                    String selectedFromList =(String) (listView.getItemAtPosition(myItemInt));
                    polecode.setText(selectedFromList);
                    listView.setVisibility(View.INVISIBLE);

                }
            });

            listView.setTextFilterEnabled(true);
            polecode.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    MapToPole.this.adapter.getFilter().filter(cs);
                    listView.setVisibility(View.VISIBLE);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub


                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub

                }
            });

        }

        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void login(){
        try{



                        Toast.makeText(getActivity(), "SUBMITTED" +
                                "", Toast.LENGTH_SHORT).show();

        }catch(Exception e){

            System.out.println("Exception : " + e.getMessage());
        }
    }
    void getspinner() throws JSONException
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/getnewspinner.php");
        nameValuePairs.add(new BasicNameValuePair("tag","sub"));
        nameValuePairs.add(new BasicNameValuePair("div",div));
        nameValuePairs.add(new BasicNameValuePair("text",text));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response1 = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response1);
            jsonResult = inputStreamToString(
                    response.getEntity().getContent()).toString();
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("trans");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name1 = jsonChildNode.optString("SubDiv");

                ar1.add(name1);
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ar1);

            // Setting the array adapter containing country list to the spinner widget
            sp2.setAdapter(adapter1);
            AdapterView.OnItemSelectedListener area1 = new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> spinner, View container,
                                           int position, long id) {
                    String s=null;
                    text = sp2.getSelectedItem().toString();
                    try {
                        gettrans();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //Toast.makeText(getActivity(),text, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            };
            sp2.setOnItemSelectedListener(area1);

        }

        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void gettrans() throws JSONException
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/getnewspinner.php");
        nameValuePairs.add(new BasicNameValuePair("tag","trans"));
        nameValuePairs.add(new BasicNameValuePair("div",div));
        nameValuePairs.add(new BasicNameValuePair("text",text));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response1 = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response1);
            jsonResult2 = inputStreamToString(
                    response.getEntity().getContent()).toString();
            JSONObject jsonResponse = new JSONObject(jsonResult2);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("trans");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name1 = jsonChildNode.optString("DTCode");

                ar2.add(name1);
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ar2);

            sp1.setAdapter(adapter1);
            AdapterView.OnItemSelectedListener area1 = new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> spinner, View container,
                                           int position, long id) {
                    String s=null;
                    text1 = sp1.getSelectedItem().toString();
                    try {
                        System.out.print("in pole gt ");
                        getspinner1();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(),"trans"+text1, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            };
            sp1.setOnItemSelectedListener(area1);

        }

        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private StringBuilder inputStreamToString(InputStream is) {
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
            Toast.makeText(	getActivity(),
                    "Error..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return answer;
    }
}

