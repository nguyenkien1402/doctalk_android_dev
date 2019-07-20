package com.mobile.docktalk.splashloginsignup;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.databinding.DataBindingUtil;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mobile.docktalk.R;
import com.mobile.docktalk.databinding.ActivityPhoneConfirmBinding;
import com.mobile.docktalk.databinding.ActivityRegisterPatientBinding;

public class RegisterPatientActivity extends AppCompatActivity {

    ActivityRegisterPatientBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_patient);
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
