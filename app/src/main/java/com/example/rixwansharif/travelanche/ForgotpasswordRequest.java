package com.example.rixwansharif.travelanche;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rixwan Sharif on 7/14/2017.
 */
public class ForgotpasswordRequest extends StringRequest {

    private Map<String,String> params;



    public ForgotpasswordRequest(String phone,Response.Listener<String> responseListener,Response.ErrorListener errorListener)
    {
        super(Method.POST, config.ForgotPassword_URL,responseListener,errorListener);
        params=new HashMap<>();
        params.put("phone",phone);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }


}
