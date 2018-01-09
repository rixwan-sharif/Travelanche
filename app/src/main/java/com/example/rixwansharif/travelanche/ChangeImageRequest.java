package com.example.rixwansharif.travelanche;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rixwan Sharif on 9/29/2017.
 */
public class ChangeImageRequest  extends StringRequest {

    private Map<String,String> params;



    public ChangeImageRequest (String phone,String image,String old_image,Response.Listener<String> responseListener,Response.ErrorListener errorListener)
    {
        super(Method.POST, config.Chaneg_Image_URL,responseListener,errorListener);
        params=new HashMap<>();
        params.put("phone",phone);
        params.put("image",image);
        params.put("old_image",old_image);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }


}
