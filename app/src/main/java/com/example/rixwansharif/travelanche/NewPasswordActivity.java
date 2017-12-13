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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

public class NewPasswordActivity extends AppCompatActivity {
    private static Button set_new_password;
    private static EditText new_pass_text,cnfrm_new_pass_text;
    String new_password,confirm_new_password;
    String phone_number;
    CheckInternet cd=new CheckInternet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_password);

        //Widgets

        new_pass_text=(EditText)findViewById(R.id.new_pass_text);
        cnfrm_new_pass_text=(EditText)findViewById(R.id.new_cnfrm_pass_text);
        set_new_password=(Button)findViewById(R.id.set_new_pass_btn);

        //getting phone

        Bundle phone_data=getIntent().getExtras();
        if(phone_data==null)
        {
            return;
        }
        phone_number=phone_data.getString("phone");




        //listener

        set_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);

                Set_New_Password();
            }
        });



    }

    //

    private void Set_New_Password()
    {
        Intialize();
        if(Validate())
        {
            Set_New_Password_Process();

        }
        else
        {
            //Toast.makeText(getApplicationContext(), "Failed to set new Password", Toast.LENGTH_SHORT).show();

        }
    }


    private boolean Validate()
    {


        boolean valid=true;

        if(password.length()<6 && password.length()!=0)
        {
            password_text.setError("Password should be atleast of 6 charactors");
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




        return valid;
    }


    //Intializing values

    private void Intialize()
    {
        new_password=new_pass_text.getText().toString().trim();
        confirm_new_password=cnfrm_new_pass_text.getText().toString().trim();

    }

    private void Set_New_Password_Process() {



        if(cd.isConnected())
        {
            final ProgressDialog loading = ProgressDialog.show(this, "Setting New Password", "Please wait...", false, false);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask()
            {
                @Override
                public void run() {


                   NewPasswordActivity.this.runOnUiThread(new Runnable()
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


                try
                {
                    if (response.equalsIgnoreCase("success")) {





                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewPasswordActivity.this);

                        // set title
                        //alertDialogBuilder.setTitle("Your Title");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Your Password has been Reset!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }
                                });

                        //
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();


                    } else {
                        //If the server response is not success
                        //Displaying an error message on toast
                        Toast.makeText(NewPasswordActivity.this, "Failed to set new password", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(NewPasswordActivity.this, "Something has gone wrong!", Toast.LENGTH_LONG).show();
                }


            }


        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                loading.dismiss();
                Toast.makeText(NewPasswordActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

            }
        };




        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(phone_number, new_password, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(newPasswordRequest);

    }
        else
        {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);

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
