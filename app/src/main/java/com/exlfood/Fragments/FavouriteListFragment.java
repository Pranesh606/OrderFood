package com.exlfood.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.exlfood.ApiConnection.ApiClient;
import com.exlfood.ApiConnection.ApiClientGson;
import com.exlfood.ApiConnection.RetrofitInterface;
import com.exlfood.CustomClasses.AppFunctions;
import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.CustomClasses.DefaultNames;
import com.exlfood.CustomClasses.NetworkAnalyser;
import com.exlfood.DataSets.VendorDataSet;
import com.exlfood.DataSets.WishListDataSet;
import com.exlfood.DataSets.WishlistVendor;
import com.exlfood.Databases.LanguageDetailsDB;
import com.exlfood.Databases.OrderTypeDB;
import com.exlfood.Databases.WishListDB;
import com.exlfood.Interfaces.NavigationBack;
import com.exlfood.Interfaces.NavigationView;
import com.exlfood.R;
import com.exlfood.databinding.FragmentFavouriteListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteListFragment extends Fragment {

    FragmentFavouriteListBinding binding;
    Activity activity;
    private ProgressDialog mProgressDialog;
    RetrofitInterface retrofitInterface;
    FavListAdapter adapter;
    NavigationView navigationView;
    NavigationBack navigationBack;


    public FavouriteListFragment() {
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
        navigationView = (NavigationView) context;
        navigationBack = (NavigationBack) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteListBinding.inflate(inflater, container, false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.loading_please_wait));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        binding.imgSrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onConfigurationChanged(Configuration newConfig){

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Log.e("updateThemeFavourites  " + "LightTheme", "");
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using the dark theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Log.e("updateThemeFavourites  " + "DarkTheme", "");
                break;
        }

        binding.favouritesLayout.setBackgroundColor(getResources().getColor(R.color.white,activity.getTheme()));
        binding.favouritsText.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.tvSrRestaurantsListEmpty.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,activity.getTheme()));
        binding.recyclerSrRestaurantsList.setBackgroundColor(getResources().getColor(R.color.white,activity.getTheme()));

        // Get the Drawable object for the vector asset icon
        Drawable iconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.baseline_arrow_back_black_48dp);
        iconDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_IN);
        binding.imgSrBack.setImageDrawable(iconDrawable);


        getList();

        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onResume() {
        super.onResume();
        navigationBack.navigationBack(2);
        navigationView.navigationView(true);
        getList();
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.navigationView(false);
    }

    private void getList() {
        if (getActivity() != null) {
            if (AppFunctions.networkAvailabilityCheck(getActivity())) {
                mProgressDialog.show();
                try {
                    retrofitInterface = ApiClientGson.getClient().create(RetrofitInterface.class);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(DefaultNames.day_id, AppFunctions.getDayId());
                    jsonObject.put(DefaultNames.language_id, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getLanguageId());
                    jsonObject.put(DefaultNames.language_code, LanguageDetailsDB.getInstance(getActivity()).get_language_Details().getCode());
                    jsonObject.put("order_type", OrderTypeDB.getInstance(activity).getUserServiceType());

                    JSONArray array = new JSONArray();

                    for (int i = 0; i < WishListDB.getInstance(activity).getWishList().size(); i++) {
                        array.put(WishListDB.getInstance(activity).getWishList().get(i));
                    }

                    jsonObject.put("vendor_list", array);

                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                    Call<WishListDataSet> Call = retrofitInterface.wishlist(body);
                    Call.enqueue(new Callback<WishListDataSet>() {
                        @Override
                        public void onResponse(@NonNull Call<WishListDataSet> call, @NonNull Response<WishListDataSet> response) {
                            mProgressDialog.cancel();
                            WishListDataSet wishListDataSet = response.body();
                            if (wishListDataSet != null && wishListDataSet.getSuccess() != null && wishListDataSet.getVendorList().size() > 0) {
                                binding.tvSrRestaurantsListEmpty.setVisibility(View.GONE);
                                binding.recyclerSrRestaurantsList.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
                                adapter = new FavListAdapter(wishListDataSet.getVendorList());
                                binding.recyclerSrRestaurantsList.setAdapter(adapter);
                            } else {
                                binding.tvSrRestaurantsListEmpty.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<WishListDataSet> call, @NonNull Throwable t) {
                            mProgressDialog.cancel();
                        }
                    });
                } catch (Exception e) {
                    mProgressDialog.cancel();
                    Log.e("getList: ", e.getMessage() + "");
                }
            } else {
                FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                NetworkAnalyser mNetworkAnalyser = new NetworkAnalyser();
                mFT.replace(R.id.layout_home_restaurant_body, mNetworkAnalyser, "mNetworkAnalyser");
                mFT.addToBackStack("mNetworkAnalyser");
                mFT.commit();
            }
        }

    }

    private class FavListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<WishlistVendor> mVendorList;

        FavListAdapter(ArrayList<WishlistVendor> vendorList) {
            this.mVendorList = vendorList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RestaurantViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.rc_fav_restaurants_row, parent, false));
        }

        @SuppressLint({"UseCompatLoadingForDrawables", "NotifyDataSetChanged"})
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            RestaurantViewHolder restaurantViewHolder = (RestaurantViewHolder) holder;
            restaurantViewHolder.tv_RestaurantTitle.setText(mVendorList.get(position).getName());
            AppFunctions.imageLoaderUsingGlide(mVendorList.get(position).getLogo(), restaurantViewHolder.iv_vendorImage, getActivity());
            restaurantViewHolder.tv_restaurant_sub_content.setText(mVendorList.get(position).getCuisines());
            String mVendorStatus = mVendorList.get(position).getVendorStatus();

            if (OrderTypeDB.getInstance(activity).getUserServiceType().equals("2")) {
                restaurantViewHolder.mDeliveryAmtContainer.setVisibility(View.GONE);
                restaurantViewHolder.mPickupContainer.setVisibility(View.VISIBLE);
                String mDTime = mVendorList.get(position).getDeliveryTime()
                        + " " + getActivity().getResources().getString(R.string.mins);
                restaurantViewHolder.mPickupTime.setText(mDTime);
            } else {
                restaurantViewHolder.mDeliveryAmtContainer.setVisibility(View.VISIBLE);
                restaurantViewHolder.mPickupContainer.setVisibility(View.GONE);
                String mDTime = mVendorList.get(position).getDeliveryTime()
                        + " " + getActivity().getResources().getString(R.string.mins);
                restaurantViewHolder.tv_DeliveryTime.setText(mDTime);
            }

            restaurantViewHolder.mDeliveryAmtContainer.setVisibility(View.GONE);
            restaurantViewHolder.mPickupContainer.setVisibility(View.GONE);

            if (WishListDB.getInstance(activity).getSizeOfList() > 0) {
                if (WishListDB.getInstance(activity).isSelected(mVendorList.get(position).getVendorId())) {
                    restaurantViewHolder.favorite_icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.baseline_favorite_primary_color_24dp));
                } else {
                    restaurantViewHolder.favorite_icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.baseline_favorite_border_primary_color_24dp));
                }
            } else {
                restaurantViewHolder.favorite_icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.baseline_favorite_border_primary_color_24dp));
            }

            restaurantViewHolder.favorite_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (WishListDB.getInstance(activity).getSizeOfList() > 0) {
                        if (WishListDB.getInstance(activity).isSelected(mVendorList.get(position).getVendorId())) {
                            WishListDB.getInstance(activity).removeFromFavouriteList(mVendorList.get(position).getVendorId());
                            restaurantViewHolder.favorite_icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.baseline_favorite_border_primary_color_24dp));
                            mVendorList.remove(position);
                            adapter.notifyDataSetChanged();

                            if (mVendorList.size() == 0) {
                                binding.tvSrRestaurantsListEmpty.setVisibility(View.VISIBLE);
                            }

                        } else {
                            WishListDB.getInstance(activity).add_vendor_id(mVendorList.get(position).getVendorId());
                            restaurantViewHolder.favorite_icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.baseline_favorite_primary_color_24dp));
                        }
                    } else {
                        WishListDB.getInstance(activity).add_vendor_id(mVendorList.get(position).getVendorId());
                        restaurantViewHolder.favorite_icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.baseline_favorite_primary_color_24dp));
                    }
                }
            });

            //1 -  open
            //2 -  busy
            //0 -  closed
            if (mVendorStatus.equals("0")) {
                //closed :-
                restaurantViewHolder.mLayImageOverStatus.setVisibility(View.VISIBLE);
                restaurantViewHolder.tv_ImageOverStatus.setText(getActivity().getResources().getString(R.string.closed));
            } else if (mVendorStatus.equals("2")) {
                //Busy :-
                restaurantViewHolder.mLayImageOverStatus.setVisibility(View.VISIBLE);
                restaurantViewHolder.tv_ImageOverStatus.setText(getActivity().getResources().getString(R.string.busy));
            } else {
                //Open :-
                restaurantViewHolder.mLayImageOverStatus.setVisibility(View.GONE);
                restaurantViewHolder.tv_ImageOverStatus.setText("");
            }

            if (mVendorList.get(position).getRating() != null) {
                String mCRating = mVendorList.get(position).getRating().getRating();
                if (mCRating != null && !mCRating.isEmpty()) {
                    restaurantViewHolder.tv_rating_statement.setText(mCRating);
                    restaurantViewHolder.rating_linear.setVisibility(View.VISIBLE);
                } else {
                    restaurantViewHolder.tv_rating_statement.setText("0");
                    restaurantViewHolder.rating_linear.setVisibility(View.GONE);
                }
            } else {
                restaurantViewHolder.tv_rating_statement.setText("0");
                restaurantViewHolder.rating_linear.setVisibility(View.GONE);
            }
            restaurantViewHolder.rating_linear.setVisibility(View.GONE);
