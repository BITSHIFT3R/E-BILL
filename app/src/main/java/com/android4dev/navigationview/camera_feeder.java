package com.android4dev.navigationview;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.provider.MediaStore;

public class camera_feeder extends ActionBarActivity {
	Uri selectedImageUri= null;
	 private Bitmap bitmap;
	Uri imageUri;
	String message,set,acno,email;
	 double latitude =0.00;
     double longitude=0.00;
	 GPSTracker gps;
	 JSONParser jsonParser = new JSONParser();
	 private ProgressDialog pDialog,dialog;
	 MediaPlayer mp=new MediaPlayer();
		static final String URL_ALBUMS = "http://www.aditya.multifacet-software.com/kesco/image_feeder.php";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		Intent intent = getIntent();
		set = intent.getStringExtra("set");
		email= intent.getStringExtra("email");
		acno = intent.getStringExtra("acno");
		Button txt1=(Button)findViewById(R.id.dissum);
		Button txt2=(Button)findViewById(R.id.dup);
		Toast.makeText(getApplicationContext(), set , Toast.LENGTH_LONG).show(); 
		
		txt1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				//Toast.makeText(getApplicationContext(), "Under construction....  " , Toast.LENGTH_LONG).show(); 

					
 //					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		             File f = new File(android.os.Environment.getExternalStorageDirectory(), "subtemp.jpg");
//		             intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		             startActivityForResult(intent, 1);
					
