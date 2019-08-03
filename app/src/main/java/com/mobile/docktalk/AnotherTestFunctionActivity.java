package com.mobile.docktalk;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class AnotherTestFunctionActivity extends AppCompatActivity {

    Button btn;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_test_function);
        btn = (Button) findViewById(R.id.thread);
        progressDialog = new ProgressDialog(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TheTestThreadAsync theTestThreadAsync = new TheTestThreadAsync();
                theTestThreadAsync.execute();
            }
        });

    }

    private class TheTestThreadAsync extends AsyncTask<Void, Void, Void>{
        TaskSendingNoti notice ;
        @Override
        protected Void doInBackground(Void... voids) {
            doTheThreadJobHere();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Finding the best doctor");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Done with the thing",Toast.LENGTH_SHORT).show();
        }

        public void doTheThreadJobHere(){
            notice = new TaskSendingNoti();
            notice.start();
            TaskGenerateFromServer();
        }

        public void TaskGenerateFromServer(){
            Random random = new Random();
            while(true){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Log.d("Test","For some reason, don't know why");
                }
                 int code = random.nextInt(8);
                 Log.d("Test","Code is: "+code);
                if(code == 1){
                    Log.d("Test","Doctor accept the request");
                    notice.shutDown();
                    break;
                }
                if(code == 0){
                    Log.d("Test","Doctor reject the request");
                    notice.interrupt();
                }

            }
        }

        private class TaskSendingNoti extends Thread{
            volatile boolean isShutdown = false;
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    if(isShutdown == true)
                        break;
                    Log.d("Test", "Send noti to the doctor number: "+i);
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        Log.d("Test","Interrupt happened");
                    }
                }
            }
            public void shutDown(){
                isShutdown = true;
            }

        }

    }
}
