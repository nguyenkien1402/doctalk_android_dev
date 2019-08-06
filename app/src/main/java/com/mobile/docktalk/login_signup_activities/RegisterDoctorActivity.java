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

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.R;
import com.mobile.docktalk.apis_controller.AccountController;
import com.mobile.docktalk.utilities.SavingLocalData;

import org.json.JSONObject;

public class RegisterDoctorActivity extends AppCompatActivity {

    Button btnDoctorNext;
    EditText edDoctorTitle, edDoctorPreferName, edDoctorHospital, edDoctorAddress,edDoctorSuburb, edDoctorState, edDoctorPostCode;
    String userId, token;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);
        edDoctorAddress = (EditText) findViewById(R.id.doctor_address);
        edDoctorTitle = (EditText) findViewById(R.id.doctor_title);
        edDoctorPreferName = (EditText) findViewById(R.id.doctor_prefer_name);
        edDoctorHospital = (EditText) findViewById(R.id.doctor_hospital);
        edDoctorSuburb = (EditText) findViewById(R.id.doctor_suburb);
        edDoctorState = (EditText) findViewById(R.id.doctor_state);
        edDoctorPostCode = (EditText) findViewById(R.id.doctor_postcode);
        btnDoctorNext = (Button) findViewById(R.id.btn_doctor_signup);

        // Get userId and token from Sharepreferences
        final SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
        userId = pref.getString("UserId",null);
        token = pref.getString("Token", null);


        //get the Patient Information

        btnDoctorNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edDoctorAddress.getText().toString();
                String title = edDoctorTitle.getText().toString();
                String preferName = edDoctorPreferName.getText().toString();
                String hospital = edDoctorHospital.getText().toString();
                String suburb = edDoctorSuburb.getText().toString();
                String state = edDoctorState.getText().toString();
                String postcode = edDoctorPostCode.getText().toString();
                try {
                    JSONObject doctor = new JSONObject();
                    doctor.put("UserId",userId);
                    doctor.put("Title",title);
                    doctor.put("FirstName",pref.getString("FirstName", null));
                    doctor.put("LastName",pref.getString("LastName", null));
                    doctor.put("PreferName",title + "."+preferName);
                    doctor.put("ClinicName",hospital);
                    doctor.put("ClinicAddress",address);
                    doctor.put("ClinicSuburb",suburb);
                    doctor.put("ClinicState",state);
                    doctor.put("ClinicPostCode",postcode);
                    GetTokenAsyn getTokenAsyn = new GetTokenAsyn();
                    getTokenAsyn.execute(doctor);
                }catch (Exception e){
                    Log.d("Erorr",e.getMessage());
                }

            }
        });
    }

    private class GetTokenAsyn extends AsyncTask<JSONObject, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            JSONObject user = jsonObjects[0];
            if(token != null){
                JSONObject jsonDoctor = AccountController.registerAsDoctor(token, user);
                return jsonDoctor;
            }else{
                Log.d("Token","Token is null");
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                Log.d("doctor", jsonObject.toString());
                int doctorId = jsonObject.getInt("doctorId");
                SavingLocalData.saveInSharePreferences(getApplicationContext(),"UserInfo","DoctorId",doctorId);
                Intent intent = new Intent(getApplicationContext(),AddingDoctorProfessionalActivity.class);
                intent.putExtra("DoctorId",doctorId);
                startActivity(intent);
            }catch (Exception e){
                Log.d("Error","Cannot get the JSON attribute");
            }
        }
    }
    public void doctorDetail(View view){
        startActivity(new Intent(this,RegisterDoctorDetailActivity.class));
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
