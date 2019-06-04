package com.example.proyectofb;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class NewPostActivity extends AppCompatActivity {



    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        this.radioGroup = findViewById(R.id.radioGroupNewPost);
        RadioButton radioButtonText =  findViewById(R.id.radioButtonTextOption);
        radioButtonText.setChecked(true);
        this.setInitialRadioButton();

    }


    public void setInitialRadioButton(){

        ImageView imageViewImage = findViewById(R.id.imageViewNewPostImage);
        imageViewImage.setVisibility(View.INVISIBLE);
        TextView textViewImageTitle = findViewById(R.id.textViewNewPostImageTitle);
        textViewImageTitle.setVisibility(View.INVISIBLE);
        Button buttonSelectImage = findViewById(R.id.buttonNewPostPhoto);
        buttonSelectImage.setVisibility(View.INVISIBLE);
        TextView textViewVideoTitle = findViewById(R.id.textViewNewPostVideoTitle);
        textViewVideoTitle.setVisibility(View.INVISIBLE);

    }

    public void OnClickButtonMakePost(View view){

        EditText editTextNewPostText = findViewById(R.id.editTextNewPostText);
        String newPostText = editTextNewPostText.getText().toString();

        int radioButton = radioGroup.getCheckedRadioButtonId();
        switch (radioButton){
            case R.id.radioButtonTextOption:
                Toast.makeText(this,newPostText,Toast.LENGTH_LONG).show();
                break;
        }

    }
    public void onClickRadioButton(View view){


        int radioButton = radioGroup.getCheckedRadioButtonId();

        switch (radioButton){

            case R.id.radioButtonVideoOption:
                break;
            case R.id.radioButtonImageOption:

                ImageView imageViewImage2 = findViewById(R.id.imageViewNewPostImage);
                imageViewImage2.setVisibility(View.VISIBLE);

                TextView textViewImageTitle2 = findViewById(R.id.textViewNewPostImageTitle);
                textViewImageTitle2.setVisibility(View.VISIBLE);

                Button buttonSelectImage2 = findViewById(R.id.buttonNewPostPhoto);
                buttonSelectImage2.setVisibility(View.VISIBLE);

                TextView textViewVideoTitle2 = findViewById(R.id.textViewNewPostVideoTitle);
                textViewVideoTitle2.setVisibility(View.INVISIBLE);

                break;
            case R.id.radioButtonTextOption:
                ImageView imageViewImage = findViewById(R.id.imageViewNewPostImage);
                imageViewImage.setVisibility(View.INVISIBLE);

                TextView textViewImageTitle = findViewById(R.id.textViewNewPostImageTitle);
                textViewImageTitle.setVisibility(View.INVISIBLE);

                Button buttonSelectImage = findViewById(R.id.buttonNewPostPhoto);
                buttonSelectImage.setVisibility(View.INVISIBLE);

                TextView textViewVideoTitle = findViewById(R.id.textViewNewPostVideoTitle);
                textViewVideoTitle.setVisibility(View.INVISIBLE);

                break;

        }

    }
}
