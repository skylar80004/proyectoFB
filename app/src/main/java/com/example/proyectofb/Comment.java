package com.example.proyectofb;

public class Comment {


    String userName;
    String commentText;

    public Comment(String userName, String commentText) {
        this.userName = userName;
        this.commentText = commentText;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentText() {
        return commentText;
    }
}
