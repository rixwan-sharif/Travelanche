package com.example.rixwansharif.travelanche;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static Button send_code_btn,dialogue_buttonConfirm;;
    private static EditText phone_number_text,dialogue_editTextConfirmFPOtp;
    String phone_number;
    CheckInternet cd=new CheckInternet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#01AA97")));
        getSupportActionBar().setTitle("Back");

        // /Define Widgets


        send_code_btn=(Button) findViewById(R.id.set_new_pass_btn);
        phone_number_text=(EditText) findViewById(R.id.forgot_pass_phone_text);


        //prefix of +92
        phone_number_text.setText("+92 ");
        Selection.setSelection(phone_number_text.getText(), phone_number_text.getText().length());
        phone_number_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("+92 ")){
                    phone_number_text.setText("+92 ");
                    Selection.setSelection(phone_number_text.getText(), phone_number_text.getText().length());
                }
            }
        });

        send_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);

                Send_Code();
            }
        });

    }

    private void Send_Code()
    {

        if(Validate())
        {
            Intialize();
            Send_Code_Process();

        }
        else
        {
           Toast.makeText(getApplicationContext(), "Failed to Send ", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean Validate()
    {
        boolean valid = true;
        if( phone_number.length() < 14)
        {

            phone_number_text.setError("Phone Number is Required");
            phone_number_text.requestFocus();
            valid=false;
        }

        return valid;
    }


    //Intializing values

    private void Intialize()
    {

        String Temp="";
        for (int i = 4; i < phone_number_text.getText().length(); i++) {

            Temp = Temp + phone_number_text.getText().toString().trim().charAt(i);
        }
        phone_number="92"+Temp;
    }

    //
    private void Send_Code_Process()
    {

        if(cd.isConnected())

        {

            final ProgressDialog loading_s = ProgressDialog.show(this, "Sending Code to your Phone", "Please wait...", false, true);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask()
            {
                @Override
                public void run() {


                    ForgotPasswordActivity.this.runOnUiThread(new Runnable()
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


                        //If it is success
                        if (jsonResponse.getString("response").equalsIgnoreCase("0"))
                        {
                            final String sent_otp = jsonResponse.getString("otp");
                            //diaglogue for code

                            LayoutInflater li = LayoutInflater.from(ForgotPasswordActivity.this);

                            //Creating a view to get the dialog box

                            View confirmDialog = li.inflate(R.layout.register_otp_dialogue, null);

                            //Initizliaing confirm button fo dialog box and edittext of dialog box

                            dialogue_buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.dialogu_buttonConfirm);
                            dialogue_editTextConfirmFPOtp = (EditText) confirmDialog.findViewById(R.id.dialogue_editTextOtp);

                            //Creating an alertdialog builder
                            android.support.v7.app.AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(ForgotPasswordActivity.this);

                            //Adding our dialog box to the view of alert dialog
                            alertDialogBuilder2.setView(confirmDialog);


                            //Creating an alert dialog
                            final android.support.v7.app.AlertDialog alertDialog2 = alertDialogBuilder2.create();

                            // show it
                            alertDialog2.show();
                            alertDialog2.setCancelable(false);

                            dialogue_buttonConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    // input
                                    final String entered_otp = dialogue_editTextConfirmFPOtp.getText().toString().trim();
                                    //loading.dismiss();

                                    if (sent_otp.equals(entered_otp))
                                    {
                                        alertDialog2.dismiss();

                                        Intent intent = new Intent(ForgotPasswordActivity.this, NewPasswordActivity.class);
                                        intent.putExtra("phone", phone_number);
                                        startActivity(intent);

                                    }
                                    else
                                    {


                                        Toast.makeText(getApplicationContext(), "Code is not correct", Toast.LENGTH_SHORT).show();


                                    }


                                }
                            });


                        }

                        else if (jsonResponse.getString("response").equalsIgnoreCase("No_User"))
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "Your Number doesn't exist", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            //If not successful user may already have registered
                            Toast.makeText(ForgotPasswordActivity.this, "Something has gone wrong!", Toast.LENGTH_LONG).show();
                        }
                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(ForgotPasswordActivity.this, "Something has gone wrong!", Toast.LENGTH_LONG).show();
                    }


                }


            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    loading_s.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                }
            };


            ForgotpasswordRequest forgotpasswordRequest = new ForgotpasswordRequest(phone_number, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);
            queue.add(forgotpasswordRequest);
        }
        else
        {
           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

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
