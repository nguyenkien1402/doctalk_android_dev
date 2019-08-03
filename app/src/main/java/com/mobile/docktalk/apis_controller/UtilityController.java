package com.mobile.docktalk.apis_controller;

import android.util.Log;

import com.mobile.docktalk.models.RequestConsultForm;
import com.mobile.docktalk.utilities.EndPointAPIs;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UtilityController {

    private static String TAG = "UtitlityController";

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

    public static JSONObject getUserTokenInfo(String token){
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
                return result;
            }
        }catch (Exception e){
            Log.d("Utitility Controller",e.getMessage());
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
