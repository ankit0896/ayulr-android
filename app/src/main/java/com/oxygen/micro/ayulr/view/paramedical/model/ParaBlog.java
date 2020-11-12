package com.oxygen.micro.ayulr.view.paramedical.model;

public class ParaBlog {
    private String ParaId;
    private String ParaUserName;
    private String ParaImage;
    private String ParaDescription;

    public ParaBlog(String paraid, String parausername, String paraimage , String paradescription) {
        this.ParaId = paraid;
        this.ParaUserName = parausername;
        this.ParaImage = paraimage;
        this.ParaDescription = paradescription;
    }

    public String getParaId() {
        return ParaId;
    }

    public String getParaUserName() {
        return ParaUserName;
    }

    public String getParaImage() {
        return ParaImage;
    }

    public String getParaDescription() {
        return ParaDescription;
    }
}
