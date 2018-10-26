package com.android4dev.navigationview;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.android4dev.navigationview.app.CheckNetwork;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;String email,div;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    String query;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        Intent intent = getIntent();
        db=openOrCreateDatabase("LESCO_data", Context.MODE_PRIVATE, null);
        email= intent.getStringExtra("email");
        div= intent.getStringExtra("div");

        System.out.print(" receiving division" + div+"-"+email);
        db=openOrCreateDatabase("Lesco_data", Context.MODE_PRIVATE, null);
        query= "SELECT uid,Div FROM emp";
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst())
        {
            email=c.getString(0);
            div=c.getString(1);
            System.out.print(" database division" + email+"-"+div);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Incorrect input details", Toast.LENGTH_LONG).show();
        }
        System.out.print("sending" + email + "-" + div);
        if(CheckNetwork.isInternetAvailable(MainActivity.this))  //if connection available
        {
            Details s1fragment=new Details();
            Bundle d1bundle = new Bundle();
            s1fragment.setArguments(d1bundle);
            d1bundle.putString("div",div);
            d1bundle.putString("email", email);
            android.support.v4.app.FragmentTransaction s1fragmentTransaction = getSupportFragmentManager().beginTransaction();
            s1fragmentTransaction.replace(R.id.frame,s1fragment).addToBackStack("fragBack").commit();
        }
        else
        {
            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "No INTERNET", Snackbar.LENGTH_LONG);
            View sview = snack.getView();
            TextView tv = (TextView) sview.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

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


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.starred:
                      Details sfragment=new Details();
                        Bundle dbundle = new Bundle();
                        sfragment.setArguments(dbundle);
                        dbundle.putString("div",div);
                        dbundle.putString("email", email);
                        android.support.v4.app.FragmentTransaction sfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        sfragmentTransaction.replace(R.id.frame,sfragment).addToBackStack("fragBack").commit();

                        Toast.makeText(getApplicationContext(),"Stared Selected",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.survey:
                        AddNew add=new AddNew();
                        Bundle badd = new Bundle();
                        add.setArguments(badd);
                        badd.putString("div",div);
                        badd.putString("email", email);
                        android.support.v4.app.FragmentTransaction MTransaction2 = getSupportFragmentManager().beginTransaction();
                        MTransaction2.replace(R.id.frame, add);
                        MTransaction2.commit();
                        //  Toast.makeText(getApplicationContext(),"Send Selected",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.addnew:
                        Survey sur=new Survey();
                        Bundle bsur = new Bundle();
                        sur.setArguments(bsur);
                        bsur.putString("div",div);
                        bsur.putString("email", email);
                        android.support.v4.app.FragmentTransaction MTransaction1 = getSupportFragmentManager().beginTransaction();
                        MTransaction1.replace(R.id.frame, sur);
                        MTransaction1.commit();
                        //  Toast.makeText(getApplicationContext(),"Send Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.logout:
                        Intent intent = new Intent(MainActivity.this,LogOut.class);
                        intent.putExtra("uid",email);
                        startActivity(intent);
                        //  Toast.makeText(getApplicationContext(),"Send Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                       return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
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
