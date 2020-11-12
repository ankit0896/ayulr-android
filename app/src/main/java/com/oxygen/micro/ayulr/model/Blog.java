package com.oxygen.micro.ayulr.model;

/**
 * Created by MICRO on 2/22/2018.
 */

public class Blog {
    private String Id;
    private String UserName;
    private String Image;
    private String Description;

    public Blog(String id, String username, String image , String description) {
        this.Id = id;
        this.UserName = username;
        this.Image = image;
        this.Description = description;
    }

    public String getId() {
        return Id;
    }

    public String getUserName() {
        return UserName;
    }

    public String getImage() {
        return Image;
    }

    public String getDescription() {
        return Description;
    }


}