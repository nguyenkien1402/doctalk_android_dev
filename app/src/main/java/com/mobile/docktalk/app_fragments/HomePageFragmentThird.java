package com.mobile.docktalk.app_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.docktalk.R;
import com.mobile.docktalk.login_signup_activities.RegisterDoctorActivity;
import com.mobile.docktalk.login_signup_activities.RegisterDoctorDetailActivity;

public class HomePageFragmentThird extends Fragment {

    Button btnDoctorRegister;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_3, container, false);
        btnDoctorRegister = (Button) view.findViewById(R.id.btntab3_register_doctor);
        Button btn = (Button) view.findViewById(R.id.btntab3_update_doctor_image);
        btnDoctorRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterDoctorActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RegisterDoctorDetailActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}
