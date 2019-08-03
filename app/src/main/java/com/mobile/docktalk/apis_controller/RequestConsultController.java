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
}
