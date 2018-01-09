package com.example.rixwansharif.travelanche;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class UploadimageRequest  extends StringRequest {

    private Map<String,String> params;



    public UploadimageRequest (String phone,String image,Response.Listener<String> responseListener,Response.ErrorListener errorListener)
    {
        super(Method.POST, config.Upload_Image_URL,responseListener,errorListener);
        params=new HashMap<>();
        params.put("phone",phone);
        params.put("image",image);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }


}

