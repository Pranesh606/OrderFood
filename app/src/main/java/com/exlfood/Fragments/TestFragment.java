package com.exlfood.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.exlfood.Databases.FCMTokenDB;
import com.exlfood.R;


public class TestFragment extends DialogFragment {

    private EditText editText;
    private View mView;


    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_test, container, false);

        if(getDialog() != null){
            if (getDialog().getWindow() != null){
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }

        editText = mView.findViewById(R.id.ed_ed_ed);
        if(FCMTokenDB.getInstance(getActivity()).getSizeOfList() > 0){
            editText.setText(FCMTokenDB.getInstance(getActivity()).getToken());
        }else {
            editText.setText("empty token.");
        }


        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null)
            return;

        int width = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._299sdp);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, height);
    }


}