package com.oxygen.micro.ayulr.model;

public class EquipmentDonor {
    private String id;
    private String Name;
    private String Email;
    private String Contact;
    private String image;

    public EquipmentDonor(String id, String name, String email, String contact, String image) {
        this.id = id;
        this.Name = name;
        this.Email = email;
        this.Contact = contact;
        this.image = image;

    }

    public String getId() {
        return id;
    }

    public String getName()
    {
        return Name;
    }

    public String getEmail()
    {
        return Email;
    }

    public String getContact()
    {
        return Contact;
    }

    public String getImage()
    {
        return image;
    }
}