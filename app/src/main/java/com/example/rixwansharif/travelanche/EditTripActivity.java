package com.example.rixwansharif.travelanche;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class EditTripActivity extends AppCompatActivity {

    int current_day,current_month,current_year;
    static final int DIALOG_ID_Time_PickUP = 0;
    static final int DIALOG_ID_Time_Drop = 3;
    int hour_p;
    int mint_p;
    int hour_d;
    int mint_d;
    static final int DIALOG_ID_Start_Date = 1;
    static final int DIALOG_ID_End_Date = 2;
    int year;
    int month;
    int day;

    //Used for calculation days b/w two dates
    String startdate,enddate;

    //for sendiong to server
    String Destination,Vehicle,Pickup_Location,StartDate,EndDate,Pickup_Time,Drop_Time,Description,User_Phone,User_City;
    String Trip_id;
    int AC=1;
    int Driver=1;
    Long Trip_Days;

    DateFormat formattime = DateFormat.getTimeInstance(DateFormat.SHORT);

    Calendar calendar=Calendar.getInstance();


    private LinearLayout background_layout;
    private TextView destination,vehicle,pickup_location,start_date,end_date,time_pickup,time_drop;
    private EditText description;
    private Button edit_trip;
    RadioGroup radioGroup_driver,radioGroup_ac;

    CheckInternet cd=new CheckInternet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c7c71")));
        getSupportActionBar().setTitle("Plan Your Trip");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);






        //
        destination=(TextView)findViewById(R.id.destination);
        vehicle=(TextView)findViewById(R.id.no_of_seats_vehicle);
        pickup_location=(TextView)findViewById(R.id.pick_location);
        time_pickup=(TextView) findViewById(R.id.Time_pickup);
        time_drop=(TextView) findViewById(R.id.Time_drop);
        start_date=(TextView) findViewById(R.id.trip_start_date);
        end_date=(TextView) findViewById(R.id.trip_end_date);
        description=(EditText) findViewById(R.id.description);
        edit_trip=(Button) findViewById(R.id.post_trip_btn);
        background_layout=(LinearLayout) findViewById(R.id.trip_back_layout);

        radioGroup_driver=(RadioGroup) findViewById(R.id.radrio_grp_driver);
        radioGroup_ac=(RadioGroup) findViewById(R.id.radrio_grp_ac);

        // Setting Previous data
        background_layout.setBackgroundResource(R.drawable.plan_your_trip_background);
        edit_trip.setText("Save");

        Bundle  intent_data=getIntent().getExtras();
        Trip_id =intent_data.getString("trip_id");

        destination.setText(intent_data.getString("destination").toString());
        vehicle.setText(intent_data.getString("vehicle").toString());
        pickup_location.setText(intent_data.getString("pick_location").toString());
        start_date.setText(intent_data.getString("start_date").toString());
        end_date.setText(intent_data.getString("end_date").toString());
        time_pickup.setText(intent_data.getString("pick_time").toString());
        time_drop.setText(intent_data.getString("drop_time").toString());

        startdate=start_date.getText().toString().trim().replace('/',' ');
        enddate=end_date.getText().toString().trim().replace('/',' ');

        if(intent_data.getString("driver").toString().equals("1"))
            radioGroup_driver.check(R.id.driver_yes_btn);
        else
            radioGroup_driver.check(R.id.driver_no_btn);

        if(intent_data.getString("ac").toString().equals("1"))
            radioGroup_ac.check(R.id.ac_yes_btn);
        else
           radioGroup_ac.check(R.id.ac_no_btn);


        //set_date_time();

        //listeners
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);
                Set_Destination();
            }
        });

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);
                Set_Vehicle();

            }
        });


        pickup_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);

                Set_Pickup_Location();
            }
        });

        time_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);
                showDialog(DIALOG_ID_Time_PickUP);
            }
        });

        time_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);
                showDialog(DIALOG_ID_Time_Drop);
            }
        });

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);
                showDialog(DIALOG_ID_Start_Date);
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);
                showDialog(DIALOG_ID_End_Date);
            }
        });

        //Post trip click listener

        edit_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Edit_Trip();

            }
        });


    }

    private void Set_Destination()
    {
        //show
        LayoutInflater li = LayoutInflater.from(EditTripActivity.this);

        //Creating a view to get the dialog box
        final View _Dialog = li.inflate(R.layout.dest_city_dialogue, null);


        //Creating an alertdialog builder
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(EditTripActivity.this);
        //Adding our dialog box to the view of alert dialog
        alertDialogBuilder.setView(_Dialog);
        //Creating an alert dialog
        final android.support.v7.app.AlertDialog dest_inputDialog = alertDialogBuilder.create();


        //get string array from source
        String[] citesArray = getResources().getStringArray(R.array.destination_city_array);

        final ListView list = (ListView) _Dialog.findViewById(R.id.cities_listView);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, citesArray));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                dest_inputDialog.dismiss();
                String selectedFromList = (list.getItemAtPosition(position).toString());

                if((selectedFromList.equals("Other")))
                {
                    LayoutInflater li = LayoutInflater.from(EditTripActivity.this);

                    //Creating a view to get the dialog box
                    View _Dialog = li.inflate(R.layout.dialogue_input_trip, null);

                    //Initizliaing confirm button fo dialog box and edittext of dialog box
                    Button temp_dialogue_btn = (AppCompatButton) _Dialog.findViewById(R.id.dialogue_input_trip_custome_trip_btn);
                    final EditText temp_dialogue_edittext=(EditText) _Dialog.findViewById(R.id.dialogue_input_trip_custome_trip_edittext);
                    TextView temp_dialogue_textview=(TextView) _Dialog.findViewById(R.id.dialogue_input_trip_custome_trip_textview);


                    temp_dialogue_textview.setText("Enter Destination");
                    temp_dialogue_edittext.setHint("Your Destination");

                    //Creating an alertdialog builder
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(EditTripActivity.this);
                    //Adding our dialog box to the view of alert dialog
                    alertDialogBuilder.setView(_Dialog);
                    //Creating an alert dialog
                    final android.support.v7.app.AlertDialog temp_inputDialog= alertDialogBuilder.create();
                    // show it
                    temp_inputDialog.show();
                    temp_inputDialog.setCanceledOnTouchOutside(false);

                    // OK btn listener
                    temp_dialogue_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            temp_inputDialog.dismiss();
                            destination.setText(temp_dialogue_edittext.getText().toString().trim());
                        }
                    });

                }
                else
                {
                    destination.setText(selectedFromList);
                }}});

        // show it
        dest_inputDialog.show();
        dest_inputDialog.setCanceledOnTouchOutside(false);
        dest_inputDialog.getWindow().setLayout(600, 800);
    }

    private void Set_Vehicle()
    {
        LayoutInflater li = LayoutInflater.from(EditTripActivity.this);

        //Creating a view to get the dialog box
        final View _Dialog = li.inflate(R.layout.vehicle_dialogue, null);


        //Creating an alertdialog builder
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(EditTripActivity.this);
        //Adding our dialog box to the view of alert dialog
        alertDialogBuilder.setView(_Dialog);
        //Creating an alert dialog
        final android.support.v7.app.AlertDialog vehicle_inputDialog = alertDialogBuilder.create();


        String[] vehicleArray = getResources().getStringArray(R.array.vehicle_array);

        final ListView list = (ListView) _Dialog.findViewById(R.id.vehilces_listView);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, vehicleArray));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vehicle_inputDialog.dismiss();
                String selectedFromList = (list.getItemAtPosition(position).toString());
                vehicle.setText(selectedFromList);
            }
        });

        // show it
        vehicle_inputDialog.show();
        vehicle_inputDialog.setCanceledOnTouchOutside(false);
        //vehicle_inputDialog.getWindow().setLayout(550, 700);
    }

    private void Set_Pickup_Location()
    {
        //show
        LayoutInflater li = LayoutInflater.from(EditTripActivity.this);

        //Creating a view to get the dialog box
        final View _Dialog = li.inflate(R.layout.pickup_location_dialogue, null);


        //Creating an alertdialog builder
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(EditTripActivity.this);
        //Adding our dialog box to the view of alert dialog
        alertDialogBuilder.setView(_Dialog);
        //Creating an alert dialog
        final android.support.v7.app.AlertDialog pickup_loc_inputDialog = alertDialogBuilder.create();
        //get string array from source
        String[] pickupLocationArray = getResources().getStringArray(R.array.pickup_location_array);

        final ListView list = (ListView) _Dialog.findViewById(R.id.pickup_locations_listView);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, pickupLocationArray));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pickup_loc_inputDialog.dismiss();
                String selectedFromList = (list.getItemAtPosition(position).toString());

                if ((selectedFromList.equals("Other"))) {
                    LayoutInflater li = LayoutInflater.from(EditTripActivity.this);

                    //Creating a view to get the dialog box
                    View _Dialog = li.inflate(R.layout.dialogue_input_trip, null);

                    //Initizliaing confirm button fo dialog box and edittext of dialog box
                    Button temp_dialogue_btn = (AppCompatButton) _Dialog.findViewById(R.id.dialogue_input_trip_custome_trip_btn);
                    final EditText temp_dialogue_edittext = (EditText) _Dialog.findViewById(R.id.dialogue_input_trip_custome_trip_edittext);
                    TextView temp_dialogue_textview = (TextView) _Dialog.findViewById(R.id.dialogue_input_trip_custome_trip_textview);


                    temp_dialogue_textview.setText("Enter Destination");
                    temp_dialogue_edittext.setHint("Your Destination");

                    //Creating an alertdialog builder
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(EditTripActivity.this);
                    //Adding our dialog box to the view of alert dialog
                    alertDialogBuilder.setView(_Dialog);
                    //Creating an alert dialog
                    final android.support.v7.app.AlertDialog temp_inputDialog = alertDialogBuilder.create();
                    // show it
                    temp_inputDialog.show();
                    temp_inputDialog.setCanceledOnTouchOutside(false);

                    // OK btn listener
                    temp_dialogue_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            temp_inputDialog.dismiss();
                            pickup_location.setText(temp_dialogue_edittext.getText().toString().trim());
                        }
                    });

                } else {
                    pickup_location.setText(selectedFromList);
                }
            }
        });

        // show it
        pickup_loc_inputDialog.show();
        pickup_loc_inputDialog.setCanceledOnTouchOutside(false);
        //pickup_loc_inputDialog.getWindow().setLayout(600, 800);

    }


    private void Edit_Trip()
    {

        if(Validate())
        {

            Initialize();
            Trip_Days=Calculate_Days(startdate, enddate);
            Edit_Trip_Process();

        }
        else
        {
            Toast.makeText(EditTripActivity.this, "Please enter all required fields ! ", Toast.LENGTH_SHORT).show();

        }
    }
    private void Initialize()
    {
        Destination=destination.getText().toString().trim();
        Vehicle=vehicle.getText().toString().trim();
        Pickup_Location=pickup_location.getText().toString().trim();
        StartDate=start_date.getText().toString().trim();
        EndDate=end_date.getText().toString().trim();
        Pickup_Time=time_pickup.getText().toString().trim();
        Drop_Time=time_drop.getText().toString().trim();
        Description=description.getText().toString().trim();



        //For AC and Driver
        int selected_radiobtn_Id;
        RadioButton temp_radionbtn;

        selected_radiobtn_Id = radioGroup_driver.getCheckedRadioButtonId();
        temp_radionbtn = (RadioButton)findViewById(selected_radiobtn_Id);
        if((temp_radionbtn.getText().toString().trim()).equals("Yes"))
        {
            Driver=1;
        }
        else
        {
            Driver=0;
        }


        selected_radiobtn_Id = radioGroup_ac.getCheckedRadioButtonId();
        temp_radionbtn = (RadioButton)findViewById(selected_radiobtn_Id);
        if((temp_radionbtn.getText().toString().trim()).equals("Yes"))
        {
            AC=1;
        }
        else
        {
            AC=0;
        }

        //Shared Prefe

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        User_City=sharedPreferences.getString(Config.City_SHARED_PREF, "Not Available").toString();
        User_Phone=sharedPreferences.getString(Config.Phone_SHARED_PREF, "Not Available").toString();

    }

    private boolean Validate()
    {

        boolean valid=true;

        if(Destination.length() == 0)
        {
            valid=false;
        }
        if(Pickup_Location.length() == 0 )
        {
            valid=false;
        }
        if(Vehicle.length() == 0 )
        {
            valid=false;
        }
        if(StartDate.length() == 0 )
        {
            valid=false;
        }
        if(EndDate.length() == 0 )
        {
            valid=false;
        }
        if(Pickup_Time.length() == 0 )
        {
            valid=false;
        }
        if(Drop_Time.length() == 0 )
        {
            valid=false;
        }
        return valid;
    }

    private void Edit_Trip_Process()
    {
        if(cd.isConnected())
        {

            if (Calculate_Days(startdate, enddate) > 30 || Calculate_Days(startdate, enddate) < 1) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder
                        .setMessage("● Trip should be less than 30 Days. \n\n" +
                                "● Start date should be less than end \n   date. \n\n" +
                                "● Pickup location should not be \n   outside of your city. \n")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
            else
            {
                final ProgressDialog loading = ProgressDialog.show(this, "Editing Your Trip", "Please wait...", false, true);

                Timer timer_a = new Timer();
                timer_a.schedule(new TimerTask()
                {
                    @Override
                    public void run() {


                      EditTripActivity.this.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run() {
                                if(loading.isShowing())
                                {
                                    loading.dismiss();

                                    Toast.makeText(EditTripActivity.this, "Something has gone wrong, Try again !", Toast.LENGTH_SHORT).show();

                                }


                            }

                        });



                    }
                }, 20000);



                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Edit_Trips_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();


                                Toast.makeText(EditTripActivity.this, response, Toast.LENGTH_LONG).show();



                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                Toast.makeText(EditTripActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        //User_City,Vehicle,Driver,AC,Destination,Trip_Days,StartDate,
                       // EndDate,Pickup_Time,Pickup_Location,Description,User_Phone
                        params.put("user_city",User_City);
                        params.put("vehicle",Vehicle);

                        params.put("driver",Driver + "");
                        params.put("ac",AC + "");

                        params.put("destination",Destination);
                        params.put("trip_days",Trip_Days + "");
                        params.put("start_date",StartDate);
                        params.put("trip_id",Trip_id);
                        params.put("end_date",EndDate);
                        params.put("time_pickup",Pickup_Time);
                        params.put("location_pickup",Pickup_Location);
                        params.put("trip_description",Description);
                        params.put("user_phone",User_Phone);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);


            }
        }
        else
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setMessage("Check Internet Connection!")
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


    //Date Time Management


    //Date Time Management
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID_Time_PickUP) {
            return new TimePickerDialog(EditTripActivity.this,myTimepickListener,hour_p,mint_p,false);
        }
        if (id == DIALOG_ID_Time_Drop) {
            return new TimePickerDialog(EditTripActivity.this,myTimedropListener,hour_d,mint_d,false);
        }
        if (id == DIALOG_ID_Start_Date) {
            DatePickerDialog d =new DatePickerDialog(EditTripActivity.this,myDateFrompickerListener,year,month,day);
            DatePicker dp = d.getDatePicker();
            dp.setMinDate(calendar.getTimeInMillis());
            return d;
        }
        if (id == DIALOG_ID_End_Date) {
            DatePickerDialog d = new DatePickerDialog(EditTripActivity.this,myDateTopickerListener,year,month,day);
            DatePicker dp = d.getDatePicker();
            dp.setMinDate(calendar.getTimeInMillis());
            return d;

        }
        else
            return null;
    }

    protected TimePickerDialog.OnTimeSetListener myTimepickListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_p=hourOfDay;
            mint_p=minute;

            if( hour_p >=12)
            {
                if (hour_p == 12)
                {
                    if(mint_p<10)
                        time_pickup.setText(12 + ":0" + mint_p + " PM");
                    else
                        time_pickup.setText(12 + ":" + mint_p + " PM");
                }
                else
                {
                    if (mint_p<10)
                        time_pickup.setText(hour_p - 12 + ":0" + mint_p + " PM");
                    else
                        time_pickup.setText(hour_p - 12 + ":" + mint_p + " PM");
                }
            }
            else
            {
                if (hour_p == 00)
                {
                    if(mint_p<10)
                        time_pickup.setText(12 + ":0" + mint_p + " AM");
                    else
                        time_pickup.setText(12 + ":" + mint_p + " AM");
                }
                else
                {
                    if(mint_p<10)
                        time_pickup.setText(hour_p + ":0" + mint_p + " AM");
                    else
                        time_pickup.setText(hour_p + ":" + mint_p + " AM");
                }
            }
        }
    };

    protected TimePickerDialog.OnTimeSetListener myTimedropListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_d=hourOfDay;
            mint_d=minute;

            if( hour_d >=12)
            {
                if (hour_d == 12)
                {
                    if(mint_d<10)
                        time_drop.setText(12 + ":0" + mint_d + " PM");
                    else
                        time_drop.setText(12 + ":" + mint_d + " PM");
                }
                else
                {
                    if (mint_d<10)
                        time_drop.setText(hour_d - 12 + ":0" + mint_d + " PM");
                    else
                        time_drop.setText(hour_d - 12 + ":" + mint_d + " PM");
                }
            }
            else
            {
                if (hour_d == 00)
                {
                    if(mint_d<10)
                        time_drop.setText(12 + ":0" + mint_d + " AM");
                    else
                        time_drop.setText(12 + ":" + mint_d + " AM");
                }
                else
                {
                    if(mint_d<10)
                        time_drop.setText(hour_d + ":0" + mint_d + " AM");
                    else
                        time_drop.setText(hour_d + ":" + mint_d + " AM");
                }
            }
        }
    };

    protected DatePickerDialog.OnDateSetListener myDateFrompickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
            day=dayOfMonth;
            month=monthOfYear;
            year=Year;

            startdate=dayOfMonth+" "+(monthOfYear+1)+" "+year;
            start_date.setText(day+"/"+(month+1)+"/"+year);
        }
    };

    protected DatePickerDialog.OnDateSetListener myDateTopickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
            day=dayOfMonth;
            month=monthOfYear;
            year=Year;

            enddate=dayOfMonth+" "+(monthOfYear+1)+" "+year;
            end_date.setText(day+"/"+(month+1)+"/"+year);
        }
    };


    private long Calculate_Days(String datefrom,String dateto)
    {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        long calculted_days=0;
        try
        {
            Date date1 = myFormat.parse(datefrom);
            Date date2 = myFormat.parse(dateto);
            calculted_days = date2.getTime() - date1.getTime();
            calculted_days=TimeUnit.DAYS.convert(calculted_days, TimeUnit.MILLISECONDS);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return calculted_days+1;
    }





    private void set_date_time()
    {

        //Current Date

        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

        start_date.setText(day+"/"+(month+1)+"/"+year);
        end_date.setText((day)+"/"+(month+1)+"/"+year);

        startdate=day+" "+month+" "+year;
        enddate=day+" "+month+" "+year;
    }


}