//          restaurantViewHolder.tv_delivery_amount.setText(mVendorList.get(position).getDeliveryCharge());
            restaurantViewHolder.tv_delivery_amount.setVisibility(View.GONE);

            String m_OfferData = mVendorList.get(position).getOffer();
            if (m_OfferData != null && !m_OfferData.isEmpty()) {
                restaurantViewHolder.mLayVLOfferContainer.setVisibility(View.VISIBLE);
                restaurantViewHolder.empty.setVisibility(View.VISIBLE);
//                    restaurantViewHolder.iv_Dot.setVisibility(View.VISIBLE);
                restaurantViewHolder.tv_offerContent.setText(m_OfferData);
            } else {
                restaurantViewHolder.mLayVLOfferContainer.setVisibility(View.GONE);
                restaurantViewHolder.empty.setVisibility(View.GONE);
//                    restaurantViewHolder.iv_Dot.setVisibility(View.GONE);
                restaurantViewHolder.tv_offerContent.setText("");
            }
            restaurantViewHolder.mLayVLOfferContainer.setVisibility(View.GONE);
            // restaurantViewHolder.mRestaurantStatusContainer.setVisibility(View.GONE);

        }

        @Override
        public int getItemCount() {
            return mVendorList.size();
        }

        class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView iv_Dot, favorite_icon;
            ShapeableImageView iv_vendorImage;
            TextView tv_RestaurantTitle, tv_DeliveryTime, tv_restaurant_sub_content;
            TextView tv_rating_statement, mPickupTime,
                    tv_delivery_amount_title, tv_delivery_amount, tv_offerContent, tv_ImageOverStatus;
            LinearLayout mDeliveryAmtContainer, mDeliveryTimeContainer, mPickupContainer,
                    mLayImageOverStatus, mLayVLOfferContainer, rating_linear, empty;

            RestaurantViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                iv_vendorImage = itemView.findViewById(R.id.iv_ar_restaurant_image);
                tv_RestaurantTitle = itemView.findViewById(R.id.tv_ar_restaurant_title);
                tv_restaurant_sub_content = itemView.findViewById(R.id.tv_ar_restaurant_sub_title);
                tv_rating_statement = itemView.findViewById(R.id.tv_ar_rating_msg);
                rating_linear = itemView.findViewById(R.id.rating_linear);

                mDeliveryAmtContainer = itemView.findViewById(R.id.delivery_container);
                mPickupContainer = itemView.findViewById(R.id.pickup_container);
                mPickupTime = itemView.findViewById(R.id.tv_ar_pickup_time);

                tv_delivery_amount_title = itemView.findViewById(R.id.tv_ar_delivery_amt_title);
                tv_delivery_amount = itemView.findViewById(R.id.tv_ar_delivery_amt_data);
                mDeliveryTimeContainer = itemView.findViewById(R.id.lay_ar_restaurant_delivery_time_container);
                tv_DeliveryTime = itemView.findViewById(R.id.tv_ar_delivery_time);
                tv_offerContent = itemView.findViewById(R.id.tv_ar_restaurant_offers);
                mLayImageOverStatus = itemView.findViewById(R.id.lay_ar_restaurant_image);
                tv_ImageOverStatus = itemView.findViewById(R.id.tv_ar_restaurant_image_over_status);
                mLayVLOfferContainer = itemView.findViewById(R.id.offer_linear);
                empty = itemView.findViewById(R.id.empty);
                iv_Dot = itemView.findViewById(R.id.img_ar_restaurant_offers_dot);
                favorite_icon = itemView.findViewById(R.id.favorite_icon);
            }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() != -1) {
                    if (mVendorList.get(getAdapterPosition()).getVendorTypeId().equals("2")) {
                        FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                        GroceryCategoryMainPage m_groceryCategoryMainPage = new GroceryCategoryMainPage();
                        Bundle mBundle = new Bundle();
                        mBundle.putString(DefaultNames.store_id, mVendorList.get(getAdapterPosition()).getVendorId());
                        mBundle.putString(DefaultNames.store_name, mVendorList.get(getAdapterPosition()).getName());
                        m_groceryCategoryMainPage.setArguments(mBundle);
                        mFT.replace(R.id.layout_app_home_body, m_groceryCategoryMainPage, "m_groceryCategoryMainPage");
                        mFT.addToBackStack("m_groceryCategoryMainPage");
                        mFT.commit();
                    } else {
                        toStoreListing(mVendorList.get(getAdapterPosition()).getVendorId(), "0");
                    }
                }
            }
        }

        public class MenuListEmptyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout menuListEmptyUI;

            MenuListEmptyViewHolder(View itemView) {
                super(itemView);
                menuListEmptyUI = itemView.findViewById(R.id.layout_restaurant_menu_item_list_empty_message);
            }
        }

        public void toStoreListing(String vendor_id, String product_id) {
            if (getActivity() != null) {
                FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                RestaurantInfo restaurantInfo = new RestaurantInfo();
                Bundle mBundle = new Bundle();
                mBundle.putString("vendor_id", vendor_id);
                mBundle.putString("product_id", product_id);
                restaurantInfo.setArguments(mBundle);
                mFT.replace(R.id.layout_app_home_body, restaurantInfo, "restaurantInfo");
                mFT.addToBackStack("restaurantInfo");
                mFT.commit();
            }
        }

    }

}