package com.mobile.docktalk.apiconsumption;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.docktalk.model.Professional;
import com.mobile.docktalk.utility.EndPointAPIs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
}
