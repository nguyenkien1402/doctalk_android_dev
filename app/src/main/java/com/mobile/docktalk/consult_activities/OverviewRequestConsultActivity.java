package com.mobile.docktalk.consult_activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.docktalk.R;
import com.mobile.docktalk.apis_controller.RequestConsultController;
import com.mobile.docktalk.apis_controller.UtilityController;
import com.mobile.docktalk.models.Doctor;
import com.mobile.docktalk.models.ImageUploadInfo;
import com.mobile.docktalk.models.RequestConsultForm;
import com.mobile.docktalk.utilities.ConvertFunctions;
import com.mobile.docktalk.utilities.Messages;
import com.mobile.docktalk.utilities.SavingLocalData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OverviewRequestConsultActivity extends AppCompatActivity {

    public static String TAG = "OVERVIEWREQUEST";
    TextView content, specification, subject;
    private RequestConsultForm requestConsultForm;
    RequestManager requestManager;
    private ViewGroup mSelectedImagesContainer;
    StorageReference storageReference;
    FirebaseFirestore db;
    View overview_container, progress;
    ProgressDialog progressDialog;
    private String token, userId;
    private int patientId;
    private List<ImageUploadInfo> imageUploadInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_request_consult);
        // Get data from intent
        Bundle bundle = getIntent().getExtras();
        requestConsultForm = bundle.getParcelable("RequestConsultForm");
        initializeView();
        getDataFromSharePreference();



    }

    private void initializeView(){
        content = (TextView) findViewById(R.id.overview_inquiry);
        specification = (TextView) findViewById(R.id.overview_specification);
        subject = (TextView) findViewById(R.id.overview_subject);
        requestManager = Glide.with(this);
        mSelectedImagesContainer = findViewById(R.id.overview_selected_photos_container);
        overview_container = (View) findViewById(R.id.overview_container);
        progressDialog = new ProgressDialog(OverviewRequestConsultActivity.this);

        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        imageUploadInfos = new ArrayList<>();

        content.setText(requestConsultForm.getContent());
        specification.setText(requestConsultForm.getSpecification());
        subject.setText(requestConsultForm.getSubject());
        showUriList(requestConsultForm.getImageUrls());
    }

    private void getDataFromSharePreference(){
        SharedPreferences pref = getSharedPreferences(SavingLocalData.ShareUserData,MODE_PRIVATE);
        userId = pref.getString(SavingLocalData.USERID,null);
        patientId = pref.getInt(SavingLocalData.PATIENTID,-1);
        token = pref.getString(SavingLocalData.TOKEN,null);
    }

    private void showUriList(List<Uri> uriList) {
        mSelectedImagesContainer.removeAllViews();
        mSelectedImagesContainer.setVisibility(View.VISIBLE);
        for(Uri uri : uriList){
            ImageView iv =(ImageView) LayoutInflater.from(this).inflate(R.layout.posting_image_item, mSelectedImagesContainer,false);
            requestManager.load(uri.toString())
                    .apply(new RequestOptions().fitCenter())
                    .into(iv);
            mSelectedImagesContainer.addView(iv);
        }
    }

    public void additionalInfo(View view){
    }

    public void startChattingSession(View view){
        // Uploading to Firebase and get the link back and then update to the main database
        UploadImageFileToFirebaseStorage();
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }

    public void UploadImageFileToFirebaseStorage() {
        // Checking whether FilePathUri Is empty or not.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        for(int i = 0 ; i < requestConsultForm.getImageUrls().size() ; i++){
            Uri FilePathUri = requestConsultForm.getImageUrls().get(i);
            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(Messages.firebase_storage_request_consult_document_path
                    + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            // Adding addOnSuccessListener to second StorageReference.
            UploadTask uploadTask = storageReference2nd.putFile(FilePathUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference2nd.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        String image_name = "patient_"+patientId + "_" + "userId_"+userId+"_" + ConvertFunctions.dateToString(Calendar.getInstance().getTime());
                        Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                        Uri downloadUri = task.getResult();
                        ImageUploadInfo imageUploadInfo = new ImageUploadInfo(image_name,  downloadUri.toString());
                        Map<String, Object> imagePostValue = imageUploadInfo.toMap();

                        // Getting image upload ID.
//                        DatabaseReference doctorRef = databaseReference.child("Doctor_1");
//                        String key = doctorRef.push().getKey();
//                        doctorRef.child(key).setValue(imagePostValue);

                        db.collection(Messages.firebase_database_consult_document_collection).document(Messages.firebase_database_consult_patient_document).
                                collection(Messages.firebase_database_consult_patient_user_collection +userId).add(imagePostValue)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        // Save the question to the Database
                                        imageUploadInfos.add(imageUploadInfo);
                                        // And then call the API to find the doctor
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                }
            });
        }
        SavingQuestionToDatabase postingQuestionAsync = new SavingQuestionToDatabase();
        postingQuestionAsync.execute();
    }


    private class SavingQuestionToDatabase extends AsyncTask<Void, Void, Doctor> {

        @Override
        protected Doctor doInBackground(Void... voids) {
            int requestId = RequestConsultController.postingQuestion(token, patientId, userId, requestConsultForm, imageUploadInfos);
            if(requestId != -1){
                Log.d(TAG,"RequestID: "+requestId);
//                List<Doctor> listDoctors = RequestConsultController.searchingDoctor(requestId,token);
//                Doctor doctor = waitingRespondFromDoctor(listDoctors, requestId);
//                return doctor;
                TaskGenerateFromServer();

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Finding the best doctor for you...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Doctor doctor) {
            super.onPostExecute(doctor);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Upload successfully", Toast.LENGTH_SHORT ).show();
            // When get the result, call the API to find the doctor.

        }

        private Doctor waitingRespondFromDoctor(List<Doctor> listDoctors, int requestId){
            SendingDoctorWithWaitingTime sendingDoctorWithWaitingTime = new SendingDoctorWithWaitingTime(listDoctors,requestId);
            sendingDoctorWithWaitingTime.start();
            int code = 2;
            while (true){
                // Listen from server and assign to the code
                if(code == 1){
                    // Mean a doctor already accept the request
                    // Starting the Chat session and break out of while
                    sendingDoctorWithWaitingTime.shutDown();
                    break;
                }
                if(code == 0){
                    // Mean a doctor reject the request, jump to the next doctor
                    sendingDoctorWithWaitingTime.interrupt();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private class SendingDoctorWithWaitingTime extends Thread{

        List<Doctor> doctors;
        volatile boolean isShutDown = false;
        int requestId;
        public SendingDoctorWithWaitingTime(List<Doctor> doctors, int requestId){
            this.doctors = doctors;
            this.requestId = requestId;
        }
        @Override
        public void run() {
            super.run();
            try {
                for(int i = 0 ; i < doctors.size() ; i ++){
                    if(isShutDown)
                        break;
                    UtilityController.sendingNotificationToDoctor(doctors.get(i).getUserId(),requestConsultForm,userId,requestId);
                    Thread.sleep(20000);
                }
            } catch (InterruptedException e) {
                Log.d(TAG,"Interrupt happend, next doctor");
            }
        }

        public void shutDown(){
            isShutDown = true;
        }
    }
    public void TaskGenerateFromServer(){
        TaskSendingNoti notice = new TaskSendingNoti();
        notice.start();
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

    public class TaskSendingNoti extends Thread{
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
