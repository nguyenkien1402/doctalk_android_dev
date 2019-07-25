package com.mobile.docktalk.apiconsumption;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.docktalk.model.RequestConsult;
import com.mobile.docktalk.utility.EndPointAPIs;

import org.json.JSONObject;

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
}
