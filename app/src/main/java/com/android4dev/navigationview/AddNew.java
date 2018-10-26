package com.android4dev.navigationview;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



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
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddNew extends Fragment{
	RadioButton b,c;String aid,oid,auth,text,pid,file,uid,div,email,set="proof",text1,text2;
EditText name,fname,add,phone,nof,mtrno,acno;Button submit,proof;
double latitude;	HttpResponse response;
double longitude ;
String jsonResult;Spinner sp,sp1,sp2;
ArrayList<String> ar = new ArrayList<String>();
ArrayList<String> ar1 = new ArrayList<String>();
ArrayList<String> ar2= new ArrayList<String>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.addnew, container, false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		Bundle bundle = this.getArguments();


		email= bundle.getString("email");
		div= bundle.getString("div");
		name = (EditText)v.findViewById(R.id.addname1);
	    
	       add = (EditText)v.findViewById(R.id.addaddress1);
	 
	        nof = (EditText)v.findViewById(R.id.addnoofmtr1);
	        mtrno= (EditText)v.findViewById(R.id.addmtrno1);
	        acno= (EditText)v.findViewById(R.id.addaccno1);
	        submit=(Button)v.findViewById(R.id.addsubmitbtn1);
	        sp= (Spinner)v.findViewById(R.id.spstation);
	        sp1 = (Spinner)v.findViewById(R.id.addnewtrans);
	        sp2 = (Spinner)v.findViewById(R.id.addnewpole);
	        Toast.makeText(getActivity(),email, Toast.LENGTH_SHORT).show();
	        getLocation();
	 	   try {
	 			getspinner();

	 			
	 		} catch (JSONException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	        submit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				
						    	login();

					getFragmentManager().popBackStack();
						    	//startActivity(getIntent());
						      	  
						  				
				}
			});
	      
      return v;
	}

	void getLocation()
	{	GPSTracker gps;
		  gps = new GPSTracker(getActivity());

			// check if GPS enabled		
	      if(gps.canGetLocation()){
	      	
	       latitude = gps.getLatitude();
	       longitude = gps.getLongitude();
	      
	      
	      	
	      }else{
	      	// can't get location
	      	// GPS or Network is not enabled
	      	// Ask user to enable GPS/network in settings
	      	gps.showSettingsAlert();
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
				
					ar.add(name1);
			}
			  ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ar);
			   	 
		        // Setting the array adapter containing country list to the spinner widget
		        sp.setAdapter(adapter1);
		        OnItemSelectedListener area1 = new OnItemSelectedListener() {
		        	 
		            @Override
		            public void onItemSelected(AdapterView<?> spinner, View container,
		                    int position, long id) {
		            	String s=null;
		            text = sp.getSelectedItem().toString();
		            try {
						getspinner1();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	Toast.makeText(getActivity(),text, Toast.LENGTH_SHORT).show();
		            }
		          
		            @Override
		            public void onNothingSelected(AdapterView<?> arg0) {
		                // TODO Auto-generated method stub
		            }
		        };
		        sp.setOnItemSelectedListener(area1);
		       
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
			Toast.makeText(getActivity(),
					"Error..." + e.toString(), Toast.LENGTH_LONG).show();
		}
		return answer;
	}
	void login(){
					
			 
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/addnewuser.php");
		
			// make sure the url is correct.
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("name",name.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];

			nameValuePairs.add(new BasicNameValuePair("add",add.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("mtrno",mtrno.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("div",div));
			nameValuePairs.add(new BasicNameValuePair("email",email));
			nameValuePairs.add(new BasicNameValuePair("sub",text));
			nameValuePairs.add(new BasicNameValuePair("trans",text1));
			nameValuePairs.add(new BasicNameValuePair("pole",text2));
			nameValuePairs.add(new BasicNameValuePair("nom",nof.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("acno",acno.getText().toString().trim()));
			//nameValuePairs.add(new BasicNameValuePair("lat",String.valueOf(latitude)));
			//nameValuePairs.add(new BasicNameValuePair("long",String.valueOf(longitude)));
			try{
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			System.out.println("Exception in out");
		 response = httpclient.execute(httppost);
			 System.out.println("response");
			// edited by James from coderzheaven.. from here....
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response);
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

					}
				});
			
			if(response.equalsIgnoreCase("User Found")){
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

					}
				});
				
			    
			    
			}else{
							
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	void getspinner2() throws JSONException
	{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/getnewspinner.php");
		nameValuePairs.add(new BasicNameValuePair("tag","pole"));
		nameValuePairs.add(new BasicNameValuePair("text", text1));
		nameValuePairs.add(new BasicNameValuePair("div", div));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response1 = httpclient.execute(httppost, responseHandler);
			System.out.println("Response of 2: " + response1); 
			jsonResult =response1.substring(3);
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("trans");
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				String name1 = jsonChildNode.optString("gispoleid");
				
					ar2.add(name1);
			}
			  ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ar2);
			   	 
		        // Setting the array adapter containing country list to the spinner widget
		        sp2.setAdapter(adapter1);
		        OnItemSelectedListener area = new OnItemSelectedListener() {
		        	 
		            @Override
		            public void onItemSelected(AdapterView<?> spinner, View container,
		                    int position, long id) {
		            	String s=null;
		            text2 = sp2.getSelectedItem().toString();
		        	
		        
		            }
		          
		            @Override
		            public void onNothingSelected(AdapterView<?> arg0) {
		                // TODO Auto-generated method stub
		            }
		        };
		        sp2.setOnItemSelectedListener(area);
		       
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
		nameValuePairs.add(new BasicNameValuePair("tag","trans"));
		nameValuePairs.add(new BasicNameValuePair("text",text));
		nameValuePairs.add(new BasicNameValuePair("div",div));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response1 = httpclient.execute(httppost, responseHandler);
			System.out.println("Response of 1 : " + response1); 
			jsonResult = inputStreamToString(
					response.getEntity().getContent()).toString();
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("trans");
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				String name1 = jsonChildNode.optString("DTCode");
				
					ar1.add(name1);
			}
			  ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ar1);
			   	 
		        // Setting the array adapter containing country list to the spinner widget
		        sp1.setAdapter(adapter1);
		        OnItemSelectedListener area = new OnItemSelectedListener() {
		        	 
		            @Override
		            public void onItemSelected(AdapterView<?> spinner, View container,
		                    int position, long id) {
		            	String s=null;
		            text1 = sp1.getSelectedItem().toString();
		            try {
						getspinner2();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        
		            }
		          
		            @Override
		            public void onNothingSelected(AdapterView<?> arg0) {
		                // TODO Auto-generated method stub
		            }
		        };
		        sp1.setOnItemSelectedListener(area);
		       
		}

		catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
