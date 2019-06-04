package com.example.proyectofb;

import android.graphics.Bitmap;

public class Post {

    private String userName;
    private String lastName;
    private String type;
    private Bitmap userPhoto;
    private String postText;
    private Bitmap postImage;

    public Post(String userName, String lastName, String type, Bitmap userPhoto, String postText, Bitmap postImage) {
        this.userName = userName;
        this.lastName = lastName;
        this.type = type;
        this.userPhoto = userPhoto;
        this.postText = postText;
        this.postImage = postImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public Bitmap getPostImage() {
        return postImage;
    }

    public void setPostImage(Bitmap postImage) {
        this.postImage = postImage;
    }
}
