package com.oxygen.micro.ayulr.model;

public class Patient {
        private String Id;
        private String Order;
        private String Name;
        private String Number;
        private String Age;
        private String Status;
        private String Date;
        private String Time;
        public Patient(String id, String order, String name, String number,String age,String status,String date,String time) {
            this.Id = id;
            this.Order = order;
            this.Name = name;
            this.Number = number;
            this.Age = age;
            this.Status = status;
            this.Date = date;
            this.Time= time;


        }

    public String getId() {
        return Id;
    }

    public String getOrder() {
        return Order;
    }

    public String getName() {
        return Name;
    }

    public String getNumber() {
        return Number;
    }

    public String getAge() {
        return Age;
    }

    public String getStatus() {
        return Status;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }
}
