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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mobile.R;
import com.mobile.docktalk.apis_controller.AccountController;
import com.mobile.docktalk.apis_controller.UtilityController;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class PhoneNumberConfirmActivity extends AppCompatActivity {

    Button btnNext;
    EditText edPhoneVerify;
    FirebaseAuth firebaseAuth;
    String mVerificationId, email, password, username,phoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_confirm);
        btnNext = (Button) findViewById(R.id.btn_phone_next_login);
        edPhoneVerify = (EditText) findViewById(R.id.ed_phone_confirm_code);
        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phonenumber");
        email = bundle.getString("email");
        password = bundle.getString("password");
        username = bundle.getString("username");
        firebaseAuth = FirebaseAuth.getInstance();

        // send verification code
        sendVerificationPhoneNumberCode(phoneNumber);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edPhoneVerify.getText().toString();
                verifyVerficationCode(code);
            }
        });
    }

    public void sendVerificationPhoneNumberCode(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+61"+phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacls
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacls = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                edPhoneVerify.setText(code);
                verifyVerficationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
    };

    private void verifyVerficationCode(String code) {
        // verify code
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential){
        // First, sign in with phone number
        // firebaseAuth.signInWithCredential(credential);
        if(credential != null){
            // Now sign in with email and password to database
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d("Firebase","Add new user to firebase successfully");
                                // Link current user to phonenumber credential
                                firebaseAuth.getCurrentUser().linkWithCredential(credential)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Log.d("Firebase","Link phone number credential successfully");
                                                addNewUserToDatabase();
                                            }
                                        });
                            }
                        }
                    });
        }

    }

    /*
    Add new user to main database
    and simultaneously start new activity
     */
    private void addNewUserToDatabase() {
        // API for adding new user to database
        UserSignupAsyn userSignupAsyn = new UserSignupAsyn();
        userSignupAsyn.execute();
    }

    private class UserSignupAsyn extends AsyncTask<Void, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result = AccountController.signup(email,username,password,phoneNumber);
            String token = UtilityController.getTokenMobile(username,password);
            try{
                result.put("Token",token);
            }catch (Exception e){
                Log.d("Error","Parsing error");
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(jsonObject != null){
                // Start a new Activity to Patient Signup
                try {
                    Intent intent = new Intent(getApplicationContext(), RegisterPatientActivity.class);
                    intent.putExtra("Id",jsonObject.getString("id"));
                    intent.putExtra("Token",jsonObject.getString("Token"));
                    //Save on Sharepreferences
                    saveUserInfoInDevice(jsonObject.getString("id"), jsonObject.getString("Token"));
                    startActivity(intent);
                }catch (Exception e){
                    Log.d("Error",e.getMessage());
                }
            }
        }
    }

    private void saveUserInfoInDevice(String userId, String token){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserId",userId);
        editor.putString("Token",token);
        editor.commit();
    }


    public void registerPatient(View view){
        startActivity(new Intent(this,RegisterPatientActivity.class));
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
