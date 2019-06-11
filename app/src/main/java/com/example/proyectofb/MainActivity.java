package com.example.proyectofb;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private int SingUpCode = 4;
    int RC_SIGN_IN = 100;
    private int signUpCode = 5;
    boolean googleUserExists = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignIn();
            }
        });


    }


    public void GoogleSignIn(){

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        if(requestCode == this.signUpCode){
            if(resultCode == Activity.RESULT_OK){

                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                this.googleUserExists = true;
                this.mGoogleSignInClient.signOut();
            }
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.


            String email = account.getEmail();
            this.FirebaseSingUp(email,"123456");


            Toast.makeText(getApplicationContext(), "Login Exitoso" ,Toast.LENGTH_LONG).show();


        } catch (ApiException e) {

            Log.d("CHES", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(), "No fue posible iniciar sesion.",Toast.LENGTH_LONG).show();

        }
    }

    public void FirebaseSingUp(final String email, String password){

        this.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "Nuevo usuario registrado con exito.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ProfileSettingsActivity.class);
                    intent.putExtra("origin","signUp");
                    startActivityForResult(intent,signUpCode);
                }
                else{
                    googleUserExists = true;
                    Toast.makeText(getApplicationContext(), "No fue posible registrar el nuevo usuario.",Toast.LENGTH_LONG).show();
                    FirebaseSingIn(email, "123456");
                }

            }
        });
    }





}
