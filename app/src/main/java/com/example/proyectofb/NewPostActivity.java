package com.example.proyectofb;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.sql.SQLTransactionRollbackException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewPostActivity extends AppCompatActivity {



    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        this.radioGroup = findViewById(R.id.radioGroupNewPost);
        RadioButton radioButtonText =  findViewById(R.id.radioButtonTextOption);
        radioButtonText.setChecked(true);
        this.setInitialRadioButton();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return false;
    }



    public void setInitialRadioButton(){

        ImageView imageViewImage = findViewById(R.id.imageViewNewPostImage);
        imageViewImage.setVisibility(View.INVISIBLE);
        TextView textViewImageTitle = findViewById(R.id.textViewNewPostImageTitle);
        textViewImageTitle.setVisibility(View.INVISIBLE);
        Button buttonSelectImage = findViewById(R.id.buttonNewPostPhoto);
        buttonSelectImage.setVisibility(View.INVISIBLE);
        TextView textViewVideoTitle = findViewById(R.id.textViewNewPostVideoTitle);
        textViewVideoTitle.setVisibility(View.INVISIBLE);

    }

    public Map<String, Object> getPostMap(String name, String lastName,String type,
                                          String text,String imageUrl,String profilePhotoUrl,
                                          String userId, String postId){
        Map<String,Object> postMap = new HashMap<>();
        postMap.put("name",name);
        postMap.put("lastName",lastName);
        postMap.put("type",type);
        postMap.put("likes", "0");
        postMap.put("dislikes","0");
        postMap.put("text",text);
        postMap.put("imageUrl", imageUrl);
        postMap.put("profilePhotoUrl",profilePhotoUrl );
        postMap.put("userId",userId);
        postMap.put("postId", postId);

        return postMap;


    }

    public void FirebaseWriteNewTextPost(final String text){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference  = firebaseDatabase.getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String userId = user.getUid();
        final String newPostId = UUID.randomUUID().toString();

        // Obtener datos del usuario que realiza el post

        final UserBasicData userBasicData = new UserBasicData();
        databaseReference.child("users").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String temporalUserKey = dataSnapshot.getKey();
                if(temporalUserKey.equals(userId)){

                    Map<String,Object> userMap = (Map<String, Object>) dataSnapshot.getValue();

                    String name = (String)userMap.get("name");
                    String lastName = (String)userMap.get("lastName");
                    String profilePhotoUrl = (String)userMap.get("profilePhotoUrl");

                    Map<String, Object> postMap = getPostMap(name, lastName,"text",text,"nullvalue",profilePhotoUrl,userId,newPostId);
                    databaseReference.child("posts").child(userId).child(newPostId).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Se ha realizado la publicación", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "No se ha realizado la publicación", Toast.LENGTH_LONG).show();


                            }
                        }
                    });


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

        Map<String,Object> dummyComment = new HashMap<>();
        dummyComment.put("dummy1", "dummy1");
        dummyComment.put("dummy2", "dummy2");
        databaseReference.child("comments").child(newPostId).child("dummy").setValue(dummyComment);
    }

    public void OnClickButtonMakePost(View view){

        EditText editTextNewPostText = findViewById(R.id.editTextNewPostText);
        String newPostText = editTextNewPostText.getText().toString();
        int radioButton = radioGroup.getCheckedRadioButtonId();
        switch (radioButton){
            case R.id.radioButtonTextOption:


                this.FirebaseWriteNewTextPost(newPostText);
                break;
        }

    }
    public void onClickRadioButton(View view){


        int radioButton = radioGroup.getCheckedRadioButtonId();

        switch (radioButton){

            case R.id.radioButtonVideoOption:
                break;
            case R.id.radioButtonImageOption:

                ImageView imageViewImage2 = findViewById(R.id.imageViewNewPostImage);
                imageViewImage2.setVisibility(View.VISIBLE);

                TextView textViewImageTitle2 = findViewById(R.id.textViewNewPostImageTitle);
                textViewImageTitle2.setVisibility(View.VISIBLE);

                Button buttonSelectImage2 = findViewById(R.id.buttonNewPostPhoto);
                buttonSelectImage2.setVisibility(View.VISIBLE);

                TextView textViewVideoTitle2 = findViewById(R.id.textViewNewPostVideoTitle);
                textViewVideoTitle2.setVisibility(View.INVISIBLE);

                break;
            case R.id.radioButtonTextOption:
                ImageView imageViewImage = findViewById(R.id.imageViewNewPostImage);
                imageViewImage.setVisibility(View.INVISIBLE);

                TextView textViewImageTitle = findViewById(R.id.textViewNewPostImageTitle);
                textViewImageTitle.setVisibility(View.INVISIBLE);

                Button buttonSelectImage = findViewById(R.id.buttonNewPostPhoto);
                buttonSelectImage.setVisibility(View.INVISIBLE);

                TextView textViewVideoTitle = findViewById(R.id.textViewNewPostVideoTitle);
                textViewVideoTitle.setVisibility(View.INVISIBLE);

                break;

        }

    }
}
