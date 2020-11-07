package com.oxygen.micro.ayulr;

import android.util.Log;

public class NursingBlog {
    private String NurId;
    private String NurUserName;
    private String NurImage;
    private String NurDescription;

    public NursingBlog(String nurid, String nurusername, String nurimage , String nurdescription) {
        this.NurId = nurid;
        this.NurUserName = nurusername;
        this.NurImage = nurimage;
        this.NurDescription = nurdescription;
    }

    public String getNurId() {
        return NurId;
    }

    public String getNurUserName() {
        return NurUserName;
    }

    public String getNurImage() {
        return NurImage;
    }

    public String getNurDescription() {
        return NurDescription;
    }
}



