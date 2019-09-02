package com.inc.ocapop;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class FullscreenReceitaActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_receita);

        ImageView imageView = (ImageView) findViewById(R.id.img_full);

        String path = getIntent().getStringExtra("img");

        Glide.with(this).load(path).into(imageView);


    }

}
