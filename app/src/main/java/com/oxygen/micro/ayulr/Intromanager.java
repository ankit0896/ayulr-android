package com.oxygen.micro.ayulr;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MICRO on 1/17/2018.
 */

public class Intromanager  {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    public Intromanager(Context context){
        this.context=context;
        pref=context.getSharedPreferences("First",0);
        editor=pref.edit();
    }
    public void setFirst(Boolean isFirst){
        editor.putBoolean("check",isFirst);
        editor.commit();
    }

   public boolean Check()
   {
       return pref.getBoolean("check",true);
   }
}
