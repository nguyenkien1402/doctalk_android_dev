package com.mobile.docktalk.consult_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.docktalk.R;
import com.mobile.docktalk.adapter.RequestConsultFirstPageAdapter;

public class FirstQuestionRequestConsultActivity extends AppCompatActivity {

    ListView listView;
    int flags[] = {R.drawable.doctor_64, R.drawable.pill_64};
    String titles[] = {"I want to have general advise","I want to have pill recommendation"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_question_request_consult);
        listView = (ListView) findViewById(R.id.rq1_list);
        RequestConsultFirstPageAdapter adapter = new RequestConsultFirstPageAdapter(getApplicationContext(),titles,flags);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),titles[position],Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),SecondQuestionRequestConsultActivity.class);
                startActivity(intent);
            }
        });
    }
}
