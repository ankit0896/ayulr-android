package com.oxygen.micro.ayulr.view.paramedical.model;

import android.util.Log;

public class Para {

    private String ParaName,ParaEmail,ParaMob;
    private Integer ParaId;



    public Para(int paraid, String paraname, String paraemail,String paramob) {
        this.ParaId= paraid;
        this.ParaName =paraname;
        this.ParaEmail= paraemail;
        this.ParaMob= paramob;
        Log.e("some value="," "+ParaId);
        Log.e("some value="," "+ParaName);
        Log.e("some value="," "+ParaEmail);
        Log.e("some value="," "+ParaMob);

    }

    public Integer getParaId() {
        return ParaId;
    }

    public String getParaName() {
        return ParaName;
    }

    public String getParaEmail() {
        return ParaEmail;
    }

    public String getParaMob() {
        return ParaMob;
    }
}