
package com.example.rixwansharif.travelanche;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
boolean loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        //Check if user alredy login

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        //If we will get true


        Thread myThread = new Thread() {
                @Override
                public void run() {
                    try {

                        sleep(2300);

                        if (loggedIn)
                        {
                            //We will start the Profile Activity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }
                        else {

                            Intent intent = new Intent(getApplicationContext(), Main_Page_Activity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };

            //Starting Thread
            myThread.start();


    }

}
