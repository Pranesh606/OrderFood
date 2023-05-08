package com.exlfood.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exlfood.ApiConnection.ApiClientGson;
import com.exlfood.ApiConnection.RetrofitInterface;
import com.exlfood.CustomClasses.AppFunctions;
import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.CustomClasses.DefaultNames;
import com.exlfood.CustomClasses.NetworkAnalyser;
import com.exlfood.DataSets.ApiResponseCheck;
import com.exlfood.DataSets.Product;
import com.exlfood.Databases.LanguageDetailsDB;
import com.exlfood.Databases.UserDetailsDB;
import com.exlfood.R;
import com.exlfood.databinding.MyOrderInfoWriteReviewBinding;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderInfoWriteReview extends Fragment implements View.OnClickListener{


    private MyOrderInfoWriteReviewBinding mMyOIWriteRBinding;
    private String mVendorName = "",mOrderId = "";
    private RetrofitInterface retrofitInterface;
    private ProgressDialog mProgressDialog;
    float mVendorRating ,mDTimeRating,mQOProductRating,mVOMoneyRating,mOPackagingRating;


    public MyOrderInfoWriteReview() {
        // Required empty public constructor
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
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.my_order_info_write_review, container, false);

        mMyOIWriteRBinding = MyOrderInfoWriteReviewBinding.inflate(inflater,container,false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.loading_please_wait));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        if (getArguments() != null) {
            mVendorName = getArguments().getString(DefaultNames.vendor_name);
            mOrderId = getArguments().getString(DefaultNames.order_id);
        }

        if(getActivity() != null){
            mMyOIWriteRBinding.tvMoIwrPageTitle.setText(mVendorName);
            String mRVTitle = getActivity().getString(R.string.rate_restaurant)+" "+mVendorName;
            mMyOIWriteRBinding.tvMoIwrRateVendorTitle.setText(mRVTitle);
            String mVATitle = getActivity().getString(R.string.rate_rest_by_aspects);
            mMyOIWriteRBinding.tvMoIwrRateVendorByAspectsTitle.setText(mVATitle);
        }

        mMyOIWriteRBinding.imgMoIwrBack.setOnClickListener(this);
        mMyOIWriteRBinding.layMoIwrSubmitBtnContainer.setOnClickListener(this);

        return mMyOIWriteRBinding.getRoot();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {


        // Set the background color of the CoordinatorLayout to white
       mMyOIWriteRBinding.LayoutWriteReview.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
       mMyOIWriteRBinding.LinearLayoutTop.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
       mMyOIWriteRBinding.FrameLayoutTitle.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
       mMyOIWriteRBinding.FrameLayoutTitle.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
       mMyOIWriteRBinding.layMoIwrParent.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));

        mMyOIWriteRBinding.tvMoIwrPageTitle.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        mMyOIWriteRBinding.tvMoIwrRateVendorTitle.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        mMyOIWriteRBinding.tvMoIwrRateVendorByAspectsTitle.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        mMyOIWriteRBinding.deliveryTime.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        mMyOIWriteRBinding.productQuantity.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        mMyOIWriteRBinding.ValueForMoney.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        mMyOIWriteRBinding.orderPackaging.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        mMyOIWriteRBinding.writeReview.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));


        Drawable iconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.svg_arrow_back_48dp);
        iconDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_IN);
        mMyOIWriteRBinding.imgMoIwrBack.setImageDrawable(iconDrawable);

        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {

        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onClick(View view) {
        int mId = view.getId();
        if (mId == R.id.img_mo_iwr_back) {
            if (getActivity() != null) {
                getParentFragmentManager().popBackStack();
            }

        }else if (mId == R.id.lay_mo_iwr_submit_btn_container) {

            if (getActivity() != null) {

                 mVendorRating = mMyOIWriteRBinding.ratingBarMoIwrRateVendor.getRating();
                mDTimeRating = mMyOIWriteRBinding.ratingBarMoIwrDeliveryTime.getRating();
                mQOProductRating = mMyOIWriteRBinding.ratingBarMoIwrQualityOfProduct.getRating();
                mVOMoneyRating = mMyOIWriteRBinding.ratingBarMoIwrValueOfMoney.getRating();
                mOPackagingRating = mMyOIWriteRBinding.ratingBarMoIwrOrderPackaging.getRating();

                if(mVendorRating != 0 && mDTimeRating != 0 && mQOProductRating != 0
                        && mVOMoneyRating != 0 && mOPackagingRating != 0){
                    callAddReviewAPi();
                   // AppFunctions.toastShort(getActivity(),"done");
                }else {
                    if(mVendorRating == 0){
                        String mPleaseRate = getActivity().getResources().getString(R.string.please_rate_the)+" "+mVendorName+".";
                        AppFunctions.msgDialogOk(getActivity(), "", mPleaseRate);
                    }else {
                        if(mDTimeRating == 0){
                            String mPleaseRate = getActivity().getResources().getString(R.string.please_rate_for_delivery_time);
                            AppFunctions.msgDialogOk(getActivity(), "", mPleaseRate);
                        }else {
                            if(mQOProductRating == 0){
                                String mPleaseRate = getActivity().getResources().getString(R.string.please_rate_for_qo_product);
                                AppFunctions.msgDialogOk(getActivity(), "", mPleaseRate);
                            }else {
                                if(mVOMoneyRating == 0){
                                    String mPleaseRate = getActivity().getResources().getString(R.string.please_rate_for_vf_money);
                                    AppFunctions.msgDialogOk(getActivity(), "", mPleaseRate);
                                }else {
                                    if(mOPackagingRating == 0){
                                        String mPleaseRate = getActivity().getResources().getString(R.string.please_rate_for_order_packaging);
                                        AppFunctions.msgDialogOk(getActivity(), "", mPleaseRate);
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }


    }


    private void callAddReviewAPi() {

        if (getActivity() != null) {
            if (AppFunctions.networkAvailabilityCheck(getActivity())) {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put(DefaultNames.order_id, mOrderId);

                    jsonObject.put(DefaultNames.vendor_rating,mVendorRating );
                    jsonObject.put(DefaultNames.delivery_time,mDTimeRating );
                    jsonObject.put(DefaultNames.quality, mQOProductRating);
                    jsonObject.put(DefaultNames.value_for_money,mVOMoneyRating );
                    jsonObject.put(DefaultNames.order_packing,mOPackagingRating );
                    jsonObject.put(DefaultNames.comment,mMyOIWriteRBinding.etMoIwrWriteReviewComment.getText().toString());


                    jsonObject.put(DefaultNames.language_id, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getLanguageId());
                    jsonObject.put(DefaultNames.language_code, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getCode());

                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                    String mCustomerAuthorization = "";
                    if (UserDetailsDB.getInstance(getActivity()).getSizeOfList() > 0) {
                        mCustomerAuthorization = UserDetailsDB.getInstance(getActivity()).getUserDetails().getCustomerKey();
                    }

                    retrofitInterface = ApiClientGson.getClient().create(RetrofitInterface.class);
                    Call<ApiResponseCheck> Call = retrofitInterface.addReviewApi(mCustomerAuthorization,body);
                    mProgressDialog.show();
                    Call.enqueue(new Callback<ApiResponseCheck>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponseCheck> call, @NonNull Response<ApiResponseCheck> response) {


                            mProgressDialog.cancel();

                            if (response.isSuccessful()) {
                                ApiResponseCheck mApiResponseCheck = response.body();

                                if (mApiResponseCheck != null) {
                                    if (mApiResponseCheck.success != null) {
                                        //Api response successDataSet :-

                                        if (getActivity() != null) {

                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                                alertDialogBuilder
                                                        .setMessage(mApiResponseCheck.success.message)
                                                        .setCancelable(false)
                                                        .setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        getParentFragmentManager().popBackStack();
                                                                        dialog.dismiss();


                                                                    }
                                                                }
                                                        );

                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                alertDialog.show();
                                        }


                                    } else {
                                        //Api response failure :-
                                        if (getActivity() != null) {
                                            if (mApiResponseCheck.error != null) {
                                                AppFunctions.msgDialogOk(getActivity(), "", mApiResponseCheck.error.message);
                                            }
                                        }
                                    }
                                }
                            } else {
                                mProgressDialog.cancel();
                                String mErrorMsgToShow = "";
                                try {
                                    ResponseBody requestBody = response.errorBody();
                                    if (requestBody != null) {
                                        mErrorMsgToShow = AppFunctions.apiResponseErrorMsg(getActivity(), requestBody);
                                    } else {
                                        mErrorMsgToShow = getActivity().getString(R.string.process_failed_please_try_again);
                                    }
                                } catch (Exception e) {
                                    // e.printStackTrace();
                                    mErrorMsgToShow = getActivity().getString(R.string.process_failed_please_try_again);
                                }
                                AppFunctions.msgDialogOk(getActivity(), "", mErrorMsgToShow);

                            }


                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiResponseCheck> call, @NonNull Throwable t) {
                            mProgressDialog.cancel();
                        }
                    });


                } catch (JSONException e) {
                    mProgressDialog.cancel();

                    //Log.e("210 Excep ", e.toString());
                    e.printStackTrace();

                }



            } else {
                FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                NetworkAnalyser mNetworkAnalyser = new NetworkAnalyser();
                mFT.replace(R.id.layout_app_check_out_body, mNetworkAnalyser, "mNetworkAnalyser");
                mFT.addToBackStack("mNetworkAnalyser");
                mFT.commit();
            }
        }


    }




}