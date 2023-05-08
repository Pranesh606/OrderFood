package com.exlfood.Activities;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.DataSets.LanguageDataSet;
import com.exlfood.Databases.LanguageDetailsDB;
import com.exlfood.Databases.OrderTypeDB;
import com.exlfood.R;
import com.exlfood.databinding.AppSplashScreenBinding;

import java.util.Timer;
import java.util.TimerTask;

public class AppSplashScreen extends AppCompatActivity {

    private Timer mTimer;
    AppSplashScreenBinding mAppSplashScreenBinding;
    ImageView img_splash_screen;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageSupport.onAttach(base));
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().getDecorView().setLayoutDirection("ae".equalsIgnoreCase(AppLanguageSupport.getLanguage(AppSplashScreen.this)) ?
                View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        setContentView(R.layout.app_splash_screen);
        mAppSplashScreenBinding = AppSplashScreenBinding.inflate(getLayoutInflater());
        View view = mAppSplashScreenBinding.getRoot();

        setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            int nightModeFlags =
                    getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                window.setStatusBarColor(Color.BLACK);

                // Remove this flag to show the status bar all the time
                window.getDecorView().setSystemUiVisibility(0);
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.white));
            }
        }
        img_splash_screen = view.findViewById(R.id.img_splash_screen);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            int nightModeFlags =
//                    getResources().getConfiguration().uiMode &
//                            Configuration.UI_MODE_NIGHT_MASK;
//            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
//                window.setStatusBarColor(Color.BLACK);
//
//                // Remove this flag to show the status bar all the time
//                window.getDecorView().setSystemUiVisibility(0);
//            } else {
//                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//            }
//        }

        // Other code for your activity...

        //If language not selected when app installed at the time the default language
        // for this app is ENGLISH and ID is 1.
        if (!LanguageDetailsDB.getInstance(AppSplashScreen.this).check_language_selected()) {
            LanguageDataSet mLanguageDataSet = new LanguageDataSet();
            mLanguageDataSet.setLanguageId("1");
            mLanguageDataSet.setName("English");
            mLanguageDataSet.setCode("en");
            LanguageDetailsDB.getInstance(AppSplashScreen.this).insert_language_detail(mLanguageDataSet);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("LongLogTag")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Log.e("updateThemeMainContext  "+"LightTheme","");
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Log.e("updateThemeMainContext  "+"DarkTheme","");
                break;
        }
//        mAppHomeBinding.navigation.setBackground(new ColorDrawable(getResources().getColor(R.color.white)));
//
//        mAppHomeBinding.layoutAppHomeBody.setBackgroundColor(getResources().getColor(R.color.white,activity.getTheme()));
//
//        mAppHomeBinding.viewCartLayout.setBackgroundColor(getResources().getColor(R.color.white_transparent1,activity.getTheme()));
//
//        mAppHomeBinding.AppHomeLayout.setBackgroundColor(getResources().getColor(R.color.white_transparent1,activity.getTheme()));


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(getResources().getColor(R.color.white));
//        window.getDecorView().setSystemUiVisibility(0);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.navigation_bar_color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window1 = getWindow();
            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            int nightModeFlags =
                    getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                window1.setStatusBarColor(Color.BLACK);

                // Remove this flag to show the status bar all the time
                window1.getDecorView().setSystemUiVisibility(0);
            } else {
                window1.setStatusBarColor(getResources().getColor(R.color.white));
                View decor = getWindow().getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
//        mAppHomeBinding.viewCartLayout.setBackgroundColor(getResources().getColor(R.color.white_transparent,this.getTheme()));
//        mAppHomeBinding.navigation.setBackgroundColor(getResources().getColor(R.color.white,this.getTheme()));
        super.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onResume() {
        super.onResume();
        startSplashScreenLoadingTimer();
        homeScreenLoading();
    }

    private void startSplashScreenLoadingTimer() {

        if (mTimer == null) {
            mTimer = new Timer();
        }
    }

    private void stopSplashScreenLoadingTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void homeScreenLoading() {

        if (OrderTypeDB.getInstance(AppSplashScreen.this).getUserServiceType() != null &&
                !OrderTypeDB.getInstance(AppSplashScreen.this).getUserServiceType().isEmpty()) {
            OrderTypeDB.getInstance(AppSplashScreen.this).updateUserServiceType("1");
        } else {
            OrderTypeDB.getInstance(AppSplashScreen.this).addUserServiceType("1");
        }

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getApplicationContext() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadAppHome();
                            stopSplashScreenLoadingTimer();
                            // //Log.e("mTimer", "Running." + String.valueOf(count++));
                        }
                    });
                }
            }
        }, 3000, 3000);

    }

    private void loadAppHome() {
        Intent mIntent = new Intent(AppSplashScreen.this, AppHome.class);
        startActivity(mIntent);
        finish();
    }


}