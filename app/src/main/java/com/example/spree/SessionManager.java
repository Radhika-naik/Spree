package com.example.spree;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "Session";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_PHNO = "phno";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUserInfo(String phno) {
        editor.putString(KEY_PHNO, phno);
        editor.apply();
    }

    public String getKeyPhno() {
        return sharedPreferences.getString(KEY_PHNO, "");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
