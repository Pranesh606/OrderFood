package com.exlfood.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.Fragments.ListHome;
import com.exlfood.Fragments.Login;
import com.exlfood.R;
import com.exlfood.databinding.AppLoginBinding;

public class AppLogin extends AppCompatActivity implements View.OnClickListener{

    AppLoginBinding mAppLoginBinding;

    public AppLogin(){

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageSupport.onAttach(base));
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().getDecorView().setLayoutDirection("ae".equalsIgnoreCase(AppLanguageSupport.getLanguage(AppLogin.this)) ?
                View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.app_login);
        mAppLoginBinding = AppLoginBinding.inflate(getLayoutInflater());
        View view = mAppLoginBinding.getRoot();
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

        FragmentTransaction mFT = getSupportFragmentManager().beginTransaction();
        Login m_login = new Login();
        mFT.replace(R.id.layout_app_login_body, m_login, "m_login");
        mFT.addToBackStack("m_login");
        mFT.commit();

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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        int mId = view.getId();

    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        backwardNavigation();
    }

    private void backwardNavigation() {

        FragmentManager mFragmentMgr = getSupportFragmentManager();
        if (mFragmentMgr.getBackStackEntryCount() > 1) {
            mFragmentMgr.popBackStack();
        } else {
            finish();
        }

    }


}