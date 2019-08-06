package com.mobile.docktalk.login_signup_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.R;
import com.mobile.docktalk.adapter.ProfessionalAdapter;
import com.mobile.docktalk.apis_controller.ProfessionalController;
import com.mobile.docktalk.models.Professional;
import com.mobile.docktalk.utilities.RecyclerItemClickListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddingDoctorProfessionalActivity extends AppCompatActivity implements ActionMode.Callback{

    List<Professional> listProfessional = null;
    List<Professional> listChoice = new ArrayList<Professional>();
    ProfessionalAdapter professionalAdapter;
    private ActionMode actionMode;
    private boolean isMultiSelect = true;
    private List<Integer> selectedIds = new ArrayList<>();
    private int doctorId;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor_professional);
        doctorId = getIntent().getIntExtra("DoctorId",0);
        SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
        token = pref.getString("Token",null);
        GetProfessionalListAsyn getProfessionalListAsyn = new GetProfessionalListAsyn();
        getProfessionalListAsyn.execute();

    }
    private class GetProfessionalListAsyn extends AsyncTask<Void, Void, List<Professional>> {
        @Override
        protected List<Professional> doInBackground(Void... voids) {
            List<Professional> professionals = ProfessionalController.getProfessionalList();
            return professionals;
        }

        @Override
        protected void onPostExecute(List<Professional> professionals) {
            listProfessional = professionals;
            selectedItem2(professionals);
        }
    }

    public void selectedItem2(List<Professional> listProfessional){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.professional_choice);
        professionalAdapter = new ProfessionalAdapter(this,listProfessional);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(professionalAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect){
                    //if multiple selection is enabled then select item on single click else perform normal click on item.
                    if (actionMode == null){
                        selectedIds = new ArrayList<>();
                        actionMode = startActionMode(AddingDoctorProfessionalActivity.this); //show ActionMode.
                    }
                    multiSelect(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect){
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode == null){
                        actionMode = startActionMode(AddingDoctorProfessionalActivity.this); //show ActionMode.
                    }
                }

                multiSelect(position);
            }
        }));
    }

    private void multiSelect(int position) {
        Professional data = professionalAdapter.getItem(position);
        if (data != null){
            if (actionMode != null) {
                if (selectedIds.contains(data.getId()))
                    selectedIds.remove(Integer.valueOf(data.getId()));
                else
                    selectedIds.add(data.getId());

                if (selectedIds.size() > 0)
                    actionMode.setTitle(String.valueOf(selectedIds.size())); //show selected item count on action mode.
                else{
                    actionMode.setTitle(""); //remove item count from action mode.
                    actionMode.finish(); //hide action mode.
                }
                professionalAdapter.setSelectedIds(selectedIds);

            }
        }
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.doctor_professionals_choice, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_choice_next:
                //just to show selected items.
                StringBuilder stringBuilder = new StringBuilder();
                for (Professional data : listProfessional) {
                    if (selectedIds.contains(data.getId()))
                        stringBuilder.append("\n").append(data.getTitle());
//                    listChoice.add(data);
                }
                Toast.makeText(this, "Selected items are :" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                AddingDoctorProfessionalAsync addingDoctorProfessionalAsync = new AddingDoctorProfessionalAsync();
                addingDoctorProfessionalAsync.execute();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = true;
        selectedIds = new ArrayList<>();
        professionalAdapter.setSelectedIds(new ArrayList<Integer>());
    }

    private class AddingDoctorProfessionalAsync extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result = ProfessionalController.addProfessionalToDoctor(token,doctorId,selectedIds);
            if(result != null){
                return result;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            // Start next activity to upload document.
            try{
                Intent intent = new Intent(getApplicationContext(),RegisterDoctorDetailActivity.class);
                intent.putExtra("DoctorId",doctorId);
                startActivity(intent);
            }catch (Exception e){
                Log.d("Error",e.getMessage());
            }

        }
    }
}


//package com.mobile.docktalk.splashloginsignup;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.util.SparseBooleanArray;
//import android.view.ActionMode;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.AbsListView;
//import android.widget.ListView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.mobile.docktalk.R;
//import com.mobile.docktalk.adapter.ProfessionalAdapter;
//import com.mobile.docktalk.apiconsumption.ProfessionalController;
//import com.mobile.docktalk.model.Professional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AddingDoctorProfessionalActivity extends AppCompatActivity {
//
//    ListView lvProfessional;
//    List<Professional> listProfessional = null;
//    List<Professional> listChoice = new ArrayList<Professional>();
//    ProfessionalAdapter professionalAdapter;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register_doctor_professional);
//        lvProfessional = (ListView) findViewById(R.id.professional_choice);
//
//        GetProfessionalListAsyn getProfessionalListAsyn = new GetProfessionalListAsyn();
//        getProfessionalListAsyn.execute();
//
//    }
//
//    private class GetProfessionalListAsyn extends AsyncTask<Void, Void, List<Professional>> {
//        @Override
//        protected List<Professional> doInBackground(Void... voids) {
//            List<Professional> professionals = ProfessionalController.getProfessionalList();
//            return professionals;
//        }
//
//        @Override
//        protected void onPostExecute(List<Professional> professionals) {
//            selectedItem(professionals);
//        }
//    }
//
//    public void selectedItem(List<Professional> listProfessional){
//        professionalAdapter = new ProfessionalAdapter(this,R.layout.listview_professional_choice_item, listProfessional);
//        lvProfessional.setAdapter(professionalAdapter);
//        lvProfessional.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        lvProfessional.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//                // Capture total checked items
//                final int checkedCount = lvProfessional.getCheckedItemCount();
//                // Set the CAB title according to total checked items
//                mode.setTitle(checkedCount + " Selected");
//                // Calls toggleSelection method from ListViewAdapter Class
//                professionalAdapter.toggleSelection(position);
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                mode.getMenuInflater().inflate(R.menu.doctor_professionals_choice, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.action_choice_next:
//                        // Calls getSelectedIds method from ListViewAdapter Class
//                        SparseBooleanArray selected = professionalAdapter
//                                .getSelectedIds();
//                        // Captures all selected ids with a loop
//                        for (int i = (selected.size() - 1); i >= 0; i--) {
//                            if (selected.valueAt(i)) {
//                                Professional selecteditem = professionalAdapter
//                                        .getItem(selected.keyAt(i));
//                                listChoice.add(selecteditem);
//                            }
//                        }
//                        // Close CAB
//                        mode.finish();
//                        Log.d("Choice size",listChoice.size()+"");
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
//        });
//    }
//}
