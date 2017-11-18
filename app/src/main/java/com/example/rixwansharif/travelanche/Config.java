package com.example.rixwansharif.travelanche;

import android.graphics.Bitmap;

/**
 * Created by Rixwan Sharif on 7/15/2017.
 */
public class Config {

    public static final String test_URL = "http://rixwanxharif.000webhostapp.com/test.php";
    public static final String Register_URL = "http://rixwanxharif.000webhostapp.com/register.php";
    public static final String SendOtp_URL = "http://rixwanxharif.000webhostapp.com/sendotp.php";
    public static final String Login_URL = "http://rixwanxharif.000webhostapp.com/login.php";
    public static final String ForgotPassword_URL = "http://rixwanxharif.000webhostapp.com/forgotpasswordcode.php";
    public static final String SetNewPassword_URL = "http://rixwanxharif.000webhostapp.com/setnewpassword.php";
    public static final String  CHANGE_PASSWORD__URL = "http://rixwanxharif.000webhostapp.com/changepassword.php";
    public static final String  Upload_Image_URL = "http://rixwanxharif.000webhostapp.com/uploadimage.php";
    public static final String  Chaneg_Image_URL = "http://rixwanxharif.000webhostapp.com/changeimage.php";
    public static final String  Update_Name_URL = "http://rixwanxharif.000webhostapp.com/updatename.php";

    public static final String  Post_Trip_URL = "http://rixwanxharif.000webhostapp.com/post_trip.php";
    public static final String  Delete_User_Trip_URL = "http://rixwanxharif.000webhostapp.com/delete_user_trip.php";
    public static final String  Load_Companies_URL = "http://rixwanxharif.000webhostapp.com/fetchcompaniesjson.php";
    public static final String  Load_Trips_URL = "http://rixwanxharif.000webhostapp.com/fetchtripjson.php";
    public static final String  Edit_Trips_URL = "http://rixwanxharif.000webhostapp.com/edit_trip.php";


    public static final String  Load_Bids_URL = "http://rixwanxharif.000webhostapp.com/load_bids_on_trip.php";
    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "ErrorMessage";


    public static final String LOGIN_SUCCESS = "success";



    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "Travelanche_login";

    //This would be used to store the phone of current logged in user
    public static final String Phone_SHARED_PREF = "phone";
    public static final String F_Name_SHARED_PREF= "first_name";
    public static final String L_Name_SHARED_PREF= "last_name";
    public static final String City_SHARED_PREF= "city";
    public static final String Image_SHARED_PREF= "image";



    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";



}
