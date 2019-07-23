package com.mobile.docktalk.splashloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.databinding.DataBindingUtil;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mobile.docktalk.R;
import com.mobile.docktalk.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    EditText edUsername, edEmail, edPasssword, edConfirmPassword, edPhoneNumber;
    Button btnPhoneNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R .layout.activity_signup);
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
