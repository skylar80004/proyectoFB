package com.example.proyectofb;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderPost> {


    ArrayList<Post> postList;

    public PostAdapter(){
        this.postList = new ArrayList<>();

    }

    public void UpdatePost(String id,Post post){

        Post tempPost;
        for(int i = 0 ; i < this.postList.size();i++){
            tempPost = postList.get(i);
            String temporalPostid = tempPost.getPostId();
            if(id.equals(temporalPostid)){
                this.postList.set(i,post);
                this.notifyItemChanged(i);
                return;
            }
        }
    }
    public void AddPost(Post post){
        this.postList.add(post);
        this.notifyDataSetChanged();
    }

    public void clearItems(){
        this.postList.clear();
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_layout,viewGroup,false);
        return new ViewHolderPost(view);

    }




    public void FirebaseLikePost(final String userId, final String postId, final Context context){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("posts").child(userId).orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();
                if(key.equals(postId)){

                    Map<String, Object> postMap = (Map<String, Object>)dataSnapshot.getValue();
                    String likes = (String)postMap.get("likes");
                    int newLikes = Integer.parseInt(likes);
                    newLikes++;
                    postMap.put("likes",String.valueOf(newLikes));

                    databaseReference.child("posts").child(userId).child(postId).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                               // Toast.makeText(context, "Se dio like al post", Toast.LENGTH_LONG).show();
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


    public void FirebaseDislikePost(final String userId, final String postId, final Context context){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("posts").child(userId).orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();
                if(key.equals(postId)){

                    Map<String, Object> postMap = (Map<String, Object>)dataSnapshot.getValue();
                    String likes = (String)postMap.get("dislikes");
                    int newLikes = Integer.parseInt(likes);
                    newLikes++;
                    postMap.put("dislikes",String.valueOf(newLikes));

                    databaseReference.child("posts").child(userId).child(postId).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                           //     Toast.makeText(context, "Se dio dislike al post", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost viewHolderUserPost, int i) {

        String name = this.postList.get(i).getUserName() + this.postList.get(i).getLastName();
        viewHolderUserPost.textViewPostUserName.setText(name);
        viewHolderUserPost.textViewPostText.setText(this.postList.get(i).getPostText());
        viewHolderUserPost.textViewPostLikes.setText(this.postList.get(i).getLikes());
        viewHolderUserPost.textViewPostDislikes.setText(this.postList.get(i).getDislikes());

        final int position = i;
        viewHolderUserPost.imageButtonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = postList.get(position).getUserId();
                String postId = postList.get(position).getPostId();
                FirebaseLikePost(userId,postId,v.getContext());

            }
        });

        viewHolderUserPost.imageButtonDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String userId = postList.get(position).getUserId();
                String postId = postList.get(position).getPostId();
                FirebaseDislikePost(userId,postId,v.getContext());
                
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.postList.size();
    }

    public class ViewHolderPost extends RecyclerView.ViewHolder  {


        TextView textViewPostUserName;
        TextView textViewPostText;
        ImageButton imageButtonLike;
        ImageButton imageButtonDislike;
        Button buttonComments;
        TextView textViewPostLikes;
        TextView textViewPostDislikes;
        public ViewHolderPost(@NonNull View itemView) {
            super(itemView);

            textViewPostUserName = itemView.findViewById(R.id.textViewPostUserName);
            textViewPostText = itemView.findViewById(R.id.textViewPostText);

            imageButtonLike = itemView.findViewById(R.id.imageButtonLike);
            imageButtonDislike =  itemView.findViewById(R.id.imageButtonDislike);

            textViewPostLikes = itemView.findViewById(R.id.textViewPostLikes);
            textViewPostDislikes = itemView.findViewById(R.id.textViewPostDislikes);






        }



    }
}