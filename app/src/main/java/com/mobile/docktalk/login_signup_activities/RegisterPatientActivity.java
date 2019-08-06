package com.mobile.docktalk.login_signup_activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mobile.R;
import com.mobile.docktalk.apis_controller.AccountController;
import com.mobile.docktalk.app_activities.MainActivity;

import org.json.JSONObject;

public class RegisterPatientActivity extends AppCompatActivity {
    /*
    This Activity do the following task
    First, get the token from the identity server
    Then user the token to sign up as a patient.
     */

    EditText edPatientFirstName, edPatientLastName, edPatientPreferName,
            edPatientAddress, edPatientSuburb, edPatientState, edPatientPostCode;
    String firstName, lastName, preferName, address, suburb, state, postcode;
    Button btnPatientSignup;
    JSONObject user = new JSONObject();
    private String userId = null, token = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);
        edPatientAddress = (EditText)findViewById(R.id.patient_address);
        edPatientFirstName = (EditText)findViewById(R.id.patient_first_name);
        edPatientLastName = (EditText)findViewById(R.id.patient_last_name);
        edPatientPreferName = (EditText)findViewById(R.id.patient_prefer_name);
        edPatientSuburb = (EditText)findViewById(R.id.patient_suburb);
        edPatientState = (EditText)findViewById(R.id.patient_state);
        edPatientPostCode = (EditText)findViewById(R.id.patient_postcode);

        btnPatientSignup = (Button) findViewById(R.id.btn_patient_signup);
        //get json user first
        userId = getIntent().getStringExtra("Id");
        token = getIntent().getStringExtra("Token");


        btnPatientSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Token first
                firstName = edPatientFirstName.getText().toString();
                lastName = edPatientLastName.getText().toString();
                preferName = edPatientPreferName.getText().toString();
                address = edPatientAddress.getText().toString();
                suburb = edPatientSuburb.getText().toString();
                state = edPatientState.getText().toString();
                postcode = edPatientPostCode.getText().toString();

                try {
                    user.put("UserId",userId);
                    user.put("FirstName",firstName);
                    user.put("Paddress", address);
                    user.put("LastName", lastName);
                    user.put("PreferName", preferName);
                    user.put("PostCode",postcode);
                    user.put("Suburb",suburb);
                    user.put("Pstate",state);

                    SignupAsPatient signupAsPatient = new SignupAsPatient();
                    signupAsPatient.execute(user);
                }catch (Exception e){
                    e.getMessage();
                }
            }
        });
    }

    private class SignupAsPatient extends AsyncTask<JSONObject, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            JSONObject patient = jsonObjects[0];
            return AccountController.registerAsPatient(token,patient);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try{
               if(jsonObject != null){
                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                   startActivity(intent);
               }
            }catch (Exception e){
                Log.d("Error",e.getMessage());
            }
        }
    }


//    private class GetTokenAsyn extends AsyncTask<JSONObject, Void, JSONObject>{
//
//        @Override
//        protected JSONObject doInBackground(JSONObject... jsonObjects) {
//            JSONObject user = jsonObjects[0];
//            String token = null;
//            try {
//                token = AccountController.getTokenMobile(user.getString("username"), user.getString("password"));
//            }catch (Exception e){
//                Log.e("Exception","No appropriate key");
//            }
//            if(token != null){
//                JSONObject jsonPatient = AccountController.registerAsPatient(token, user);
//                return jsonPatient;
//            }else{
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            super.onPostExecute(jsonObject);
//            Log.d("patient",jsonObject.toString());
//        }
//    }


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
