package com.mobile.docktalk.apiconsumption;

import android.util.Log;

import com.mobile.docktalk.model.RequestConsult;
import com.mobile.docktalk.utility.EndPointAPIs;

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

    public static boolean sendingNotificationToDoctor(String doctorId, RequestConsult requestConsult, String patientId){
        JSONObject notic = new JSONObject();
        JSONObject data = new JSONObject();
        OkHttpClient okHttpClient = new OkHttpClient();
        try{
            data.put("id",requestConsult.getId());
            data.put("title", requestConsult.getBriefOverview());
            data.put("content", requestConsult.getInquiry());
            data.put("imageUrl","http://h5.4j.com/thumb/Ninja-Run.jpg");
            data.put("gameUrl","https://h5.4j.com/Ninja-Run/index.php?pubid=noa");
            data.put("patientId",patientId);
            notic.put("data",data);
            notic.put("to","/topics/doctor_"+doctorId);

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
