package com.oxygen.micro.ayulr;

import android.util.Log;

public class Nur {
    private String NurName,NurEmail,NurMob;
    private Integer NurId;



    public Nur(int nurid, String nurname, String nuremail,String nurmob) {
        this.NurId= nurid;
        this.NurName =nurname;
        this.NurEmail= nuremail;
        this.NurMob= nurmob;
        Log.e("some value="," "+NurId);
        Log.e("some value="," "+NurName);
        Log.e("some value="," "+NurEmail);
        Log.e("some value="," "+NurMob);

    }

    public String getNurName() {
        return NurName;
    }

    public String getNurEmail() {
        return NurEmail;
    }

    public String getNurMob() {
        return NurMob;
    }

    public Integer getNurId() {
        return NurId;
    }
}
