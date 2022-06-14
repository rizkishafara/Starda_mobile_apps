package com.example.stardaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private int load_time=4000;
    SharedPreferences sharedPreferences;
    String resultNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        resultNama = sharedPreferences.getString("result_nama",null);

        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(resultNama != null) {
                    Intent home = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(home);
                    finish();
                }else {
                    Intent login = new Intent(SplashScreen.this, com.example.stardaapp.LoginActivity.class);
                    startActivity(login);
                    finish();
                }

            }
        },load_time);
    }
}