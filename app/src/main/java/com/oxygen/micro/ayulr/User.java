package com.oxygen.micro.ayulr;

import android.util.Log;

/**
 * Created by MICRO on 11/25/2017.
 */
public class User {

    private String Name,Email,Mob;
    private Integer Id;



    public User(int id, String name, String email,String mob) {
        this.Id= id;
        this.Name =name;
        this.Email= email;
        this.Mob= mob;
        Log.e("some value="," "+Id);
        Log.e("some value="," "+Name);
        Log.e("some value="," "+Email);
        Log.e("some value="," "+Mob);

    }


    public Integer getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getMob() {
        return Mob;
    }
}