package com.example.proyectofb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderPost> {


    public ArrayList<Post> postList;

    public PostAdapter(){
        this.postList = new ArrayList<>();

    }

    public void UpdatePost(String id,Post post){

        Post tempPost;
        int pos = 0;
        for(int i = 0 ; i < this.postList.size();i++){
            tempPost = postList.get(i);
            String temporalPostid = tempPost.getPostId();
            if(id.equals(temporalPostid)){
                this.postList.set(i,post);
                pos = i;

                break;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            this.postList.sort(new Comparator<Post>() {
                @Override
                public int compare(Post o1, Post o2) {

                    return o1.getTotalTime().compareTo(o2.getTotalTime()) * -1;
                }
            });
        }


        this.notifyItemChanged(pos);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AddPost(Post post){

        this.postList.add(post);

            this.postList.sort(new Comparator<Post>() {
                @Override
                public int compare(Post o1, Post o2) {

                    return o1.getTotalTime().compareTo(o2.getTotalTime()) * -1;
                }
            });


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
                String totalTime = (String) postMap.get("totalTime");
                String fileName = (String) postMap.get("fileName");

                String year = (String) postMap.get("year");
                String month = (String) postMap.get("month");
                String day = (String) postMap.get("day");
                String hour = (String) postMap.get("hour");
                String minute = (String) postMap.get("minute");



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
                        likes,disLikes,userId,postId,totalTime,year,month,day,hour,minute,fileName);

                UpdatePost(key,post);
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
                String totalTime = (String) postMap.get("totalTime");
                String fileName = (String) postMap.get("fileName");

                String year = (String) postMap.get("year");
                String month = (String) postMap.get("month");
                String day = (String) postMap.get("day");
                String hour = (String) postMap.get("hour");
                String minute = (String) postMap.get("minute");


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
                        likes,disLikes,userId,postId,totalTime,year,month,day,hour,minute,fileName);

                UpdatePost(key,post);
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


    public String getPostHoursHumanFormat(Post post){

        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        int currentMonth = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int currentDay = now.get(Calendar.DAY_OF_MONTH);
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        String postYearString = post.getYear();
        String postMonthString = post.getMonth();
        String postDayString = post.getDay();
        String postHourString = post.getHour();
        String postMinuteString = post.getMinute();

        int postYear = Integer.valueOf(postYearString);
        int postMonth = Integer.valueOf(postMonthString);
        int postDay = Integer.valueOf(postDayString);
        int postHour = Integer.valueOf(postHourString);
        int postMinute = Integer.valueOf(postMinuteString);

        if(currentYear > postYear){

            int yearsAgo = Math.abs(currentYear - postYear);
            if(yearsAgo == 1){
                return "Hace 1 año";
            }
            else{
                return "Hace " + String.valueOf(yearsAgo) + " años";
            }
        }
        else{

            if(currentMonth > postMonth){

                int monthsAgo = Math.abs(currentMonth - postMonth);
                if(monthsAgo == 1){
                    return "Hace 1 mes";

                }
                else{
                    return "Hace " + String.valueOf(monthsAgo) + " meses";

                }
            }

            else{

                if(currentDay > postDay){


                    int daysAgo = Math.abs(currentDay - postDay);
                    if(daysAgo == 1){
                        return "Ayer";
                    }
                    else{
                        return "Hace " + String.valueOf(daysAgo) + " dias";
                    }
                }
                else{

                    if(currentHour > postHour){

                        int hoursAgo = Math.abs(currentHour - postHour);
                        if(hoursAgo == 1){
                            return "Hace 1 hora";
                        }
                        else{
                            return "Hace " + String.valueOf(hoursAgo) + " horas";
                        }
                    }
                    else{

                        if(currentMinute > postMinute){

                            int minutesAgo = Math.abs(currentMinute - postMinute);
                            if(minutesAgo == 1){
                                return "Hace 1 minuto";
                            }
                            else{
                                return "Hace " + String.valueOf(minutesAgo) + " minutos";
                            }
                        }
                        else{
                            return "Hace un momento";
                        }

                    }


                }

            }
        }


    }



    @Override
    public int getItemCount() {
        return this.postList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost viewHolderUserPost, int i) {

        String name = this.postList.get(i).getUserName() + " " + this.postList.get(i).getLastName();
        viewHolderUserPost.textViewPostUserName.setText(name);
        viewHolderUserPost.textViewPostText.setText(this.postList.get(i).getPostText());
        viewHolderUserPost.textViewPostLikes.setText(this.postList.get(i).getLikes());
        viewHolderUserPost.textViewPostDislikes.setText(this.postList.get(i).getDislikes());
        viewHolderUserPost.imageViewPostUserPhoto.setImageBitmap(this.postList.get(i).getUserPhoto());
        viewHolderUserPost.textViewPostHours.setText(this.getPostHoursHumanFormat(this.postList.get(i)));
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

        viewHolderUserPost.buttonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), CommentsActivity.class);
                intent.putExtra("type","post");
                intent.putExtra("postId", postList.get(position).getPostId() );
                v.getContext().startActivity(intent);

            }
        });

        String type = this.postList.get(i).getType();
        if(type.equals("image")){

            ViewGroup.LayoutParams params = viewHolderUserPost.textViewPostText.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            viewHolderUserPost.textViewPostText.setLayoutParams(params);


            viewHolderUserPost.imageViewPostImage.setVisibility(View.VISIBLE);
            Bitmap bitmap = this.postList.get(i).getPostImage();
            viewHolderUserPost.imageViewPostImage.setImageBitmap(bitmap);

        }

        final String id = this.postList.get(i).getUserId();
        final String postId = this.postList.get(i).getPostId();
        final int pos = i;

        viewHolderUserPost.textViewPostUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                intent.putExtra("userID",id );
                v.getContext().startActivity(intent);

            }
        });

        viewHolderUserPost.imageViewPostUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                intent.putExtra("userID",id );
                v.getContext().startActivity(intent);


            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String currentUser = user.getUid();

        if(!currentUser.equals(id)){
            viewHolderUserPost.buttonDeletePost.setVisibility(View.INVISIBLE);

        }
        else{
            viewHolderUserPost.buttonDeletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseDeletePost(postId,currentUser,pos,v);
                }
            });

        }
    }



    public void FirebaseDeletePost(final String postId, String userId, final int i, final View view){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("posts").child(userId).child(postId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    postList.remove(i);
                    notifyItemRemoved(i);
                    Toast.makeText(view.getContext(), " Se eliminó la publicación",Toast.LENGTH_LONG).show();
                }
            }
        });

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        String fileName = this.postList.get(i).getFileName();
        storageReference.child(userId).child(fileName).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        });

        databaseReference.child(userId).child(fileName).removeValue();
    }


    public class ViewHolderPost extends RecyclerView.ViewHolder  {


        TextView textViewPostUserName;
        TextView textViewPostText;
        TextView textViewPostLikes;
        TextView textViewPostDislikes;
        TextView textViewPostHours;
        ImageButton imageButtonLike;
        ImageButton imageButtonDislike;
        Button buttonComments;
        Button buttonDeletePost;

        ImageView imageViewPostImage;
        ImageView imageViewPostUserPhoto;
        public ViewHolderPost(@NonNull View itemView) {
            super(itemView);

            textViewPostUserName = itemView.findViewById(R.id.textViewPostUserName);
            textViewPostText = itemView.findViewById(R.id.textViewPostText);

            imageButtonLike = itemView.findViewById(R.id.imageButtonLike);
            imageButtonDislike =  itemView.findViewById(R.id.imageButtonDislike);

            textViewPostLikes = itemView.findViewById(R.id.textViewPostLikes);
            textViewPostDislikes = itemView.findViewById(R.id.textViewPostDislikes);

            buttonComments = itemView.findViewById(R.id.buttonPostComments);
            imageViewPostImage = itemView.findViewById(R.id.imageViewPostImage);
            imageViewPostUserPhoto = itemView.findViewById(R.id.imageViewPostUserPhoto);

            textViewPostHours = itemView.findViewById(R.id.textViewPostHours);
            buttonDeletePost = itemView.findViewById(R.id.buttonDeletePost);



        }


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

