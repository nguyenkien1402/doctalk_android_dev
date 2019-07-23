package com.mobile.docktalk.splashloginsignup;

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
import androidx.databinding.DataBindingUtil;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mobile.docktalk.R;
import com.mobile.docktalk.apiconsumption.AccountController;
import com.mobile.docktalk.databinding.ActivityRegisterDoctorBinding;

import org.json.JSONObject;

public class RegisterDoctorActivity extends AppCompatActivity {

    ActivityRegisterDoctorBinding binding;
    Button btnDoctorNext;
    EditText edDoctorTitle, edDoctorPreferName, edDoctorHospital, edDoctorAddress,edDoctorSuburb, edDoctorState, edDoctorPostCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_doctor);
        edDoctorAddress = (EditText) findViewById(R.id.doctor_address);
        edDoctorTitle = (EditText) findViewById(R.id.doctor_title);
        edDoctorPreferName = (EditText) findViewById(R.id.doctor_prefer_name);
        edDoctorHospital = (EditText) findViewById(R.id.doctor_hospital);
        edDoctorSuburb = (EditText) findViewById(R.id.doctor_suburb);
        edDoctorState = (EditText) findViewById(R.id.doctor_state);
        edDoctorPostCode = (EditText) findViewById(R.id.doctor_postcode);
        btnDoctorNext = (Button) findViewById(R.id.btn_doctor_signup);

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

                address = "Caulfield";
                title = "Dr";
                preferName = title + "Bob";
                hospital = "Monash";
                suburb = "Clayton";
                state = "VIC";
                postcode = "3146";

                try {
                    JSONObject doctor = new JSONObject();
                    doctor.put("UserId","d31eac6d-6849-49b4-be3c-d05a25a3d8c4");
                    doctor.put("FirstName","bob");
                    doctor.put("LastName","bob");
                    doctor.put("PreferName",preferName);
                    doctor.put("ClinicName",hospital);
                    doctor.put("ClinicAddress",address);
                    doctor.put("ClinicSuburb",suburb);
                    doctor.put("ClinicState",state);
                    doctor.put("ClinicPostCode",postcode);
                    doctor.put("username","bob");
                    doctor.put("password","Pass123$");

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
            String token = null;
            try {
                token = AccountController.getTokenMobile(user.getString("username"), user.getString("password"));
            }catch (Exception e){
                Log.e("Exception","No appropriate key");
            }
            if(token != null){
                JSONObject jsonPatient = AccountController.registerAsDoctor(token, user);
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
