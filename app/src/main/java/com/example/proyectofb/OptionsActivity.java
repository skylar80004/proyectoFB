package com.example.proyectofb;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OptionsActivity extends AppCompatActivity {

    DatabaseReference myRef;
    int profileOptionsSettingsCode = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return false;
    }

    public void OnClickButtonSettings(View view){

        Intent intent  = new Intent(this,ProfileSettingsActivity.class);
        intent.putExtra("origin","profile");
        startActivityForResult(intent,this.profileOptionsSettingsCode);
    }

    public void OnClickButtonLogOut(View view){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void FireBaseDeleteAcount(){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        deleteInfo(user.getUid());
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Se ha eliminado la cuenta", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void deleteInfo(String uId) {
        myRef = FirebaseDatabase.getInstance().getReference("education").child(uId);
        myRef.removeValue();
        myRef = FirebaseDatabase.getInstance().getReference("friends").child(uId);
        myRef.removeValue();
        myRef = FirebaseDatabase.getInstance().getReference("photos").child(uId);
        myRef.removeValue();
        myRef = FirebaseDatabase.getInstance().getReference("users").child(uId);
        myRef.removeValue();
    }


    public void OnClickButtonDeleteAccount(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro de que desea eliminar su cuenta?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FireBaseDeleteAcount();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == this.profileOptionsSettingsCode){
            if(resultCode == Activity.RESULT_OK){

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }

    }
}
