package com.mobile.docktalk.consult_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.R;
import com.mobile.docktalk.adapter.RequestConsultSecondPageAdapter;
import com.mobile.docktalk.models.Professional;
import com.mobile.docktalk.models.RequestConsultForm;

import java.util.List;

public class SecondQuestionRequestConsultActivity extends AppCompatActivity {

    List<Professional> stModel;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_question_request_consult);
        listView = (ListView) findViewById(R.id.rq2_list);
        stModel = Professional.getListProfessional();

        RequestConsultSecondPageAdapter adapter = new RequestConsultSecondPageAdapter(getApplicationContext(),stModel);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = getIntent().getExtras();
                RequestConsultForm requestConsultForm = bundle.getParcelable("RequestConsultForm");
                requestConsultForm.setSubject(stModel.get(position).getTitle());
                requestConsultForm.setSubjectId(stModel.get(position).getId());
                Intent intent = new Intent(getApplicationContext(),OverviewRequestConsultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


}
