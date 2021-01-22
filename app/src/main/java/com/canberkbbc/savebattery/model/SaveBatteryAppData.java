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
    public void setInstalledApp(List<PackageInfo> installedApp){ this.installedApp = installedApp; }

    private InfoModel infoModel;
    public InfoModel getInfoModel(){
        return infoModel;
    }
    public void setInfoModel(InfoModel infoModel){
        this.infoModel = infoModel;
    }

    private int portNumber;
    public int getPortNumber(){
        return portNumber;
    }
    public void setPortNumber(int portNumber){
        this.portNumber = portNumber;
    }
}
