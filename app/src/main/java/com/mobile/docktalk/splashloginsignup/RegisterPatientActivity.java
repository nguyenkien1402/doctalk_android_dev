package com.mobile.docktalk.splashloginsignup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.databinding.DataBindingUtil;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mobile.docktalk.R;
import com.mobile.docktalk.apiconsumption.AccountController;
import com.mobile.docktalk.databinding.ActivityRegisterPatientBinding;

import org.json.JSONObject;

public class RegisterPatientActivity extends AppCompatActivity {
    /*
    This Activity do the following task
    First, get the token from the identity server
    Then user the token to sign up as a patient.
     */

    ActivityRegisterPatientBinding binding;
    EditText edPatientFirstName, edPatientLastName, edPatientPreferName,
            edPatientAddress, edPatientSuburb, edPatientState, edPatientPostCode;
    String firstName, lastName, preferName, address, suburb, state, postcode;
    Button btnPatientSignup;
    String mobile_token;
    JSONObject user = new JSONObject();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_patient);
        edPatientAddress = (EditText)findViewById(R.id.patient_address);
        edPatientFirstName = (EditText)findViewById(R.id.patient_first_name);
        edPatientLastName = (EditText)findViewById(R.id.patient_last_name);
        edPatientPreferName = (EditText)findViewById(R.id.patient_prefer_name);
        edPatientSuburb = (EditText)findViewById(R.id.patient_suburb);
        edPatientState = (EditText)findViewById(R.id.patient_state);
        edPatientPostCode = (EditText)findViewById(R.id.patient_postcode);

        btnPatientSignup = (Button) findViewById(R.id.btn_patient_signup);
        // get json user first
//        try{
//            user = new JSONObject(getIntent().getExtras().getString("user"));
//        }catch (JSONException e){
//            Log.e("Failure JSon","Failed to parse the JSON");
//        }


        btnPatientSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Token first
//                firstName = edPatientFirstName.getText().toString();
//                lastName = edPatientLastName.getText().toString();
//                preferName = edPatientPreferName.getText().toString();
//                address = edPatientAddress.getText().toString();
//                suburb = edPatientSuburb.getText().toString();
//                state = edPatientState.getText().toString();
//                postcode = edPatientPostCode.getText().toString();
                firstName = "Laolao";
                lastName = "DaoDao";
                preferName = "LaoDao";
                address = "Caulfield";
                suburb = "Caulfield";
                state = "VIC";
                postcode = "3145";
                try {
                    user.put("Id","51a63d74-59d0-4f12-8f0b-fdeccef2f301");
                    user.put("username","llao");
                    user.put("password","Pass123$");
                    user.put("firstname", firstName);
                    user.put("lastname", lastName);
                    user.put("prefername", preferName);
                    user.put("address",address);
                    user.put("suburb",suburb);
                    user.put("state",state);
                    user.put("postcode",postcode);
                    GetTokenAsyn getTokenAsyn = new GetTokenAsyn();
                    getTokenAsyn.execute(user);
                }catch (Exception e){
                    e.getMessage();
                }
            }
        });
    }




    private class GetTokenAsyn extends AsyncTask<JSONObject, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            JSONObject user = jsonObjects[0];
            String token = null;
            try {
                token = AccountController.getTokenMobile(user.getString("username"), user.getString("password"));
            }catch (Exception e){
                Log.e("Exception","No appropriate key");
            }
            if(token != null){
                JSONObject jsonPatient = AccountController.registerAsPatient(token, user);
                return jsonPatient;
            }else{
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Log.d("patient",jsonObject.toString());
        }
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
