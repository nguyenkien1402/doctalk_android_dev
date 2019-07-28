package com.mobile.docktalk;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.mobile.docktalk.apiconsumption.RequestConsultController;

import org.json.JSONObject;

public class TestFunctionActivity extends AppCompatActivity {

    HubConnection hubConnection;
    Button btn;
    EditText ed;
    TextView tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_function);
        btn = (Button) findViewById(R.id.btn_test);
        ed = (EditText) findViewById(R.id.ed_consultid);
        tx = (TextView) findViewById(R.id.txt_result);

        // Get Token from Sharepreferences
        SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String token = pref.getString("Token",null);
        String userId = pref.getString("UserId",null);
        hubConnection = HubConnectionBuilder.create("http://192.168.132.1:5001/searchingdoctorhub").build();
        if(hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED)
            hubConnection.start();

        hubConnection.on("d31eac6d-6849-49b4-be3c-d05a25a3d8c4",(code) -> {
            tx.setText("Code: "+code);
        },String.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MatchingDoctorAsync matchingDoctorAsync = new MatchingDoctorAsync();
                matchingDoctorAsync.execute(new String[]{userId,token});
//                if(hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED)
//                    hubConnection.start();
//                if(hubConnection.getConnectionState() == HubConnectionState.CONNECTED){
//                    hubConnection.send("SearchingAvailableDoctor",1005,"d31eac6d-6849-49b4-be3c-d05a25a3d8c4","a2661b40-2b93-48b8-a5aa-90e6c69797ae");
//                }
            }
        });
    }

    private class MatchingDoctorAsync extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject result = RequestConsultController.searchingDoctor(Integer.parseInt(ed.getText().toString()),strings[1]);
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Toast.makeText(getApplicationContext(),jsonObject.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
