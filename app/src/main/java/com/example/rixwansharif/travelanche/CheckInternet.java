package com.example.rixwansharif.travelanche;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by Rixwan Sharif on 7/19/2017.
 */
public class CheckInternet {

    Context context;

    public CheckInternet(Context context)
    {
        this.context=context;
    }

    public boolean isConnected()
    {
        ConnectivityManager connectivity=(ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity!=null)
        {
            NetworkInfo info=connectivity.getActiveNetworkInfo();
            if(info!=null)
            {
                if(info.getState()==NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }

        }
        return false;

    }









}
