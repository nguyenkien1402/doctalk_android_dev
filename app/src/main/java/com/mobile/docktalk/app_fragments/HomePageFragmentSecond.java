package com.mobile.docktalk.app_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.mobile.docktalk.R;
import com.mobile.docktalk.apis_controller.RequestConsultController;
import com.mobile.docktalk.apis_controller.UtilityController;
import com.mobile.docktalk.models.Doctor;
import com.mobile.docktalk.models.RequestConsult;

import java.util.List;

public class HomePageFragmentSecond extends Fragment {

    private static String TAG = "SecondHomePage";
    HubConnection hubConnection;
    Button btnSearchDoctor;
    View view;
    EditText edRequestId;
    RequestConsult requestConsult;
    String patientId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_2, container, false);
        btnSearchDoctor = (Button) view.findViewById(R.id.btn_matching_doctor);
        edRequestId = (EditText) view.findViewById(R.id.ed_requestId);

        requestConsult = new RequestConsult();
        requestConsult.setId(Integer.parseInt(edRequestId.getText().toString()));
        requestConsult.setBriefOverview("Overview");
        requestConsult.setInquiry("This is an overview");


        // Get patientId which is userId from Share Preferences;
        SharedPreferences pref = getActivity().getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        patientId = pref.getString("UserId",null);


        hubConnection = HubConnectionBuilder.create("http://192.168.132.1:5001/searchingdoctorhub").build();

        btnSearchDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED)
                    hubConnection.start();
                MatchingDoctorAsync matchingDoctorAsync = new MatchingDoctorAsync();
                matchingDoctorAsync.execute();
            }
        });


        return view;
    }

    private class MatchingDoctorAsync extends AsyncTask<Void, Void, List<Doctor>> {

        @Override
        protected List<Doctor> doInBackground(Void... voids) {
            SharedPreferences pref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            String token = pref.getString("Token",null);
            if(token != null){
                int requestId = Integer.parseInt(edRequestId.getText().toString());
                List<Doctor> jsonObject = RequestConsultController.searchingDoctor(requestId,token);
                return jsonObject;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Doctor> jsonObject) {
            super.onPostExecute(jsonObject);
            if(jsonObject != null)
                doSendingRequest(jsonObject);

        }
    }


    public void doSendingRequest(List<Doctor> result){
        SendingRequestThread sendingRequestThread = new SendingRequestThread(result);
        sendingRequestThread.start();
        final int[] response = {2};
        while(true){
            Log.d(TAG,"Code: "+response[0]);
            hubConnection.on(patientId,(code) -> {
                if(code != null)
                    response[0] =  Integer.parseInt(code);
            },String.class);
            if(response[0] == 0)
                sendingRequestThread.interrupt();
            if(response[0] == 1){
                sendingRequestThread.stop();
                break;
            }
            // Sleep for 5 second before check again.
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


    private class SendingRequestThread extends Thread{
        List<Doctor> doctors;

        public SendingRequestThread(List<Doctor> doctors){
            this.doctors = doctors;
        }

        @Override
        public void run() {
            super.run();
            // do the send request right here.
            for(int i = 0 ; i < doctors.size() ; i++){
                Doctor d = doctors.get(i);
                SendingNotiAsync sendingNotiAsync = new SendingNotiAsync();
                sendingNotiAsync.execute(d);
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    Log.d(TAG,"Interrupt");
                }
            }
        }

        private class SendingNotiAsync extends AsyncTask<Doctor, Void, Boolean>{
            @Override
            protected Boolean doInBackground(Doctor... doctors) {
                boolean result = UtilityController.sendingNotificationToDoctor(doctors[0].getUserId(),requestConsult, patientId);
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
            }
        }
    }
}
