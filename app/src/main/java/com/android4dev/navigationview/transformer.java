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
import android.content.ComponentName;
//import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
import android.widget.AdapterView.OnItemSelectedListener;


public class transformer extends Fragment {
	Button b,b1,b2;private String url = "http://www.aditya.multifacet-software.com/php/store/getspinner.php",outPut,text,file;

	private final static String TAG_FRAGMENT = "TAG_FRAGMENT";ArrayAdapter<String> adapter1;
	EditText et,sp1;ListView lv;
	TextView textview1,textview2,tvdiv;
	ArrayList<String> ar = new ArrayList<String>();
	ArrayList<String> ar1 = new ArrayList<String>();
	 HttpResponse response;;String type,email,acno,div,jsonResult,jsonResult2;Spinner et1;
	  String[] sub = new String[] {
			    "Lakhimpur",
		        "Maswanpur",
		        "Nirala Nagar",
		       
		        
		    };
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.transformer, container, false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);
		Bundle bundle = this.getArguments();
		lv=(ListView)v.findViewById(R.id.translv);
		acno =bundle.getString("acno");
		email= bundle.getString("email");
		div= bundle.getString("div");
		 b = (Button)v.findViewById(R.id.replaceimagemtr);
		 b1 = (Button)v.findViewById(R.id.replaceseal);
		
		 tvdiv = (TextView)v.findViewById(R.id.ettranscode);
		 tvdiv.setText(div);
	        et1= (Spinner)v.findViewById(R.id.ettransname);
		lv.setVisibility(View.INVISIBLE);
	        sp1 = (EditText) v.findViewById(R.id.sptranscode);

    b.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			 new Thread(new Runnable() {
				    public void run() {
						camera_tranformer fragment = new camera_tranformer();
						Bundle bundle = new Bundle();
						bundle.putString("email", email);
						bundle.putString("set", "addpole");
						fragment.setArguments(bundle);
						android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

						fragmentTransaction.replace(R.id.frame, fragment);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						      
				    }
				  }).start();
			 
			 }
	});
    b1.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			 new Thread(new Runnable() {
				    public void run() {
				    	 store();

						getFragmentManager().popBackStack();
					    	//startActivity(getIntent());
				    }
				  }).start();	
			 
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
	/*public void onBackPressed() {
		final transformer fragment = (transformer) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
		int count = getFragmentManager().getBackStackEntryCount();
		if (count == 0) {
			super.getActivity().onBackPressed();
			//additional code
		} else {
			getFragmentManager().popBackStack();
		}
	}*/
	void store(){
		try{			
			 
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/transformer.php"); 
	
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("type",type));  // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("code",sp1.getText().toString().trim()));
			
			nameValuePairs.add(new BasicNameValuePair("email",email));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//Execute HTTP Post Request
			 response = httpclient.execute(httppost);
			
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
						Toast.makeText(getActivity(), "Got Hit", Toast.LENGTH_SHORT).show();
					}
				});


			}else{
							
			}
			
		}catch(Exception e){
			
			System.out.println("Exception : " + e.getMessage());
		}
	}
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
			et1.setAdapter(adapter);
			OnItemSelectedListener area = new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> spinner, View container,
										   int position, long id) {
					String s=null;
					text = et1.getSelectedItem().toString();
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
			et1.setOnItemSelectedListener(area);

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
			 adapter1 = new ArrayAdapter<String>(	getActivity(), android.R.layout.simple_dropdown_item_1line, ar1);

			// Setting the array adapter containing country list to the spinner widget
			lv.setAdapter(adapter1);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
					String selectedFromList =(String) (lv.getItemAtPosition(myItemInt));
					sp1.setText(selectedFromList);
					lv.setVisibility(View.INVISIBLE);

				}
			});

			lv.setTextFilterEnabled(true);
			sp1.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					// When user changed the Text
					transformer.this.adapter1.getFilter().filter(cs);
					lv.setVisibility(View.VISIBLE);
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
}
