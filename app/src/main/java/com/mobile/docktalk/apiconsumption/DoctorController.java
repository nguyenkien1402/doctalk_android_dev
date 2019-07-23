package com.mobile.docktalk.apiconsumption;

import android.util.Log;

import com.mobile.docktalk.utility.EndPointAPIs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorController {

    public static JSONObject addProfessional(String token, List<String> professionalList){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.adding_doctor_professional;

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        JSONObject professional = new JSONObject();
        try{
            professional.put("ProfessionalName",professionalList);
        }catch (JSONException e){
            Log.e("Parse JSON Error", e.getMessage());
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, professional.toString());
        Request request = new Request.Builder().url(url).post(body)
                .addHeader("Authorization","Bearer "+token)
                .addHeader("Content-Type","application/json")
                .build();
        JSONObject result = null;
        try{
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                result = new JSONObject(response.body().string());
            }else{
                Log.d("Failure response","Something went wrong");
            }
        }catch (Exception e){
            Log.d("Failure response",e.getMessage());
        }
        return result;
    }

    public static JSONArray getProfessionalList(String token){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.get_professional_list;
        Request request = new Request.Builder().url(url).get().
                addHeader("Content-Type","application/json")
                .addHeader("Authorization","Bearer "+token)
                .build();
        try{
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                JSONArray jsonArray = new JSONArray(response.body().string());
                return jsonArray;
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return null;
    }
}
