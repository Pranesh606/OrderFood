package com.exlfood.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exlfood.ApiConnection.ApiClient;
import com.exlfood.ApiConnection.ApiClientGson;
import com.exlfood.ApiConnection.RetrofitInterface;
import com.exlfood.CustomClasses.AppFunctions;
import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.CustomClasses.DefaultNames;
import com.exlfood.CustomClasses.NetworkAnalyser;
import com.exlfood.DataSets.AreaGeoCodeDataSet;
import com.exlfood.DataSets.PaymentMethod;
import com.exlfood.DataSets.Vendor_Info;
import com.exlfood.Databases.AreaGeoCodeDB;
import com.exlfood.Databases.LanguageDetailsDB;
import com.exlfood.Databases.OrderTypeDB;
import com.exlfood.Databases.UserDetailsDB;
import com.exlfood.Interfaces.CartInfo;
import com.exlfood.Interfaces.MakeBottomMarginForViewBasket;
import com.exlfood.R;
import com.exlfood.databinding.FragmentRestaurantDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetails extends Fragment {

    String vendor_id = "";
    RetrofitInterface retrofitInterface;
    private ProgressDialog mProgressDialog;
    public Vendor_Info vendor_info;
    FragmentRestaurantDetailsBinding binding;
    Boolean isFirst = false;
    Activity activity;
    CartInfo cartInfo;

    private MakeBottomMarginForViewBasket mMakeBottomMarginForViewBasket;

    public RestaurantDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vendor_id = getArguments().getString("vendor_id");
        }
        isFirst = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(AppLanguageSupport.onAttach(context));
        if (getActivity() != null) {
            getActivity().getWindow().getDecorView().setLayoutDirection(
                    "ae".equalsIgnoreCase(AppLanguageSupport.getLanguage(getActivity())) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);

        }
        activity = getActivity();
        cartInfo = (CartInfo) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        mView = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        binding = FragmentRestaurantDetailsBinding.inflate(inflater, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.loading_please_wait));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        //rating_frame_layout


        binding.layFrdParent.setVisibility(View.GONE);

        mMakeBottomMarginForViewBasket = (MakeBottomMarginForViewBasket) getActivity();

        activity = getActivity();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        get_vendor_info();
        return binding.getRoot();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        binding.RestaurantInfoFrameLayout.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        binding.RestaurantInfoLinearLayout.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        binding.RestaurantInfoFrameLayout.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        binding.RestaurantInfoFrameLayout.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));


        binding.resName.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.resCuisines.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.reviewText.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.ratingCount.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.RestaurantArea.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.resArea.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.openingHours.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.resOpeningHours.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.deliveryTime.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.resDeliveryTime.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.minOrder.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.resMinOrder.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.deliveryFee.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.resDeliveryFee.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.paymentOption.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));

        Drawable iconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.baseline_arrow_back_black_48dp);
        iconDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_IN);
        binding.backBtn.setImageDrawable(iconDrawable);

