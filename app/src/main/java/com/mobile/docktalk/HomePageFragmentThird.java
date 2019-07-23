package com.mobile.docktalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.docktalk.splashloginsignup.RegisterDoctorActivity;

public class HomePageFragmentThird extends Fragment {

    Button btnDoctorRegister;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_3, container, false);
        btnDoctorRegister = (Button) view.findViewById(R.id.btntab3_register_doctor);
        btnDoctorRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterDoctorActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
