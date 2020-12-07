package com.nemocorp.cv19.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nemocorp.cv19.R;

public class Splash extends AppCompatActivity {

    int TIME_OUT=1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, TIME_OUT);
        ImageView logo=findViewById(R.id.imageView);
        Animation anim_logo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        logo.startAnimation(anim_logo);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if(!MainActivity.seen)MainActivity.seen=true;
        else
            finish();
    }
}
