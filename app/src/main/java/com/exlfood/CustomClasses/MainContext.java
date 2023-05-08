package com.exlfood.CustomClasses;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.exlfood.Interfaces.ActivityLifecycleCallbacks;
import com.exlfood.R;
import com.onesignal.OneSignal;


public class MainContext extends MultiDexApplication implements MultiDexApplication.ActivityLifecycleCallbacks {


    private static MainContext mInstance;
    private static final String ONESIGNAL_APP_ID = "29479eaf-60d9-4003-9e2d-3b9d904e113e";

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);

        MultiDex.install(this);
        mInstance = this;
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }



    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageSupport.onAttach(base,"en"));
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        updateTheme(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
//        updateTheme(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        updateTheme(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        updateTheme(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
//        updateTheme(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        updateTheme(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
//        updateTheme(activity);
    }

    public void updateTheme(Activity activity) {
        int nightModeFlags = activity.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        boolean isNightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

//        Log.e("updateTheme"+isNightMode,"");

        // Set the appropriate theme based on the system's night mode
        if (isNightMode) {
//            activity.setTheme(R.style.AppThemeDark);
        } else {
//            activity.setTheme(R.style.AppTheme);
        }

        // Explicitly apply the theme to the activity
//        activity.getTheme().applyStyle(activity.getThemeResId(), true);

        // If necessary, call setContentView() to update the activity's layout with the new theme
        // activity.setContentView(R.layout.activity_main);
    }
}
