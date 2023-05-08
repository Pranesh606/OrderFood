package com.exlfood.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.exlfood.CustomClasses.AppFunctions;
import com.exlfood.CustomClasses.AppLanguageSupport;
import com.exlfood.DataSets.BusinessTypesDataSet;
import com.exlfood.Interfaces.CartInfo;
import com.exlfood.Interfaces.NavigationBack;
import com.exlfood.Interfaces.NavigationView;
import com.exlfood.R;
import com.exlfood.databinding.FragmentBusinessTypeListBinding;

import java.util.ArrayList;

public class BusinessTypeListFragment extends BottomSheetDialogFragment {

    FragmentBusinessTypeListBinding binding;
    ArrayList<BusinessTypesDataSet> businessTypeList;
    Activity activity;
    NavigationView navigationView;
    NavigationBack navigationBack;

    public BusinessTypeListFragment(ArrayList<BusinessTypesDataSet> businessTypeList) {
        this.businessTypeList = businessTypeList;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            businessTypeList = (ArrayList<BusinessTypesDataSet>) getArguments().getSerializable("businessTypeList");
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
        binding = FragmentBusinessTypeListBinding.inflate(inflater, container, false);

        if (businessTypeList != null && businessTypeList.size() > 0) {
            binding.businessTypeRecView.setVisibility(View.VISIBLE);
            binding.layoutRestaurantEmptyMessage.setVisibility(View.GONE);
            binding.businessTypeRecView.setLayoutManager(new GridLayoutManager(activity, 4));
            binding.businessTypeRecView.setAdapter(new BusinessListAdapter(businessTypeList));
        } else {
            binding.businessTypeRecView.setVisibility(View.GONE);
            binding.layoutRestaurantEmptyMessage.setVisibility(View.VISIBLE);
        }

//        binding.imgClBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

        return binding.getRoot();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        Log.e("onConfigurationChanged","AddressChangeLocation");

        binding.businessTypeLayout.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        binding.imgClBack.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));

        if (businessTypeList != null && businessTypeList.size() > 0) {
            binding.businessTypeRecView.setVisibility(View.VISIBLE);
            binding.layoutRestaurantEmptyMessage.setVisibility(View.GONE);
            binding.businessTypeRecView.setLayoutManager(new GridLayoutManager(activity, 4));
            binding.businessTypeRecView.setAdapter(new BusinessListAdapter(businessTypeList));
        } else {
            binding.businessTypeRecView.setVisibility(View.GONE);
            binding.layoutRestaurantEmptyMessage.setVisibility(View.VISIBLE);
        }

        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onResume() {
        super.onResume();
        navigationBack.navigationBack(1);
        navigationView.navigationView(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.navigationView(false);
    }

    private class BusinessListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<BusinessTypesDataSet> businessTypeList;

        BusinessListAdapter(ArrayList<BusinessTypesDataSet> businessTypeList) {
            this.businessTypeList = businessTypeList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new CuisineViewHolder(LayoutInflater.from(activity).inflate(R.layout.business_type_list, viewGroup, false));

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            CuisineViewHolder restaurantViewHolder = (CuisineViewHolder) viewHolder;
            restaurantViewHolder.tv_RestaurantTitle.setText(businessTypeList.get(i).getName());
            AppFunctions.imageLoaderUsingGlide(businessTypeList.get(i).getLogo(), restaurantViewHolder.iv_RestaurantImage, activity);
        }

        @Override
        public int getItemCount() {
            return businessTypeList.size();
        }
    }

    class CuisineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_RestaurantImage;
        TextView tv_RestaurantTitle;

        CuisineViewHolder(View itemView) {
            super(itemView);
            iv_RestaurantImage = itemView.findViewById(R.id.iv_home_restaurant_image);
            tv_RestaurantTitle = itemView.findViewById(R.id.tv_home_restaurant_title);
            iv_RestaurantImage.setOnClickListener(this);
            tv_RestaurantTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.iv_home_restaurant_image || id == R.id.tv_home_restaurant_title) {

                dismiss();

                FragmentTransaction mFT = getParentFragmentManager().beginTransaction();
                if (businessTypeList.get(getAdapterPosition()).getType_id().equals("2")) {
                    GroceryAllStores m_groceryAllStores = new GroceryAllStores();
                    mFT.replace(R.id.layout_app_home_body, m_groceryAllStores, "m_groceryAllStores");
                    mFT.addToBackStack("m_groceryAllStores");
                    mFT.commit();
                } else {
                    AllRestaurants m_AllRestaurants = new AllRestaurants();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("business_type_id", businessTypeList.get(getAdapterPosition()).getType_id());
                    mBundle.putString("top_pick_id", "0");
                    m_AllRestaurants.setArguments(mBundle);
                    mFT.replace(R.id.layout_app_home_body, m_AllRestaurants, "m_AllRestaurants");
                    mFT.addToBackStack("m_AllRestaurants");
                    mFT.commit();
                }
            }
        }
    }
}
