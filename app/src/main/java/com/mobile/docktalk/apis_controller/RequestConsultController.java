package com.mobile.docktalk.apis_controller;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.docktalk.models.Doctor;
import com.mobile.docktalk.models.ImageUploadInfo;
import com.mobile.docktalk.models.RequestConsult;
import com.mobile.docktalk.models.RequestConsultDocument;
import com.mobile.docktalk.models.RequestConsultForm;
import com.mobile.docktalk.utilities.EndPointAPIs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestConsultController {

    private static String TAG = "REQUESTCONTROLLER";
    public static int postingQuestion(String token, int patientId, String userId, RequestConsultForm requestConsultForm, List<ImageUploadInfo> imageFirebaseUrls){
        // Convert RequestConsultForm to RequestConsult
        // First, get the RequestConsultDocument
        List<RequestConsultDocument> requestConsultDocuments = new ArrayList<RequestConsultDocument>();
        RequestConsultDocument requestConsultDocument = new RequestConsultDocument();
        for(int i = 0 ; i < imageFirebaseUrls.size() ; i++){
            requestConsultDocument.setDocumentLink(imageFirebaseUrls.get(i).getImageURL());
            requestConsultDocument.setDocumentName(imageFirebaseUrls.get(i).getImageName());
            requestConsultDocument.setDocumentType("image");
            requestConsultDocuments.add(requestConsultDocument);
        }
        RequestConsult requestConsult = new RequestConsult();
        requestConsult.setInquiry(requestConsultForm.getContent());
        requestConsult.setPatientId(patientId);
        requestConsult.setSpecification(requestConsultForm.getSpecification());
        requestConsult.setRequestConsultDocument(requestConsultDocuments);
        requestConsult.setProfessionalId(requestConsultForm.getSubjectId());
        requestConsult.setBriefOverview(requestConsultForm.getSubject());
        requestConsult.setUrgent(1);

        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.posting_question_consult;
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        try{
            String strData = new Gson().toJson(requestConsult);
            Log.d(TAG,strData);
            RequestBody body = RequestBody.create(MEDIA_TYPE, strData.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization","Bearer "+token)
                    .addHeader("Content-Type","application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                JSONObject result = new JSONObject(response.body().string());
                return result.getInt("requestId");
            }
        }catch (Exception e){
            Log.d("RequestController",e.getMessage());
        }
        return -1;
    }

    public static List<Doctor> searchingDoctor(int requestId, String token){
        List<Doctor> doctors = new ArrayList<Doctor>();
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.get_searching_doctor_for_request + requestId;
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization","Bearer "+token)
                    .addHeader("Content-Type","application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                JSONArray array = new JSONArray(response.body().string());
                for(int i = 0 ; i < array.length() ; i++){
                    Doctor d = new Gson().fromJson(array.get(i).toString(),Doctor.class);
                    doctors.add(d);
                }
                return doctors;
            }
        }catch (Exception e){
            Log.d("RequestController",e.getMessage());
        }
        return null;
    }

    public static boolean sendingNotificationToDoctor(String userDoctorId, RequestConsultForm requestConsult, String userPatientId, int requestId){
        JSONObject notic = new JSONObject();
        JSONObject data = new JSONObject();
        OkHttpClient okHttpClient = new OkHttpClient();
        try{
            data.put("id",requestId);
            data.put("title", "New request: "+requestConsult.getSpecification());
            data.put("content", requestConsult.getContent());
            data.put("imageUrl","http://h5.4j.com/thumb/Ninja-Run.jpg");
            data.put("gameUrl","https://h5.4j.com/Ninja-Run/index.php?pubid=noa");
            data.put("patientId",userPatientId);
            notic.put("data",data);
            notic.put("to","/topics/doctor_"+userDoctorId);

            // Do Post Request
            MediaType MEDIA_TYPE = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE, notic.toString());
            Request request = new Request.Builder()
                    .url(EndPointAPIs.firebase_push_notification)
                    .post(requestBody)
                    .addHeader("Content-Type","application/json")
                    .addHeader("Authorization","key=AAAAST4PGVw:APA91bHGopJqbpePv79V5qiClVfF4PIm6N0s09MN889BqlfgfXvCQfkO4sSTyeyP0Yr5WCvftz7ftqqoJSJ2SwGS_d44-Rw01PtIqJnuL-p_6oXqY1uKUbffixfXsZiBtbRmn7Do7V2u")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                // Mean the notification has been send to next doctor.
                Log.d(TAG,"Send to another doctor successfully ");
                return true;
            }else{
                Log.d("Failure response","Something went wrong");
            }
        }catch(Exception e){
            Log.d(TAG, e.getMessage());
        }
        return false;
    }
}
