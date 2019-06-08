package com.example.proyectofb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofb.ui.main.CommentsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommentsActivity extends AppCompatActivity {

    String postId;
    String userId;
    String type;

    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentsAdapter = new CommentsAdapter();
        RecyclerView recyclerViewComments = findViewById(R.id.recyclerViewComments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComments.setAdapter(this.commentsAdapter);

        Intent intent  = getIntent();
        type = intent.getStringExtra("type");
        if(type.equals("post")){
            postId = intent.getStringExtra("postId");
        }

        this.FirebaseGetAllComments();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onSupportNavigateUp(){
        
        onBackPressed();
        return false;
    }




    public void FirebaseGetAllComments(){


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        if(type.equals("post")){

            databaseReference.child("comments").child(this.postId).orderByKey().addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String key = dataSnapshot.getKey();
                    if(!key.equals("dummy")){

                        Map<String,Object> commentMap = (Map<String, Object>)dataSnapshot.getValue();

                        String userName = (String)commentMap.get("name");
                        String text = (String)commentMap.get("text");
                        Comment comment = new Comment(userName,text);
                        commentsAdapter.addComment(comment);

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
        else{

        }

    }
    public void FirebaseWriteNewComment(final String text, final String userId, final String postId){


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String currentUserId = user.getUid();
        databaseReference.child("users").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();
             //   Toast.makeText(getApplicationContext(), key,Toast.LENGTH_LONG).show();


                if(key.equals(currentUserId)){

                    Map<String,Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                    String name = (String)userMap.get("name");

                    Map<String,Object> commentMap = new HashMap<>();
                    commentMap.put("text",text);
                    commentMap.put("name",name);

                    String commentId = UUID.randomUUID().toString();
                    databaseReference.child("comments").child(postId).child(commentId).setValue(commentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Se agregó el comentario",Toast.LENGTH_LONG).show();
                            }
                            else {

                                Toast.makeText(getApplicationContext(), "No Se agregó el comentario",Toast.LENGTH_LONG).show();


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



    }
    public void OnClickButtonMakeComment(View view){

        EditText editTextCommentText = findViewById(R.id.editTextCommentText);
        String commentText = editTextCommentText.getText().toString();
        this.FirebaseWriteNewComment(commentText,this.userId,this.postId);


    }
}
