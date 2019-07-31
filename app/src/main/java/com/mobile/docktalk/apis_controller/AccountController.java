package com.mobile.docktalk.apis_controller;

import android.util.Log;

import com.mobile.docktalk.utilities.EndPointAPIs;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountController {

    public static JSONObject signup(String email, String username, String password, String phoneNumber){
        // Consume Post method to Identity Server
        JSONObject result = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        JSONObject newUser = new JSONObject();
        try{
            newUser.put("UserName",username);
            newUser.put("Email",email);
            newUser.put("PhoneNumber",phoneNumber);
            newUser.put("Password",password);
            newUser.put("ConfirmPassword",password);
        }catch (JSONException e){
            Log.e("Parse JSON Error", e.getMessage());
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, newUser.toString());
        Request request = new Request.Builder()
                .url(EndPointAPIs.signup_url)
                .post(body)
                .addHeader("Content-Type","application/json")
                .build();
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
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                String result = response.body().string();
                JSONObject jsonObject = new JSONObject(result);
                token = jsonObject.getString("access_token");
            }
        } catch (Exception e) {
            Log.d("Error",e.getMessage());
            e.printStackTrace();
        }
        return token;
    }

    public static JSONObject registerAsPatient(String token, JSONObject user){
        JSONObject result = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        JSONObject newPatient = new JSONObject();
        try{
            newPatient.put("UserId",user.getString("UserId"));
            newPatient.put("FirstName",user.getString("FirstName"));
            newPatient.put("LastName",user.getString("LastName"));
            newPatient.put("PreferName",user.getString("PreferName"));
            newPatient.put("Paddress",user.getString("Paddress"));
            newPatient.put("Suburb",user.getString("Suburb"));
            newPatient.put("Pstate",user.getString("Pstate"));
            newPatient.put("PostCode",user.getString("PostCode"));
        }catch (JSONException e){
            Log.e("Parse JSON Error", e.getMessage());
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, newPatient.toString());
        Request request = new Request.Builder()
                .url(EndPointAPIs.register_patient)
                .post(body)
                .addHeader("Authorization","Bearer "+token)
                .addHeader("Content-Type","application/json")
                .build();
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

    public static JSONObject registerAsDoctor(String token, JSONObject user){
        JSONObject result = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        JSONObject newDoctor = new JSONObject();
        try{
            newDoctor.put("UserId",user.getString("UserId"));
            newDoctor.put("FirstName",user.getString("FirstName"));
            newDoctor.put("LastName",user.getString("LastName"));
            newDoctor.put("PreferName",user.getString("PreferName"));
            newDoctor.put("ClinicName",user.getString("ClinicName"));
            newDoctor.put("ClinicAddress",user.getString("ClinicAddress"));
            newDoctor.put("ClinicSuburb",user.getString("ClinicSuburb"));
            newDoctor.put("ClinicPostCode",user.getString("ClinicPostCode"));
            newDoctor.put("ClinicState",user.getString("ClinicState"));
            newDoctor.put("Title",user.getString("Title"));
        }catch (JSONException e){
            Log.e("Parse JSON Error", e.getMessage());
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, newDoctor.toString());
        Request request = new Request.Builder()
                .url(EndPointAPIs.register_doctor)
                .post(body)
                .addHeader("Authorization","Bearer "+token)
                .addHeader("Content-Type","application/json")
                .build();
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

    public static JSONObject getPatientInfo(String userId, String token){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndPointAPIs.get_patient_info + userId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization","Bearer "+token)
                .addHeader("Content-Type","application/json")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                return new JSONObject(response.body().string());
            }
        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }
        return null;
    }
}
