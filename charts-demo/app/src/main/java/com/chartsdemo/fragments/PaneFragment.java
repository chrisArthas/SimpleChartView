package com.chartsdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chartsdemo.R;

/**
 * Created by win on 2017/4/21.
 */

public class PaneFragment extends Fragment{

    private static final String TAG = "PaneFragment";

    public PaneFragment()
    {

    }

    static public PaneFragment getInstance()
    {
        return new PaneFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.panel_view_layout,null,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
