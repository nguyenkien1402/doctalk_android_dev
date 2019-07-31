package com.mobile.docktalk.login_signup_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.docktalk.app_activities.MainActivity;
import com.mobile.docktalk.R;
import com.mobile.docktalk.apis_controller.UtilityController;

import org.json.JSONObject;

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

    private class GetTokenAsync extends AsyncTask<String,Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            token = UtilityController.getTokenMobile(strings[0],strings[1]);
            saveUserInfoInDevice("Token",token);
            JSONObject result = UtilityController.getUserTokenInfo(token);
            if(result != null){
                return result;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            // save userId in sharepreference
            if(result != null){
                try{
                    String userId = result.getString("sub");
                    String email = result.getString("email");
                    saveUserInfoInDevice("UserId",userId);
                    saveUserInfoInDevice("Email",email);
                    signinWithFirebase(email,edPassword.getText().toString());
                }catch (Exception e){
                    Log.d("Error",e.getMessage());
                }
            }else{
                Toast.makeText(getApplicationContext(),"Incorrect email or password",Toast.LENGTH_SHORT).show();
            }

            // start new activity

        }
    }

    private void signinWithFirebase(String email, String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"Cannot login to Firebase server",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
