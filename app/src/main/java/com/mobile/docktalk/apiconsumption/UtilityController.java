package com.mobile.docktalk.apiconsumption;

import android.util.Log;

import com.mobile.docktalk.utility.EndPointAPIs;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UtilityController {

    public static String getTokenMobile(String username, String password){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.get_token_mobile + username +"/"+password;
        Log.d("URL",url);
        String token = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type","application/json")
                .build();
        try {
            Log.d("Before crash 1","Yeah");
            Response response = okHttpClient.newCall(request).execute();
            Log.d("Before crash 2","Yeah");
            Log.d("Code",response.code()+"");
            if(response.code() == 200){
                String result = response.body().string();
                Log.d("Result",result);
                JSONObject jsonObject = new JSONObject(result);
                token = jsonObject.getString("access_token");
            }
        } catch (Exception e) {
            Log.d("Error","Error in parsing");
            e.printStackTrace();
        }
        return token;
    }

    public static String getUserTokenInfo(String token){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                            .url(EndPointAPIs.get_user_token_info)
                            .get()
                            .addHeader("Authorization","Bearer "+token)
                            .build();
        try{
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                JSONObject result = new JSONObject(response.body().string());
                String userId = result.getString("sub");
                return userId;
            }
        }catch (Exception e){
            Log.d("Utitility Controller",e.getMessage());
        }
        return null;
    }
}
