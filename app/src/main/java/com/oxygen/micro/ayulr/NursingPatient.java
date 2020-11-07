package com.oxygen.micro.ayulr;

public class NursingPatient {
    private String NurId;
    private String NurOrder;
    private String NurName;
    private String NurNumber;
    private String NurAge;
    private String NurStatus;
    private String NurDate;
    private String NurTime;
    public NursingPatient(String nurid, String nurorder, String nurname, String nurnumber,String nurage,String nurstatus,String nurdate,String nurtime) {
        this.NurId = nurid;
        this.NurOrder = nurorder;
        this.NurName = nurname;
        this.NurNumber = nurnumber;
        this.NurAge = nurage;
        this.NurStatus = nurstatus;
        this.NurDate = nurdate;
        this.NurTime= nurtime;


    }

    public String getNurId() {
        return NurId;
    }

    public String getNurOrder() {
        return NurOrder;
    }

    public String getNurName() {
        return NurName;
    }

    public String getNurNumber() {
        return NurNumber;
    }

    public String getNurAge() {
        return NurAge;
    }

    public String getNurStatus() {
        return NurStatus;
    }

    public String getNurDate() {
        return NurDate;
    }

    public String getNurTime() {
        return NurTime;
    }
}
