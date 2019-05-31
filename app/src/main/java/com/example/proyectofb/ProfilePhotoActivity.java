package com.example.proyectofb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfilePhotoActivity extends AppCompatActivity {

    int GALLERY_REQUEST = 100;
    private Uri profilePhotoUri;
    private StorageReference mStorageReference;
    private String profilePhotoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);
        this.profilePhotoUrl = "";
        this.profilePhotoUri = null;
        this.mStorageReference = FirebaseStorage.getInstance().getReference();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
        onBackPressed();
        return false;
    }


    public void OnClickButtonUploadPhoto(View view){

        if(this.profilePhotoUri != null){

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = auth.getInstance().getCurrentUser();
            final String id = firebaseUser.getUid();

            final StorageReference photoReference = this.mStorageReference.child(id).child("profilePhoto");
            photoReference.putFile(this.profilePhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    photoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profilePhotoUrl = uri.toString();
                            // Actualizar perfil
                            DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
                            mDataBase.child("users").child(id).child("profilePhotoUrl").setValue(profilePhotoUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        Toast.makeText(getApplicationContext(),"Foto de perfil actualizada", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            mDataBase.child("photos").child(id).child("profilePhotoUrl").setValue(profilePhotoUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), " Debe primero seleccionar una imagn de la galeria",Toast.LENGTH_LONG).show();
            return;
        }





    }
    public void OnClickButtonSelectPhoto(View view){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == this.GALLERY_REQUEST){
                Uri selectedImage = data.getData();
                this.profilePhotoUri = selectedImage;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ImageView imageView = findViewById(R.id.imageViewPhotoProfileUpdate);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    //Log.i("TAG", "Some exception " + e);
                }
            }
        }
    }
}
