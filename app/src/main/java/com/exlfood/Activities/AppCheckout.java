package com.exlfood.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.CustomClasses.DefaultNames;
import com.exlfood.DataSets.CheckOutDBDataSet;
import com.exlfood.Databases.CheckOutDetailsDB;
import com.exlfood.Databases.UserDetailsDB;
import com.exlfood.Fragments.CheckOut;
import com.exlfood.Fragments.OrderConfirmation;
import com.exlfood.Interfaces.CheckOutBackPress;
import com.exlfood.Interfaces.OrderConfirmBackPressUI;
import com.exlfood.R;
import com.exlfood.databinding.AppCheckoutBinding;

import java.util.List;

public class AppCheckout extends AppCompatActivity implements View.OnClickListener, CheckOutBackPress, OrderConfirmBackPressUI {

    AppCheckoutBinding mAppCheckoutBinding;
    private Boolean mIsCheckOutSuccess = false;
    private boolean mTrackOrderShowing;
    private LinearLayout mConfirmOrderUi;
    private LinearLayout mTrackOderUi;
    private TextView mPageTitle;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageSupport.onAttach(base));
    }

    public AppCheckout() {

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().getDecorView().setLayoutDirection("ae".equalsIgnoreCase(AppLanguageSupport.getLanguage(AppCheckout.this)) ?
                View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.app_checkout);
        mAppCheckoutBinding = AppCheckoutBinding.inflate(getLayoutInflater());
        View view = mAppCheckoutBinding.getRoot();
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

        if (getIntent().getStringExtra("info") != null) {
            FragmentTransaction mFT = getSupportFragmentManager().beginTransaction();
            OrderConfirmation m_orderConfirmation = new OrderConfirmation();
            Bundle bundle = new Bundle();
            bundle.putString(DefaultNames.from, "order_info");
            bundle.putString(DefaultNames.order_id, getIntent().getStringExtra("info"));
            m_orderConfirmation.setArguments(bundle);
            mFT.replace(R.id.layout_app_check_out_body, m_orderConfirmation, "m_orderConfirmation");
            mFT.addToBackStack("m_orderConfirmation");
            mFT.commit();
        } else {

            if (CheckOutDetailsDB.getInstance(AppCheckout.this).getSizeOfList() > 0) {
                CheckOutDetailsDB.getInstance(AppCheckout.this).deleteDB();
            }

            //For fresh checkout details gathering :-
            CheckOutDBDataSet mCheckOutDBDs = new CheckOutDBDataSet();
            mCheckOutDBDs.setCouponId("");
            mCheckOutDBDs.setCouponCode("");
            mCheckOutDBDs.setAddressId("");
            mCheckOutDBDs.setPaymentListId("");
            mCheckOutDBDs.setContactLessDeliveryChecked("");
            mCheckOutDBDs.setPaymentCode("");
            CheckOutDetailsDB.getInstance(AppCheckout.this).add(mCheckOutDBDs);

            FragmentTransaction mFT = getSupportFragmentManager().beginTransaction();
            CheckOut m_checkOut = new CheckOut();
            Bundle bundle = new Bundle();
            bundle.putString("vendor_id",getIntent().getStringExtra("vendor_id"));
            m_checkOut.setArguments(bundle);
            mFT.replace(R.id.layout_app_check_out_body, m_checkOut, "m_checkOut");
            mFT.addToBackStack("m_checkOut");
            mFT.commit();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("LongLogTag")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
//        mAppCheckoutBinding.layoutAppCheckOutBody.setBackgroundColor(getResources().getColor(R.color.white_transparent1,this.getTheme()));
//        mAppCheckoutBinding.CheckOutLayout.setBackgroundColor(getResources().getColor(R.color.white_transparent1,this.getTheme()));

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

        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onClick(View view) {
        int mId = view.getId();
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
    public void onBackPressed() {
        Log.e("91", "called - " + mIsCheckOutSuccess);
        if (mIsCheckOutSuccess) {
            //This case occur, the user press back button when currently in checkout success page.
            //mTrackOrderShowing,mConfirmOrderUi,mTrackOderUi
            if (mTrackOrderShowing) {
                mTrackOrderShowing = false;
                if (mConfirmOrderUi != null && mTrackOderUi != null && mPageTitle != null) {
                    mConfirmOrderUi.setVisibility(View.VISIBLE);
                    mTrackOderUi.setVisibility(View.GONE);
                    mPageTitle.setText(getResources().getString(R.string.oc_order_confirmation));
                }
            } else {
                checkOutSuccessBackPressed();
            }
        } else {
            backwardNavigation();
        }
    }

    private void backwardNavigation() {

        //  //Log.e("92","called - "+mIsCheckOutSuccess);
        FragmentManager mFragmentMgr = getSupportFragmentManager();
        if (mFragmentMgr.getBackStackEntryCount() > 1) {
            mFragmentMgr.popBackStack();
        } else {
            finish();
        }

    }


    @Override
    public void checkOutBackPressed() {

        // //Log.e("105","called - "+mIsCheckOutSuccess);

        if (mIsCheckOutSuccess) {
            //This case occur, the user press back button when currently in checkout success page.
            checkOutSuccessBackPressed();
        } else {
            backwardNavigation();
        }

    }

    @Override
    public void checkOutSuccessBackPressed() {

        //Log.e("132","called - "+mIsCheckOutSuccess);

        Intent intent = new Intent(AppCheckout.this, AppHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void checkOutSuccessStatus(Boolean successStatus) {
        mIsCheckOutSuccess = successStatus;

        // Log.e("146","called - "+mIsCheckOutSuccess);

    }


    @Override
    public void orderConfirmBackPressUI(boolean trackOrderShowing, LinearLayout confirmOrderUi,
                                        LinearLayout trackOderUi, TextView pageTitle) {
        //mTrackOrderShowing,mConfirmOrderUi,mTrackOderUi
        mTrackOrderShowing = trackOrderShowing;
        mConfirmOrderUi = confirmOrderUi;
        mTrackOderUi = trackOderUi;
        mPageTitle = pageTitle;

    }
}