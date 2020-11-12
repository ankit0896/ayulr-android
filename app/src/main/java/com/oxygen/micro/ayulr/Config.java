package com.oxygen.micro.ayulr;

/**
 * Created by MICRO on 1/23/2018.
 */
public class Config {
    //URLs to register.php and confirm.php file

    public static final String BASEURL = "http://ameygraphics.com/ayulr/api/";
    public static final String REGISTER_URL = BASEURL+"register.php";
    public static final String CONFIRM_URL = BASEURL+"confirm.php";
    public static final String COMMENT_URL = BASEURL+"comment.php";
    public static final String Forgot_URL= BASEURL+"forgot_password.php";
    public static final String CONFIRMFORGOT_URL = BASEURL+"confirm_forgot.php";

    //Keys to send username, password, phone and otp
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";
    public static final String KEY_PAYMENT = "paymentmode";

    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "ErrorMessage";
    public static final String PARAREGISTER_URL = BASEURL+"paramedical/register.php";
    public static final String PARACONFIRM_URL = BASEURL+"paramedical/confirm.php";
    public static final String NURREGISTER_URL = BASEURL+"nursing/register.php";
    public static final String NURCONFIRM_URL = BASEURL+"nursing/confirm.php";
    public static final String BLOODDONOR_URL = BASEURL+"donor/insert_blood_donor.php";
    public static final String EQUIPMENTDONOR_URL = BASEURL+"insert_equi.php";
}
