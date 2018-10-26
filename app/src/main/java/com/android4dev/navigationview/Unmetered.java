package com.android4dev.navigationview;


import java.util.ArrayList;



import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.ComponentName;
//import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class Unmetered extends Fragment {
	Button b,b1;
	EditText et,et1,et2,et3,et4;
	private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
	 HttpResponse response;
	 String acno,div,email;
	 TextView tv1,tv2;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.disconnection,container,false);
		Bundle bundle = this.getArguments();
		email=  bundle.getString("email");
		acno = bundle.getString("acno");
		div= bundle.getString("div");
		System.out.println(div);
		 b = (Button)v.findViewById(R.id.mainsubmit);

		 et = (EditText)v.findViewById(R.id.mainnumber);
		 et1 = (EditText)v.findViewById(R.id.mainname);
	        et2 = (EditText)v.findViewById(R.id.mainfather);
	        et3 = (EditText)v.findViewById(R.id.mainadd);
	  	  et4 = (EditText)v.findViewById(R.id.discheq);
	  	  tv1=(TextView)v.findViewById(R.id.disdiv);
	  	tv2=(TextView)v.findViewById(R.id.disac);
	  	tv1.setText(div);
	  	tv2.setText(acno);
  
    b.setOnClickListener(new OnClickListener() {
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

	void store(){
		try{			
			 
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/Unmetered.php");
		
			// make sure the url is correct.
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("acno",acno));
			nameValuePairs.add(new BasicNameValuePair("div",div)); // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("check_amount",et1.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("discon",et2.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("bank",et3.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("chequeno",et4.getText().toString().trim())); 
			nameValuePairs.add(new BasicNameValuePair("rdfee",et.getText().toString().trim())); 
			nameValuePairs.add(new BasicNameValuePair("email",email));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//Execute HTTP Post Request
			
			
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

}
