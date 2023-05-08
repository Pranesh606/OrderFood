package com.exlfood.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exlfood.Activities.AppHome;
import com.exlfood.ApiConnection.ApiClientGson;
import com.exlfood.ApiConnection.RetrofitInterface;
import com.exlfood.CustomClasses.AppFunctions;
import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.CustomClasses.DefaultNames;
import com.exlfood.CustomClasses.NetworkAnalyser;
import com.exlfood.DataSets.ApiResponseCheck;
import com.exlfood.DataSets.DeleteAccountReasonDataSet;
import com.exlfood.DataSets.MyOrderDataSet;
import com.exlfood.DataSets.MyOrderListApi;
import com.exlfood.Databases.LanguageDetailsDB;
import com.exlfood.Databases.UserDetailsDB;
import com.exlfood.Interfaces.NavigationBack;
import com.exlfood.Interfaces.NavigationView;
import com.exlfood.R;
import com.exlfood.databinding.DeleteAccountBinding;
import com.exlfood.databinding.DeliveryLocationBinding;
import com.exlfood.databinding.MyOrderListBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteAccount extends Fragment implements View.OnClickListener {

    private DeleteAccountBinding mDelACBinding;
    private ProgressDialog mProgressDialog;
    RetrofitInterface retrofitInterface;
    private MyOrderListApi mDeleteAccountApi;

    private RecyclerView.LayoutManager mOrdersListLayMgr;
    private MyOrdersListAdapter mMyOrdersListAdapter;
    //NavigationView navigationView;
    //NavigationBack navigationBack;

    public ArrayList<DeleteAccountReasonDataSet> mReasonlist = new ArrayList<>();
    public String selectedReason = "" ;
    public int selectedRadioId = -1;

    public DeleteAccount() {
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
        //navigationView = (NavigationView) context;
        //navigationBack = (NavigationBack) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.my_order_list, container, false);
        mDelACBinding = DeleteAccountBinding.inflate(inflater, container, false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.loading_please_wait));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        DeleteAccountReasonDataSet deleteAccountReasonDataSet = new DeleteAccountReasonDataSet();
        deleteAccountReasonDataSet.setReason(getContext().getResources().getString(R.string.something_was_broken));
        mReasonlist.add(deleteAccountReasonDataSet);

        DeleteAccountReasonDataSet deleteAccountReasonDataSet0 = new DeleteAccountReasonDataSet();
        deleteAccountReasonDataSet0.setReason(getContext().getResources().getString(R.string.Im_not_any_invites));
        mReasonlist.add(deleteAccountReasonDataSet0);

        DeleteAccountReasonDataSet deleteAccountReasonDataSet1 = new DeleteAccountReasonDataSet();
        deleteAccountReasonDataSet1.setReason(getContext().getResources().getString(R.string.I_have_privacy));
        mReasonlist.add(deleteAccountReasonDataSet1);

        DeleteAccountReasonDataSet deleteAccountReasonDataSet2 = new DeleteAccountReasonDataSet();
        deleteAccountReasonDataSet2.setReason(getContext().getResources().getString(R.string.other));
        mReasonlist.add(deleteAccountReasonDataSet2);

        //Initialize
        mDelACBinding.imgMolBack.setOnClickListener(this);
        mDelACBinding.layDlOtherReasonContainer.setVisibility(View.GONE);

        mDelACBinding.rdoGrpReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Log.e("Test","Checked Id = "+checkedId);
                //Log.e("Test","Radio1 = "+mDelACBinding.rdoReason1.getId()+" Radio2 = "+mDelACBinding.rdoReason2.getId());
//                for(int i=0;i<mReasonlist.size();i++){
//                    if(checkedId==mReasonlist.size()-1){
//                        mDelACBinding.layDlOtherReasonContainer.setVisibility(View.VISIBLE);
//                    } else {
//                        mDelACBinding.layDlOtherReasonContainer.setVisibility(View.GONE);
//                    }
//                }

                if(checkedId == 0){
                    mDelACBinding.layDlOtherReasonContainer.setVisibility(View.GONE);
                } else if (checkedId == 1){
                    mDelACBinding.layDlOtherReasonContainer.setVisibility(View.GONE);
                } else if (checkedId == 2){
                    mDelACBinding.layDlOtherReasonContainer.setVisibility(View.GONE);
                } else if (checkedId == 3){
                    mDelACBinding.layDlOtherReasonContainer.setVisibility(View.VISIBLE);
                }
                selectedRadioId = checkedId;
            }
        });

        for(int i=0;i<mReasonlist.size();i++){

            RadioButton mRadio = new RadioButton(getContext());
            mRadio.setId(i);
            mRadio.setText(mReasonlist.get(i).getReason());
            mRadio.setPadding(0,(int) getContext().getResources().getDimension(com.intuit.sdp.R.dimen._2sdp),0,0);
            Typeface typeface = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                typeface = getResources().getFont(R.font.poppins_medium);
                mRadio.setTypeface(typeface);
            }

            if(i == 0){
                mRadio.setChecked(true);
                selectedRadioId = i;
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) ViewGroup.LayoutParams.WRAP_CONTENT,(int) ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = (int) getContext().getResources().getDimension(com.intuit.sdp.R.dimen._2sdp);
            mRadio.setLayoutParams(params);
            mDelACBinding.rdoGrpReason.addView(mRadio);

        }

        mDelACBinding.layCpSubmitBtnContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedRadioId != -1){
                    //Check user select other reason
                    if(selectedRadioId == mReasonlist.size()-1){
                        //selectedReason = mDelACBinding.edtOtherReason.getText().toString().trim();
                        if(!mDelACBinding.edtOtherReason.getText().toString().trim().equals("") &&
                                mDelACBinding.edtOtherReason.getText().toString().trim().length()>4){
                            selectedReason = mDelACBinding.edtOtherReason.getText().toString().trim();
                            ConfirmDel_ApiCall(selectedReason);
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder
                                    .setMessage(getActivity().getString(R.string.pls_enter_atleast_5_characters))
                                    .setCancelable(false)
                                    .setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }

                    } else {
                        selectedReason = mReasonlist.get(selectedRadioId).getReason();
                        ConfirmDel_ApiCall(selectedReason);
                    }
                }

            }
        });

        return mDelACBinding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        mDelACBinding.frameLayoutDeleteAccount.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mDelACBinding.LinearLayoutTop.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mDelACBinding.frameLayoutTittle.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mDelACBinding.edtOtherReasonLayout.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mDelACBinding.layMolMyOrdersListParent.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        mDelACBinding.edtOtherReason.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        super.onConfigurationChanged(newConfig);
    }
    public void ConfirmDel_ApiCall(String reason){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle(getActivity().getString(R.string.we_re_sorry))
                .setMessage(getActivity().getString(R.string.are_you_sure))
                .setCancelable(false)
                .setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callDeleteAccountAPi(reason);
                        //Test
                        Log.e("Test","ConfirmDel_APi Calling selected Reason = "+reason);
                    }
                })
                .setNegativeButton(getActivity().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
        //navigationBack.navigationBack(3);
        //navigationView.navigationView(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        //navigationView.navigationView(false);
    }

    private void showOderListEmptyMsg() {
//        mDelACBinding.tvMolMyOrdersListEmptyMsg.setVisibility(View.VISIBLE);
//        mDelACBinding.recyclerMolMyOrdersList.setVisibility(View.GONE);
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
        public void onBindViewHolder(final DataObjectHolder holder, @SuppressLint("RecyclerView") final int position) {


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

    private void callDeleteAccountAPi(String reason) {

        if (getActivity() != null) {
            if (AppFunctions.networkAvailabilityCheck(getActivity())) {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put(DefaultNames.reason,reason);

                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                    String mCustomerAuthorization = "";
                    if (AppFunctions.isUserLoggedIn(getActivity())) {
                        mCustomerAuthorization = UserDetailsDB.getInstance(getActivity()).getUserDetails().getCustomerKey();
                    }
                    retrofitInterface = ApiClientGson.getClient().create(RetrofitInterface.class);
                    Call<ApiResponseCheck> Call = retrofitInterface.customer_account_deletion(mCustomerAuthorization, body);
                    mProgressDialog.show();
                    Call.enqueue(new Callback<ApiResponseCheck>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponseCheck> call, @NonNull Response<ApiResponseCheck> response) {

                            mProgressDialog.cancel();

                            if (getActivity() != null) {
                                if (response.isSuccessful()) {

                                    ApiResponseCheck mApiResponseCheck = response.body();
                                    if (mApiResponseCheck != null) {
                                        //Log.e("mApiResponseCheck", "not null");
                                        if (mApiResponseCheck.success != null) {
                                            //Api response successDataSet :-
                                            if (getActivity() != null) {

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


//                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                                                alertDialogBuilder
//                                                        .setMessage(mApiResponseCheck.success.message)
//                                                        .setCancelable(false)
//                                                        .setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//                                                                    public void onClick(DialogInterface dialog, int id) {
//                                                                        getParentFragmentManager().popBackStack();
//                                                                    }
//                                                                }
//                                                        );
//
//                                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                                alertDialog.show();
                                            }

                                        } else {
                                            //Api response failure :-
                                            if (mApiResponseCheck.error != null) {
                                                AppFunctions.msgDialogOk(getActivity(), "", mApiResponseCheck.error.message);
                                            }

                                        }
                                    }

                                } else {

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
                mFT.replace(R.id.layout_app_home_body, mNetworkAnalyser, "mNetworkAnalyser");
                mFT.addToBackStack("mNetworkAnalyser");
                mFT.commit();
            }
        }


    }


}