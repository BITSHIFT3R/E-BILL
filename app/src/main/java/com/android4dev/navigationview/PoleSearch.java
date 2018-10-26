package com.android4dev.navigationview;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
import android.app.AlertDialog;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class PoleSearch extends Fragment {
    Button b,map,reset;
    EditText et,et1,et2,et3,polecode;

    TextView tv;
    HttpPost httppost;
    HttpPost httppost1;
    StringBuffer buffer;
    HttpResponse response;ListView mListView;
    String text,text1,value;ArrayAdapter<String> adapter;
    ArrayList<String> ar = new ArrayList<String>();
    ArrayList<String> ar1 = new ArrayList<String>();
    ArrayList<String> ar2 = new ArrayList<String>();
    HttpClient httpclient;String email,div;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    RadioButton rsearch,rnew;String s,jsonResult,jsonResult2,text2;
    Spinner sp1,sp2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pole_search, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Bundle bundle = this.getArguments();
        email= bundle.getString("email");
        div= bundle.getString("div");
        map = (Button)v.findViewById(R.id.maptopole);
        b = (Button)v.findViewById(R.id.polesearchbtn);
       reset = (Button)v.findViewById(R.id.polesreset);
        polecode= (EditText)v.findViewById(R.id.etpolecode);
        mListView = (ListView) v.findViewById(R.id.lvpolesrch);
        sp2 = (Spinner)v.findViewById(R.id.sppolesub);
        sp1 = (Spinner)v.findViewById(R.id.sppoletrans);
        rsearch=(RadioButton)v.findViewById(R.id.rbpolesearch);
        rnew=(RadioButton)v.findViewById(R.id.rbaddpole);
        mListView.setVisibility(View.INVISIBLE);
       // Toast.makeText(getActivity(),email, Toast.LENGTH_SHORT).show();
        try {
            getspinner();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        rsearch.setChecked(true);
        rnew.setChecked(false);
        rnew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rsearch.setChecked(false);
                AddnewPole fragment = new AddnewPole();
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("div", div);
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
       map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                MapToPole fragment = new MapToPole();
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("div", div);
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        rsearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rnew.setChecked(false);
                Intent intent = new Intent(getActivity(), PoleSearch.class);
                startActivity(intent);
            }
        });
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Validating user...",true);
                new Thread(new Runnable() {
                    public void run() {

                        login();
                    }
                }).start();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                       PoleSearch a=new PoleSearch();
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
            Toast.makeText(getActivity(),
                    "Error..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return answer;
    }
    void getspinner1() throws JSONException
    {      Toast.makeText(getActivity(),"trpole n" ,Toast.LENGTH_SHORT).show();

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        HttpClient httpclient = new DefaultHttpClient();
        System.out.print(text+"-"+text1);
        HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/getnewspinner.php");
        nameValuePairs.add(new BasicNameValuePair("tag","pole"));
        nameValuePairs.add(new BasicNameValuePair("text",text1));
        nameValuePairs.add(new BasicNameValuePair("div",div));
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
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                    String selectedFromList =(String) (mListView.getItemAtPosition(myItemInt));
                    polecode.setText(selectedFromList);
                    mListView.setVisibility(View.INVISIBLE);


                }
            });

            mListView.setTextFilterEnabled(true);
            polecode.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    PoleSearch.this.adapter.getFilter().filter(cs);
                    mListView.setVisibility(View.VISIBLE);
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
            OnItemSelectedListener area1 = new OnItemSelectedListener() {

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
    void login(){
        try{
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://www.aditya.multifacet-software.com/php/store/polesearch.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("polecode",polecode.getText().toString()));  // $Edittext_value = $_POST['Edittext_value'];
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response=httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });

            if(response.contains("User Found")){
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Got Hit", Toast.LENGTH_SHORT).show();
                    }
                });
                PoleDetails fragment = new PoleDetails();
                Bundle bundle = new Bundle();
                bundle.putString("code",polecode.getText().toString());
                bundle.putString("div", div);
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }else{
                showAlert();
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    public void showAlert(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Login Error.");
                builder.setMessage("POLE not Found.")
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
            OnItemSelectedListener area1 = new OnItemSelectedListener() {

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
}
