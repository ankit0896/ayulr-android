package com.oxygen.micro.ayulr.view.paramedical.model;

public class ParaPatient {
    private String ParaId;
    private String ParaOrder;
    private String ParaName;
    private String ParaNumber;
    private String ParaAge;
    private String ParaStatus;
    private String ParaDate;
    private String ParaTime;
    public ParaPatient(String paraid, String paraorder, String paraname, String paranumber,String paraage,String parastatus,String paradate,String paratime) {
        this.ParaId = paraid;
        this.ParaOrder = paraorder;
        this.ParaName = paraname;
        this.ParaNumber = paranumber;
        this.ParaAge = paraage;
        this.ParaStatus = parastatus;
        this.ParaDate = paradate;
        this.ParaTime= paratime;


    }

    public String getParaId() {
        return ParaId;
    }

    public String getParaOrder() {
        return ParaOrder;
    }

    public String getParaName() {
        return ParaName;
    }

    public String getParaNumber() {
        return ParaNumber;
    }

    public String getParaAge() {
        return ParaAge;
    }

    public String getParaStatus() {
        return ParaStatus;
    }

    public String getParaDate() {
        return ParaDate;
    }

    public String getParaTime() {
        return ParaTime;
    }
}
