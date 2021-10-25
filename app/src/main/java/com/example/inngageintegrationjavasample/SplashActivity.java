package com.example.inngageintegrationjavasample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.inngageintegrationjavasample.libs.InngageIntentService;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        InngageIntentService.startHandleNotifications(this, getIntent());

        startActivity(new Intent(this, MainActivity.class));

    }
}