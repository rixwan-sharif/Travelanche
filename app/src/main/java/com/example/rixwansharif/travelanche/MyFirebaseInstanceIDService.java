package com.example.rixwansharif.travelanche;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Rixwan Sharif on 12/22/2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        Log.i("Firebase", "tokenid " + FirebaseInstanceId.getInstance().getToken());

        SaveDeviceToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void SaveDeviceToken(String device_token)
    {

        SharedPreferences sharedPreferences = MyFirebaseInstanceIDService.this.getSharedPreferences(config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(config.Device_Token, device_token);

        //Saving values to editor
        editor.commit();

    }
}
