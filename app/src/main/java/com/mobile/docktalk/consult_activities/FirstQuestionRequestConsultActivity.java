package com.mobile.docktalk.consult_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.docktalk.R;
import com.mobile.docktalk.adapter.RequestConsultFirstPageAdapter;
import com.mobile.docktalk.models.RequestConsultForm;

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
                Bundle bundle = getIntent().getExtras();
                RequestConsultForm requestConsultForm = bundle.getParcelable("RequestConsultForm");
                requestConsultForm.setSpecification(titles[position]);

                Intent intent = new Intent(getApplicationContext(),SecondQuestionRequestConsultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
