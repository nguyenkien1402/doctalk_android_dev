package com.mobile.docktalk.apis_controller;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.docktalk.models.Doctor;
import com.mobile.docktalk.models.RequestConsult;
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

    public static JSONObject PostingQuestion(String token, RequestConsult requestConsult){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.posting_question_consult;
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        try{
            String strData = new Gson().toJson(requestConsult);
            RequestBody body = RequestBody.create(MEDIA_TYPE, strData.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization","Bearer "+token)
                    .addHeader("Content-Type","application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                return new JSONObject(response.body().string());
            }
        }catch (Exception e){
            Log.d("RequestController",e.getMessage());
        }
        return null;
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
