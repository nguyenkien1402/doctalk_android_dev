package com.mobile.docktalk.splashloginsignup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.databinding.DataBindingUtil;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mobile.docktalk.R;
import com.mobile.docktalk.apiconsumption.ProfessionalController;
import com.mobile.docktalk.databinding.ActivityRegisterDoctorDetailBinding;
import com.mobile.docktalk.model.Professional;

import java.util.List;

public class RegisterDoctorDetailActivity extends AppCompatActivity {

    ActivityRegisterDoctorDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_doctor_detail);

        // get list professional
        GetProfessionalListAsyn getProfessionalListAsyn = new GetProfessionalListAsyn();
        getProfessionalListAsyn.execute();
    }

    private class GetProfessionalListAsyn extends AsyncTask<Void, Void, List<Professional>>{
        @Override
        protected List<Professional> doInBackground(Void... voids) {
            List<Professional> professionals = ProfessionalController.getProfessionalList();
            return professionals;
        }

        @Override
        protected void onPostExecute(List<Professional> professionals) {
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
