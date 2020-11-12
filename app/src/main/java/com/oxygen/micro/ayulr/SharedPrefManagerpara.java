package com.oxygen.micro.ayulr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.oxygen.micro.ayulr.commonactivity.CategoryActivity;

public class SharedPrefManagerpara {
    private static final String KEY_PARANAME = "keyparaname";
    private static final String KEY_PARAEmail = "keyparaemail";
    private static final String KEY_PARAMOB = "keyparamob";
    private static final String KEY_PARAID = "keyparaid";
    private static final String SHARED_PREF_NAMEPARA = "simplifiedcodingshared";

    private static SharedPrefManagerpara mmInstance;
    private static Context mmCtx;

    private SharedPrefManagerpara(Context context) {
        mmCtx = context;
    }

    public static synchronized SharedPrefManagerpara getInstance(Context context) {
        if (mmInstance == null) {
            mmInstance = new SharedPrefManagerpara(context);
        }
        return mmInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLoginPara(Para para) {
        SharedPreferences sharedPref = mmCtx.getSharedPreferences(SHARED_PREF_NAMEPARA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_PARAID, (para.getParaId()));
        editor.putString(KEY_PARANAME, para.getParaName());
        editor.putString(KEY_PARAEmail, para.getParaEmail());
        editor.putString(KEY_PARAMOB, para.getParaMob());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedInPara() {
        SharedPreferences sharedPref = mmCtx.getSharedPreferences(SHARED_PREF_NAMEPARA, Context.MODE_PRIVATE);
        return sharedPref.getString(KEY_PARAEmail, null) != null;
    }

    //this method will give the logged in user
    public Para getUserPara() {
        SharedPreferences sharedPref = mmCtx.getSharedPreferences(SHARED_PREF_NAMEPARA, Context.MODE_PRIVATE);
        return new Para(
                sharedPref.getInt(KEY_PARAID, -1),
                sharedPref.getString(KEY_PARANAME, null),
                sharedPref.getString(KEY_PARAEmail, null),
                sharedPref.getString(KEY_PARAMOB, null)

        );
    }

    //this method will logout the user
    public void Paralogout() {
        SharedPreferences sharedPref = mmCtx.getSharedPreferences(SHARED_PREF_NAMEPARA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        mmCtx.startActivity(new Intent(mmCtx, CategoryActivity.class));
    }
}