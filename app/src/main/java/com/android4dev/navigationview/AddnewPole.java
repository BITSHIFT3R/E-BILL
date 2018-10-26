package com.android4dev.navigationview;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddnewPole extends Fragment {
	private String jsonResult,jsonResult2;
	private String url = "http://www.aditya.multifacet-software.com/php/store/getspinner.php",outPut,text,file;
	private ListView listView;
	 Button b,b1;
	private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
	  ArrayList<String> ar = new ArrayList<String>();
	  ArrayList<String> ar1 = new ArrayList<String>();
	  Spinner sp,sp1;String div,text1,email;
	EditText pname,pcode,ptype;
	  List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();
		String date;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.addpole,container,false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Bundle bundle = this.getArguments();
	        div=  bundle.getString("div");
	        email= bundle.getString("email");
	        sp=(Spinner)v.findViewById(R.id.spaddpole);
	        sp1=(Spinner)v.findViewById(R.id.sptrans);

	        pcode=(EditText)v.findViewById(R.id.etaddpole);
	    
	        b=(Button)v.findViewById(R.id.submitnewpole);
	        b1=(Button)v.findViewById(R.id.capturpolebtn);
	        Toast.makeText(getActivity(),div, Toast.LENGTH_SHORT).show();
	        Toast.makeText(getActivity(),email, Toast.LENGTH_SHORT).show();
	        b1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					 camera fragment = new camera();
					Bundle bundle = new Bundle();
					bundle.putString("email", email);
					bundle.putString("set", "addpole");
					fragment.setArguments(bundle);
					android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

					fragmentTransaction.replace(R.id.frame, fragment);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();

						  				
				}
			});
	        b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				
						    	set();

					getFragmentManager().popBackStack();
						    //	startActivity(getArguments());
						      	  
						  				
				}
			});
	       
	        accessWebService();
	        try {
				gettrans();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return v;
	}
	public void onBackPressed() {
		final PoleDetails fragment = (PoleDetails) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
		int count = getFragmentManager().getBackStackEntryCount();
		if (count == 0) {
			super.getActivity().onBackPressed();
			//additional code
		} else {
			getFragmentManager().popBackStack();
		}
	}
void set()
{
	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/addnewpole.php");
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	nameValuePairs.add(new BasicNameValuePair("trans",text1));
	nameValuePairs.add(new BasicNameValuePair("email",email));

	try {
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpclient.execute(httppost);
		jsonResult = inputStreamToString(
				response.getEntity().getContent()).toString();
		System.out.println("After submits"+jsonResult);
	}

	catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
	

	// Async Task to access the web
	private class JsonReadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("tag","sub"));
			nameValuePairs.add(new BasicNameValuePair("div",div));
			nameValuePairs.add(new BasicNameValuePair("text",text));
		
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
			ListDrwaer();
		}
	}// end async task

	public void accessWebService() {
		JsonReadTask task = new JsonReadTask();
		// passes values for the urls string array
		task.execute(new String[] { url });
	}

	// build hash set for list view
	public void ListDrwaer() {
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("trans");
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				String name = jsonChildNode.optString("SubDiv");
				
					ar.add(name);
			}
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ar);
		   	 
	        // Setting the array adapter containing country list to the spinner widget
	        sp.setAdapter(adapter);
	        OnItemSelectedListener area = new OnItemSelectedListener() {
	        	 
	            @Override
	            public void onItemSelected(AdapterView<?> spinner, View container,
	                    int position, long id) {
	            	String s=null;
	            text = sp.getSelectedItem().toString();
	            try {
					gettrans();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
			e.printStackTrace();
				}
	         	Toast.makeText(	getActivity(), s,
						Toast.LENGTH_SHORT).show();
	            }
	          
	            @Override
	            public void onNothingSelected(AdapterView<?> arg0) {
	                // TODO Auto-generated method stub
	            }
	        };
	        sp.setOnItemSelectedListener(area);
	       
		} catch (JSONException e) {
			Toast.makeText(	getActivity(), "Error" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}        String[] from = { "flag","txt"};

		
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
	void gettrans() throws JSONException
	{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/getspinner.php");
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
				String name1 = jsonChildNode.optString("Code");
				
					ar1.add(name1);
			}
			  ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(	getActivity(), android.R.layout.simple_dropdown_item_1line, ar1);
			   	 
		        // Setting the array adapter containing country list to the spinner widget
		        sp1.setAdapter(adapter1);
		        OnItemSelectedListener area1 = new OnItemSelectedListener() {
		        	 
		            @Override
		            public void onItemSelected(AdapterView<?> spinner, View container,
		                    int position, long id) {
		            	String s=null;
		            text1 = sp1.getSelectedItem().toString();
		           
		        
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