					String fileName = "subimage.jpg";
	                   //create parameters for Intent with filename
	                   ContentValues values = new ContentValues();
	                   values.put(MediaStore.Images.Media.TITLE, fileName);
	                   values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
	                   //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
	                   imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	                   //create new Intent
	                   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                   intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	                   intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	                   startActivityForResult(intent, 1);
				}
				
		    	
		    }); 
		txt2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					gps = new GPSTracker(camera_feeder.this);
					 
	                // check if GPS enabled    
					if(gps.canGetLocation()){
	                     
	                    latitude = gps.getLatitude();
	                   longitude = gps.getLongitude();
	                     if(latitude!=0 && longitude!=0)
	                     {
	                    	 if (bitmap == null) {
	 							Toast.makeText(getApplicationContext(),
	 									"Please Select Image ..", Toast.LENGTH_SHORT).show();
	 						} else {
//	 							dialog = ProgressDialog.show(SellMyProperty.this, "Uploading",
//	 									"Please wait...", true);
	 						
	 					new ImageUploadTask().execute();
	 					finish();
	 						}
	                     }
	                     else{
								Toast.makeText(getApplicationContext(), "Please Wait Gps fetch the value....  " , Toast.LENGTH_LONG).show(); 
							}
	                    // \n is for new line
	                   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();   
	               //latlong="Lat:-"+latitude+"Long:-"+longitude+" ";
	               
					
	                
	              
	                }else{
	                    // can't get location
	                    // GPS or Network is not enabled
	                    // Ask user to enable GPS/network in settings
	                	 buildAlertMessageNoGps();
	                   
	                 //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();   
		               // latlong="Lat:-"+latitude+"Long:-"+longitude+" ";
	                    
	                }
					
				
				}
				});
		
	}

	

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        String filePath = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

            	 selectedImageUri = imageUri;
            } else if (requestCode == 2) {
            	
            	selectedImageUri = data.getData();
//                Uri selectedImageUri = data.getData();
//                String p=selectedImageUri.getPath();
//
//                String tempPath = data.getDataString();
//                Log.i("path of image from gallery......******************.........", p);
//                Bitmap bitmap;
//                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                bitmap = BitmapFactory.decodeFile(tempPath, bitmapOptions);
//                imgView.setImageBitmap(bitmap);

//                Uri selectedImage = data.getData();
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
//                Log.w("path of image from gallery......******************.........", picturePath+"");
//                imgView.setImageBitmap(bitmap);
            }
            
            if(selectedImageUri != null){
             try {
                 // OI FILE Manager
                 String filemanagerstring = selectedImageUri.getPath();

                 // MEDIA GALLERY
                 String selectedImagePath = getPath(selectedImageUri);
//                 txt9.setText(selectedImagePath);
                 if (selectedImagePath != null) {
                     filePath = selectedImagePath;
                 } else if (filemanagerstring != null) {
                     filePath = filemanagerstring;
                 } else {
                     Toast.makeText(getApplicationContext(), "Unknown path",
                             Toast.LENGTH_LONG).show();
                     Log.e("Bitmap", "Unknown path");
                 }

                 if (filePath != null) {
                     decodeFile(filePath);
                 } else {
                     bitmap = null;
                 }
             } catch (Exception e) {
                 Toast.makeText(getApplicationContext(), "Internal error",
                         Toast.LENGTH_LONG).show();
                 Log.e(e.getClass().getName(), e.getMessage(), e);
             }
     }
        }



}
    public void decodeFile(String filePath) {
		// Decode image size
    	//txt9.setText(filePath);
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE =256;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		Toast.makeText(getApplicationContext(),  "Image Captured....",
					Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(),  acno,
				Toast.LENGTH_LONG).show();
		//imgView.setImageBitmap(bitmap);

	}
    public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}
    class ImageUploadTask extends AsyncTask<Void, Void, String> {
   	  protected void onPreExecute() {
  			super.onPreExecute();
  			dialog = ProgressDialog.show(camera_feeder.this, "Image Uploading",
  					"Please wait...", true);
  			
  		}

  		@SuppressWarnings("unused")
  		@Override
  		protected String doInBackground(Void... unsued) {

  			InputStream is;

  			BitmapFactory.Options bfo;
  			Bitmap bitmapOrg;
  			ByteArrayOutputStream bao;

  		 bfo = new BitmapFactory.Options();
       bfo.inSampleSize = 3;
               
       bao = new ByteArrayOutputStream();
       bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bao);
       
       byte [] ba = bao.toByteArray();
       
        String ims = Base64.encodeToString(ba,Base64.DEFAULT);

  	               System.out.println(ims);
  	  			
  		                List<NameValuePair> params = new ArrayList<NameValuePair>();
  		            	params.add(new BasicNameValuePair("method", "upimage"));
  		            	params.add(new BasicNameValuePair("image", ims));
  		            	params.add(new BasicNameValuePair("acno", acno));
  		          	params.add(new BasicNameValuePair("Lat", Double.toString(latitude)));
  		      	params.add(new BasicNameValuePair("Long", Double.toString(longitude)));
  		            	params.add(new BasicNameValuePair("cmd", "Nano"+System.currentTimeMillis()+".PNG"));
  		            	params.add(new BasicNameValuePair("set", set));
  		          	params.add(new BasicNameValuePair("email", email));

  			            	// getting JSON string from URL
  			    			String json = jsonParser.makeHttpRequest(URL_ALBUMS, "POST",
  			    					params);
  			            	
  			    		
							try {
                                 JSONObject jObj = new JSONObject(json);
  			    				
  								//image.add(j,jObj.getString("image"));
  			    				message = jObj.getString("message");
  			    				
  			    				
//  			            	Toast.makeText(getApplicationContext(),
//  			            			albums,
//  			                        Toast.LENGTH_LONG).show();
  			            	
  			            	}catch(Exception e){
  			            	Log.v("log_tag", "Error in http connection "+e.toString());
  			            	}
  			    			
  			            return message;
  			            // (null);
  			        }

  				

  		  		@Override
  				protected void onProgressUpdate(Void... unsued) {

  				}

  				@Override
  				protected void onPostExecute(String sResponse) {
  					try {
  						if (dialog.isShowing())
  							dialog.dismiss();
  						//txt9.setText(albums);
  						Toast.makeText(getApplicationContext(),  sResponse,
  								Toast.LENGTH_LONG).show();
  													
  					selectedImageUri = null;
  						
  						
  					} catch (Exception e) {
  						Toast.makeText(getApplicationContext(), e.getMessage(),
  								Toast.LENGTH_LONG).show();
  						Log.e(e.getClass().getName(), e.getMessage(), e);
  					}
  				}
  			 
  			    }   
    	
	

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("  Enable  GPS  ")
                .setCancelable(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    }

