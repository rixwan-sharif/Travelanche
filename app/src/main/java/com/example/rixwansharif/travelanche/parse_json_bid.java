package com.example.rixwansharif.travelanche;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rixwan Sharif on 11/17/2017.
 */
public class parse_json_bid {

    public static String[] bid_id;
    public static String[] vehicle;
    public static String[] bachat_price;
    public static String[] lambsamb_price;
    public static String[] company_name;
    public static String[] company_phone;
    public static String[] company_image;

    private JSONArray bids_on_trip = null;

    private String json;

    public parse_json_bid(String json)
    {
        this.json = json;
    }



    protected void parseJSON()

    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(json);
            bids_on_trip = jsonObject.getJSONArray("bids_on_trip");

            bid_id = new String[bids_on_trip.length()];
            vehicle =new String[bids_on_trip.length()];
            bachat_price = new String[bids_on_trip.length()];
            lambsamb_price=new String[bids_on_trip.length()];
            company_name=new String[bids_on_trip.length()];
            company_phone = new String[bids_on_trip.length()];
            company_image =new String[bids_on_trip.length()];

            for (int i = 0; i < bids_on_trip.length(); i++)
            {
                JSONObject jo = bids_on_trip.getJSONObject(i);


                bid_id[i] = jo.getString("bid_id");
                vehicle[i] = jo.getString("vehicle");
                bachat_price[i] = jo.getString("bachat_price");
                lambsamb_price[i]=jo.getString("lambsamb_price");
                company_image[i]=jo.getString("company_image");
                company_name[i] = jo.getString("company_name");
                company_phone[i] = jo.getString("company_phone");

            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
