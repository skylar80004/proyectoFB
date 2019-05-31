package com.example.proyectofb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private int SingUpCode = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    public void OnClickButtonSignUp(View view){

        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivityForResult(intent,this.SingUpCode);


    }
    public void OnClickButtonSignIn(View view){

        EditText editTextEmail = findViewById(R.id.editTextLoginEmail);
        EditText editTextPassword = findViewById(R.id.editTextLoginPassword);
        String email = editTextEmail.getText().toString();
        String password  = editTextPassword.getText().toString();
        FirebaseSingIn(email,password);


        //Intent intent = new Intent(getApplicationContext(), TabHomeActivity.class);
        //startActivity(intent);
    }

    public void OnClickResetPassword(View view){

        EditText editTextEmail = findViewById(R.id.editTextLoginEmail);
        String email = editTextEmail.getText().toString();
        FirebaseResetPassword(email);


    }

    public void FirebaseSingUp(String email, String password){

        this.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "Nuevo usuario registrado con exito.",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "No fue posible registrar el nuevo usuario.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void FirebaseResetPassword(String email){

        this.mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "Se ha enviado un correo electronico para recuperar la contraseña",Toast.LENGTH_LONG).show();

                }
            }
        });

    }
    public void FirebaseSingIn(String email, String password){

        this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login Exitoso" ,Toast.LENGTH_LONG).show();
                    Intent intent  = new Intent(getApplicationContext(),TabHomeActivity.class);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(getApplicationContext(), "No fue posible iniciar sesion.",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        
    }

}
