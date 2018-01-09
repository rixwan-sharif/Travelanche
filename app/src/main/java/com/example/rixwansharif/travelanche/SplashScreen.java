
package com.example.rixwansharif.travelanche;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {


boolean loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        //Check if user alredy login

        SharedPreferences sharedPreferences = getSharedPreferences(config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(config.LOGGEDIN_SHARED_PREF, false);
        //If we will get true

        //ImageView sp=(ImageView) findViewById((R.id.rabia));

        //Animation zm= AnimationUtils.loadAnimation(this,R.anim.splash_screen_anim);
        //sp.startAnimation(zm);

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
