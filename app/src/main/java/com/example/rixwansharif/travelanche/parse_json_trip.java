package com.example.rixwansharif.travelanche;

/**
 * Created by Rixwan Sharif on 9/23/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class parse_json_trip {

    public static String[] trip_id;
    public static String[] trip_destination;
    public static String[] trip_pickup_location;
    public static String[] trip_pick_time;
    public static String[] trip_drop_time;
    public static String[] trip_vehicle;
    public static String[] trip_start_date;
    public static String[] trip_end_date;
    public static String[] trip_driver;
    public static String[] trip_ac;
    public static String[] bids_on_trip;



    private JSONArray my_trips = null;

    private String json;

    public parse_json_trip(String json)
    {
        this.json = json;
    }

    protected void parseJSON()

    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(json);
            my_trips = jsonObject.getJSONArray("my_trips");

            trip_id = new String[my_trips.length()];
            trip_destination = new String[my_trips.length()];
            trip_pickup_location = new String[my_trips.length()];
            trip_pick_time=new String[my_trips.length()];
            trip_drop_time=new String[my_trips.length()];
            trip_vehicle = new String[my_trips.length()];
            trip_start_date = new String[my_trips.length()];
            trip_end_date = new String[my_trips.length()];
            trip_driver = new String[my_trips.length()];
            trip_ac = new String[my_trips.length()];
            bids_on_trip = new String[my_trips.length()];


            for (int i = 0; i < my_trips.length(); i++)
            {
                JSONObject jo = my_trips.getJSONObject(i);


                trip_id[i] = jo.getString("id");
                trip_destination[i] = jo.getString("dest");
                trip_pickup_location[i] = jo.getString("pickup_loc");
                trip_pick_time[i]=jo.getString("pick_time");
                trip_drop_time[i]=jo.getString("drop_time");
                trip_vehicle[i] = jo.getString("vehicle");
                trip_start_date[i] = jo.getString("date_from");
                trip_end_date[i] = jo.getString("date_to");
                trip_driver[i] = jo.getString("driver");
                trip_ac[i] = jo.getString("ac");
                bids_on_trip[i] = jo.getString("bids_on_trip");
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


}
