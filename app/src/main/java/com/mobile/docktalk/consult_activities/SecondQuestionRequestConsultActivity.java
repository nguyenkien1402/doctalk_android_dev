package com.mobile.docktalk.consult_activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.docktalk.R;
import com.mobile.docktalk.adapter.RequestConsultSecondPageAdapter;
import com.mobile.docktalk.models.SpecificTypeModel;

import java.util.ArrayList;
import java.util.List;

public class SecondQuestionRequestConsultActivity extends AppCompatActivity {

    List<SpecificTypeModel> stModel;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_question_request_consult);
        listView = (ListView) findViewById(R.id.rq2_list);
        initListOfSpecificType();

        RequestConsultSecondPageAdapter adapter = new RequestConsultSecondPageAdapter(getApplicationContext(),stModel);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),stModel.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initListOfSpecificType(){
        stModel = new ArrayList<SpecificTypeModel>();
        for(int i = 0 ; i < 20 ; i++){
            String title = "This is the title "+i;
            SpecificTypeModel s = new SpecificTypeModel();
            s.setTitle(title);
            stModel.add(s);
        }
    }
}
