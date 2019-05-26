package com.example.proyectofb.ui.main;

import android.graphics.Bitmap;

public class UserThumbnail {

    private String name;
    private String lastName;
    private Bitmap bitmap;
    private String id;

    public UserThumbnail(String name, String lastName, Bitmap bitmap,String id) {
        this.name = name;
        this.lastName = lastName;
        this.bitmap = bitmap;
        this.id = id;
    }


    public String getId(){
        return id;
    }
    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
