package com.mobile.docktalk.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SavingLocalData {

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
