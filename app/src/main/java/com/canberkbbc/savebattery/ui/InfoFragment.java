package com.canberkbbc.savebattery.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.canberkbbc.savebattery.MainActivity;
import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.adapter.InfoAdapter;
import com.canberkbbc.savebattery.databinding.FragmInfoBinding;
import com.canberkbbc.savebattery.model.InfoModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFragment extends BaseFragment {
    View view;
    private FragmInfoBinding infoBinding;
    private InfoAdapter infoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        infoBinding = DataBindingUtil.inflate(inflater, R.layout.fragm_info, container, false);
        view = infoBinding.getRoot();
        getInfoList();
        return view;
    }

    private void getInfoList() {
        Call<ArrayList<InfoModel>> call = MainActivity.iApiService.getInfoList();
        call.enqueue(new Callback<ArrayList<InfoModel>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<InfoModel>> call, @NotNull Response<ArrayList<InfoModel>> response) {
                if (response.body() != null) {
                    setAdapter(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<InfoModel>> call, @NotNull Throwable t) {
                Log.e("Response onFailure", "onFailure:" + t.toString());
            }
        });
    }

    void setAdapter(ArrayList<InfoModel> infoList) {
        infoAdapter = new InfoAdapter(activity, infoList);
        infoBinding.recyclerInfo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        infoBinding.recyclerInfo.setHasFixedSize(true);
        infoBinding.recyclerInfo.setAdapter(infoAdapter);
    }
}
