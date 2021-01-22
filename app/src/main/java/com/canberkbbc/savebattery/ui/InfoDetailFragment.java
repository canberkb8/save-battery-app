package com.canberkbbc.savebattery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.databinding.FragmInfoDetailBinding;
import com.canberkbbc.savebattery.model.InfoModel;
import com.canberkbbc.savebattery.model.SaveBatteryAppData;

public class InfoDetailFragment extends BaseFragment {
    View view;
    private FragmInfoDetailBinding infoDetailBinding;
    InfoModel infoModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        infoDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragm_info_detail, container, false);
        view = infoDetailBinding.getRoot();

        setView();
        return view;
    }

    private void setView() {
        infoModel = SaveBatteryAppData.getInstance().getInfoModel();
        infoDetailBinding.txtTitle.setText(infoModel.getTitle());
        infoDetailBinding.txtContentInfo.setText(infoModel.getContentinfo());
        infoDetailBinding.txtContent.setText(infoModel.getContent());
        Glide.with(this).load(infoModel.getImgurl()).fitCenter().into(infoDetailBinding.imgInfodetail);
    }

}
