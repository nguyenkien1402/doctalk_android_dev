package com.mobile.docktalk.app_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.docktalk.R;
import com.mobile.docktalk.apis_controller.AccountController;
import com.mobile.docktalk.app_fragments.HomePageFragmentFirst;
import com.mobile.docktalk.app_fragments.HomePageFragmentSecond;
import com.mobile.docktalk.app_fragments.HomePageFragmentThird;
import com.mobile.docktalk.consult_activities.PostingRequestConsultActivity;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private String userId;
    private String token;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomePageFragmentFirst();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new HomePageFragmentSecond();
                    break;
                case R.id.navigation_notifications:
                    fragment = new HomePageFragmentThird();
                    break;
                case R.id.navigation_posting_request:
                    Intent intent = new Intent(getApplicationContext(), PostingRequestConsultActivity.class);
                    startActivity(intent);
                    return true;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,new HomePageFragmentFirst()).commit();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserInfo",MODE_PRIVATE);
        userId = pref.getString("UserId",null);
        token = pref.getString("Token",null);

        //Get Patient Info
        GetPatientInfo getPatientInfo = new GetPatientInfo();
        getPatientInfo.execute();
    }

    private class GetPatientInfo extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result = AccountController.getPatientInfo(userId,token);
            Log.d("Patient",result.toString());
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            // Do something with the result here
            SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            try {
                Log.d("FirstName",jsonObject.getString("firstName"));
                Log.d("LastName",jsonObject.getString("lastName"));
                editor.putString("FirstName", jsonObject.getString("firstName"));
                editor.putString("LastName", jsonObject.getString("lastName"));
                editor.putInt("PatientId", jsonObject.getInt("id"));
                editor.commit();
            }catch (Exception e){
                Log.d("Error","Error at getting JSON object");
            }
            Log.d("patient",jsonObject.toString());
        }
    }

}
