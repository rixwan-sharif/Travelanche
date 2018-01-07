package com.example.rixwansharif.travelanche;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    private static TextView forgot_pass_text;
    private static Button login_btn;
    private  static EditText phone_text,pass_text;
    private String phone_number,password;

    private ImageView temp_img;
    private Bitmap temp_bitmap;

    CheckInternet cd=new CheckInternet(this);

    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_login);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#01AA97")));
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // /Define Widgets


        forgot_pass_text=(TextView) findViewById(R.id.forgot_pass_textview);
        login_btn=(Button) findViewById(R.id.set_new_pass_btn);
        phone_text=(EditText) findViewById(R.id.login_phone_number_text);
        pass_text=(EditText) findViewById(R.id.new_cnfrm_pass_text);


        //prefix of +92
        phone_text.setText("+92 ");
        Selection.setSelection(phone_text.getText(), phone_text.getText().length());
        phone_text.addTextChangedListener(new TextWatcher() {

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
                    phone_text.setText("+92 ");
                    Selection.setSelection(phone_text.getText(), phone_text.getText().length());
                }
            }
        });

        //Underline Text
        forgot_pass_text.setPaintFlags(forgot_pass_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgot_pass_text.setText("Forgot Password");



        //Click listener for forgot password

        forgot_pass_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);
                forgot_pass_text_click();
            }
        });





        //Click listener login

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);

                Login();
            }
        });


    }


    private void forgot_pass_text_click()
    {
        Intent intent=new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(intent);

    }

    private void Login()
    {

        if(Validate())
        {
            Intialize();
            Login_Process();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
        }
    }

    //validation

       private boolean Validate()
    {


        boolean valid=true;

        if( phone_text.getText().length() < 14)
        {

            phone_text.setError("Phone Number is Required");
            phone_text.requestFocus();
            valid=false;
        }
        if( pass_text.getText().length() == 0 )
        {
            pass_text.setError("Password is required");
            pass_text.requestFocus();
            valid=false;
        }


        return valid;
    }


    //Intializing values

    private void Intialize()
    {
        String Temp="";

        for (int i = 4; i < phone_text.getText().length(); i++) {

            Temp = Temp + phone_text.getText().toString().trim().charAt(i);
        }
        phone_number="92"+Temp;
        password=pass_text.getText().toString().trim();
    }


    //Login
    private void Login_Process()
    {



        if (cd.isConnected()) {

            final ProgressDialog loading = ProgressDialog.show(this, "Signing In", "Please wait...", false, true);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask() {
                @Override
                public void run() {


                    LoginActivity.this.runOnUiThread(new Runnable() {
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


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        if ((jsonResponse.getString("response").equalsIgnoreCase("success"))) {
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.Phone_SHARED_PREF, phone_number);
                            editor.putString(Config.F_Name_SHARED_PREF, jsonResponse.getString("first_name"));
                            editor.putString(Config.L_Name_SHARED_PREF, jsonResponse.getString("last_name"));
                            editor.putString(Config.City_SHARED_PREF, jsonResponse.getString("city"));
                            editor.putString(Config.Image_SHARED_PREF, jsonResponse.getString("image_path"));
                            //Saving values to editor
                            editor.commit();


                            loading.dismiss();
                            //Starting profile activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else

                        {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        loading.dismiss();
                        Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }


            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    loading.dismiss();
                    Toast.makeText(LoginActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                }
            };


            LoginRequest loginRequest = new LoginRequest(phone_number, password,getDeviceToken(), responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
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

    private String getDeviceToken()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String token= sharedPreferences.getString(Config.Device_Token, "Not Available");

        return token;
    }

    //store image


//string image

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,Main_Page_Activity.class);
        startActivity(intent);
    }

}
