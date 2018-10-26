package com.android4dev.navigationview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.MapFragment;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

@SuppressLint("NewApi")
public class Mappath extends Fragment {
	private static  LatLng LOWER_MANHATTAN = new LatLng(26.472620,
			80.323107);
	ArrayList<Driver> drivers=new ArrayList<Driver>();HashMap<Marker, Driver> markers=new HashMap<Marker, Driver>();
	String jsonloc;ArrayList<Marker> mapMarker;
	private static final LatLng TIMES_SQUARE = new LatLng(40.7577, -73.9857);
	private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
	ArrayList<LatLng> points = new ArrayList<LatLng>();
	PolylineOptions polyLineOptions =new PolylineOptions();
	private GoogleMap googleMap;
	 ArrayList<String> lat=new ArrayList<String>();
	 ArrayList<String> log=new ArrayList<String>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.path,container,false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);
		//String[] array=b.getStringArray(key);
		
	//	setUpMapIfNeeded();
		call();
		return v;
	}

	private void setUpMapIfNeeded() {
		// check if we have got the googleMap already
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (googleMap != null) {
				googleMap.addPolyline(polyLineOptions);
			}
		}
	}

	private void addLines() {

		googleMap
				.addPolyline((new PolylineOptions())
						.add(TIMES_SQUARE, BROOKLYN_BRIDGE, LOWER_MANHATTAN,
								TIMES_SQUARE).width(5).color(Color.BLUE)
						.geodesic(true));
		// move camera to zoom on map
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN,
				13));
	}
	
	public boolean call()
	{
		
		 HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.aditya.multifacet-software.com/kesco/store/getlocation.php"); 
		
			// make sure the url is correct.
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("div","Nawabganj"));  
			
			try
			{// $Edittext_value = $_POST['Edittext_value'];
				System.out.println("in try"); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			System.out.println("in entity"); 
			HttpResponse response = httpclient.execute(httppost);
			//Execute HTTP Post Request
			
			 
			
		
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response1 = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response1); 
			jsonloc = inputStreamToString(
					response.getEntity().getContent()).toString();
			JSONObject jsonResponse = new JSONObject(jsonloc);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("location");
				
			for (int i = 0; i < jsonMainNode.length(); i++) {
				Driver d=new Driver();
				
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				d.setLatitude(jsonChildNode.getString("Lat"));
				d.setLongitude(jsonChildNode.getString("Longi"));
				d.setName(jsonChildNode.getString("CONSUMER_NAME"));
		
			drivers.add(d);
			System.out.println(drivers.get(i).getLatitude());
		
		
		
			}
		
			for (int j = 0; j <drivers.size(); j++) {
				

				double lat = Double.parseDouble(drivers.get(j).getLatitude());
				double lng = Double.parseDouble(drivers.get(j).getLongitude());
				LatLng position = new LatLng(lat, lng);

				points.add(position);
			}

			polyLineOptions.addAll(points);
				polyLineOptions.width(2);
			polyLineOptions.color(Color.BLUE);
		
			setUpMapIfNeeded();
			LOWER_MANHATTAN=new LatLng( Double.parseDouble(drivers.get(0).getLatitude()),Double.parseDouble(drivers.get(0).getLongitude()));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN,
				13));
		mapMarker=new ArrayList<Marker>();
		System.out.println(drivers.size());
		
		for (int i = 0; i < drivers.size(); i++) {
	 MarkerOptions mark=new MarkerOptions();
	 String lati = drivers.get(i).getLatitude();
	 String longit = drivers.get(i).getLongitude();
	 System.out.println(lati+longit);
	mark.position(new LatLng(Double.parseDouble(lati),Double.parseDouble(longit)));
	mark.title(drivers.get(i).getName());
	mark.icon(BitmapDescriptorFactory.fromResource(R.drawable.hl));
	Marker m=googleMap.addMarker(mark);
	mapMarker.add(m);
	markers.put(m, drivers.get(i));
		}
			if(response1.equalsIgnoreCase("User Found")){
			
				    	//Toast.makeText(Location.this,"Got Hit", Toast.LENGTH_SHORT).show();
				   
				
				
			    
			    
			}else{
							
			}
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception in loc: " + e.getMessage());
		}
			return true;
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
		
		}
		return answer;
	}
}