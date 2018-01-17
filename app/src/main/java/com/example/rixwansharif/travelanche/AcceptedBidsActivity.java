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

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptedBidsActivity extends AppCompatActivity {

    String phone_number;
    private ListView accepted_bid_listView;
    CheckInternet cd = new CheckInternet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_bids);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c7c71")));
        getSupportActionBar().setTitle("My Trips");

        accepted_bid_listView = (ListView) findViewById(R.id.accepted_bid_listView);

        SharedPreferences sharedPreferences = getSharedPreferences(config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        phone_number = sharedPreferences.getString(config.Phone_SHARED_PREF,"Not Available");

        Load_Accepted_Bids();
    }

    private void Load_Accepted_Bids() {
        if (cd.isConnected()) {

            final ProgressDialog loading = ProgressDialog.show(this, "Please Wait", "Please wait...", false, true);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask() {
                @Override
                public void run() {


                    AcceptedBidsActivity.this.runOnUiThread(new Runnable() {
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


            //fetch

            //Creating an string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.Load_Accepted_Bids_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            showJSON(response);
                           //Toast.makeText(AcceptedBidsActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText(AcceptedBidsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_phone", phone_number);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(AcceptedBidsActivity.this);
            requestQueue.add(stringRequest);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            //alertDialogBuilder.setTitle("Your Title");
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

    private void showJSON(String json) {
        parse_json_accepted_bids pj = new parse_json_accepted_bids(json);
        pj.parseJSON();

        custom_row_for_accepted_bid cr=new custom_row_for_accepted_bid(this,parse_json_accepted_bids.id,parse_json_accepted_bids.trip_destination
                ,parse_json_accepted_bids.bid_vehicle,parse_json_accepted_bids.bid_vehicle_img,parse_json_accepted_bids.trip_id,
                parse_json_accepted_bids.bid_accept_date,parse_json_accepted_bids.bid_accept_time,parse_json_accepted_bids.company_phone);
        accepted_bid_listView.setAdapter(cr);
    }

    public class custom_row_for_accepted_bid extends ArrayAdapter<String> {


        private String[] accepted_bid_id;

        // Trip
        private String[] trip_destination;
        private String[] trip_id;


        //Company
        private String[] company_phone;

        //Bid
        private String[] bid_vehicle;
        private String[] bid_vehicle_img;
        private String[] bid_accept_date;
        private String[] bid_accept_time;


        private Activity context;

        public custom_row_for_accepted_bid(Activity context, String[] accepted_bid_id, String[] trip_destination,String[] bid_vehicle,String[] bid_vehicle_img
                                           ,String[] trip_id,String[] bid_accept_date,String[] bid_accept_time,String[] company_phone) {
            super(context, R.layout.custom_row_for_trip_bid,accepted_bid_id);
            this.context = context;

            this.accepted_bid_id = accepted_bid_id;
            this.trip_destination = trip_destination;
            this.trip_id = trip_id;

            //
            this.company_phone = company_phone;

            //
            this.bid_vehicle = bid_vehicle;
            this.bid_vehicle_img = bid_vehicle_img;
            this.bid_accept_date = bid_accept_date;
            this.bid_accept_time = bid_accept_time;

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = context.getLayoutInflater();
            final View listViewItem = inflater.inflate(R.layout.custom_row_for_trip_bid, null, true);

            //
            Button Trip_Button = (Button) listViewItem.findViewById(R.id.accepted_trip_trip_btn);
            Button Bid_Button = (Button) listViewItem.findViewById(R.id.accepted_trip_bid_btn);
            TextView Trip_Name = (TextView) listViewItem.findViewById(R.id.accepted_trip_destination_text);
            TextView Vehicle_Name = (TextView) listViewItem.findViewById(R.id.accepted_trip_vehicle_text);
            TextView Bid_Date = (TextView) listViewItem.findViewById(R.id.accepted_trip_bid_date_text);
            TextView Bid_Time = (TextView) listViewItem.findViewById(R.id.accepted_trip_bid_time_text);
            //final CircleImageView Client_Pic=(CircleImageView) listViewItem.findViewById(R.id.accepted_bid_client_pic);
            final CircleImageView Vehicle_Pic=(CircleImageView) listViewItem.findViewById(R.id.accepted_trip_vehicle_image);

            Trip_Name.setText(trip_destination[position]);
            Vehicle_Name.setText(bid_vehicle[position]);
            Bid_Date.setText(bid_accept_date[position]);
            Bid_Time.setText(bid_accept_time[position]);


            Picasso.with(getApplicationContext())
                    .load("http://rixwanxharif.000webhostapp.com/uploads/" + "vehilce_image.jpg")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(Vehicle_Pic, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(getApplicationContext())
                                    .load("http://rixwanxharif.000webhostapp.com/uploads/" + "vehilce_image.jpg")
                                    .into(Vehicle_Pic);
                        }
                    });

            Trip_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                    buttonClick.setDuration(175);
                    v.startAnimation(buttonClick);
                    Intent intent = new Intent(AcceptedBidsActivity.this, TripDetailActivity.class);
                    intent.putExtra("trip_id", trip_id[position]);
                    startActivity(intent);
                }
            });

            Bid_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                    buttonClick.setDuration(175);
                    v.startAnimation(buttonClick);
                    Intent intent = new Intent(AcceptedBidsActivity.this, CompanyBidDetailActivity.class);
                    intent.putExtra("accepted_bid_id", accepted_bid_id[position]);
                    intent.putExtra("trip_id", trip_id[position]);
                    intent.putExtra("company_phone", company_phone[position]);
                    startActivity(intent);
                }
            });

            //
        /*    //Trip details
            TextView destination_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_dest_text);
            TextView trip_vehicle_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_trip_vehicle_text);
            TextView pick_loc_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_pick_loc_text);
            TextView driver_ac_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_driver_ac_text);
            TextView date_from_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_date_from_text);
            TextView date_to_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_date_to_text);
            TextView pick_time_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_pick_time_text);
            TextView drop_time_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_drop_time_text);

            //Company

            TextView comp_name_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_comp_name_text);
            TextView comp_number_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_comp_number_text);
            TextView comp_address_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_comp_address_text);


           //Bid

            TextView rate_per_day_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_rate_text);
            TextView total_fare_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_total_fare_txt);
            TextView no_bid_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_bids_text);

            TextView vehicle_detail_text = (TextView) listViewItem.findViewById(R.id.accepted_bid_vehicle_detail_text);
            ImageView vehicle_img = (ImageView) listViewItem.findViewById(R.id.accepted_bid_vehicle_image);



            //Trip

            destination_text.setText(trip_destination[position]);
            trip_vehicle_text.setText(trip_vehicle[position]);
            pick_loc_text.setText(trip_pickup_location[position]);
            date_from_text.setText(trip_start_date[position]);
            date_to_text.setText(trip_end_date[position]);
            pick_time_text.setText(trip_pick_time[position]);
            drop_time_text.setText(trip_drop_time[position]);

            if (trip_driver[position].equals("1") && trip_ac[position].equals("1")) {
            driver_ac_text.setText("Yes / Yes");
            }
            else if (trip_driver[position].equals("1") && trip_ac[position].equals("0"))
            {
                driver_ac_text.setText("Yes / No");
            }
            else if (trip_driver[position].equals("0") && trip_ac[position].equals("1"))
            {
                driver_ac_text.setText("No / Yes");
            }
            else
            {
                driver_ac_text.setText("No / No");
            }

            //Company

            comp_name_text.setText(company_name[position]);
            comp_number_text.setText(company_number[position]);
            comp_address_text.setText(company_address[position]);


            //Bid

            rate_per_day_text.setText(bid_rate_per_day[position]);
            total_fare_text.setText(bid_total_fare[position]);
            no_bid_text.setText(bids_on_trip[position]);

            vehicle_detail_text.setText(bid_vehicle[position]);
            //Image Code

*/
            return listViewItem ;

        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        Load_Accepted_Bids();
    }

}
