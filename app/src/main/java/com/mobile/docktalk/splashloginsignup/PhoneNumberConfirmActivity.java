package com.mobile.docktalk.splashloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.databinding.DataBindingUtil;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mobile.docktalk.R;
import com.mobile.docktalk.databinding.ActivityPhoneConfirmBinding;

public class PhoneNumberConfirmActivity extends AppCompatActivity {

    ActivityPhoneConfirmBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_confirm);
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
