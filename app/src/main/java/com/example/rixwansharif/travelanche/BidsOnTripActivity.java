package com.example.rixwansharif.travelanche;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BidsOnTripActivity extends AppCompatActivity {

    String User_Phone,Trip_id;
    private ListView bids_listView;
    CheckInternet cd=new CheckInternet(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids_on_trip);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c7c71")));
        getSupportActionBar().setTitle("Bids");

        bids_listView=(ListView) findViewById(R.id.bids_listView);

        //set data
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Bundle  intent_data=getIntent().getExtras();
        User_Phone= sharedPreferences.getString(Config.Phone_SHARED_PREF,"Not Available");
        Trip_id =intent_data.getString("trip_id");

        Load_Bids();
    }

    //
    private void Load_Bids()
    {

        if (cd.isConnected()) {

            final ProgressDialog loading = ProgressDialog.show(this, "Please Wait", "Please wait...", false, true);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask() {
                @Override
                public void run() {


                    BidsOnTripActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loading.isShowing()) {
                                loading.dismiss();

                                Toast.makeText(getApplicationContext(), "Something has gone wrong, Try again !", Toast.LENGTH_SHORT).show();

                            }


                        }

                    });


                }
            }, 15000);


            //Creating an string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Load_Bids_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            showJSON(response);
                            //Toast.makeText( BidsOnTripActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText( BidsOnTripActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("trip_id", Trip_id);
                    params.put("user_phone", User_Phone);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(BidsOnTripActivity.this);
            requestQueue.add(stringRequest);

        }
        else
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            //alertDialogBuilder.setTitle("Your Title");
            alertDialogBuilder
                    .setMessage("Check Internet Connection !")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

        }

    }

    private void showJSON(String json){
        parse_json_bid pd = new parse_json_bid(json);
        pd.parseJSON();

        custom_row_for_bid cb = new custom_row_for_bid(this, parse_json_bid.bid_id,parse_json_bid.vehicle,parse_json_bid.bachat_price,parse_json_bid.lambsamb_price,
                parse_json_bid.company_name,parse_json_bid.company_phone,parse_json_bid.company_image);

        bids_listView.setAdapter(cb);
    }

    public class custom_row_for_bid extends ArrayAdapter<String> {


        private String[] bid_id;
        private String[] vehicle;
        private String[] bachat_price;
        private String[] lambsamb_price;
        private String[] company_name;
        private String[] company_phone;
        private String[] company_image;

        private Activity context;

        public custom_row_for_bid(Activity context, String[] bid_id, String[] vehicle, String[] bachat_price, String[] lambsamb_price,
                                  String[] company_name, String[] company_phone, String[] company_image) {
            super(context, R.layout.custom_row_bid, bid_id);
            this.context = context;

            this.bid_id = bid_id;
            this.vehicle = vehicle;
            this.bachat_price = bachat_price;
            this.lambsamb_price = lambsamb_price;
            this.company_phone = company_phone;
            this.company_name = company_name;
            this.company_image = company_image;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = context.getLayoutInflater();
            final View listViewItem = inflater.inflate(R.layout.custom_row_bid, null, true);


            TextView company_txt = (TextView) listViewItem.findViewById(R.id.bids_comp_name_text);
            TextView vehicle_txt = (TextView) listViewItem.findViewById(R.id.bid_vehicle);
            TextView vehiclee_txt = (TextView) listViewItem.findViewById(R.id.bid_vehiclee);
            TextView rate_per_day_txt = (TextView) listViewItem.findViewById(R.id.bid_rate_per_day);
            TextView total_fare_txt = (TextView) listViewItem.findViewById(R.id.bid_total_fare);

            final ImageView comp_image = (ImageView) listViewItem.findViewById(R.id.bids_image_of_company);


            Button accept_bid = (Button) listViewItem.findViewById(R.id.accept_bid_btn);

            Picasso.with(getApplicationContext())
                    .load("http://rixwanxharif.000webhostapp.com/" + company_image[position])
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(comp_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(getApplicationContext())
                                    .load("http://rixwanxharif.000webhostapp.com/" + company_image[position])
                                    .into(comp_image);
                        }
                    });


            company_txt.setText(company_name[position]);

            if (bachat_price[position].equals("0")) {
                rate_per_day_txt.setText("N/A");
                vehicle_txt.setText("N/A");

                vehiclee_txt.setText(vehicle[position]);
                total_fare_txt.setText(lambsamb_price[position]);
            } else if (lambsamb_price[position].equals("0")) {
                total_fare_txt.setText("N/A");
                vehiclee_txt.setText("N/A");

                vehicle_txt.setText(vehicle[position]);
                rate_per_day_txt.setText(bachat_price[position]);
            } else {
                vehiclee_txt.setText(vehicle[position]);
                total_fare_txt.setText(lambsamb_price[position]);
                vehicle_txt.setText(vehicle[position]);
                rate_per_day_txt.setText(bachat_price[position]);
            }


            accept_bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                    buttonClick.setDuration(175);
                    v.startAnimation(buttonClick);

                    Accept_Bid(company_name[position],company_phone[position],vehicle[position],bachat_price[position],
                            lambsamb_price[position]);
                }
            });

            return listViewItem;
        }

        private void Accept_Bid(final String _company_name, final String _company_phone, final String _vehicle, final String _bachat_price,
                                final String _lambsamb_price) {

            if (cd.isConnected()) {

                final ProgressDialog loading = ProgressDialog.show(BidsOnTripActivity.this, "Please Wait", "Please wait...", false, true);

                Timer timer_a = new Timer();
                timer_a.schedule(new TimerTask() {
                    @Override
                    public void run() {


                        BidsOnTripActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loading.isShowing()) {
                                    loading.dismiss();

                                    Toast.makeText(getApplicationContext(), "Something has gone wrong, Try again !", Toast.LENGTH_SHORT).show();

                                }


                            }

                        });


                    }
                }, 15000);


                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Accept_Bid_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();
                                Toast.makeText( BidsOnTripActivity.this, response, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(BidsOnTripActivity.this,MyTripsActivity.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                Toast.makeText( BidsOnTripActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("trip_id", Trip_id);
                        params.put("vehicle", _vehicle);
                        params.put("company_name",_company_name);
                        params.put("company_phone", _company_phone);
                        params.put("rate_per_day", _bachat_price);
                        params.put("total_fare", _lambsamb_price);
                        params.put("user_phone",  User_Phone);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(BidsOnTripActivity.this);
                requestQueue.add(stringRequest);

            }
            else
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BidsOnTripActivity.this);

                //alertDialogBuilder.setTitle("Your Title");
                alertDialogBuilder
                        .setMessage("Check Internet Connection !")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

            }

        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Load_Bids();
    }
}
