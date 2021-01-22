package com.canberkbbc.savebattery.services;

import com.canberkbbc.savebattery.model.InfoModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IApiService {
    @GET("info.json")
    Call<ArrayList<InfoModel>> getInfoList();

}
