package com.example.proyectofb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.proyectofb.ui.main.UserThumbnail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FriendsListActivity extends AppCompatActivity {


    private String userID;
    private UserThumbnailAdapter userThumbnailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFriends2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.userThumbnailAdapter = new UserThumbnailAdapter();
        recyclerView.setAdapter(this.userThumbnailAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return false;
    }


    public void OnClickButtonAllFriends(View view) {

        FirebaseGetAllFriendsCurrentUser();

    }

    public void FirebaseGetAllFriendsCurrentUser() {

        final String id = this.userID;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();

        //FALTA

        reference.child("friends").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String temporalUserKey = dataSnapshot.getKey();

                if (temporalUserKey.equals(id)) { // Amigos del usuario actual

                    Map<String, Object> friendMap = (Map<String, Object>) dataSnapshot.getValue();
                    for (Map.Entry<String, Object> entry : friendMap.entrySet()) {

                        final String friendId = (String) entry.getValue();
                        reference.child("users").orderByKey().addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                String temporalUserId = dataSnapshot.getKey();
                                if (temporalUserId.equals(friendId)) {

                                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                                    String name = (String) userMap.get("name");
                                    String lastName = (String) userMap.get("lastName");
                                    String photoUrl = (String) userMap.get("profilePhotoUrl");
                                    ImageDownloader imageDownloader = new ImageDownloader();
                                    Bitmap bitmap = null;
                                    try {
                                        bitmap = imageDownloader.execute(photoUrl).get();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    UserThumbnail userThumbnail = new UserThumbnail(name, lastName, bitmap, temporalUserId);
                                    userThumbnailAdapter.AddUserThumbnail(userThumbnail);
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public class ImageDownloader extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

    }
}
