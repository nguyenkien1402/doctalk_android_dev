package com.mobile.docktalk;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

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
//        String token = pref.getString("Token",null);
//        String userId = pref.getString("UserId",null);
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImU2MjIxYTkyMjQyZWQ4NzE5YTg2ZGNmODg4MWY3ODUxIiwidHlwIjoiSldUIn0.eyJuYmYiOjE1NjM5Njg2OTEsImV4cCI6MTU5NTUwNDY5MSwiaXNzIjoiaHR0cDovLzE5Mi4xNjguMTMyLjE6NTAwMCIsImF1ZCI6WyJodHRwOi8vMTkyLjE2OC4xMzIuMTo1MDAwL3Jlc291cmNlcyIsImRvY3RhbGtfYXV0aF9hcGkiXSwiY2xpZW50X2lkIjoicm8uY2xpZW50Iiwic3ViIjoiYTI2NjFiNDAtMmI5My00OGI4LWE1YWEtOTBlNmM2OTc5N2FlIiwiYXV0aF90aW1lIjoxNTYzOTY4NjkxLCJpZHAiOiJsb2NhbCIsInNjb3BlIjpbImVtYWlsIiwib3BlbmlkIiwicHJvZmlsZSIsImRvY3RhbGtfYXV0aF9hcGkiXSwiYW1yIjpbInB3ZCJdfQ.nQW-zhmEPFkIAI8xNQGQ924WRbhbRX7gLzkr7UJhteHme9k-qKwqLrq5NJezGuRQ8ErDBquu1b7BrSeeuaK15TN6ipei9fY2COsYVuxgYyQ8HotkmrYmD96Prej2O90Yct5y8vvWAELgNOzNFPk8GVQ0YgBIbvuuFdjkfp70bw8xk9f-dRcr0QLxJ_smMpC5Wg4vDptC02ksgbDu-a8MIEX537A2VMPbtTPko71nFUQyTlaCfB9-xOGykTSCrdKfwux2ab6o8cY3b3Sm2JJeSbbQUG7ivB6aOqKqzdREB3PNTUDPoUkq2L2pxOuRjIr4V0esLH78wxBh3QhceIaqZQ";
        String userId = "hadfhjdjfksjhfwe-w-cw-rc-wec";

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
            Log.d("TestFA","Token "+strings[1]);
//            JSONObject result = RequestConsultController.searchingDoctor(Integer.parseInt(ed.getText().toString()),strings[1]);
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Toast.makeText(getApplicationContext(),jsonObject.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
