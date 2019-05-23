package com.example.proyectofb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
    }

    public void OnClickButtonSignUpInitial(View view){


        EditText editTextPass = findViewById(R.id.editTextSignUpPassword);
        EditText editText = findViewById(R.id.editTextSignUpCpnfirmPass);

        String pass, confirmPass;
        pass = editTextPass.getText().toString();
        confirmPass = editTextPass.getText().toString();

        if(pass.equals(confirmPass)){

            EditText editTextEmail = findViewById(R.id.editTextSignUpEmail);
            String email = editTextEmail.getText().toString();
            this.FirebaseSingUp(email,pass);

        }
        else{

            Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void FirebaseSingUp(String email, String password){

        this.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "Nuevo usuario registrado con exito.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ProfileSettingsActivity.class);
                    intent.putExtra("origin","signUp");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No fue posible registrar el nuevo usuario.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
