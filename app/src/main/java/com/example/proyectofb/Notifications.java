package com.example.proyectofb;

public class Notifications {

    private String userName;
    private String action;
    private String notificationType;
    private String userIdOwner;
    private String userIdFrom;

    public Notifications(String userName, String action,String notificationType,
                         String userIdOwner, String userIdFrom) {
        this.userName = userName;
        this.action = action;
        this.notificationType = notificationType;
        this.userIdOwner = userIdOwner;
        this.userIdFrom = userIdFrom;
    }

    public String getUserIdOwner() {
        return userIdOwner;
    }

    public String getUserIdFrom() {
        return userIdFrom;
    }

    public String getNotificationType(){
        return notificationType;
    }

    public String getUserName() {
        return userName;
    }

    public String getAction() {
        return action;
    }
}
