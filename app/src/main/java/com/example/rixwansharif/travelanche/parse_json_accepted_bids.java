package com.example.rixwansharif.travelanche;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rixwan Sharif on 11/23/2017.
 */
public class parse_json_accepted_bids {


    public static String[] id;

    // Trip
    public static String[] trip_id;
    public static String[] trip_destination;
    //Company
    public static String[] company_phone;
    //Bid
    public static String[] bid_vehicle;
    public static String[] bid_vehicle_img;
    public static String[] bid_accept_date;
    public static String[] bid_accept_time;

    private JSONArray accepted_bids = null;

    private String json;

    public parse_json_accepted_bids(String json)
    {
        this.json = json;
    }



    protected void parseJSON()

    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(json);
            accepted_bids = jsonObject.getJSONArray("accepted_bids");

            id = new String[accepted_bids.length()];
            trip_destination =new String[accepted_bids.length()];

            trip_id = new String[accepted_bids.length()];


            company_phone=new String[accepted_bids.length()];
            bid_vehicle =new String[accepted_bids.length()];
            bid_vehicle_img = new String[accepted_bids.length()];
            bid_accept_date=new String[accepted_bids.length()];
            bid_accept_time=new String[accepted_bids.length()];

            for (int i = 0; i < accepted_bids.length(); i++)
            {
                JSONObject jo = accepted_bids.getJSONObject(i);


                id[i] = jo.getString("id");
                trip_destination[i] = jo.getString("destination");
                trip_id[i] = jo.getString("trip_id");

                company_phone[i] = jo.getString("company_phone");

                bid_vehicle[i] = jo.getString("bid_vehicle");
                bid_vehicle_img[i]=jo.getString("vehicle_img");
                bid_accept_date[i] = jo.getString("bid_accept_date");
                bid_accept_time[i] = jo.getString("bid_accept_time");

            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
