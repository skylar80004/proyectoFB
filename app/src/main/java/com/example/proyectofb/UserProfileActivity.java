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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    String userID;
    PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        postAdapter = new PostAdapter();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOtherProfilePosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.postAdapter);

        Intent intent  = getIntent();
        userID = intent.getStringExtra("userID");

        Toast.makeText(this, "Cargando perfil...",Toast.LENGTH_LONG).show();


        ImageView imageViewProfilePhoto = findViewById(R.id.imageViewProfilePhoto2);
        imageViewProfilePhoto.setOnClickListener(this);

        Button buttonPhotos = findViewById(R.id.buttonProfilePhotos2);
        buttonPhotos.setOnClickListener(this);

        Button buttonSendFriends = findViewById(R.id.buttonUserProfileSendFriend);
        buttonSendFriends.setOnClickListener(this);

        Button buttonSeeFriends = findViewById(R.id.buttonProfileFriends2);
        buttonSeeFriends.setOnClickListener(this);
        ReadTest();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.GetCurrentUserPosts();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return false;
    }
    public void ReadTest(){

        // Personal Info
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("users");
        String id = this.userID;

        databaseRef.child(id).orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();
                String value = "";
                if(key.equals("name")){
                    value = (String)dataSnapshot.getValue();
                    TextView textView= findViewById(R.id.textViewProfileName2);
                    textView.setText(value);

                }
                else if(key.equals("lastName")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= findViewById(R.id.textViewProfileName2);
                    String actualText = textView.getText().toString();
                    String newText = actualText + " " + value;
                    textView.setText(newText);
                }
                else if(key.equals("city")){
                    value = (String)dataSnapshot.getValue();
                    TextView textView= findViewById(R.id.textViewProfileCity2);
                    textView.setText(value);

                }
                else if(key.equals("birthDate")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= findViewById(R.id.textViewProfileBirthDate2);
                    textView.setText(value);
                }
                else if(key.equals("email")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= findViewById(R.id.textViewProfileEmail2);
                    textView.setText(value);
                }
                else if(key.equals("gender")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= findViewById(R.id.textViewProfileGender2);
                    textView.setText(value);
                }
                else if(key.equals("phone")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= findViewById(R.id.textViewProfilePhone2);
                    textView.setText(value);
                }
                else if(key.equals("profilePhotoUrl")){

                    value = (String)dataSnapshot.getValue();
                    ImageView imageView = findViewById(R.id.imageViewProfilePhoto2);

                    ImageDownloader imageDownloader = new ImageDownloader();

                    try {
                        Bitmap bitmap = imageDownloader.execute(value).get();
                        imageView.setImageBitmap(bitmap);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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

        // Education Info
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("education");

        id = userID;

        databaseRef.child(id).orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                LinearLayout linearLayoutEducationProfile = findViewById(R.id.linearLayoutEducationProfile2);
                String value = (String)dataSnapshot.getValue();
                TextView textViewEducation = new TextView(getApplicationContext());
                textViewEducation.setText(" " + value + " ");
                linearLayoutEducationProfile.addView(textViewEducation);

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

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.buttonProfileFriends2:
                Intent intentFriends = new Intent(this, FriendsListActivity.class);
                intentFriends.putExtra("userID", this.userID);
                startActivity(intentFriends);
                break;
            case R.id.imageViewProfilePhoto2:
                Intent intent = new Intent(this, ProfilePhotoActivity.class);
                startActivity(intent);
                break;

            case R.id.buttonProfilePhotos2:
                Intent intentProfilePhotos = new Intent(this, PhotosActivity.class);
                intentProfilePhotos.putExtra("from", "otherProfile");
                intentProfilePhotos.putExtra("userId",userID);
                startActivity(intentProfilePhotos);
                break;
            case R.id.buttonUserProfileSendFriend:
                FirebaseSendFriendSolicitude();
                break;

        }

    }


    public void FirebaseSendFriendSolicitude(){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String currentSessionUserID = user.getUid();

        FirebaseDatabase database  =  FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.child("notifications").child(userID).child("friendSoli").child(currentSessionUserID).setValue("pending").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Solicitud de Amistad Enviada",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

    }


    public void GetCurrentUserPosts(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final String userId = this.userID;
        databaseReference.child("posts").child(userId).orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Map<String,Object> postMap = (Map<String,Object>) dataSnapshot.getValue();

                String postId = dataSnapshot.getKey();
                String userNamePost = (String)postMap.get("name");
                String lastNamePost = (String)postMap.get("lastName");
                String type = (String)postMap.get("type");
                String profilePhotoUrl = (String) postMap.get("profilePhotoUrl"); // Con este link se descarga la foto del usuario, pero como estamos en el perfil , ya se descargo previamente y esta ubicada en el imageView de foto de perfil
                String text = (String) postMap.get("text");
                String likes = (String) postMap.get("likes");
                String disLikes = (String) postMap.get("dislikes");
                String imageUrl = (String) postMap.get("imageUrl");


                ImageDownloader imageDownloader = new ImageDownloader();
                Bitmap bitmapProfilePhoto = null;
                try {
                    bitmapProfilePhoto = imageDownloader.execute(profilePhotoUrl).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ImageDownloader imageDownloader2 = new ImageDownloader();
                Bitmap bitmapPostImage = null;
                try {
                    bitmapPostImage = imageDownloader2.execute(imageUrl).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Post post = new Post(userNamePost,lastNamePost,type,bitmapProfilePhoto,text,bitmapPostImage,
                        likes,disLikes,userId,postId);
                postAdapter.AddPost(post);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();

                Map<String,Object> postMap = (Map<String,Object>) dataSnapshot.getValue();

                String postId = dataSnapshot.getKey();
                String userNamePost = (String)postMap.get("name");
                String lastNamePost = (String)postMap.get("lastName");
                String type = (String)postMap.get("type");
                String profilePhotoUrl = (String) postMap.get("profilePhotoUrl"); // Con este link se descarga la foto del usuario, pero como estamos en el perfil , ya se descargo previamente y esta ubicada en el imageView de foto de perfil
                String text = (String) postMap.get("text");
                String likes = (String) postMap.get("likes");
                String disLikes = (String) postMap.get("dislikes");
                String imageUrl = (String) postMap.get("imageUrl");


                ImageDownloader imageDownloader = new ImageDownloader();
                Bitmap bitmapProfilePhoto = null;
                try {
                    bitmapProfilePhoto = imageDownloader.execute(profilePhotoUrl).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ImageDownloader imageDownloader2 = new ImageDownloader();
                Bitmap bitmapPostImage = null;
                try {
                    bitmapPostImage = imageDownloader2.execute(imageUrl).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Post post = new Post(userNamePost,lastNamePost,type,bitmapProfilePhoto,text,bitmapPostImage,
                        likes,disLikes,userId,postId);

                postAdapter.UpdatePost(key,post);
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

        // Ordenan array
        // despues de ordenar, añadir al adapter
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
