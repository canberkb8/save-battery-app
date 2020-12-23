package com.canberkbbc.savebattery.model;

import android.content.pm.PackageInfo;

import java.util.List;

public class SaveBatteryAppData {

    private static final SaveBatteryAppData ourInstance = new SaveBatteryAppData();
    public static SaveBatteryAppData getInstance(){
        return ourInstance;
    }

    public SaveBatteryAppData() {
    }

    private List<PackageInfo> installedApp;
    public List<PackageInfo> getInstalledApp(){
        return installedApp;
    }
    public void setInstalledApp(List<PackageInfo> installedApp){
        this.installedApp = installedApp;
    }
}
