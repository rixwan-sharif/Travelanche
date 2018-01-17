package com.example.rixwansharif.travelanche;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CompanyBidDetailActivity extends AppCompatActivity {

    TextView rate_per_day_text,total_fare_text,vehicle_text,vehiclee_text;
    ImageView vehicle_img;
    String Trip_Id,Company_Phone,Bid_Id;
    CheckInternet cd = new CheckInternet(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_bid_detail);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c7c71")));
        getSupportActionBar().setTitle("Bid Detail");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        //
        rate_per_day_text = (TextView) findViewById(R.id.bid_rate_per_day);
        total_fare_text = (TextView) findViewById(R.id.bid_total_fare);
        vehicle_text = (TextView) findViewById(R.id.bid_vehicle);
        vehiclee_text = (TextView) findViewById(R.id.bid_vehiclee);
        vehicle_img = (ImageView) findViewById(R.id.bid_vehicle_image);


        //Get Trip Id
        Bundle intent_data=getIntent().getExtras();
        Trip_Id=intent_data.getString("trip_id");
        Bid_Id=intent_data.getString("accepted_bid_id");
        Company_Phone=intent_data.getString("company_phone");

        //Fetch Trip Details
        Load_Bid_Details();

    }

    private void Load_Bid_Details()
    {

        if(cd.isConnected())
        {
            final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, true);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask() {
                @Override
                public void run() {
                    CompanyBidDetailActivity.this.runOnUiThread(new Runnable() {
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.load_Bid_Detail,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.getString("response").equals("success")) {
                                    rate_per_day_text.setText(jsonResponse.getString("rate_per_day").equals("0")?"N/A":jsonResponse.getString("rate_per_day"));
                                    vehicle_text.setText(jsonResponse.getString("rate_per_day").equals("0")?"N/A":jsonResponse.getString("vehicle"));
                                    total_fare_text.setText(jsonResponse.getString("total_fare").equals("0")?"N/A":jsonResponse.getString("total_fare"));
                                    vehiclee_text.setText(jsonResponse.getString("total_fare").equals("0")?"N/A":jsonResponse.getString("vehicle"));

                                    Picasso.with(getApplicationContext())
                                            .load("http://rixwanxharif.000webhostapp.com/uploads/" + "vehilce_image.jpg")
                                            .networkPolicy(NetworkPolicy.OFFLINE)
                                            .into(vehicle_img, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                }

                                                @Override
                                                public void onError() {
                                                    Picasso.with(getApplicationContext())
                                                            .load("http://rixwanxharif.000webhostapp.com/uploads/" + "vehilce_image.jpg")
                                                            .into(vehicle_img);
                                                }
                                            });

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
                            Toast.makeText(CompanyBidDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("trip_id", Trip_Id);
                    params.put("bid_id", Bid_Id);
                    params.put("company_phone", Company_Phone);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(CompanyBidDetailActivity.this);
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
