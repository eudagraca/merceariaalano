package com.mercearia.alano.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class AppIntroFragment extends Fragment {


    //Layout id
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;

    public AppIntroFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static AppIntroFragment newInstance(int layoutResId) {
        AppIntroFragment sampleSlide = new AppIntroFragment();

        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(bundleArgs);

        return sampleSlide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(layoutResId, container, false);
    }

}