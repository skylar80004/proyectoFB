package com.example.proyectofb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class FullScreenPhotosActivity extends AppCompatActivity {


    ViewPager viewPager;
    int position;
    int size = 0;
    ArrayList<Bitmap> bitmapList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_screen_photos);


        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);

        viewPager = findViewById(R.id.viewPagerFullScreen);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,position);
        viewPager.setAdapter(viewPagerAdapter);
    }
}
