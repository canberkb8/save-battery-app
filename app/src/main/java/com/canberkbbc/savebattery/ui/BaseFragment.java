package com.canberkbbc.savebattery.ui;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.canberkbbc.savebattery.MainActivity;

public abstract class BaseFragment extends Fragment {
    protected MainActivity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = ((MainActivity) context);
    }
}
