package com.exlfood.Fragments;

import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.exlfood.DataSets.CheckOutDBDataSet;
import com.exlfood.Databases.CheckOutDetailsDB;
import com.exlfood.Interfaces.CheckOutBackPress;
import com.exlfood.R;
import com.exlfood.databinding.CheckOutSuccessBinding;

public class CheckOutSuccess extends Fragment implements View.OnClickListener{

    private CheckOutSuccessBinding mCheckOutSBinding;
    private CheckOutBackPress mCheckOutBackPress;

    public CheckOutSuccess() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCheckOutSBinding = CheckOutSuccessBinding.inflate(inflater,container,false);
        mCheckOutBackPress = (CheckOutBackPress) getActivity();
        if (mCheckOutBackPress != null) {
            mCheckOutBackPress.checkOutSuccessStatus(true);
        }
        mCheckOutSBinding.layCoSContinue.setOnClickListener(this);

        String thanku_msg = getResources().getString(R.string.str_1) + "<br>" + getResources().getString(R.string.str_2) + "<br>" +
                getResources().getString(R.string.str_3) ;

        mCheckOutSBinding.tvCoSThankU.setText(Html.fromHtml(thanku_msg));

        return mCheckOutSBinding.getRoot();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mCheckOutSBinding.layCoSContainer.setBackgroundColor(getResources().getColor(R.color.white,getActivity().getTheme()));

        mCheckOutSBinding.placedText.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));
        mCheckOutSBinding.tvCoSThankU.setTextColor(getResources().getColor(R.color.ar_filter_title_text_color,getActivity().getTheme()));

        Drawable iconDrawable1 = ContextCompat.getDrawable(getContext(), R.drawable.bg_for_layout_with_image);
        iconDrawable1.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_IN);
        mCheckOutSBinding.frameLayoutMain.setBackground(iconDrawable1);

        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onClick(View view) {
        int mId = view.getId();
        if (mId == R.id.img_check_out_back) {
            if (mCheckOutBackPress != null) {
                mCheckOutBackPress.checkOutSuccessBackPressed();
            }
        }else if(mId == R.id.lay_co_s_continue){

            if (mCheckOutBackPress != null) {
                mCheckOutBackPress.checkOutSuccessBackPressed();
            }

        }
    }
}