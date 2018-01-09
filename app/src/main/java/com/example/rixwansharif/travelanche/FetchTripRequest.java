package com.example.rixwansharif.travelanche;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rixwan Sharif on 9/23/2017.
 */
public class FetchTripRequest extends StringRequest {

    private Map<String,String> params;



    public FetchTripRequest(String phone,String trip_status,Response.Listener<String> responseListener,Response.ErrorListener errorListener)
    {
        super(Request.Method.POST, config.Load_Trips_URL,responseListener,errorListener);
        params=new HashMap<>();
        params.put("phone",phone);
        params.put("trip_status",trip_status);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
