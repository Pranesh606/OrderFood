package com.exlfood.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.exlfood.ApiConnection.ApiClientGson;
import com.exlfood.ApiConnection.RetrofitInterface;
import com.exlfood.CustomClasses.AppFunctions;
import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.CustomClasses.DefaultNames;
import com.exlfood.CustomClasses.NetworkAnalyser;
import com.exlfood.DataSets.MyOrderDataSet;
import com.exlfood.DataSets.MyOrderListApi;
import com.exlfood.Databases.LanguageDetailsDB;
import com.exlfood.Databases.UserDetailsDB;
import com.exlfood.Interfaces.NavigationBack;
import com.exlfood.Interfaces.NavigationView;
import com.exlfood.R;
import com.exlfood.databinding.MyOrderListBinding;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderList extends Fragment implements View.OnClickListener {

    private MyOrderListBinding mMOListBinding;
    private ProgressDialog mProgressDialog;
    RetrofitInterface retrofitInterface;
    private MyOrderListApi mMyOrderListApi;

    private RecyclerView.LayoutManager mOrdersListLayMgr;
    private MyOrdersListAdapter mMyOrdersListAdapter;
    NavigationView navigationView;
    NavigationBack navigationBack;

    public MyOrderList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        navigationView = (NavigationView) context;
        navigationBack = (NavigationBack) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.my_order_list, container, false);
        mMOListBinding = MyOrderListBinding.inflate(inflater, container, false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.loading_please_wait));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        mMOListBinding.imgMolBack.setOnClickListener(this);

        mMOListBinding.tvMolMyOrdersListEmptyMsg.setVisibility(View.GONE);

        return mMOListBinding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

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

        mMOListBinding.MyOrdersLayout.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mMOListBinding.MyOrdersText.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        mMOListBinding.myOrderLayoutTop.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mMOListBinding.recyclerMolMyOrdersList.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mMOListBinding.topLayout.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mMOListBinding.layMolMyOrdersListParent.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mMOListBinding.tvMolMyOrdersListEmptyMsg.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));

        // Get the Drawable object for the vector asset icon
        Drawable iconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.baseline_arrow_back_black_48dp);
        iconDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_IN);
        mMOListBinding.imgMolBack.setImageDrawable(iconDrawable);

        callOrdersListApi();

        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onClick(View view) {

        int mId = view.getId();

        if (mId == R.id.img_mol_back) {
            if (getActivity() != null) {
                getParentFragmentManager().popBackStack();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationBack.navigationBack(3);
        navigationView.navigationView(true);
        if (getActivity() != null) {
            callOrdersListApi();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.navigationView(false);
    }

    private void showOderListEmptyMsg() {
        mMOListBinding.tvMolMyOrdersListEmptyMsg.setVisibility(View.VISIBLE);
        mMOListBinding.recyclerMolMyOrdersList.setVisibility(View.GONE);
    }

    private void callOrdersListApi() {

        if (getActivity() != null) {
            if (AppFunctions.networkAvailabilityCheck(getActivity())) {


                try {

                    String mCustomerAuthorization = "";
                    if (AppFunctions.isUserLoggedIn(getActivity())) {
                        mCustomerAuthorization = UserDetailsDB.getInstance(getActivity()).getUserDetails().getCustomerKey();
                    }

                    JSONObject jsonObject = new JSONObject();
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
                    mProgressDialog.show();
                    retrofitInterface = ApiClientGson.getClient().create(RetrofitInterface.class);
                    Call<MyOrderListApi> Call = retrofitInterface.ordersListsApi(mCustomerAuthorization, body);
                    Call.enqueue(new Callback<MyOrderListApi>() {
                        @Override
                        public void onResponse(@NonNull Call<MyOrderListApi> call, @NonNull Response<MyOrderListApi> response) {

                            mProgressDialog.cancel();

                            if (getActivity() != null) {

                                if (response.isSuccessful()) {
                                    mMyOrderListApi = response.body();

                                    if (mMyOrderListApi != null) {
                                        //Log.e("mMyOrderListApi", "not null");
                                        if (mMyOrderListApi.success != null) {
                                            //Api response successDataSet :-

                                            if (mMyOrderListApi.myOrderList != null && mMyOrderListApi.myOrderList.size() > 0) {

                                                mMOListBinding.tvMolMyOrdersListEmptyMsg.setVisibility(View.GONE);
                                                mMOListBinding.recyclerMolMyOrdersList.setVisibility(View.VISIBLE);

                                                mOrdersListLayMgr = new LinearLayoutManager(getActivity());
                                                mMOListBinding.recyclerMolMyOrdersList.setLayoutManager(mOrdersListLayMgr);
                                                mMyOrdersListAdapter = new MyOrdersListAdapter(mMyOrderListApi.myOrderList);
                                                mMOListBinding.recyclerMolMyOrdersList.setAdapter(mMyOrdersListAdapter);

                                            } else {
                                                showOderListEmptyMsg();
                                            }


                                        } else {

                                            showOderListEmptyMsg();

                                            mProgressDialog.cancel();

                                            //Api response failure :-
                                            if (mMyOrderListApi.error != null) {
                                                AppFunctions.msgDialogOk(getActivity(), "", mMyOrderListApi.error.message);
                                            }
                                        }
                                    } else {

                                        showOderListEmptyMsg();

                                        mProgressDialog.cancel();

                                        //Log.e("mMyOrderListApi", "null");
                                    }
                                } else {

                                    showOderListEmptyMsg();

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


                        }

                        @Override
                        public void onFailure(@NonNull Call<MyOrderListApi> call, @NonNull Throwable t) {
                            mProgressDialog.cancel();
                            showOderListEmptyMsg();

                        }
                    });

                } catch (Exception e) {

                    showOderListEmptyMsg();

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

    public class MyOrdersListAdapter extends RecyclerView.Adapter<MyOrdersListAdapter.DataObjectHolder> {

        private final ArrayList<MyOrderDataSet> mAMyOrdersList;
        private LinearLayout mAMyOrdersListEmptyMsg;


        public MyOrdersListAdapter(ArrayList<MyOrderDataSet> myOrdersList) {

            this.mAMyOrdersList = myOrdersList;

        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_list_row, parent, false);
            return new DataObjectHolder(mView);
        }


        @Override
        public void onBindViewHolder(final DataObjectHolder holder, final int position) {


            AppFunctions.imageLoaderUsingGlide(mAMyOrdersList.get(position).getLogo(), holder.mRestaurantImage, getActivity());

            holder.mRestaurantName.setText(mAMyOrdersList.get(position).getVendor_name());

            holder.mOrderStatus.setText(mAMyOrdersList.get(position).getStatus());

            String mOrderDateTime = getActivity().getResources().getString(R.string.mol_order_date) + " " +
                    mAMyOrdersList.get(position).getOrdered_date() + " " + mAMyOrdersList.get(position).getOrdered_time();
            holder.mOrderDate.setText(mOrderDateTime);

            String mOrderID = getActivity().getResources().getString(R.string.mol_order_id) + " " +
                    mAMyOrdersList.get(position).getOrder_id();
            holder.mOrderId.setText(mOrderID);

            String mOrder_type ="";
            if (mAMyOrdersList.get(position).getOrder_type().equals("1")) {
                mOrder_type = getActivity().getResources().getString(R.string.order_type) + ": " + getActivity().getResources().getString(R.string.delivery);
            }else {
                mOrder_type = getActivity().getResources().getString(R.string.order_type) + ": " + getActivity().getResources().getString(R.string.pick_up);
            }
            holder.order_type.setText(mOrder_type);

            holder.mLayRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getActivity() != null) {
                        FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                        MyOrderInfo m_myOrderInfo = new MyOrderInfo();
                        Bundle mBundle = new Bundle();
                        mBundle.putString(DefaultNames.order_id, mAMyOrdersList.get(position).getOrder_id());
                        m_myOrderInfo.setArguments(mBundle);
                        mFT.replace(R.id.layout_app_home_body, m_myOrderInfo, "m_myOrderInfo");
                        mFT.addToBackStack("m_myOrderInfo");
                        mFT.commit();
                    }
                }
            });


        }


        @Override
        public int getItemCount() {
            // return this.mAMyOrdersList.size();

            return mAMyOrdersList.size();


        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {

            TextView mRestaurantName, mOrderId, mOrderStatus, mOrderDate, order_type;
            private ImageView mRestaurantImage;
            private LinearLayout mLayRow;


            public DataObjectHolder(View view) {
                super(view);
                order_type = view.findViewById(R.id.tv_mol_order_type);
                mRestaurantImage = view.findViewById(R.id.iv_mol_restaurant_image);
                mRestaurantName = view.findViewById(R.id.tv_mol_restaurant_name);
                mOrderStatus = view.findViewById(R.id.tv_mol_order_status);
                mOrderDate = view.findViewById(R.id.tv_mol_order_date);
                mOrderId = view.findViewById(R.id.tv_mol_order_id);
                mLayRow = view.findViewById(R.id.lay_mol_row);


            }
        }


    }


}