//        Drawable iconDrawable1 = ContextCompat.getDrawable(getContext(), R.drawable.ic_restaurant_location);
//        iconDrawable1.setColorFilter(ContextCompat.getColor(getContext(), R.color.ar_filter_title_text_color), PorterDuff.Mode.SRC_IN);
//        binding.areaIcon.setBackgroundDrawable (iconDrawable1);
//
//        Drawable iconDrawable2 = ContextCompat.getDrawable(getContext(), R.drawable.baseline_access_time_black_48dp);
//        iconDrawable2.setColorFilter(ContextCompat.getColor(getContext(), R.color.ar_filter_title_text_color), PorterDuff.Mode.SRC_IN);
//        binding.openingHoursIcon.setBackgroundDrawable (iconDrawable2);
//
//        Drawable iconDrawable3 = ContextCompat.getDrawable(getContext(), R.drawable.ic_food_bike_delivery);
//        iconDrawable3.setColorFilter(ContextCompat.getColor(getContext(), R.color.ar_filter_title_text_color), PorterDuff.Mode.SRC_IN);
//        binding.deliveryTime.setBackgroundDrawable (iconDrawable3);
//
//        Drawable iconDrawable4 = ContextCompat.getDrawable(getContext(), R.drawable.ic_wallet);
//        iconDrawable4.setColorFilter(ContextCompat.getColor(getContext(), R.color.ar_filter_title_text_color), PorterDuff.Mode.SRC_IN);
//        binding.deliveryTime.setBackgroundDrawable (iconDrawable4);
//
//        Drawable iconDrawable5 = ContextCompat.getDrawable(getContext(), R.drawable.ic_bill);
//        iconDrawable5.setColorFilter(ContextCompat.getColor(getContext(), R.color.ar_filter_title_text_color), PorterDuff.Mode.SRC_IN);
//        binding.deliveryFee.setBackgroundDrawable (iconDrawable5);
//
//        Drawable iconDrawable6 = ContextCompat.getDrawable(getContext(), R.drawable.ic_online_card_payment);
//        iconDrawable6.setColorFilter(ContextCompat.getColor(getContext(), R.color.ar_filter_title_text_color), PorterDuff.Mode.SRC_IN);
//        binding.paymentIcon.setBackgroundDrawable (iconDrawable6);

        get_vendor_info();

        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            get_vendor_info();
        }
        cart_product_count();
    }

    private void cart_product_count() {

        if (AppFunctions.networkAvailabilityCheck(activity)) {

            mProgressDialog.show();

            try {
                retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
                String mCustomerAuthorization = "";
                if (AppFunctions.isUserLoggedIn(getActivity())) {
                    mCustomerAuthorization = UserDetailsDB.getInstance(getActivity()).getUserDetails().getCustomerKey();
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(DefaultNames.day_id, AppFunctions.getDayId());
                if (AppFunctions.isUserLoggedIn(getActivity())) {
                    jsonObject.put(DefaultNames.guest_status, "0");
                    jsonObject.put(DefaultNames.guest_id, "");
                } else {
                    jsonObject.put(DefaultNames.guest_status, "1");
                    jsonObject.put(DefaultNames.guest_id, AppFunctions.getAndroidId(getActivity()));
                }
                jsonObject.put(DefaultNames.language_id, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getLanguageId());
                jsonObject.put(DefaultNames.language_code, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getCode());


                RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Call<String> Call = retrofitInterface.cart_product_count(mCustomerAuthorization, body);
                Call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        mProgressDialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (!object.isNull("qty_count")) {
                                if (!object.getString("qty_count").equals("0")) {
                                    if (!object.isNull("total")) {
                                        mMakeBottomMarginForViewBasket.toMakeBottomMarginForViewBasket(true);
                                        cartInfo.cart_info(true, object.getString("qty_count"), object.getString("total"));
                                    } else {
                                        mMakeBottomMarginForViewBasket.toMakeBottomMarginForViewBasket(false);
                                    }
                                } else {
                                    mMakeBottomMarginForViewBasket.toMakeBottomMarginForViewBasket(false);
                                    cartInfo.cart_info(false, "", "");
                                }
                            } else {
                                mMakeBottomMarginForViewBasket.toMakeBottomMarginForViewBasket(false);
                            }
                        } catch (JSONException e) {
                            mMakeBottomMarginForViewBasket.toMakeBottomMarginForViewBasket(false);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        mMakeBottomMarginForViewBasket.toMakeBottomMarginForViewBasket(false);
                        mProgressDialog.cancel();
                    }
                });
            } catch (Exception e) {
                mMakeBottomMarginForViewBasket.toMakeBottomMarginForViewBasket(false);
            }


        } else {
            FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
            NetworkAnalyser mNetworkAnalyser = new NetworkAnalyser();
            mFT.replace(R.id.layout_home_restaurant_body, mNetworkAnalyser, "mNetworkAnalyser");
            mFT.addToBackStack("mNetworkAnalyser");
            mFT.commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFirst = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        isFirst = false;
        cartInfo.cart_info(false, "", "");
        mMakeBottomMarginForViewBasket.toMakeBottomMarginForViewBasket(false);
    }

    private void get_vendor_info(){

        if (AppFunctions.networkAvailabilityCheck(activity)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(DefaultNames.vendor_id, vendor_id);
                jsonObject.put(DefaultNames.day_id, AppFunctions.getDayId());
                jsonObject.put(DefaultNames.vendor_type_id, "");
                String latitude = "", longitude = "";
                if (AreaGeoCodeDB.getInstance(getActivity()).getSizeOfList() > 0) {
                    AreaGeoCodeDataSet mAreaGeoCodeDS = AreaGeoCodeDB.getInstance(getActivity()).getUserAreaGeoCode();
                    latitude = mAreaGeoCodeDS.getmLatitude();
                    longitude = mAreaGeoCodeDS.getmLongitude();
                }
                jsonObject.put(DefaultNames.latitude, latitude);
                jsonObject.put(DefaultNames.longitude, longitude);
                jsonObject.put(DefaultNames.language_id, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getLanguageId());
                jsonObject.put(DefaultNames.language_code, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getCode());
                jsonObject.put("order_type", String.valueOf(OrderTypeDB.getInstance(getActivity()).getUserServiceType()));

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                retrofitInterface = ApiClientGson.getClient().create(RetrofitInterface.class);
                Call<Vendor_Info> Call = retrofitInterface.vendorInfo(body);
                mProgressDialog.show();
                Call.enqueue(new Callback<Vendor_Info>() {
                    @Override
                    public void onResponse(@NonNull Call<Vendor_Info> call, @NonNull Response<Vendor_Info> response) {

                        mProgressDialog.cancel();
                        if (response.isSuccessful()) {
                            vendor_info = response.body();

                            if (vendor_info != null) {

                                if (vendor_info.getVendor() != null) {


                                    if (getActivity() != null) {
                                        if (vendor_info.getSuccess() != null) {
                                            //Api response successDataSet :-

                                            binding.layFrdParent.setVisibility(View.VISIBLE);

                                            binding.resName.setText(vendor_info.getVendor().getName());
                                            AppFunctions.imageLoaderUsingGlide(vendor_info.getVendor().getLogo(), binding.resImage, getActivity());
                                            StringBuilder mCuisine = new StringBuilder();
                                            if (vendor_info.getVendor().getCuisine() != null) {
                                                for (int i = 0; i < vendor_info.getVendor().getCuisine().size(); i++) {
                                                    if (mCuisine.toString().isEmpty()) {
                                                        mCuisine.append(vendor_info.getVendor().getCuisine().get(i).getName());
                                                    } else {
                                                        mCuisine.append(", ").append(vendor_info.getVendor().getCuisine().get(i).getName());
                                                    }
                                                }
                                            }

                                            if (vendor_info.getVendor().getRating() != null) {
                                                String mRATING = vendor_info.getVendor().getRating().getRating();
                                                if (mRATING != null && !mRATING.isEmpty()) {
                                                    binding.ratingCount.setText(mRATING);
                                                    binding.ratingFrameLayout.setVisibility(View.VISIBLE);
                                                } else {
                                                    binding.ratingCount.setText(activity.getResources().getString(R.string.no_rating));
                                                    binding.ratingFrameLayout.setVisibility(View.GONE);
                                                }
                                            } else {
                                                binding.ratingCount.setText(activity.getResources().getString(R.string.no_rating));
                                                binding.ratingFrameLayout.setVisibility(View.GONE);
                                            }

                                            binding.resCuisines.setText(mCuisine.toString());
                                            binding.resArea.setText(vendor_info.getVendor().getAddress());
                                            binding.resDeliveryFee.setText(vendor_info.getVendor().getDeliveryCharge());

                                            String mDTime = vendor_info.getVendor().getDeliveryTime()
                                                    + " " + getActivity().getResources().getString(R.string.mins);
                                            binding.resDeliveryTime.setText(mDTime);


                                            binding.resMinOrder.setText(vendor_info.getVendor().getMinimumAmount());
                                            binding.resOpeningHours.setText(vendor_info.getVendor().getWorking_hours());
                                            binding.paymentRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                                            binding.paymentRecView.setAdapter(new PaymentListAdapter(vendor_info.getVendor().getPaymentMethod()));

                                        } else {
                                            //Api response failure :-
                                            if (getActivity() != null) {
                                                if (vendor_info.error != null) {
                                                    AppFunctions.msgDialogOk(getActivity(), "", vendor_info.error.message);
                                                }
                                            }
                                        }
                                    }


                                } else {
                                    if (vendor_info.getError() != null) {
                                        getDialog(vendor_info.getError().getMessage());
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
                    public void onFailure(@NonNull Call<Vendor_Info> call, @NonNull Throwable t) {
                        mProgressDialog.cancel();
                    }
                });
            } catch (JSONException e) {
                mProgressDialog.cancel();
                e.printStackTrace();
            }
        } else {
            FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
            NetworkAnalyser mNetworkAnalyser = new NetworkAnalyser();
            mFT.replace(R.id.layout_home_restaurant_body, mNetworkAnalyser, "mNetworkAnalyser");
            mFT.addToBackStack("mNetworkAnalyser");
            mFT.commit();
        }


    }

    private void getDialog(String data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(data);
        builder.setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    public class PaymentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<PaymentMethod> paymentMethodArrayList;

        public PaymentListAdapter(ArrayList<PaymentMethod> paymentMethodArrayList) {
            this.paymentMethodArrayList = paymentMethodArrayList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list, parent, false);
            return new PaymentListAdapter.DataObjectHolder(mView);
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            DataObjectHolder holder1 = (DataObjectHolder) holder;
            AppFunctions.imageLoaderUsingGlide(paymentMethodArrayList.get(position).getImage(), holder1.image_payment, getActivity());
            //Glide.with(getActivity()).load(url).apply(getOption("Default")).into(imageView);
        }

        @Override
        public int getItemCount() {
            return paymentMethodArrayList.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {

            ImageView image_payment;

            public DataObjectHolder(View view) {
                super(view);
                image_payment = view.findViewById(R.id.image_payment);
            }
        }

    }

}