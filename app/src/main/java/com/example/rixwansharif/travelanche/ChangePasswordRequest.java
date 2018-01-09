package com.example.rixwansharif.travelanche;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ChangePasswordRequest extends StringRequest {

    private Map<String,String> params;



    public ChangePasswordRequest(String phone,String c_password,String n_password,Response.Listener<String> responseListener,Response.ErrorListener errorListener)
    {
        super(Method.POST, config.CHANGE_PASSWORD__URL,responseListener,errorListener);
        params=new HashMap<>();
        params.put("phone",phone);
        params.put("c_password",c_password);
        params.put("n_password",n_password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }


}
