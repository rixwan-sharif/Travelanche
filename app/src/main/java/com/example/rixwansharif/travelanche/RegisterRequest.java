package com.example.rixwansharif.travelanche;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rixwan Sharif on 7/14/2017.
 */
public class RegisterRequest extends StringRequest {

    private Map<String,String> params;



    public RegisterRequest(String first_name,String last_name,String city,String phone,String password,Response.Listener<String> responseListener,Response.ErrorListener errorListener)
    {
        super(Method.POST,Config.Register_URL,responseListener,errorListener);
        params=new HashMap<>();
        params.put("first_name",first_name);
        params.put("last_name",last_name);
        params.put("city",city);
        params.put("phone",phone);
        params.put("password",password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }




}
