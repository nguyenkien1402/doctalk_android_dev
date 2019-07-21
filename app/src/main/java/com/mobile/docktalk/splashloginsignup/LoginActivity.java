package com.mobile.docktalk.splashloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.docktalk.FirebaseUploadDemo;
import com.mobile.docktalk.R;
import com.mobile.docktalk.databinding.ActivityLoginAppBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_app);

    }

    public void appLogin(View view){
        startActivity(new Intent(this, FirebaseUploadDemo.class));
    }
    public void signup(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}
