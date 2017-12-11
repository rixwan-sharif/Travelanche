package com.example.rixwansharif.travelanche;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private TextView name_text,password_text,phone_text,city_text;
    private ImageButton edit_name_btn,edit_password_btn,change_city_btn;
    private Button camera_btn,update_name_btn,change_pass_btn;
    private CircleImageView profile_imgview;
    private EditText dialogue_fname,dialogue_lname;
    private String fname,lname;
    private EditText dialogue_cpass,dialogue_npass,dialogue_cnpass;
    private String current_pass,new_pass,cnfirm_pass;
    Bitmap bitmap;
    private RequestQueue requestQueue;
    private String phone_number;

    CheckInternet cd=new CheckInternet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#01AA97")));
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        //Widgets
        name_text=(TextView) findViewById(R.id.setting_name_txt);
        //password_text=(TextView) findViewById(R.id.setting_pass_txt);
        phone_text=(TextView) findViewById(R.id.setting_phone_txt);
        city_text=(TextView) findViewById(R.id.setting_city_txt);

        camera_btn=(Button) findViewById(R.id.setting_camera_btn);
        edit_name_btn=(ImageButton) findViewById(R.id.setting_name_btn);
        edit_password_btn=(ImageButton) findViewById(R.id.setting_pass_btn);
        change_city_btn=(ImageButton) findViewById(R.id.setting_city_btn);
        profile_imgview=(CircleImageView) findViewById(R.id.setting_profile_pic);


        //
        Set_profile();





        //click listeners
        change_city_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);

                LayoutInflater li = LayoutInflater.from(SettingActivity.this);

                //Creating a view to get the dialog box

                View confirmDialog = li.inflate(R.layout.city_dialogue, null);

                //Initizliaing confirm button fo dialog box and edittext of dialog box



                //Creating an alertdialog builder
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(SettingActivity.this);
                //Adding our dialog box to the view of alert dialog
                alertDialogBuilder.setView(confirmDialog);
                //Creating an alert dialog
                final android.support.v7.app.AlertDialog city_inputDialog= alertDialogBuilder.create();
                // show it
                city_inputDialog.show();
                city_inputDialog.setCanceledOnTouchOutside(false);
                city_inputDialog.getWindow().setLayout(550, 700);


            }
        });




        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);
                Select_Pic();

            }
        });





        edit_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);

                //show
                LayoutInflater li = LayoutInflater.from(SettingActivity.this);

                //Creating a view to get the dialog box

                View confirmDialog = li.inflate(R.layout.change_pass_dialogue, null);

                //Initizliaing confirm button fo dialog box and edittext of dialog box

                change_pass_btn = (AppCompatButton) confirmDialog.findViewById(R.id.dialogu_change_pass_btn);
                dialogue_cpass=(EditText)confirmDialog.findViewById(R.id.dialogue_cpass);
                dialogue_npass=(EditText)confirmDialog.findViewById(R.id.dialogue_npass);
                dialogue_cnpass=(EditText)confirmDialog.findViewById(R.id.dialogue_cnpass);

                //Creating an alertdialog builder
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(SettingActivity.this);
                //Adding our dialog box to the view of alert dialog
                alertDialogBuilder.setView(confirmDialog);
                //Creating an alert dialog
                final android.support.v7.app.AlertDialog pass_inputDialog= alertDialogBuilder.create();
                // show it
                pass_inputDialog.show();
                pass_inputDialog.setCanceledOnTouchOutside(false);

                change_pass_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        current_pass = dialogue_cpass.getText().toString().trim();
                        new_pass = dialogue_npass.getText().toString().trim();
                        cnfirm_pass = dialogue_cnpass.getText().toString().trim();
                        if (Validate_Password())
                        {
                            pass_inputDialog.dismiss();
                            Change_Password_Process();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Enter Valid Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        edit_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlphaAnimation buttonClick = new AlphaAnimation(1.0F, 0.2F);
                buttonClick.setDuration(250);
                v.startAnimation(buttonClick);

                //show
                LayoutInflater li = LayoutInflater.from(SettingActivity.this);

                //Creating a view to get the dialog box

                View confirmDialog = li.inflate(R.layout.editname_dialogue, null);

                //Initizliaing confirm button fo dialog box and edittext of dialog box

                update_name_btn = (AppCompatButton) confirmDialog.findViewById(R.id.dialogu_name_update_btn);
                dialogue_fname = (EditText) confirmDialog.findViewById(R.id.dialogue_update_fname);
                dialogue_lname = (EditText) confirmDialog.findViewById(R.id.dialogue_update_lname);

                dialogue_fname.setText(fname);
                dialogue_lname.setText(lname);

                //Creating an alertdialog builder
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(SettingActivity.this);

                //Adding our dialog box to the view of alert dialog
                alertDialogBuilder.setView(confirmDialog);
                //Creating an alert dialog
                final android.support.v7.app.AlertDialog inputDialog= alertDialogBuilder.create();

                // show it
                inputDialog.show();
                inputDialog.setCanceledOnTouchOutside(false);
                //

                update_name_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //set name to strings
                        fname = dialogue_fname.getText().toString().trim();
                        lname = dialogue_lname.getText().toString().trim();
                        if (Validate_Name())
                        {
                            inputDialog.dismiss();

                            Update_Name();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }


    private void Update_Name()
    {
        if (cd.isConnected()) {

            final ProgressDialog loading = ProgressDialog.show(this, "Updating", "Please wait...", false, true);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask() {
                @Override
                public void run() {


                    SettingActivity.this.runOnUiThread(new Runnable() {
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Update_Name_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();

                            if (response.equalsIgnoreCase("success"))
                            {
                                SharedPreferences sharedPreferences = SettingActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //Adding values to editor

                                editor.putString(Config.F_Name_SHARED_PREF, fname);
                                editor.putString(Config.L_Name_SHARED_PREF, lname);
                                editor.commit();

                                //

                                name_text.setText(fname+" "+lname);

                                Toast.makeText(SettingActivity.this, "Name Changed!", Toast.LENGTH_LONG).show();

                            }
                            else
                            {

                                Toast.makeText(SettingActivity.this, "Error! Try Again", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText(SettingActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username
                    params.put("first_name", fname);
                    params.put("last_name", lname);
                    params.put("phone", phone_number);

                    return params;
                }
            };

            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


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
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }


    }


    private void Upload_Photo()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String old_image_link=sharedPreferences.getString(Config.Image_SHARED_PREF, "Not Available").toString();

        if(cd.isConnected())
        {


            final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    //Disimissing the progress dialog
                    Toast.makeText(SettingActivity.this, response, Toast.LENGTH_LONG).show();

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        loading.dismiss();

                      if ((jsonResponse.getString("response").equalsIgnoreCase("success"))) {

                            SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Config.Image_SHARED_PREF, jsonResponse.getString("image_path"));
                            editor.commit();

                        }
                        else {
                            Toast.makeText(SettingActivity.this, "Error Changing Picture !", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        loading.dismiss();
                        Toast.makeText(SettingActivity.this, "Error!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }


            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();

                }
            };

            String string_image = getStringImage(bitmap);

            ChangeImageRequest changeImageRequest = new ChangeImageRequest(phone_number,string_image,old_image_link , responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
            queue.add(changeImageRequest);

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

    //
    private void Change_Password_Process() {


        if (cd.isConnected()) {

            final ProgressDialog loading = ProgressDialog.show(this, "Changing Password", "Please wait...", false, false);

            Timer timer_a = new Timer();
            timer_a.schedule(new TimerTask() {
                @Override
                public void run() {


                   SettingActivity.this.runOnUiThread(new Runnable() {
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

                    loading.dismiss();


                    try {

                        if (response.equalsIgnoreCase("success"))
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingActivity.this);

                            alertDialogBuilder
                                    .setMessage("Your Password is Changed !")
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
                        else
                        {

                            Toast.makeText(SettingActivity.this, "Incorret current password", Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(SettingActivity.this, "Something has gone wrong!", Toast.LENGTH_LONG).show();
                    }


                }


            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    loading.dismiss();
                    Toast.makeText(SettingActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                }
            };

            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(phone_number,current_pass ,new_pass, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
            queue.add(changePasswordRequest);
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

    //
    private void Select_Pic() {

        try {
            Intent cropIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 250);
            cropIntent.putExtra("outputY", 250);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 1);
        }

        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 ) {
            if(resultCode == Activity.RESULT_OK)
            {
                try {
                    Bundle extras = data.getExtras();
                    bitmap = extras.getParcelable("data");
                    // Set The Bitmap Data To ImageView
                    profile_imgview.setImageBitmap(bitmap);
                    Upload_Photo();

                    // Toast.makeText(this, "L" + getStringImage(bitmap).length(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {

                    e.printStackTrace();
                    Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

// validate text fields for name

    private boolean Validate_Name()
    {


        boolean valid=true;
        if( fname.length() == 0 || fname.length()>32)
        {

            dialogue_fname.setError("Please Enter Valid First Name");
            dialogue_fname.requestFocus();
            valid=false;

        }
        if( lname.length() == 0 || lname.length()>32)
        {
            dialogue_lname.setError("Please Enter Valid Last Name");
            dialogue_lname.requestFocus();
            valid=false;
        }

        return valid;
    }

    private boolean Validate_Password()
    {


        boolean valid=true;

        if( current_pass.length() == 0 )
        {
            dialogue_cpass.setError("Current Password is required");
            dialogue_cpass.requestFocus();
            valid=false;
        }
        if( new_pass.length() == 0 )
        {
            dialogue_npass.setError("New Password is required");
            dialogue_npass.requestFocus();
            valid=false;
        }
        if( cnfirm_pass.length() == 0 )
        {
            dialogue_cnpass.setError("Confirm New Password is required");
            dialogue_cnpass.requestFocus();
            valid=false;
        }


        if(!new_pass.equals(cnfirm_pass))
        {
            dialogue_cnpass.setError("Password Not matched");
            dialogue_cnpass.requestFocus();
            valid=false;
        }
        if(new_pass.length()<6)
        {
            dialogue_npass.setError("Password should be atleast of 6 charactors");
            dialogue_npass.requestFocus();
            valid=false;
        }
        return valid;
    }


    //fun
    private void Set_profile()
    {



        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        fname=sharedPreferences.getString(Config.F_Name_SHARED_PREF, "Not Available").toString();
        lname=sharedPreferences.getString(Config.L_Name_SHARED_PREF, "Not Available").toString();
        phone_number=sharedPreferences.getString(Config.Phone_SHARED_PREF, "Not Available").toString();


        name_text.setText(fname+" "+lname);
        phone_text.setText(phone_number);
        city_text.setText(sharedPreferences.getString(Config.City_SHARED_PREF, "Not Available"));

        final String pic_path=sharedPreferences.getString(Config.Image_SHARED_PREF, "Not Available");


       Picasso.with(getApplicationContext())
                .load("http://rixwanxharif.000webhostapp.com/"+pic_path)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(profile_imgview, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(getApplicationContext())
                                .load("http://rixwanxharif.000webhostapp.com/" + pic_path)
                                .into(profile_imgview);
                    }
                });
    }

    //decode string to Bitmap

    public static Bitmap getBitmapImage(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    ////decode Bitmap to string

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }



}
