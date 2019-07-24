package com.mobile.docktalk.splashloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.docktalk.R;

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
