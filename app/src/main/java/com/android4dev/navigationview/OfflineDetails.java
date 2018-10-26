package com.android4dev.navigationview;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Context;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class OfflineDetails extends Fragment {
    Button b;
    EditText et,et1,et2,et3;
    TextView tv;
    StringBuffer buffer;
    ProgressDialog dialog = null;
    SQLiteDatabase db;
    String email,div;
    boolean count=false;
    String ctID,ctCode,ctName,ctOfficeID,ctflag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        db=getActivity().openOrCreateDatabase("LESCO", Context.MODE_PRIVATE, null);
        Bundle bundle = this.getArguments();
        email= bundle.getString("email");
        div= bundle.getString("div");
        System.out.println("details div"+div);
        b = (Button)v.findViewById(R.id.Button01);
        et = (EditText)v.findViewById(R.id.inputacno);
        //pass= (EditText)findViewById(R.id.password);
        et1 = (EditText)v.findViewById(R.id.inputmtrno);
        et2 = (EditText)v.findViewById(R.id.inputbook);
        et3 = (EditText)v.findViewById(R.id.inputscno);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        login();
            }
        });

        return v;
    }

    void login(){
       // db=getActivity().openOrCreateDatabase("LESCO", Context.MODE_PRIVATE, null);
        String username=et.getText().toString().trim();
        String meter=et1.getText().toString().trim();
        String book=et2.getText().toString().trim();
        String scno=et3.getText().toString().trim();
        if(meter.length() > 0)
        {            Cursor c = db.rawQuery("select * from Customer_Details where  MtrSrlNo='"+meter+"'  AND Div=(select code from Division_Master where Id='"+div+"')", null);
            if (c.moveToFirst()){
                count=true;
            }
            else
            {
                count =false;
            }
        }
        else if(username.length() > 0) {
            String query = "select * from Customer_Details where  ACCTID='" + username + "'  AND Div=(select code from Division_Master where Id='" + div + "')";
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                count = true;
            } else {
                count =false;
            }

        }
        if(count)
        {
            OfflineAct fragment = new OfflineAct();
            Bundle bundle = new Bundle();
            System.out.println("ACCno"+username);
            bundle.putString("uname", et.getText().toString());
            bundle.putString("area", div);
            bundle.putString("meter", et1.getText().toString());
            bundle.putString("book", et2.getText().toString());
            bundle.putString("scno", et3.getText().toString());
            bundle.putString("email", email);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.offlineframe,fragment).addToBackStack("fragBack").commit();
        }
        else {
            Snackbar.make(getView(),"No User Found", Snackbar.LENGTH_LONG).show();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_settings1);
        item.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.action_settings3);
        item2.setVisible(false);
        MenuItem item3 = menu.findItem(R.id.action_scan);
        item3.setVisible(false);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {


            // Launching the login activity
            getActivity().finish();
            System.exit(0);
        }


        return super.onOptionsItemSelected(item);
    }


}
