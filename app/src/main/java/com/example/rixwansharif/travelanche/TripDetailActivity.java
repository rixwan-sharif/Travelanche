package com.example.rixwansharif.travelanche;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TripDetailActivity extends AppCompatActivity {

    String Trip_Id,Phone;
    CheckInternet cd = new CheckInternet(this);
    TextView destination_text,trip_vehicle_text,pick_loc_text,date_from_text,date_to_text,pick_time_text,drop_time_text,
            driver_text,ac_text,time_text,date_text,no_bis_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c7c71")));
        getSupportActionBar().setTitle("Trip Detail");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        //
        destination_text = (TextView) findViewById(R.id.trip_details_client_destination);
        trip_vehicle_text = (TextView) findViewById(R.id.trip_details_client_vehicle_to_rental);
        pick_loc_text = (TextView) findViewById(R.id.trip_details_client_pickup_location_to_rental);
        driver_text = (TextView) findViewById(R.id.trip_details_client_driver_to_rental);
        ac_text = (TextView) findViewById(R.id.trip_details_client_AC_to_rental);
        date_from_text = (TextView) findViewById(R.id.trip_details_client_start_date_to_rental);
        date_to_text = (TextView) findViewById(R.id.trip_details_client_end_date_to_rental);
        pick_time_text = (TextView) findViewById(R.id.trip_details_client_pick_time_to_rental);
        drop_time_text = (TextView) findViewById(R.id.trip_details_client_drop_time_to_rental);
        date_text = (TextView) findViewById(R.id.trip_details_date);
        time_text = (TextView) findViewById(R.id.trip_details_time);
       // no_bis_text = (TextView) findViewById(R.id.no_bids_on_client_trip);

        //Get Trip Id
        SharedPreferences sharedPreferences = getSharedPreferences(config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Phone= sharedPreferences.getString(config.Phone_SHARED_PREF,"Not Available");
        Bundle intent_data=getIntent().getExtras();
        Trip_Id=intent_data.getString("trip_id");

        //Fetch Trip Details
        Load_Trip_Details();

    }

    private void Load_Trip_Details()
    {
        if(cd.isConnected())
        {
            final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, true);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask() {
                @Override
                public void run() {
                    TripDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loading.isShowing()) {
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(), "Something has gone wrong, Try again !", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            }, 20000);

            //
            //Creating an string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.load_Trip_Detail,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            try {
                                //Creating the json object from the response
                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.getString("response").equals("success")) {
                                    destination_text.setText(jsonResponse.getString("destination"));
                                    trip_vehicle_text.setText(jsonResponse.getString("vehicle"));
                                    pick_loc_text.setText(jsonResponse.getString("location_pickup"));
                                    date_from_text.setText(jsonResponse.getString("start_date"));
                                    date_to_text.setText(jsonResponse.getString("end_date"));
                                    pick_time_text.setText(jsonResponse.getString("time_pickup"));
                                    drop_time_text.setText(jsonResponse.getString("time_drop"));
                                    date_text.setText(jsonResponse.getString("trip_date"));
                                    time_text.setText(jsonResponse.getString("trip_time"));
                                    //no_bis_text.setText(jsonResponse.getString("bids_on_trip"));
                                    ac_text.setText(jsonResponse.getString("ac").equals("1")?"Yes":"No");
                                    driver_text.setText(jsonResponse.getString("driver").equals("1")?"Yes":"No");
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText(TripDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("trip_id", Trip_Id);
                    params.put("user_phone", Phone);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(TripDetailActivity.this);
            requestQueue.add(stringRequest);
            //

        }
    else
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setMessage("Check Internet Connection !")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });

        //
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    }
}
