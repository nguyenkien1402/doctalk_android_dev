package com.mobile.docktalk.login_signup_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.R;

public class SplashAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_app);
    }

    public void  login(View view)
    {
        startActivity(new Intent(this, LoginActivity.class));
    }


    public void  getStarted(View view)
    {
        startActivity(new Intent(this,SignupActivity.class));
    }

}
