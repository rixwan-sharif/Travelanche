package com.example.rixwansharif.travelanche;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rixwan Sharif on 7/14/2017.
 */
public class LoginRequest extends StringRequest {

    private Map<String,String> params;



    public LoginRequest(String phone,String password,String device_token,Response.Listener<String> responseListener,Response.ErrorListener errorListener)
    {
        super(Method.POST,Config.Login_URL,responseListener,errorListener);
        params=new HashMap<>();
        params.put("phone",phone);
        params.put("password",password);
        params.put("device_token",device_token);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }


}
