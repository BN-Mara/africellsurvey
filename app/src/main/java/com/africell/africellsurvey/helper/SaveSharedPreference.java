package com.africell.africellsurvey.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.africell.africellsurvey.model.User;

/**
 * SharedPreference management class
 */
public class SaveSharedPreference {
    private static final String LOGGED_IN_PREF = "login_status";
    private static final String USER_NAME = "logged_username";
    private static final String PASSWD = "logged_password";

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static void setLoggedIn(Context context, User user, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(USER_NAME, user.getUsername());
        editor.putString(PASSWD,user.getPasswd());
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }
    public static void addToShared(Context context, String key, String value){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getShared(Context context,String key) {
        return getPreferences(context).getString(key, null);
    }
    public static void deleteShared(Context contex, String key){
        getPreferences(contex).edit().remove(key).apply();
    }

}
