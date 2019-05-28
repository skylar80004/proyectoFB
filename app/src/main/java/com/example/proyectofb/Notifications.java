package com.example.proyectofb;

public class Notifications {

    private String userName;
    private String action;
    private String notificationType;

    public Notifications(String userName, String action,String notificationType) {
        this.userName = userName;
        this.action = action;
        this.notificationType = notificationType;
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
