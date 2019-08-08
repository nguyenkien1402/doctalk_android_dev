package com.mobile.docktalk.apis_controller;

import android.util.Log;

import com.mobile.cometchat.Contracts.StringContract;
import com.mobile.docktalk.utilities.EndPointAPIs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CometChatController {

    public static String TAG = "CometChatController";

    public static JSONObject createCometChatAccount(String uid, String name, String email){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.cometchat_users_create;
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        JSONObject object = new JSONObject();
        try{
            object.put("uid",uid);
            object.put("name",name);
            object.put("email",email);
            object.put("role","user_patient");
            RequestBody body = RequestBody.create(MEDIA_TYPE, object.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type","application/json")
                    .addHeader("Accept","application/json")
                    .addHeader("appId", StringContract.AppDetails.APP_ID)
                    .addHeader("apiKey", StringContract.AppDetails.API_KEY)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 200){
                return new JSONObject(response.body().string());
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return null;
    }

    public static JSONObject createUserAuthToken(String uid){
        String url = MessageFormat.format(EndPointAPIs.cometchat_auth_token_create, uid);
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(MEDIA_TYPE,"");
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("appId", StringContract.AppDetails.APP_ID)
                    .addHeader("apiKey", StringContract.AppDetails.API_KEY)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                return new JSONObject(response.body().string());
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return null;
    }

    public static String getUserAuthToken(String uid){
        String url = MessageFormat.format(EndPointAPIs.cometchat_auth_token_create,uid);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("appId", StringContract.AppDetails.APP_ID)
                .addHeader("apiKey", StringContract.AppDetails.API_KEY)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                JSONArray array = new JSONObject(response.body().string()).getJSONArray("data");
                return array.getJSONObject(0).getString("authToken");
            }
        } catch (Exception e) {
            Log.d(TAG,e.getMessage());
        }
        return null;
    }


}
