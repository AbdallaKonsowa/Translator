package com.example.api.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.example.api.R;
import com.example.api.Main.Translation;

public class Splash extends AppCompatActivity {
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        animationView=findViewById(R.id.Start_animation);
        animationView.playAnimation();
        splashTimer();

    }
    public void splashTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i =new Intent(Splash.this, Translation.class);
                startActivity(i);
                finish();

            }
        }, 4700);
    }
}