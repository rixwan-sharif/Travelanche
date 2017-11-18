package com.example.rixwansharif.travelanche;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MyTripsActivity extends AppCompatActivity{

    String phone_number;
    private ListView trip_listView;
    CheckInternet cd=new CheckInternet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c7c71")));
        getSupportActionBar().setTitle("My Trips");

        trip_listView=(ListView) findViewById(R.id.trips_listView);

        Load_Trip();
    }

    private void Load_Trip()
    {



        if (cd.isConnected()) {

            final ProgressDialog loading = ProgressDialog.show(this, "Please Wait", "Please wait...", false, true);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask() {
                @Override
                public void run() {


                    MyTripsActivity.this.runOnUiThread(new Runnable() {
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


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loading.dismiss();
                    showJSON(response);
                }

            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    loading.dismiss();
                    Toast.makeText(MyTripsActivity.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();

                }
            };

            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            phone_number=sharedPreferences.getString(Config.Phone_SHARED_PREF, "Not Available").toString();

            FetchTripRequest fetchTripRequest = new FetchTripRequest(phone_number, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(MyTripsActivity.this);
            queue.add(fetchTripRequest);



        }
        else
        {
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


    private void showJSON(String json){
        parse_json_trip pj = new parse_json_trip(json);
        pj.parseJSON();

        custom_row_for_trip cr = new custom_row_for_trip(this, parse_json_trip.trip_id,parse_json_trip.trip_destination,
                parse_json_trip.trip_pickup_location,parse_json_trip.trip_pick_time,parse_json_trip.trip_drop_time,parse_json_trip.trip_vehicle,parse_json_trip.trip_start_date,parse_json_trip.trip_end_date,
                parse_json_trip.trip_driver,parse_json_trip.trip_ac,parse_json_trip.bids_on_trip,phone_number);

        trip_listView.setAdapter(cr);
    }


    public class custom_row_for_trip extends ArrayAdapter<String> {



        private String[] trip_id;
        private String[] trip_destination;
        private String[] trip_pick_time;
        private String[] trip_drop_time;
        private String[] trip_pickup_location;
        private String[] trip_vehicle;
        private String[] trip_start_date;
        private String[] trip_end_date;
        private String[] trip_driver;
        private String[] trip_ac;
        private String[] bids_on_trip;

        String Phone;
        String trip_ID,dest,pick_loc,veh,st_dt,end_dt,driver,ac;

        String Trip_Status;

        private Activity context;

        public custom_row_for_trip(Activity context,String[] trip_id, String[] trip_destination, String[] trip_pickup_location,String[] trip_pick_time,
                                   String[] trip_drop_time,String[] trip_vehicle, String[] trip_start_date,String[] trip_end_date,String[] driver,
                                   String[] ac,String[] bids_on_trip,String phone_number)
        {
            super(context, R.layout.custom_row_for_trip,trip_id);
            this.context = context;

            this.trip_id=trip_id;
            this.trip_destination = trip_destination;
            this.trip_pickup_location = trip_pickup_location;
            this.trip_pick_time=trip_pick_time;
            this.trip_drop_time=trip_drop_time;
            this.trip_vehicle = trip_vehicle;
            this.trip_start_date = trip_start_date;
            this.trip_end_date = trip_end_date;
            this.trip_driver = driver;
            this.trip_ac = ac;
            this.bids_on_trip=bids_on_trip;
            this.Phone=phone_number;

        }







        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final LayoutInflater inflater = context.getLayoutInflater();
            final View listViewItem = inflater.inflate(R.layout.custom_row_for_trip, null, true);


            TextView Destination = (TextView) listViewItem.findViewById(R.id.t_d_destination_textview);
            TextView Pickup_Loc = (TextView) listViewItem.findViewById(R.id.t_d_pickup_loc_textview);
            TextView Vehicle = (TextView) listViewItem.findViewById(R.id.t_d_vehcle_textview);
            TextView from_date = (TextView) listViewItem.findViewById(R.id.t_d_from_textview);
            TextView to_date = (TextView) listViewItem.findViewById(R.id.t_d_to_textview);
            TextView driver = (TextView) listViewItem.findViewById(R.id.t_d_driver_textview);
            TextView ac = (TextView) listViewItem.findViewById(R.id.t_d_ac_textview);
            TextView bids_text = (TextView) listViewItem.findViewById(R.id.bids_on_trip_text);

            ImageButton view_btn=(ImageButton) listViewItem.findViewById(R.id.trip_view_btn);
            final ImageButton edit_trip_btn=(ImageButton) listViewItem.findViewById(R.id.edit_trip_btn);
            final ImageButton delete_trip_btn=(ImageButton) listViewItem.findViewById(R.id.delete_trip_btn);
            final LinearLayout bottom_tab=(LinearLayout) listViewItem.findViewById(R.id.mytrip_bottom_tab);

            Button my_trip_status_btn=(Button) listViewItem.findViewById(R.id.my_trip_status);
            LinearLayout my_trip_bids_btn=(LinearLayout) listViewItem.findViewById(R.id.my_trip_bids);
            Button my_trip_view_detail_btn=(Button) listViewItem.findViewById(R.id.my_trip_view_detail);




            Destination.setText(trip_destination[position]);
            Pickup_Loc.setText(trip_pickup_location[position]);
            Vehicle.setText(trip_vehicle[position]);
            from_date.setText(trip_start_date[position]);
            to_date.setText(trip_end_date[position]);
            bids_text.setText(bids_on_trip[position]);

            trip_ID=trip_id[position];


            if(trip_driver[position].equals("1"))
            {
                driver.setText("Yes");
            }
            else
            {
                driver.setText("No");
            }

            if(trip_ac[position].equals("1"))
            {
                ac.setText("Yes");
            }
            else
            {
                ac.setText("No");
            }




            view_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                    buttonClick.setDuration(175);
                    v.startAnimation(buttonClick);

                    if (bottom_tab.getVisibility() == View.GONE) {
                        bottom_tab.setVisibility(View.VISIBLE);
                    }
                    else {
                        bottom_tab.setVisibility(View.GONE);
                    }
                }
            });

            edit_trip_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                    buttonClick.setDuration(175);
                    v.startAnimation(buttonClick);

                    //Creating an alert dialog to confirm logout
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Previous Bids will be cancelled. \nAre you sure to Edit ? ");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1)
                                {
                                    Intent intent=new Intent(getApplicationContext(),EditTripActivity.class);

                                    intent.putExtra("trip_id", trip_id[position]);
                                    intent.putExtra("destination",trip_destination[position]);
                                    intent.putExtra("vehicle",trip_vehicle[position]);
                                    intent.putExtra("pick_location",trip_pickup_location[position]);
                                    intent.putExtra("pick_time",trip_pick_time[position]);
                                    intent.putExtra("drop_time",trip_drop_time[position]);
                                    intent.putExtra("start_date",trip_start_date[position]);
                                    intent.putExtra("end_date",trip_end_date[position]);
                                    intent.putExtra("driver",trip_driver[position]);
                                    intent.putExtra("ac",trip_ac[position]);
                                    startActivity(intent);


                                }
                            });

                    alertDialogBuilder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    //Showing the alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();


                }
            });


            delete_trip_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                    buttonClick.setDuration(175);
                    v.startAnimation(buttonClick);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Are you sure to delete your Trip ? ");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1)
                                {
                                   Delete_Trip();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    //Showing the alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();

                }
            });


            my_trip_status_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                    buttonClick.setDuration(175);
                    v.startAnimation(buttonClick);

                    //
                }
            });

            my_trip_bids_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                    buttonClick.setDuration(175);
                    v.startAnimation(buttonClick);
                    //
                    Intent intent=new Intent(getApplicationContext(),BidsOnTripActivity.class);
                    intent.putExtra("trip_id", trip_id[position]);
                    startActivity(intent);
                }
            });

            my_trip_view_detail_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                    buttonClick.setDuration(175);
                    v.startAnimation(buttonClick);



                    if(Trip_Status.equals("Pending"))
                    {

                    }
                    else if(Trip_Status.equals("Bid Done"))
                    {

                    }
                    else if(Trip_Status.equals("Completed"))
                    {

                    }
                    else
                    {

                    }

                    //
                }
            });


            return listViewItem ;
        }


        private void Delete_Trip()
        {
            if (cd.isConnected()) {

                final ProgressDialog loading = ProgressDialog.show(getContext(), "Deleteing", "Please wait...", false, true);

                Timer timer_a = new Timer();
                timer_a.schedule(new TimerTask() {
                    @Override
                    public void run() {


                        MyTripsActivity.this.runOnUiThread(new Runnable() {
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
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Delete_User_Trip_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();
                                Load_Trip();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                Toast.makeText(MyTripsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("trip_id", trip_ID);
                        params.put("phone", Phone);

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);


            }
            else
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder
                        .setMessage("Check Internet Connection !")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        }

    }



}
