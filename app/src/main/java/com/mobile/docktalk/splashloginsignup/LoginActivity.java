package com.mobile.docktalk.splashloginsignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.docktalk.MainActivity;
import com.mobile.docktalk.R;
import com.mobile.docktalk.apiconsumption.UtilityController;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edUsername, edPassword;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_app);
        edUsername = (EditText) findViewById(R.id.et_login_user_name);
        edPassword = (EditText) findViewById(R.id.et_login_password);
        btnLogin = (Button) findViewById(R.id.btn_app_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                GetTokenAsync getTokenAsync = new GetTokenAsync();
                getTokenAsync.execute(new String[]{username,password});
            }
        });
    }

    private class GetTokenAsync extends AsyncTask<String,Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            token = UtilityController.getTokenMobile(strings[0],strings[1]);
            saveUserInfoInDevice("Token",token);
            String userId = UtilityController.getUserTokenInfo(token);
            if(userId != null){
                return userId;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String userId) {
            super.onPostExecute(userId);
            // save userId in sharepreference
            saveUserInfoInDevice("UserId",userId);
            // start new activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void saveUserInfoInDevice(String key, String userId){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,userId);
        editor.commit();
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
