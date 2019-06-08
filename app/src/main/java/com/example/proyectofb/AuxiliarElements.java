package com.example.proyectofb;

import java.util.ArrayList;

public class AuxiliarElements {

    ArrayList<String> friendsIdsList;
    ArrayList<Post> postsList;


    AuxiliarElements(){

        this.friendsIdsList = new ArrayList<>();
        this.postsList = new ArrayList<>();

    }


    public ArrayList<String> getFriendsIdsList() {
        return friendsIdsList;
    }

    public ArrayList<Post> getPostsList() {
        return postsList;
    }

    public void addFriendId(String id){
        this.friendsIdsList.add(id);
    }
    public void addPost(Post post){
        this.postsList.add(post);
    }




}
