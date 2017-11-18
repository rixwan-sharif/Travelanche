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



    public FetchTripRequest(String phone,Response.Listener<String> responseListener,Response.ErrorListener errorListener)
    {
        super(Request.Method.POST,Config.Load_Trips_URL,responseListener,errorListener);
        params=new HashMap<>();
        params.put("phone",phone);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
