package com.oxygen.micro.ayulr.constant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.oxygen.micro.ayulr.commonactivity.CategoryActivity;

import com.oxygen.micro.ayulr.model.User;

/**
 * Created by MICRO on 11/25/2017.
 */
public class SharedPrefManager {

    //the constants

    private static final String KEY_NAME = "keyname";
    private static final String KEY_Email = "keyemail";
    private static final String KEY_MOB = "keymob";
    private static final String KEY_ID = "keyid";
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, (user.getId()));
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_Email, user.getEmail());
        editor.putString(KEY_MOB, user.getMob());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_Email, null) != null;
    }

    //this method will give the logged in user
  public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
               sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_Email, null),
                sharedPreferences.getString(KEY_MOB, null)

               );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, CategoryActivity.class));
    }
}