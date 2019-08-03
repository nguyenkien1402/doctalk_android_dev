package com.mobile.docktalk.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SavingLocalData {

    public static String ShareUserData = "UserInfo";
    public static String TOKEN = "Token";
    public static String USERID = "UserId";
    public static String PATIENTID = "PatientId";
    public static String EMAIL = "Email";
    public static String FIRSTNAME = "FirstName";
    public static String LASTNAME = "LastName";
    public static void saveInSharePreferences(Context context,String name, String key, String value){
        SharedPreferences pref = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static void saveInSharePreferences(Context context,String name, String key, int value){
        SharedPreferences pref = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key,value);
        editor.commit();
    }
}
