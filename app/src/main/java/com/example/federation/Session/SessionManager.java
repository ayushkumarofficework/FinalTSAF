package com.example.federation.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.federation.LoginActivity;
import com.example.federation.PinActivity;

/**
 * Created by Aakash on 14-Apr-17.
 */

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Tata";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_PIN = "pin";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";


    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(String pin,String email,String name) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PIN, pin);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_NAME,name);
        editor.commit();
    }

    public String checkLogin() {


        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else
        {
            String value=pref.getString(KEY_NAME,"");
            return value;
        }
        return null;
    }

    public String getPin()
    {
        return pref.getString(KEY_PIN,"");
    }


        public void logoutUser(){
            editor.clear();
            editor.commit();
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
