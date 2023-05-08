package com.exlfood.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.onesignal.OneSignal;
import com.exlfood.Activities.AppHome;
import com.exlfood.Activities.AppLogin;
import com.exlfood.ApiConnection.ApiClient;
import com.exlfood.ApiConnection.RetrofitInterface;
import com.exlfood.CustomClasses.AppFunctions;
import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.CustomClasses.DefaultNames;
import com.exlfood.CustomClasses.NetworkAnalyser;
import com.exlfood.Databases.AreaGeoCodeDB;
import com.exlfood.Databases.UserDetailsDB;
import com.exlfood.Interfaces.CartInfo;
import com.exlfood.Interfaces.NavigationBack;
import com.exlfood.Interfaces.NavigationView;
import com.exlfood.R;
import com.exlfood.databinding.FragmentMyAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccount extends Fragment implements View.OnClickListener {

    FragmentMyAccountBinding binding;
    ProgressDialog mProgressDialog;
    RetrofitInterface retrofitInterface;
    Activity activity;
    NavigationView navigationView;
    NavigationBack navigationBack;

    public MyAccount() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(AppLanguageSupport.onAttach(context));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity() != null) {

                getActivity().getWindow().getDecorView().setLayoutDirection(
                        "ae".equalsIgnoreCase(AppLanguageSupport.getLanguage(getActivity())) ?
                                View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);

            }
        }
        activity = getActivity();
        navigationView = (NavigationView) context;
        navigationBack = (NavigationBack) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountBinding.inflate(inflater, container, false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.loading_please_wait));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        binding.layoutLhMenuLogoutParent.setOnClickListener(this);
        binding.layLhProfileDetailsContainer.setOnClickListener(this);
        binding.layoutLhMenuAboutUsParent.setOnClickListener(this);
        binding.layoutLhMenuChangePwdParent.setOnClickListener(this);
        binding.layoutLhMenuMyAddressesParent.setOnClickListener(this);
        binding.layoutLhMenuChangeLanguageParent.setOnClickListener(this);
        binding.layoutLhMenuContactUsParent.setOnClickListener(this);
        binding.layoutLhMenuMyFavouritesParent.setOnClickListener(this);
        binding.layoutLhMenuMyOrdersParent.setOnClickListener(this);
        binding.layoutLhMenuDeleteAccountParent.setOnClickListener(this);
        binding.layoutLhMenuPrivacyPolicyParent.setOnClickListener(this);
        binding.layoutLhMenuTermsAndConditionsParent.setOnClickListener(this);

        return binding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        binding.myAccountLayout.setBackgroundColor(getResources().getColor(R.color.white,activity.getTheme()));
        binding.layoutListHomeMenuSideBar.setBackgroundColor(getResources().getColor(R.color.white,activity.getTheme()));
        binding.layLhProfileDetailsContainer.setBackgroundColor(getResources().getColor(R.color.white,activity.getTheme()));
        binding.scrollView.setBackgroundColor(getResources().getColor(R.color.white,activity.getTheme()));
        binding.tvLhUserName.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhMyAddresses.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhMyOrders.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhMyFavourites.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhChangePwd.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhChangeLanguage.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhContactUs.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhAboutUs.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhPrivacyPolicy.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhTermsAndConditions.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhDeleteAccount.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvLhLogout.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));


        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onClick(View v) {
        int mRestaurantViewId = v.getId();
        if (mRestaurantViewId == R.id.lay_lh_profile_details_container) {
            if (getActivity() != null) {
                if (UserDetailsDB.getInstance(getActivity()).getSizeOfList() > 0) {
                    //User currently logged in :-
                    FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                    EditProfile m_editProfile = new EditProfile();
                    mFT.replace(R.id.layout_app_home_body, m_editProfile, "m_editProfile");
                    mFT.addToBackStack("m_editProfile");
                    mFT.commit();
                } else {
                    AppFunctions.toastShort(getActivity(), getActivity().getString(R.string.please_login_to_proceed_further));
                    Intent intent = new Intent(getActivity(), AppLogin.class);
                    getActivity().startActivity(intent);
                }
            }
        } else if (mRestaurantViewId == R.id.layout_lh_menu_my_orders_parent) {
            if (getActivity() != null) {
                //User currently logged in :-
                FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                MyOrderList m_myOrderList = new MyOrderList();
                mFT.replace(R.id.layout_app_home_body, m_myOrderList, "m_myOrderList");
                mFT.addToBackStack("m_myOrderList");
                mFT.commit();
            }
        }
        else if (mRestaurantViewId == R.id.layout_lh_menu_my_addresses_parent) {
            if (getActivity() != null) {

                if (UserDetailsDB.getInstance(getActivity()).getSizeOfList() > 0) {
                    //User currently logged in :-
                    FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                    CheckoutAddressList m_CheckoutAddressList = new CheckoutAddressList();
                    Bundle mBundle = new Bundle();
                    mBundle.putString(DefaultNames.addressBook_callFrom, DefaultNames.callFor_MyAccountAddsBook);
                    m_CheckoutAddressList.setArguments(mBundle);
                    mFT.replace(R.id.layout_app_home_body, m_CheckoutAddressList, "m_CheckoutAddressList");
                    mFT.addToBackStack("m_CheckoutAddressList");
                    mFT.commit();
                } else {
                    AppFunctions.toastShort(getActivity(), getActivity().getString(R.string.please_login_to_proceed_further));
                    Intent intent = new Intent(getActivity(), AppLogin.class);
                    getActivity().startActivity(intent);
                }
            }
        } else if (mRestaurantViewId == R.id.layout_lh_menu_change_pwd_parent) {

            if (getActivity() != null) {
                if (UserDetailsDB.getInstance(getActivity()).getSizeOfList() > 0) {
                    //User currently logged in :-
                    FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                    ChangePassword m_changePassword = new ChangePassword();
                    mFT.replace(R.id.layout_app_home_body, m_changePassword, "m_changePassword");
                    mFT.addToBackStack("m_changePassword");
                    mFT.commit();
                } else {
                    AppFunctions.toastShort(getActivity(), getActivity().getString(R.string.please_login_to_proceed_further));
                    Intent intent = new Intent(getActivity(), AppLogin.class);
                    getActivity().startActivity(intent);
                }
            }
        } else if (mRestaurantViewId == R.id.layout_lh_menu_contact_us_parent) {
            FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
            ContactUs m_contactUs = new ContactUs();
            mFT.replace(R.id.layout_app_home_body, m_contactUs, "m_contactUs");
            mFT.addToBackStack("m_contactUs");
            mFT.commit();
        } else if (mRestaurantViewId == R.id.layout_lh_menu_my_favourites_parent) {
            FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
            FavouriteListFragment favouriteList = new FavouriteListFragment();
            mFT.replace(R.id.layout_app_home_body, favouriteList, "favouriteList");
            mFT.addToBackStack("favouriteList");
            mFT.commit();
        } else if (mRestaurantViewId == R.id.layout_lh_menu_about_us_parent) {
            DialogueWebView mDialogueWebView = new DialogueWebView();
            Bundle mBundle = new Bundle();
            mBundle.putString(DefaultNames.from, DefaultNames.AboutUs);
            mBundle.putString(DefaultNames.thePageCallFrom, DefaultNames.thePageCall_ForHomeLay);
            mDialogueWebView.setArguments(mBundle);
            mDialogueWebView.show(getParentFragmentManager(), "mDialogueWebView");
        } else if (mRestaurantViewId == R.id.layout_lh_menu_privacy_policy_parent) {
            DialogueWebView mDialogueWebView = new DialogueWebView();
            Bundle mBundle = new Bundle();
            mBundle.putString(DefaultNames.from, DefaultNames.PrivacyPolicy);
            mBundle.putString(DefaultNames.thePageCallFrom, DefaultNames.thePageCall_ForHomeLay);
            mDialogueWebView.setArguments(mBundle);
            mDialogueWebView.show(getParentFragmentManager(), "mDialogueWebView");
        } else if (mRestaurantViewId == R.id.layout_lh_menu_terms_and_conditions_parent) {
            DialogueWebView mDialogueWebView = new DialogueWebView();
            Bundle mBundle = new Bundle();
            mBundle.putString(DefaultNames.from, DefaultNames.TermsAndConditions);
            mBundle.putString(DefaultNames.thePageCallFrom, DefaultNames.thePageCall_ForHomeLay);
            mDialogueWebView.setArguments(mBundle);
            mDialogueWebView.show(getParentFragmentManager(), "mDialogueWebView");
        } else if (mRestaurantViewId == R.id.layout_lh_menu_change_language_parent) {
            // AppFunctions.toastShort(getActivity(),"change_language");
            if (getActivity() != null) {
                Language m_language = new Language();
                m_language.show(getParentFragmentManager(), "m_language");
            }
        } else if (mRestaurantViewId == R.id.layout_lh_menu_delete_account_parent) {
            if (getActivity() != null) {
                if (UserDetailsDB.getInstance(getActivity()).getSizeOfList() > 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder
                            .setMessage(getActivity().getString(R.string.delete_account_warning_msg))
                            .setCancelable(false)
                            .setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                                    DeleteAccount m_DeleteAccount = new DeleteAccount();
                                    mFT.replace(R.id.layout_app_home_body, m_DeleteAccount, "m_DeleteAccount");
                                    mFT.addToBackStack("m_DeleteAccount");
                                    mFT.commit();

                                }
                            })
                            .setNegativeButton(getActivity().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    AppFunctions.toastShort(getActivity(), getActivity().getString(R.string.please_login_to_proceed_further));
                    Intent intent = new Intent(getActivity(), AppLogin.class);
                    getActivity().startActivity(intent);
                }
            }
        } else if (mRestaurantViewId == R.id.layout_lh_menu_logout_parent) {
            log_out();
        }
    }

    private void log_out() {
        mProgressDialog.show();
        if (AppFunctions.networkAvailabilityCheck(activity)) {
            JSONObject object = new JSONObject();
            try {
                if (OneSignal.getDeviceState() != null && OneSignal.getDeviceState().getUserId() != null) {
                    object.put(DefaultNames.push_id, OneSignal.getDeviceState().getUserId());
                } else {
                    object.put(DefaultNames.push_id, "");
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());
                retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
                String mCustomerAuthorization = "";
                if (AppFunctions.isUserLoggedIn(getActivity())) {
                    mCustomerAuthorization = UserDetailsDB.getInstance(getActivity()).getUserDetails().getCustomerKey();
                }
                Call<String> call = retrofitInterface.log_out(mCustomerAuthorization, body);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if (!jsonObject.isNull("success")) {
                                    if (getActivity() != null) {
                                        if (UserDetailsDB.getInstance(getActivity()).getSizeOfList() > 0) {
                                            //User currently logged in :-
                                            UserDetailsDB.getInstance(getActivity()).deleteUserDetailsDB();
                                        }
                                        Intent intent = new Intent(getActivity(), AppHome.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                    JSONObject object1 = jsonObject.getJSONObject("success");
                                    AppFunctions.toastShort(activity, object1.getString("message"));
                                } else if (!jsonObject.isNull("error")) {
                                    JSONObject object1 = jsonObject.getJSONObject("error");
                                    AppFunctions.toastShort(activity, object1.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mProgressDialog.cancel();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        mProgressDialog.cancel();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                mProgressDialog.cancel();
            }
        } else {
            mProgressDialog.cancel();
            FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
            NetworkAnalyser mNetworkAnalyser = new NetworkAnalyser();
            mFT.replace(R.id.layout_app_home_body, mNetworkAnalyser, "mNetworkAnalyser");
            mFT.addToBackStack("mNetworkAnalyser");
            mFT.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationBack.navigationBack(4);
        navigationView.navigationView(true);

        if (getActivity() != null) {
            if (UserDetailsDB.getInstance(getActivity()).getSizeOfList() > 0) {
                //User currently logged in :-
                binding.layoutLhMenuLogoutParent.setVisibility(View.VISIBLE);
                binding.layoutLhMenuDeleteAccountParent.setVisibility(View.VISIBLE);
                String mImg = UserDetailsDB.getInstance(getActivity()).getUserDetails().getImage();
                if (mImg != null && !mImg.isEmpty()) {
                    //Toast.makeText(getContext(), "Image "+mImg, Toast.LENGTH_SHORT).show();
//                    Log.e("Image path ",mImg);
                    String mFinalPath = mImg.replace("\\", "");
                    //  Log.e("mImg",mImg);
                    //  Log.e("mFinalPath",mFinalPath);
                    Glide.with(getActivity()).load(mFinalPath).into(binding.imgViewLhProfileImg);
                } else {
                    binding.imgViewLhProfileImg.setImageResource(R.drawable.svg_account_circle_48dp);
                }
                String mUserName = UserDetailsDB.getInstance(getActivity()).getUserDetails().getFirstName() +
                        " " + UserDetailsDB.getInstance(getActivity()).getUserDetails().getLastName();
                binding.tvLhUserName.setText(mUserName);
            } else {
                //User currently logged out :-
                binding.layoutLhMenuLogoutParent.setVisibility(View.GONE);
                binding.layoutLhMenuDeleteAccountParent.setVisibility(View.GONE);
                binding.imgViewLhProfileImg.setImageResource(R.drawable.svg_account_circle_48dp);
                binding.tvLhUserName.setText(getActivity().getResources().getString(R.string.login_to));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.navigationView(false);
    }

}