package com.exlfood.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exlfood.ApiConnection.ApiClientGson;
import com.exlfood.ApiConnection.RetrofitInterface;
import com.exlfood.CustomClasses.AppFunctions;
import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.CustomClasses.DefaultNames;
import com.exlfood.CustomClasses.NetworkAnalyser;
import com.exlfood.DataSets.ReviewListDataSet;
import com.exlfood.Databases.LanguageDetailsDB;
import com.exlfood.DataSets.ReviewListApi;
import com.exlfood.R;
import com.exlfood.databinding.FragmentRatingListBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingList extends Fragment {

    FragmentRatingListBinding binding;
    Activity activity;
    private int mPageCount = 1;
    private String mVendorID = "",mVendorImgUrlPath = "";
    private RetrofitInterface retrofitInterface;
    private ProgressDialog mProgressDialog;
    private ReviewListApi mReviewListApi;

    public RatingList() {
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
        if (getActivity() != null) {
            getActivity().getWindow().getDecorView().setLayoutDirection(
                    "ae".equalsIgnoreCase(AppLanguageSupport.getLanguage(getActivity())) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);

        }
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRatingListBinding.inflate(inflater, container, false);

        binding.cardViewRestaurantImage.setVisibility(View.GONE);
        binding.layRlSectionRatingHolder.setVisibility(View.GONE);
        binding.tvRatingListTitle.setVisibility(View.GONE);


        if(getArguments() != null){
            mVendorID = getArguments().getString(DefaultNames.vendor_id);
            mVendorImgUrlPath = getArguments().getString(DefaultNames.vendor_image_url_path);
        }

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.loading_please_wait));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        binding.ratingNestedView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 20) {
                    binding.linearTitleToolbar.setVisibility(View.VISIBLE);
                } else {
                    binding.linearTitleToolbar.setVisibility(View.INVISIBLE);
                }
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        binding.RatingListLayout.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));

        binding.ratingValueTitleToolbar.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.basedOnToolbar.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.ratingValueTitle.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.basedOn.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.deliveryTime.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.productQuantity.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.ValueForMoney.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        binding.orderPackaging.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));

        Drawable iconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.baseline_arrow_back_black_48dp);
        iconDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_IN);
        binding.backBtn.setImageDrawable(iconDrawable);

        callReviewListAPi();
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



        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() != null){

            callReviewListAPi();

        }

    }


    public class RatingListAdapter extends RecyclerView.Adapter<RatingListAdapter.ViewHolder> {
        ArrayList<ReviewListDataSet> mReviewList;

        public RatingListAdapter(ArrayList<ReviewListDataSet> reviewList) {
           this.mReviewList = reviewList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.rating_rec_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            String mCustomerName = mReviewList.get(position).getCustomer_name();
            String mDate = mReviewList.get(position).getDate();
            String mRatingTIME = mCustomerName+", "+mDate;

            holder.rating_time.setText(mRatingTIME);

            String mCOMMENT = mReviewList.get(position).getComment();
            if(mCOMMENT != null && !mCOMMENT.isEmpty()){
                holder.rating_desc.setText(mCOMMENT);
                holder.rating_desc.setVisibility(View.VISIBLE);
            }else {
                holder.rating_desc.setText("");
                holder.rating_desc.setVisibility(View.GONE);
            }

            holder.review_value.setText(mReviewList.get(position).getVendor_rating());

        }


        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mReviewList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private TextView review_value, rating_desc, rating_time;

            public ViewHolder(View itemView) {
                super(itemView);

                review_value = itemView.findViewById(R.id.review_value);
                rating_desc = itemView.findViewById(R.id.rating_desc);
                rating_time = itemView.findViewById(R.id.rating_time);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }

    private void callReviewListAPi() {

        if (getActivity() != null) {
            if (AppFunctions.networkAvailabilityCheck(getActivity())) {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put(DefaultNames.vendor_id,mVendorID);
                    jsonObject.put(DefaultNames.page, 1);
                    mPageCount = 1;
                    jsonObject.put(DefaultNames.page_per_unit, 10);

                    jsonObject.put(DefaultNames.language_id, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getLanguageId());
                    jsonObject.put(DefaultNames.language_code, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getCode());

                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                    retrofitInterface = ApiClientGson.getClient().create(RetrofitInterface.class);
                    Call<ReviewListApi> Call = retrofitInterface.reviewListApi(body);
                    mProgressDialog.show();
                    Call.enqueue(new Callback<ReviewListApi>() {
                        @Override
                        public void onResponse(@NonNull Call<ReviewListApi> call, @NonNull Response<ReviewListApi> response) {

                            mProgressDialog.cancel();
                            if (response.isSuccessful()) {
                                mReviewListApi = response.body();
                                if (mReviewListApi != null) {
                                    if (mReviewListApi.success != null) {
                                        //Api response successDataSet :-
                                        if (getActivity() != null) {


                                            binding.layRlSectionRatingHolder.setVisibility(View.VISIBLE);
                                            binding.cardViewRestaurantImage.setVisibility(View.VISIBLE);

                                            //mVendorImgUrlPath
                                            if(mVendorImgUrlPath != null  && !mVendorImgUrlPath.isEmpty()){
                                                AppFunctions.bannerLoaderUsingGlide(mVendorImgUrlPath,binding.restaurantImage,getActivity());
                                            }

                                            String mPrefix = getActivity().getResources().getString(R.string.based_on);
                                            String mRatingTotal = mReviewListApi.getTotal();
                                            String mPostfix = getActivity().getResources().getString(R.string.ratings);
                                            String mBasedOnRating = mPrefix+" "+mRatingTotal+" "+mPostfix;
                                            binding.ratingValueTitleToolbar.setText(mReviewListApi.getAvg_vendor_rating());
                                            binding.basedOnToolbar.setText(mBasedOnRating);

                                            binding.ratingValueTitle.setText(mReviewListApi.getAvg_vendor_rating());
                                            binding.basedOn.setText(mBasedOnRating);

                                            String mRatingDT = mReviewListApi.getAvg_delivery_time();
                                            String mRatingOP = mReviewListApi.getAvg_order_packing();
                                            String mRatingQF = mReviewListApi.getAvg_quality();
                                            String mRatingVM = mReviewListApi.getAvg_value_for_money();

                                            if(mRatingDT != null && !mRatingDT.isEmpty()){
                                                binding.ratingDeliveryTime.setRating(Float.parseFloat(mRatingDT));
                                                binding.tvRatingDeliveryTime.setText(mRatingDT);
                                            }
                                            if(mRatingOP != null && !mRatingOP.isEmpty()){
                                                binding.ratingOrderPackaging.setRating(Float.parseFloat(mRatingOP));
                                                binding.tvRatingOrderPackaging.setText(mRatingOP);

                                            }
                                            if(mRatingQF != null && !mRatingQF.isEmpty()){
                                                binding.ratingQualityOfFood.setRating(Float.parseFloat(mRatingQF));
                                                binding.tvRatingQualityOfFood.setText(mRatingQF);
                                            }
                                            if(mRatingVM != null && !mRatingVM.isEmpty()){
                                                binding.ratingValueOfMoney.setRating(Float.parseFloat(mRatingVM));
                                                binding.tvRatingValueOfMoney.setText(mRatingVM);
                                            }


                                            if(mReviewListApi != null && mReviewListApi.getReviewList() != null
                                            && mReviewListApi.getReviewList().size() > 0){
                                                binding.tvRatingListTitle.setVisibility(View.VISIBLE);
                                                binding.ratingList.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
                                                binding.ratingList.setAdapter(new RatingListAdapter(mReviewListApi.getReviewList()));
                                            }else {
                                                binding.tvRatingListTitle.setVisibility(View.GONE);
                                            }



                                        }
                                    } else {
                                        //Api response failure :-
                                        if (getActivity() != null) {
                                            if (mReviewListApi.error != null) {
                                                AppFunctions.msgDialogOk(getActivity(), "", mReviewListApi.error.message);
                                            }
                                        }
                                    }
                                }
                            }else {
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
                        public void onFailure(@NonNull Call<ReviewListApi> call, @NonNull Throwable t) {
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
                mFT.replace(R.id.layout_app_home_body, mNetworkAnalyser, "mNetworkAnalyser");
                mFT.addToBackStack("mNetworkAnalyser");
                mFT.commit();
            }
        }


    }




}