package com.example.rixwansharif.travelanche;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class RegistrationActivity extends AppCompatActivity {
    private static Button sign_up,dialogue_buttonConfirm;
    private String first_name,last_name,phone_number,city,password,confirm_password;
    private static EditText first_name_text,last_name_text,phone_number_text,password_text,confirm_password_text,dialogue_editTextConfirmOtp;
    Spinner city_text;
    CheckInternet cd=new CheckInternet(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#01AA97")));
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setTitle("Back");



        // /Define Widgets

        first_name_text=(EditText) findViewById(R.id.first_name);
        last_name_text=(EditText) findViewById(R.id.last_name);
        phone_number_text=(EditText) findViewById(R.id.signup_phone_number);
        city_text=(Spinner) findViewById(R.id.spCities);
        password_text=(EditText) findViewById(R.id.signup_password);
        confirm_password_text=(EditText) findViewById(R.id.signup_confirm_password);

        sign_up=(Button) findViewById(R.id.signup_btn);
        //Listener for sign up

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);

                Signup();
            }
        });



    }

    private void Signup()
    {

        if(Validate())
        {
            Intialize();
            Signup_Process();

        }
        else
        {
           // Toast.makeText(getApplicationContext(), "Failed to Register", Toast.LENGTH_SHORT).show();


        }
    }


    //validation

    private boolean Validate()
    {


        boolean valid=true;
        if( first_name.length() == 0 || first_name.length()>32)
        {

            first_name_text.setError("Please Enter Valid First Name");
            first_name_text.requestFocus();
            valid=false;

        }




        if( last_name.length() == 0 || last_name.length()>32)
        {
            last_name_text.setError("Please Enter Valid Last Name");
            last_name_text.requestFocus();
            valid=false;
        }
        if( phone_number.length() == 0 || phone_number.length()>12)
        {

            phone_number_text.setError("Enter Valid Phone Number");
            phone_number_text.requestFocus();
            valid=false;
        }


        if( password.length() == 0 )
        {
            password_text.setError("Password is required");
            password_text.requestFocus();
            valid=false;
        }
        if( confirm_password.length() == 0 )
        {
            confirm_password_text.setError("Password is required");
            confirm_password_text.requestFocus();
            valid=false;
        }

        if(!password.equals(confirm_password))
        {
            confirm_password_text.setError("Password Not matched");
            confirm_password_text.requestFocus();
            valid=false;
        }
        if(password.length()<6 && password.length()!=0)
        {
            password_text.setError("Password should be atleast of 6 charactors");
            password_text.requestFocus();
            valid=false;
        }



        return valid;
    }

    //Intializing values

    private void Intialize()
    {

        first_name=first_name_text.getText().toString().trim();
        last_name=last_name_text.getText().toString().trim();
        phone_number=phone_number_text.getText().toString().trim();
        city = city_text.getSelectedItem().toString().trim();
        password=password_text.getText().toString().trim();
        confirm_password=confirm_password_text.getText().toString().trim();
    }



    // Process

    private void Signup_Process()
    {


        if(cd.isConnected())
        {
            final ProgressDialog loading_s = ProgressDialog.show(this, "Registering", "Please wait...", false, true);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask()
            {
                @Override
                public void run() {


                   RegistrationActivity.this.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            if(loading_s.isShowing())
                            {
                                loading_s.dismiss();

                                Toast.makeText(getApplicationContext(), "Something has gone wrong, Try again !", Toast.LENGTH_SHORT).show();

                            }


                        }

                    });



                }
            }, 20000);



            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    loading_s.dismiss();


                    try {
                        //Creating the json object from the response

                        JSONObject jsonResponse = new JSONObject(response);

                        final String sent_otp = jsonResponse.getString("otp");

                        //If it is success
                        if (jsonResponse.getString("response").equalsIgnoreCase("0")) {



                            LayoutInflater li = LayoutInflater.from(RegistrationActivity.this);
                            //Creating a view to get the dialog box
                            View confirmDialog = li.inflate(R.layout.register_otp_dialogue, null);

                            //Initizliaing confirm button fo dialog box and edittext of dialog box
                            dialogue_buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.dialogu_buttonConfirm);
                            dialogue_editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.dialogue_editTextOtp);

                            //Creating an alertdialog builder
                            android.support.v7.app.AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(RegistrationActivity.this);

                            //Adding our dialog box to the view of alert dialog
                            alertDialogBuilder2.setView(confirmDialog);


                            //Creating an alert dialog
                            final android.support.v7.app.AlertDialog alertDialog2 = alertDialogBuilder2.create();

                            // show it
                            alertDialog2.show();
                            alertDialog2.setCancelable(false);

                            //button

                            dialogue_buttonConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    String entered_otp = dialogue_editTextConfirmOtp.getText().toString().trim();


                                    if (sent_otp.equals(entered_otp)) {
                                        alertDialog2.dismiss();


                                        Register();


                                    }
                                    else
                                    {

                                        Toast.makeText(getApplicationContext(), "Code is not correct", Toast.LENGTH_SHORT).show();
                                    }


                                    //

                                }

                                ;

                            });


                        }
                        else
                        {


                            //Error
                            Toast.makeText(RegistrationActivity.this, "Failed to send code", Toast.LENGTH_LONG).show();

                        }
                    }

                    catch (Exception e)
                    {

                        e.printStackTrace();
                    }

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    loading_s.dismiss();
                    Toast.makeText(RegistrationActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                }
            };


            //creating and adding request to queue
            SendOTPRequest sendOTPRequest = new SendOTPRequest(phone_number, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
            queue.add(sendOTPRequest);
        }
        else
        {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);

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
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }


    }


    private void Register() {



        if(cd.isConnected())
        {
            final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask()
            {
                @Override
                public void run() {


                    RegistrationActivity.this.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            if(loading.isShowing())
                            {
                                loading.dismiss();

                                Toast.makeText(getApplicationContext(), "Something has gone wrong, Try again !", Toast.LENGTH_SHORT).show();

                            }


                        }

                    });



                }
            }, 20000);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loading.dismiss();



                if (response.equalsIgnoreCase("success"))
                {
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(RegistrationActivity.this);

                    // set title
                    alertDialogBuilder.setTitle("Congratulations !");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Your Account has been Registered.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent=new Intent(RegistrationActivity.this,UploadPhotoActivity.class);
                                    intent.putExtra("phone", phone_number);
                                    startActivity(intent);





                                }
                            });

                    //
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                }
                else
                {
                    //If the server response is not success
                    //Displaying an error message on toast
                    Toast.makeText(RegistrationActivity.this, "User already Registered", Toast.LENGTH_LONG).show();
                }

            }


        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                loading.dismiss();
                Toast.makeText(RegistrationActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

            }
        };


        RegisterRequest registerRequest = new RegisterRequest(first_name, last_name, city, phone_number, password, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        queue.add(registerRequest);
    }
        else
        {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);

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
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }


    }


}
