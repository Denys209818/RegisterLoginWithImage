package com.example.accountservicewithimage.network;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.accountservicewithimage.security.IJwtSecurityService;

public class HomeApplication extends Application implements IJwtSecurityService {
    private static HomeApplication _application;
    private static Context _context;
    private static int _activityId;

    public static void setAppContext(Context context)
    {
        _context = context;
    }

    public static HomeApplication getApplication()
    {
        return _application;
    }

    public static Context getContext()
    {
        return _context;
    }

    public static int getActivityId()
    {
       return _activityId;
    }

    public static void setActivityId(int id)
    {
        _activityId = id;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _application = this;
        setAppContext(this.getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void saveToken(String token) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        preferences = getSharedPreferences("jwtToken", MODE_PRIVATE);
        editor = preferences.edit();

        editor.putString("token", token);
        editor.commit();
    }

    @Override
    public String getToken() {
        SharedPreferences preferences = getSharedPreferences("jwtToken", MODE_PRIVATE);
        return preferences.getString("token", "defaultValue");
    }

    @Override
    public void deleteToken() {
        SharedPreferences preferences = getSharedPreferences("jwtToken", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        try
        {
            editor.remove("token");
            editor.commit();
            editor.apply();

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
