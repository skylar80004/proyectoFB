package com.example.proyectofb;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class PhotosActivity extends AppCompatActivity {


    private int GALLERY_REQUEST = 100;
    Uri uploadPhoto;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        Toast.makeText(getApplicationContext(),"Cargando Fotos...", Toast.LENGTH_LONG).show();


        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        if(from.equals("currentProfile")){

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            String userId = firebaseUser.getUid();

            viewPager = findViewById(R.id.viewPagerPhotos);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
            viewPagerAdapter.setUserId(userId);
            viewPager.setAdapter(viewPagerAdapter);


        }
        else{
            String userId = intent.getStringExtra("userId");
            viewPager = findViewById(R.id.viewPagerPhotos);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
            viewPagerAdapter.setUserId(userId);
            viewPager.setAdapter(viewPagerAdapter);

        }

        uploadPhoto = null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return false;
    }


    public void OnClickButtonUploadPhoto(View view){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);


    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        result = result.replace(".","a");
        return result;
    }



    public void FirebaseUploadPhoto(){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getInstance().getCurrentUser();
        final String id = firebaseUser.getUid();

        final String name = this.getFileName(this.uploadPhoto);
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference photoReference = mStorageReference.child(id).child(name);
        photoReference.putFile(this.uploadPhoto).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Se subió la imagen", Toast.LENGTH_LONG).show();
                    photoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = uri.toString();
                            DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
                            mDataBase.child("photos").child(id).child(name).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == this.GALLERY_REQUEST){

                Uri selectedImage = data.getData();
                this.uploadPhoto = selectedImage;
                this.FirebaseUploadPhoto();

            }
        }
    }



}
