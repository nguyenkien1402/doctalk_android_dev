package com.mobile.docktalk.login_signup_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.docktalk.R;

public class SignupActivity extends AppCompatActivity {
    EditText edUsername, edEmail, edPasssword, edConfirmPassword, edPhoneNumber;
    Button btnPhoneNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R .layout.activity_signup);
        setContentView(R .layout.activity_signup);
        edUsername = (EditText) findViewById(R.id.signup_username);
        edEmail = (EditText) findViewById(R.id.signup_email_address);
        edPasssword = (EditText) findViewById(R.id.signup_password);
        edConfirmPassword = (EditText) findViewById(R.id.signup_confirm_password);
        edPhoneNumber = (EditText) findViewById(R.id.signup_phonenumber);
        btnPhoneNext = (Button) findViewById(R.id.btn_singup_phone_next);

        btnPhoneNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, PhoneNumberConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username",edUsername.getText().toString());
                bundle.putString("email",edEmail.getText().toString());
                bundle.putString("password", edPasssword.getText().toString());
                bundle.putString("phonenumber",edPhoneNumber.getText().toString());
                intent.putExtras(bundle);

                // Save email to Sharepreferences
                SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Email",edEmail.getText().toString());
                editor.commit();
                startActivity(intent);
            }
        });
    }


    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
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
