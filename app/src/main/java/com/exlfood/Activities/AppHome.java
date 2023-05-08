package com.exlfood.Activities;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.exlfood.CustomClasses.AppFunctions;
import com.exlfood.CustomClasses.MainContext;
import com.exlfood.Fragments.CheckOut;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarMenuView;
import com.google.android.material.navigation.NavigationBarView;
import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.Databases.AreaGeoCodeDB;
import com.exlfood.Databases.DeliveryLocationSearchDB;
import com.exlfood.Fragments.CartList;
import com.exlfood.Fragments.DeliveryLocation;
import com.exlfood.Fragments.FavouriteListFragment;
import com.exlfood.Fragments.ListHome;
import com.exlfood.Fragments.MyAccount;
import com.exlfood.Fragments.MyOrderList;
import com.exlfood.Interfaces.CartInfo;
import com.exlfood.Interfaces.MakeBottomMarginForViewBasket;
import com.exlfood.Interfaces.NavigationBack;
import com.exlfood.Interfaces.NavigationView;
import com.exlfood.R;
import com.exlfood.databinding.AppHomeBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.lang.reflect.Field;

public class AppHome extends AppCompatActivity implements View.OnClickListener, CartInfo,
        MakeBottomMarginForViewBasket, NavigationView, NavigationBack, InstallStateUpdatedListener /*, onSetUpdate */ {

    private AppHomeBinding mAppHomeBinding;
    Activity activity;

    AppUpdateManager appUpdateManager;
    private static int MY_REQUEST_CODE = 4518, AUTOCOMPLETE_REQUEST_CODE = 23487;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageSupport.onAttach(base));
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().getDecorView().setLayoutDirection("ae".equalsIgnoreCase(AppLanguageSupport.getLanguage(AppHome.this)) ?
                View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState){

//        new MainContext().updateTheme(this);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.app_home);
        mAppHomeBinding = AppHomeBinding.inflate(getLayoutInflater());
        View view = mAppHomeBinding.getRoot();
        setContentView(view);
//
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
        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        if(FCMTokenDB.getInstance(AppHome.this).getSizeOfList() > 0){
                            FCMTokenDB.getInstance(AppHome.this).deleteDB();
                        }
                        FCMTokenDB.getInstance(AppHome.this).add(token);
                        if(AppHome.this != null){
                            TestFragment testFragment = new TestFragment();
                            testFragment.show(getSupportFragmentManager(),"testFragment");
                        }
                        AppFunctions.toastShort(AppHome.this,"onComplete called.");
                        // Log and toast
                        String msg = "Token : "+token;
                        Log.e(TAG, msg);
                        //Toast.makeText(AppHome.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        if(CheckDB.getInstance(AppHome.this).getSizeOfList() > 0){
            Log.e("CheckDB" ,"Has list.");
            CheckDB.getInstance(AppHome.this).toPrint();
        }else {
            Log.e("CheckDB" ,"empty list.");
        }*/

        activity = this;

        if (AreaGeoCodeDB.getInstance(AppHome.this).getSizeOfList() > 0) {
            FragmentTransaction mFT = getSupportFragmentManager().beginTransaction();
            ListHome m_listHome = new ListHome();
            Bundle mBundle = new Bundle();
            mBundle.putString("FromHome", "FromHome");
            m_listHome.setArguments(mBundle);
            mFT.replace(R.id.layout_app_home_body, m_listHome, "m_listHome");
            mFT.addToBackStack("m_listHome");
            mFT.commit();
        } else {
            //the DeliveryLocationSearchDB for to save  DeliveryLocationSearch page process.
            //And its used for DeliveryLocation page only.
            //So every time must refresh the DB before going to DeliveryLocation page.
            if (DeliveryLocationSearchDB.getInstance(AppHome.this).getSizeOfList() > 0) {
                DeliveryLocationSearchDB.getInstance(AppHome.this).deleteDB();
            }
            FragmentTransaction mFT = getSupportFragmentManager().beginTransaction();
            DeliveryLocation m_deliveryLocation = new DeliveryLocation();
            mFT.replace(R.id.layout_app_home_body, m_deliveryLocation, "m_deliveryLocation");
            mFT.addToBackStack("m_deliveryLocation");
            mFT.commit();
        }

        BottomNavigationViewHelper.removeShiftMode(mAppHomeBinding.navigation);
        mAppHomeBinding.navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction mFT = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.navigation_home) {
                    ListHome m_listHome = new ListHome();
                    mFT.replace(R.id.layout_app_home_body, m_listHome, "m_listHome");
                    mFT.addToBackStack("m_listHome");
                    mFT.commit();
                    return true;
                } else if (item.getItemId() == R.id.navigation_favourites) {

                    cart_info(false, "", "");

                    FavouriteListFragment favouriteList = new FavouriteListFragment();
                    mFT.replace(R.id.layout_app_home_body, favouriteList, "favouriteList");
                    mFT.addToBackStack("favouriteList");
                    mFT.commit();
                    return true;
                } else if (item.getItemId() == R.id.navigation_my_orders) {

                    cart_info(false, "", "");

                    MyOrderList m_myOrderList = new MyOrderList();
                    mFT.replace(R.id.layout_app_home_body, m_myOrderList, "m_myOrderList");
                    mFT.addToBackStack("m_myOrderList");
                    mFT.commit();
                    return true;
                } else if (item.getItemId() == R.id.navigation_account) {

                    cart_info(false, "", "");

                    MyAccount myAccount = new MyAccount();
                    mFT.replace(R.id.layout_app_home_body, myAccount, "myAccount");
                    mFT.addToBackStack("myAccount");
                    mFT.commit();
                    return true;
                }
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            appUpdateManager = AppUpdateManagerFactory.create(this);
            appUpdateManager.registerListener(this);
            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnSuccessListener(appUpdateInfo -> {

                        //UPDATE_AVAILABLE = 2
                        //DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS = 3
                        //UNKNOWN = 0
                        //UPDATE_NOT_AVAILABLE = 1

                        // Log.e("packageName", appUpdateInfo.packageName());
                        //  Log.e("availableVersionCode", "" + appUpdateInfo.availableVersionCode());
                        //  Log.e("installStatus", "" + appUpdateInfo.installStatus());
                        //  Log.e("isUpdateTypeAllowed", "" + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE));
                        //  Log.e("updateAvailability", "" + appUpdateInfo.updateAvailability());
                        // Log.e("UPDATE_AVAILABLE", "" + UpdateAvailability.UPDATE_AVAILABLE);

                        String s = "packageName=" + appUpdateInfo.packageName() + "\n" +
                                "availableVersionCode=" + appUpdateInfo.availableVersionCode() + "\n" +
                                "installStatus=" + appUpdateInfo.installStatus() + "\n" +
                                "isUpdateTypeAllowed" + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) + "\n" +
                                "updateAvailability" + appUpdateInfo.updateAvailability() + "\n" +
                                "UPDATE_AVAILABLE" + UpdateAvailability.UPDATE_AVAILABLE;

                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {   //  check for the type of update flow you want
                            requestUpdate(appUpdateInfo);
                            // Log.e("Update", "Available");
                            //  Constant.showToast("Update-Available");
                        }
                    });
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
//      mAppHomeBinding.navigation.setBackground(new ColorDrawable(getResources().getColor(R.color.white)));
        mAppHomeBinding.viewCartLayout.setBackgroundColor(getResources().getColor(R.color.white,activity.getTheme()));
        mAppHomeBinding.AppHomeLayout.setBackgroundColor(getResources().getColor(R.color.white_transparent1,activity.getTheme()));
        mAppHomeBinding.layoutAppHomeBody.setBackgroundColor(getResources().getColor(R.color.white,activity.getTheme()));
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//      window.setStatusBarColor(getResources().getColor(R.color.white));
//      window.getDecorView().setSystemUiVisibility(0);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.navigation_bar_color));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window1 = getWindow();
            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int nightModeFlags =
                    getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES){
                window1.setStatusBarColor(Color.BLACK);
                // Remove this flag to show the status bar all the time
                window1.getDecorView().setSystemUiVisibility(0);
            }else {
                window1.setStatusBarColor(getResources().getColor(R.color.white));
                View decor = getWindow().getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
//      mAppHomeBinding.viewCartLayout.setBackgroundColor(getResources().getColor(R.color.white_transparent,this.getTheme()));
        mAppHomeBinding.navigation.setBackgroundColor(getResources().getColor(R.color.white,this.getTheme()));
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void navigationView(Boolean value) {
        if (value) {
            mAppHomeBinding.navigation.setVisibility(View.VISIBLE);
        } else {
            mAppHomeBinding.navigation.setVisibility(View.GONE);
        }
    }
    @Override
    public void navigationBack(int type) {
        if (type == 1) {
            mAppHomeBinding.navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        }
        if (type == 2) {
            mAppHomeBinding.navigation.getMenu().findItem(R.id.navigation_favourites).setChecked(true);
        }
        if (type == 3) {
            mAppHomeBinding.navigation.getMenu().findItem(R.id.navigation_my_orders).setChecked(true);
        }
        if (type == 4) {
            mAppHomeBinding.navigation.getMenu().findItem(R.id.navigation_account).setChecked(true);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Log.e("RESULT_OK: ", "" + resultCode);
                AppFunctions.toastShort(activity, "Update Completed!");
            } else {
                // If the update is cancelled or fails,
                // you can request to start the update again.
                if (resultCode == RESULT_CANCELED) {
                    // Log.e("RESULT_CANCELED: ", "" + resultCode);
                    AppFunctions.toastShort(activity, "Update Canceled!");
                } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                    // Log.e("RESULT_UPDATE_FAILED: ", "" + resultCode);
                    AppFunctions.toastShort(activity, "Update Failed!");
                } else {
                    AppFunctions.toastShort(activity, "Update Failed!");
                    // Log.e("Result code other: ", "" + resultCode);
                }
            }
        }
    }
    @Override
    public void onStateUpdate(@NonNull InstallState installState) {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            notifyUser();
        }
    }
    private void notifyUser() {
        Snackbar snackbar =
                Snackbar.make(
                        mAppHomeBinding.layoutAppHomeBody,
                        getResources().getString(R.string.an_update_dowmloaded),
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getResources().getString(R.string.restart), view -> {
                    appUpdateManager.completeUpdate();
                    appUpdateManager.unregisterListener(this);
                }
        );
        snackbar.setActionTextColor(
                getResources().getColor(R.color.white));
        snackbar.show();
    }
    private void requestUpdate(AppUpdateInfo appUpdateInfo) {
        if (appUpdateManager != null) {
            try {
                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE, //  HERE specify the type of update flow you want
                        this,   //  the instance of an activity
                        MY_REQUEST_CODE
                );
            } catch (Exception e) {
                //  Log.e("requestUpdate excep", e.toString());
                AppFunctions.toastShort(activity, "RUException: " + e.toString());
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(this);
        }
    }
    static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView navigation) {
            NavigationBarMenuView bottomNavigationMenuView = (NavigationBarMenuView) navigation.getChildAt(0);
            try {
                Field shiftingMode = bottomNavigationMenuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(bottomNavigationMenuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
                    NavigationBarItemView bottomNavigationItemView = (NavigationBarItemView) bottomNavigationMenuView.getChildAt(i);
                    bottomNavigationItemView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_AUTO);
                    bottomNavigationItemView.setChecked(bottomNavigationItemView.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                // AppFunctions.toastLong(MainContext.getAppContext(), "N:" + e.toString());
                // e.printStackTrace();
            } catch (IllegalAccessException e) {
                //  AppFunctions.toastLong(MainContext.getAppContext(), "ILL:" + e.toString());
                // e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        backwardNavigation();
    }
    private void backwardNavigation() {

        FragmentManager mFragmentMgr = getSupportFragmentManager();

        for (int i = 0; i < mFragmentMgr.getBackStackEntryCount(); i++) {
            Log.e("" + i, "" + mFragmentMgr.getBackStackEntryAt(i));
            Log.e("" + i, "" + mFragmentMgr.getBackStackEntryAt(i).getName());
            /* if(mFragmentMgr.getBackStackEntryAt(i).getName() != null){
                if(mFragmentMgr.getBackStackEntryAt(i).getName().equals("mDLAndALBottomSheet")){
                    mFragmentMgr.popBackStack();
                    break;
                }
            } */

        }

        if (mFragmentMgr.getBackStackEntryCount() > 1) {
            mFragmentMgr.popBackStack();
        } else {
            finish();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();

        if (appUpdateManager != null) {
            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnCompleteListener(appUpdateInfo -> {
//                              AppFunctions.toastLong(AppHome.this,"Update Completed.");
                    });
            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnFailureListener(appUpdateInfo -> {
                        // AppFunctions.toastLong(AppHome.this, "GPFailedMsg : " + appUpdateInfo.getMessage());
//                            Log.e("onResume: ",appUpdateInfo.getMessage());
                    });

            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnSuccessListener(appUpdateInfo -> {
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            notifyUser();
                        }
                    });
        }
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
    public void cart_info(Boolean data, String count, String total) {
        if (data) {
            mAppHomeBinding.viewBasketLinear.setVisibility(View.VISIBLE);
            mAppHomeBinding.cartCount.setText(count);
            mAppHomeBinding.cartPrice.setText(total);
            mAppHomeBinding.viewBasketLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction mFT = getSupportFragmentManager().beginTransaction();
                    CartList m_cartList = new CartList();
                    mFT.replace(R.id.layout_app_home_body, m_cartList, "m_cartList");
                    mFT.addToBackStack("m_cartList");
                    mFT.commit();
                }
            });
        } else {
            mAppHomeBinding.viewBasketLinear.setVisibility(View.GONE);
//            mAppHomeBinding.viewCartLayout.setVisibility(View.GONE);
        }
    }

    private void toMakeBottomMarginForViewBasketUI(Boolean isShow) {

        if (isShow) {

            //To create bottom margin :-
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._65sdp));
            mAppHomeBinding.layoutAppHomeBody.setLayoutParams(layoutParams);

        } else {

            //To remove bottom margin
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, 0);
            mAppHomeBinding.layoutAppHomeBody.setLayoutParams(layoutParams);

        }
    }

    @Override
    public void toMakeBottomMarginForViewBasket(Boolean toMakeMargin) {
        toMakeBottomMarginForViewBasketUI(toMakeMargin);
    }
//    @Override
//    public void reload() {
//        FragmentTransaction mFT = getSupportFragmentManager().beginTransaction();
//        ListHome m_listHome = new ListHome();
//        Bundle mBundle = new Bundle();
//        mBundle.putString("FromHome", "FromHome");
//        m_listHome.setArguments(mBundle);
//        mFT.replace(R.id.layout_app_home_body, m_listHome, "m_listHome");
//        mFT.addToBackStack("m_listHome");
//        mFT.detach(m_listHome);
//        mFT.attach(m_listHome);
//        mFT.commit();
//    }
}