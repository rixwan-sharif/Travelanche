package com.example.rixwansharif.travelanche;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rixwan Sharif on 7/14/2017.
 */
public class PostTripRequest extends StringRequest {

    private Map<String,String> params;



    public PostTripRequest(String user_city,String vehicle,int driver,int ac,String destination,Long trip_days,String start_date,String end_date,
                           String time_pickup,String time_drop,String location_pickup,String trip_description,
                           String user_phone,Response.Listener<String> responseListener,Response.ErrorListener errorListener)
    {
        super(Method.POST, config.Post_Trip_URL,responseListener,errorListener);
        params=new HashMap<>();

        params.put("user_city",user_city);
        params.put("vehicle",vehicle);

        params.put("driver",driver + "");
        params.put("ac",ac + "");

        params.put("destination",destination);
        params.put("trip_days",trip_days + "");
        params.put("start_date",start_date);
        params.put("end_date",end_date);
        params.put("time_pickup",time_pickup);
        params.put("time_drop",time_drop);
        params.put("location_pickup",location_pickup);
        params.put("trip_description",trip_description);
        params.put("user_phone",user_phone);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
