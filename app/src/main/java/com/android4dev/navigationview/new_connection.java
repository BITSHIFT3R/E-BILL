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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



public class new_connection extends Fragment {
	Button b,b1,b2,b3;
	EditText et,et1,et2,et3;
	private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
	 HttpResponse response;HttpClient httpclient;HttpPost httppost;
	 TextView tv1,tv2;
	 Spinner sp1,sp2;
	String acno,div,type,status,set="new",set1="seal",email;
	 String[] mtype = new String[] {
			    "(None)",
		        "DIGITAL",
		        "CLOCK",
		        "MECHANICAL","SEMI-ELECTRONIC",
		        
		    };
	 String[] mstatus = new String[] {
			    "(None)",
		        "DEFECTIVE",
		        "EFFECTIVE",
		        "NORMAL",
		        
		    };
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.new_connection,container,false);
		Bundle bundle = this.getArguments();
		email=  bundle.getString("email");
		acno = bundle.getString("acno");
		div= bundle.getString("div");
		 b = (Button)v.findViewById(R.id.replaceimagemtr);
		
		 b2 = (Button)v.findViewById(R.id.replaceseal);
		 b1 = (Button)v.findViewById(R.id.replacesubmit);
		
		 et = (EditText)v.findViewById(R.id.replacmtrmake);
	       
	        et2 = (EditText)v.findViewById(R.id.replacnmtrn);
	  	  et3 = (EditText)v.findViewById(R.id.discheq);
	      tv1= (TextView)v.findViewById(R.id.replacacc);
	      tv2= (TextView)v.findViewById(R.id.repldiv);
	      tv1.setText(acno);
	     tv2.setText(div);
	     sp1 = (Spinner) v.findViewById(R.id.replacmtrt);
	     Toast.makeText(getActivity(),div, Toast.LENGTH_SHORT).show();
	     ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mtype);
	   	 
	        // Setting the array adapter containing country list to the spinner widget
	        sp1.setAdapter(adapter);
	        OnItemSelectedListener area = new OnItemSelectedListener() {
	        	 
	            @Override
	            public void onItemSelected(AdapterView<?> spinner, View container,
	                    int position, long id) {
	            	
	             type = sp1.getSelectedItem().toString();
	             
	            }
	 
	            @Override
	            public void onNothingSelected(AdapterView<?> arg0) {
	                // TODO Auto-generated method stub
	            }
	        };
	        sp1.setOnItemSelectedListener(area);   
	        sp2 = (Spinner) v.findViewById(R.id.replacmtrst);
		     ArrayAdapter<String> adapterst = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mstatus);
		   	 
		        // Setting the array adapter containing country list to the spinner widget
		        sp2.setAdapter(adapterst);
		        OnItemSelectedListener areas = new OnItemSelectedListener() {
		        	 
		            @Override
		            public void onItemSelected(AdapterView<?> spinner, View container,
		                    int position, long id) {
		            	
		             status = sp2.getSelectedItem().toString();
		             
		            }
		 
		            @Override
		            public void onNothingSelected(AdapterView<?> arg0) {
		                // TODO Auto-generated method stub
		            }
		        };
		        sp2.setOnItemSelectedListener(areas);   
    b.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			 new Thread(new Runnable() {
				    public void run() {
						camera fragment = new camera();
						Bundle bundle = new Bundle();
						bundle.putString("set", set);
						bundle.putString("acno", acno);
						bundle.putString("email",email);
						fragment.setArguments(bundle);
						android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

						fragmentTransaction.replace(R.id.frame, fragment);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
				    }
				  }).start();				
		}
	});
    b2.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			 new Thread(new Runnable() {
				    public void run() {
						camera fragment = new camera();
						Bundle bundle = new Bundle();
						bundle.putString("set", set1);
						bundle.putString("acno", acno);
						bundle.putString("email",email);
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
					    //	startActivity(getIntent());
				    }
				  }).start();	
			 Toast.makeText(getActivity(),"SUBMITTED", Toast.LENGTH_SHORT).show();
			
		}
		
	});
  return v;
}

	void store(){
		try{			
			 
		 httpclient = new DefaultHttpClient();
			 httppost = new HttpPost("http://www.aditya.multifacet-software.com/php/store/replacement.php"); 
		
			// make sure the url is correct.
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("acno",acno));  // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("div",div));//.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("mtrtype",type));
			nameValuePairs.add(new BasicNameValuePair("mtrmake",et.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("mtrstatus",status)); 

			nameValuePairs.add(new BasicNameValuePair("newmtrno",et2.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("email",email));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//Execute HTTP Post Request
			 response = httpclient.execute(httppost);
			

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
				Toast.makeText(getActivity(),"Galat h", Toast.LENGTH_SHORT).show();
			}
			
		}catch(Exception e){
			
			System.out.println("Exception : " + e.getMessage());
		}
	}

}
