package com.android4dev.navigationview;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class PoleDetails extends Fragment {
	private String jsonResult;
	private String url = "http://www.aditya.multifacet-software.com/php/store/getpoledetail.php";
	EditText et,et1,et2,et3,et4,et5,et6,et7;
	private ListView listView;String docno;
	private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
	
	  
	  List<Map<String, String>> List = new ArrayList<Map<String, String>>();
	  String name,transcode,subcode,pno,code,pcode,div;
	String[] from = { "psacc","psname","pstrans","psetpolecode"};
	int[] to = { R.id.psacc,R.id.psname,R.id.pstrans,R.id.psetpolecode};
	Button b,cammtr,camdoc,cambld;
	TextView tv1;
	HttpResponse response;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.pole_details,container,false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Bundle bundle = this.getArguments();
		pcode= bundle.getString("code");
		div= bundle.getString("div");
		  
		   
			listView = (ListView) v.findViewById(R.id.listView1);
			Toast.makeText(getActivity(),
					pcode, Toast.LENGTH_LONG).show();
			accessWebService();
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
		// Async Task to access the web
		private class JsonReadTask extends AsyncTask<String, Void, String> {
			@Override
			protected String doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(params[0]);
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				
				nameValuePairs.add(new BasicNameValuePair("code",pcode));

				try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					jsonResult = inputStreamToString(
							response.getEntity().getContent()).toString();
					System.out.println("Response : " + jsonResult); 
				}

				catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
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
			List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();
			try {
				JSONObject jsonResponse = new JSONObject(jsonResult);
				JSONArray jsonMainNode = jsonResponse.optJSONArray("pole_detail");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					String name = jsonChildNode.optString("PerName");
					
					String acc = jsonChildNode.optString("ACCTID");
					String trans = jsonChildNode.optString("DTCode");
					String pole= jsonChildNode.optString("gispoleid");
					 employeeList.add(createEmployee("employees", acc,name,trans,pole));
				}
			} catch (JSONException e) {
				Toast.makeText(getActivity(), "Error" + e.toString(),
						Toast.LENGTH_SHORT).show();
			}       

			SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), employeeList,
				    R.layout.polesearchitem,
				  from, to){
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {

					// get filled view from SimpleAdapter
					View itemView = super.getView(position, convertView, parent);
					Button up=(Button)itemView.findViewById(R.id.psupdate);
					final TextView acc=(TextView)itemView.findViewById(R.id.psacc);
					final EditText pol=(EditText)itemView.findViewById(R.id.psetpolecode);
					System.out.print("acc"+acc.getText().toString());
					System.out.print("pole"+pol.getText().toString());
							up.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {


							HttpClient httpclient = new DefaultHttpClient();
							HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/updatepole.php");
							ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

							nameValuePairs.add(new BasicNameValuePair("acc", acc.getText().toString()));
							nameValuePairs.add(new BasicNameValuePair("pole", pol.getText().toString().trim()));
							try {
								httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
								HttpResponse response = httpclient.execute(httppost);
								jsonResult = inputStreamToString(
										response.getEntity().getContent()).toString();
								System.out.println("response" + jsonResult);
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							getFragmentManager().popBackStack();
						}
					});
					return itemView;
				}
				};
				listView.setAdapter(simpleAdapter);
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
		private HashMap<String, String> createEmployee(String name, String acc,String nam,String trans,String pole) {
			HashMap<String, String> employeeNameNo = new HashMap<String, String>();
			
		
			employeeNameNo.put("psacc", acc );
			employeeNameNo.put("psname", nam);
			employeeNameNo.put("pstrans", trans);
			employeeNameNo.put("psetpolecode", pole);
		
			
			return employeeNameNo;
		}
}
