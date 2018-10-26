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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
import android.widget.AdapterView.OnItemSelectedListener;


public class substation extends Fragment {
	Button b,b1,reset;String url = "http://www.aditya.multifacet-software.com/php/store/getspinner.php",outPut,text,file;
	ArrayList<String> ar = new ArrayList<String>();
	EditText et,et1,sp1;TextView tvdiv;ListView lv;	ArrayAdapter<String> adapter;
	private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
	 HttpResponse response;String type,acno,div,email,jsonResult;
	  String[] sub = new String[] {
			     "(None)",
		        "Nawabganj",
		        "mall road",
		        "kidwai nagar",
		        
		    };
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.substation, container, false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);
		Intent intent = getActivity().getIntent();
		email= intent.getStringExtra("email");
		div= intent.getStringExtra("div");
		lv=(ListView)v.findViewById(R.id.sublv);
		lv.setVisibility(View.INVISIBLE);
		 b = (Button)v.findViewById(R.id.replaceimagemtr);
		reset = (Button)v.findViewById(R.id.subreset);
		b1 = (Button)v.findViewById(R.id.replaceseal);
		 tvdiv = (TextView)v.findViewById(R.id.etsubcode);
	        et1= (EditText)v.findViewById(R.id.etsubname);
	       tvdiv.setText(div);
	      	Toast.makeText(getActivity(),email + div , Toast.LENGTH_SHORT).show();
			   
	        sp1 = (EditText) v.findViewById(R.id.spsubcode);
		accessWebService();
    b.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {

			new Thread(new Runnable() {
				public void run() {
					camera_substation fragment = new camera_substation();
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
		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new Thread(new Runnable() {
					public void run() {
						substation a=new substation();
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
   return v;
	}
	/*public void onBackPressed() {
		final substation fragment = (substation) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
		int count = getFragmentManager().getBackStackEntryCount();
		if (count == 0) {
			super.getActivity().onBackPressed();
			//additional code
		} else {


		}
	}*/
	void store(){
		try{			
			 
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/substation.php"); 
		
			// make sure the url is correct.
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("type",type));  // $Edittext_value = $_POST['Edittext_value'];

			nameValuePairs.add(new BasicNameValuePair("name",et1.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("email",email));
			nameValuePairs.add(new BasicNameValuePair("div",div));
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
			 adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ar);

			// Setting the array adapter containing country list to the spinner widget
			lv.setAdapter(adapter);
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
					substation.this.adapter.getFilter().filter(cs);
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
}
