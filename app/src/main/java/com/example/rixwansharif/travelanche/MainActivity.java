package com.example.rixwansharif.travelanche;



import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import junit.framework.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mylayout;
    private ActionBarDrawerToggle mytoggle;
    private Toolbar mtoolbar;
    private Button plan_a_trip,companies,my_trips;
    private NavigationView navigationView;


    private CircleImageView profile_imageView;
    private TextView username,phone_number;


    String phone,name,city;


    CheckInternet cd=new CheckInternet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //widgets

        mtoolbar=(Toolbar) findViewById(R.id.nav_action);
        mylayout=(DrawerLayout) findViewById(R.id.mylayout);
        navigationView=(NavigationView) findViewById(R.id.mynavigationview);
        my_trips=(Button) findViewById(R.id.my_trips);
        plan_a_trip=(Button) findViewById(R.id.plan_a_trip);
        companies=(Button) findViewById(R.id.companies);
        profile_imageView=(CircleImageView) findViewById(R.id.profile_image);
        username=(TextView) findViewById(R.id.profile_user_name);
        phone_number=(TextView) findViewById(R.id.profile_user_phone);



        //Set action bar
        setSupportActionBar(mtoolbar);


       // NavigationView

        mytoggle=new ActionBarDrawerToggle(this,mylayout,R.string.open,R.string.close);
        mylayout.setDrawerListener(mytoggle);
        mytoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);


        //Set profile

        Set_profile();






        plan_a_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(175);
                v.startAnimation(buttonClick);
                Intent intent = new Intent(MainActivity.this, TripActivity.class);
                startActivity(intent);


            }
        });

        my_trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(175);
                v.startAnimation(buttonClick);
                Intent intent=new Intent(MainActivity.this,MyTripsActivity.class);
                startActivity(intent);


            }
        });

        companies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(175);
                v.startAnimation(buttonClick);
                Intent intent = new Intent(MainActivity.this, CompaniesActivity.class);
                startActivity(intent);
            }
        });

    }




 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (mytoggle.onOptionsItemSelected(item)) {


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {


        int id = menuItem.getItemId();

        if(id==R.id.notification)
        {

        }

        if(id==R.id.my_trips)
        {
            Intent intent=new Intent(MainActivity.this,AcceptedBidsActivity.class);
            startActivity(intent);
        }
        if(id==R.id.settings)
        {
            Intent intent=new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
        }
        if(id==R.id.logout)
        {
            logout();
        }
        if(id==R.id.address)
        {
            Intent intent=new Intent(MainActivity.this,ContactUsActivity.class);
            startActivity(intent);
        }
        if(id==R.id.privacy_policy)
        {


        }



        mylayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //logout function

    private void logout()
    {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Drawer_menu_Theme));
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.Phone_SHARED_PREF, "");
                        editor.putString(Config.F_Name_SHARED_PREF, "");
                        editor.putString(Config.L_Name_SHARED_PREF, "");
                        editor.putString(Config.City_SHARED_PREF, "");
                        editor.putString(Config.Image_SHARED_PREF, "");


                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();



    }

    private void Set_profile()
    {



        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        phone= sharedPreferences.getString(Config.Phone_SHARED_PREF,"Not Available");
        name=sharedPreferences.getString(Config.F_Name_SHARED_PREF, "Not Available")+" "+sharedPreferences.getString(Config.L_Name_SHARED_PREF, "Not Available");


        username.setText(name);
        phone_number.setText(phone);


        final String pic_path=sharedPreferences.getString(Config.Image_SHARED_PREF, "Not Available");

      Picasso.with(getApplicationContext())
                .load("http://rixwanxharif.000webhostapp.com/" + pic_path)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(profile_imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(getApplicationContext())
                                .load("http://rixwanxharif.000webhostapp.com/" + pic_path)
                                .into(profile_imageView);
                    }
                });



    }


    //



    public static Bitmap getBitmapImage(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    public void Notify(View view) {

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_green_car_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_green_car_icon));
        builder.setContentTitle("Notification");
        builder.setContentText("this is my first notification");

        builder.setColor(101195);
        builder.setNumber(9);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());


        TaskStackBuilder stackBuilder= TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        Intent i=new Intent(this,BidsOnTripActivity.class);
        stackBuilder.addNextIntent(i);

        //to run
        PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,builder.build());
    }





    @Override
    protected void onResume()
    {
        super.onResume();
      Set_profile();
    }

    //back key
    @Override
    public void onBackPressed()
    {

        if(mylayout.isDrawerOpen(GravityCompat.START))
        {
            mylayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            moveTaskToBack(true);
            finishAffinity();
        }
    }

}
