package com.example.proyectofb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ViewPagerAdapter extends PagerAdapter{




    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Bitmap> bitmapList = new ArrayList<>();
    private int position = 0 ;
    private boolean fromFullScreen = false;
    private boolean validateFull = false;
    String userId;

    public ViewPagerAdapter(Context context){
        this.context = context;
        this.fromFullScreen = false;
    }
    public ViewPagerAdapter(Context context, int position){
        this.context = context;
        this.position = position;
        this.fromFullScreen = true;
        this.FireBaseGetAllPhotos();

    }

    public void setUserId(String userId){
        this.userId = userId;
        this.FireBaseGetAllPhotos();
    }

    @Override
    public int getCount() {
        return this.bitmapList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        if(this.fromFullScreen){
            this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custom_layout,null);
            ImageView imageView = view.findViewById(R.id.imageViewCustomLayout);
            imageView.setImageBitmap(this.bitmapList.get(this.position));
            ViewPager viewPager = (ViewPager)container;
            viewPager.addView(view,0);
            this.fromFullScreen = false;
            validateFull = true;

            return view;
        }
        else if(!this.fromFullScreen && validateFull){

            this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custom_layout,null);
            ImageView imageView = view.findViewById(R.id.imageViewCustomLayout);
            imageView.setImageBitmap(this.bitmapList.get(position));
            ViewPager viewPager = (ViewPager)container;
            viewPager.addView(view,0);
            this.fromFullScreen = false;
            return view;


        }
        else{

            this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custom_layout,null);
            ImageView imageView = view.findViewById(R.id.imageViewCustomLayout);
            imageView.setImageBitmap(this.bitmapList.get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context,FullScreenPhotosActivity.class);
                    context.startActivity(intent);
                    intent.putExtra("position",position);
                    context.startActivity(intent);


                }
            });

            ViewPager viewPager = (ViewPager)container;
            viewPager.addView(view,0);
            return view;

        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);

    }

    public void FireBaseGetAllPhotos(){



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String id =  firebaseUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("photos");
        databaseRef.child(id).orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                String value = (String)dataSnapshot.getValue();
                ImageDownloader imageDownloader = new ImageDownloader();

                try {
                    Bitmap bitmap = imageDownloader.execute(value).get();
                    bitmapList.add(bitmap);
                    notifyDataSetChanged();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
