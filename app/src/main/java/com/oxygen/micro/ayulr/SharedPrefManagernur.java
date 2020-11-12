package com.oxygen.micro.ayulr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.oxygen.micro.ayulr.commonactivity.CategoryActivity;

public class SharedPrefManagernur {
    private static final String KEY_NURNAME = "keynurname";
    private static final String KEY_NUREmail = "keynuremail";
    private static final String KEY_NURMOB = "keynurmob";
    private static final String KEY_NURID = "keynurid";
    private static final String SHARED_PREF_NAMENUR = "simplifiedcodingshared";

    private static SharedPrefManagernur mmInstance;
    private static Context mmCtx;

    private SharedPrefManagernur(Context context) {
        mmCtx = context;
    }

    public static synchronized SharedPrefManagernur getInstance(Context context) {
        if (mmInstance == null) {
            mmInstance = new SharedPrefManagernur(context);
        }
        return mmInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLoginNur(Nur nur) {
        SharedPreferences shared = mmCtx.getSharedPreferences(SHARED_PREF_NAMENUR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(KEY_NURID, (nur.getNurId()));
        editor.putString(KEY_NURNAME, nur.getNurName());
        editor.putString(KEY_NUREmail, nur.getNurEmail());
        editor.putString(KEY_NURMOB, nur.getNurMob());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedInNur() {
        SharedPreferences sharedPref = mmCtx.getSharedPreferences(SHARED_PREF_NAMENUR, Context.MODE_PRIVATE);
        return sharedPref.getString(KEY_NUREmail, null) != null;
    }

    //this method will give the logged in user
    public Nur getUserNur() {
        SharedPreferences shared = mmCtx.getSharedPreferences(SHARED_PREF_NAMENUR, Context.MODE_PRIVATE);
        return new Nur(
                shared.getInt(KEY_NURID, -1),
                shared.getString(KEY_NURNAME, null),
                shared.getString(KEY_NUREmail, null),
                shared.getString(KEY_NURMOB, null)

        );
    }

    //this method will logout the user
    public void Nurlogout() {
        SharedPreferences shared = mmCtx.getSharedPreferences(SHARED_PREF_NAMENUR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.apply();
        mmCtx.startActivity(new Intent(mmCtx, CategoryActivity.class));
    }
}