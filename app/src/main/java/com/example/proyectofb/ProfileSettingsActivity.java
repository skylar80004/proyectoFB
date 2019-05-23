package com.example.proyectofb;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileSettingsActivity extends AppCompatActivity {


    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    String origin = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Intent intent = getIntent();
        this.origin = intent.getStringExtra("origin");
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextProfileEmailSettings);
        editTextEmail.setText( mAuth.getInstance().getCurrentUser().getEmail());

    }





    public void OnClickButtonAddEducation(View view){

        EditText editTextEducation = findViewById(R.id.editTextProfileEducationSettings);
        String education = editTextEducation.getText().toString();
        LinearLayout linearLayoutEducation = findViewById(R.id.linearLayoutEducation);
        TextView textView = new TextView(this);
        textView.setText(education);
        linearLayoutEducation.addView(textView);


    }

    public  Map<String,Object> GetUserMap(String name, String lastName, String city, String birthDate,
                           String gender, String phone, String email){

        Map<String,Object> mapUser  = new HashMap<>();
        mapUser.put("name",name);
        mapUser.put("lastName",lastName);
        mapUser.put("city",city);
        mapUser.put("birthDate",birthDate);
        mapUser.put("gender",gender);
        mapUser.put("phone",phone);
        mapUser.put("email",email);
        return mapUser;


    }
    public void FirebaseWriteNewUser(Map<String,Object> mapUser  ){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser fireBaseUser = mAuth.getCurrentUser();

        //Toast.makeText(getApplicationContext(), email,Toast.LENGTH_LONG).show();

        String id = fireBaseUser.getUid();

        mDataBase.child("users").child(id).setValue(mapUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Usuario Agregado", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"No se pudo agregar el usuario", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void OnClickButtonAddDataToProfile(View view){

        EditText editTextName, editTextLastName,editTextEducation, editTextCity,
                editTextBirthDate, editTextGender, editTextPhone,
                editTextEmail;

        editTextName = findViewById(R.id.editTextProfileNameSettings);
        editTextLastName =  findViewById(R.id.editText2çProfileLastNameSettings);
        editTextCity = findViewById(R.id.editTextProfileCitySettings);
        editTextBirthDate = findViewById(R.id.editTextProfileBirthDateSettings);
        editTextGender = findViewById(R.id.editTextProfileGenderSettings);
        editTextPhone = findViewById(R.id.editTextProfileSettingsPhone);
        editTextEmail = findViewById(R.id.editTextProfileEmailSettings);

        String name = editTextName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String city = editTextCity.getText().toString();
        String birthDate = editTextBirthDate.getText().toString();
        String gender = editTextGender.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();


        if(this.origin.equals("signUp")){

            Toast.makeText(getApplicationContext(),"holaaaaaa", Toast.LENGTH_LONG).show();
            Map<String,Object> userMap = GetUserMap( name,  lastName,  city,  birthDate,
                    gender,  phone,  email);
            this.FirebaseWriteNewUser(userMap);

        }
        else if(origin.equals("profile")){

            //  POST to

        }
        else{
            Toast.makeText(getApplicationContext(),"holaaaaaa", Toast.LENGTH_LONG).show();
        }





    }
}
