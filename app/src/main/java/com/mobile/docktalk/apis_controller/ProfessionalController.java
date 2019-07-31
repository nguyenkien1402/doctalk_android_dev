package com.mobile.docktalk.apis_controller;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.docktalk.models.Professional;
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

public class ProfessionalController {

    public static List<Professional> getProfessionalList(){
        List<Professional> result = new ArrayList<Professional>();
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.professional_list;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type","application/json")
                .build();
        try{
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                JSONArray jsonArray = new JSONArray(response.body().string());
                Log.d("JSON Array",jsonArray.toString());
                for(int i = 0 ; i < jsonArray.length() ; i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Professional professional = new Gson().fromJson(object.toString(), Professional.class);
                    result.add(professional);
                }
            }
        }catch (Exception e){
            Log.d("Error","Failed to get response");
        }
        return result;
    }

    public static JSONObject addProfessionalToDoctor(String token, int doctorId, List<Integer> professionalIds){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.adding_doctor_professional;
        JSONObject dp = new JSONObject();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        try{
            // Convert professionalIds to JSONArray
            JSONArray jsonArray = new JSONArray();
            for(int id : professionalIds){
                jsonArray.put(id);
            }
            dp.put("DoctorId",doctorId);
            dp.put("ProfessionalId",jsonArray);
            Log.d("JSON File",dp.toString());
            RequestBody body = RequestBody.create(MEDIA_TYPE, dp.toString());
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
            Log.e("Error",e.getMessage());
        }
        return null;
    }
}
