package com.example.proyectofb.ui.main;

import android.graphics.Bitmap;

public class UserThumbnail {

    private String name;
    private String lastName;
    private Bitmap bitmap;

    public UserThumbnail(String name, String lastName, Bitmap bitmap) {
        this.name = name;
        this.lastName = lastName;
        this.bitmap = bitmap;
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
