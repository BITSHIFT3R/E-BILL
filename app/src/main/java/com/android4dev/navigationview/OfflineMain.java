package com.android4dev.navigationview;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

public class OfflineMain extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;String email,div;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    String query;
    SQLiteDatabase db,userdata;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_main_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        Intent intent = getIntent();
        uid= intent.getStringExtra("email");
        div= intent.getStringExtra("div");
        db=openOrCreateDatabase("Lesco_data", Context.MODE_PRIVATE, null);
        Cursor ldc = db.rawQuery("SELECT uid,Div FROM emp", null);
        System.out.println("sql"+ldc.moveToFirst());
        if(ldc.moveToFirst())
        {
            uid=ldc.getString(0);
            div=ldc.getString(1);
            System.out.println(" database division" + uid + "-" + div);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Incorrect input details", Toast.LENGTH_LONG).show();
        }
        System.out.print("sending" + email + "-" + uid);
        OfflineDetails s1fragment=new OfflineDetails();
        Bundle d1bundle = new Bundle();
        s1fragment.setArguments(d1bundle);
        d1bundle.putString("email", uid);
        d1bundle.putString("div",div);
        s1fragment.setArguments(d1bundle);
        android.support.v4.app.FragmentTransaction s1fragmentTransaction = getSupportFragmentManager().beginTransaction();
        s1fragmentTransaction.replace(R.id.offlineframe,s1fragment).addToBackStack("fragBack").commit();

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.offlinetoolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.offline_navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){

                    case R.id.offlinestarred:
                        OfflineDetails sfragment=new OfflineDetails();
                        Bundle dbundle = new Bundle();
                        sfragment.setArguments(dbundle);
                        dbundle.putString("email", uid);
                        dbundle.putString("div",div);
                        android.support.v4.app.FragmentTransaction sfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        sfragmentTransaction.replace(R.id.offlineframe,sfragment).addToBackStack("fragBack").commit();

                        Toast.makeText(getApplicationContext(),"Stared Selected",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.offlineupload:
                        Upload sfragment1=new Upload();
                        Bundle dbundle1 = new Bundle();
                        sfragment1.setArguments(dbundle1);
                        dbundle1.putString("uid",uid);
                        dbundle1.putString("email", email);
                        android.support.v4.app.FragmentTransaction sfragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        sfragmentTransaction1.replace(R.id.offlineframe,sfragment1).addToBackStack("fragBack").commit();

                        Toast.makeText(getApplicationContext(),"Stared Selected",Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.offlinedrawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
