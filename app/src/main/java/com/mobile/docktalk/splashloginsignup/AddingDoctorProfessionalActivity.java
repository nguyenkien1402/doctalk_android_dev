package com.mobile.docktalk.splashloginsignup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mobile.docktalk.R;
import com.mobile.docktalk.apiconsumption.DoctorController;
import com.mobile.docktalk.apiconsumption.UtilityController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class AddingDoctorProfessionalActivity extends AppCompatActivity {

    TextInputEditText doctor_detail_form_ps;
    EditText doctor_selection;
    Button btn_add_doctor_professional;
    String token = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor_professional);

        doctor_detail_form_ps = (TextInputEditText) findViewById(R.id.doctor_detail_form_ps);
        btn_add_doctor_professional = (Button) findViewById(R.id.btn_add_doctor_professional);
        doctor_selection = (EditText) findViewById(R.id.doctor_selection);
        token = UtilityController.getTokenMobile("bob","Pass123$");

        GetProfessionalList getProfessionalList = new GetProfessionalList();
        getProfessionalList.execute(token);

        btn_add_doctor_professional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = doctor_selection.getText().toString();
                List<String> s = Arrays.asList(selection.split(","));
                AddProfessionalList addProfessionalList = new AddProfessionalList();
                addProfessionalList.execute(s);
            }
        });
    }

    private class AddProfessionalList extends AsyncTask<List<String>,Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(List<String>... lists) {
            List<String> selection = lists[0];
            JSONObject result = DoctorController.addProfessional(token,selection);
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject b) {
            super.onPostExecute(b);
        }
    }

    private class GetProfessionalList extends AsyncTask<String, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(String... strings) {
            String token = strings[0];
            JSONArray jsonArray = DoctorController.getProfessionalList(token);
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            doctor_detail_form_ps.setText(jsonArray.toString());
        }
    }
}
