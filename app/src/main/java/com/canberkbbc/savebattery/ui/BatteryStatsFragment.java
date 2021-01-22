package com.canberkbbc.savebattery.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.databinding.FragmBatterystatsBinding;
import com.canberkbbc.savebattery.services.CleanService;
import com.canberkbbc.savebattery.services.SpeedService;
import com.github.pwittchen.rxbattery.library.RxBattery;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BatteryStatsFragment extends BaseFragment {
    View view;
    private FragmBatterystatsBinding batterystatsBinding;

    BatteryManager mBatteryManager;
    int status;
    double mAh;


    private static final int DELAY = 3500;
    private boolean isWork = false;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        batterystatsBinding = DataBindingUtil.inflate(inflater, R.layout.fragm_batterystats, container, false);
        view = batterystatsBinding.getRoot();

        click();
        stats();
        rxBattery();

        activity.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        return view;
    }

    void click() {
        batterystatsBinding.btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service("cache");
            }
        });
        batterystatsBinding.btnSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service("speed");
            }
        });
    }

    void service(String service){
        if (isWork){
            return;
        }
        isWork = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isWork = false;
                if (service.equals("cache")){
                    Toast.makeText(activity,"Cache Cleared",Toast.LENGTH_SHORT).show();
                }else if(service.equals("speed")){
                    Toast.makeText(activity,"Accelerated",Toast.LENGTH_SHORT).show();
                }
            }
        },DELAY);
        if (service.equals("cache")){
            startCleanService();
        }else if(service.equals("speed")){
            startSpeedService();
        }
    }

    void startSpeedService(){
        getActivity().startService(new Intent(activity, SpeedService.class));
    }
    void startCleanService(){
        getActivity().startService(new Intent(activity, CleanService.class));
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            batterystatsBinding.txtChargeStatus.setText("Charge Status : " + getStatusString(status));
        }
    };

    private String getStatusString(int status) {
        String statusString = "Unknown";
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = "Not Charging";
                break;
        }
        return statusString;
    }


    @SuppressLint("CheckResult")
    public void rxBattery() {
        RxBattery.observe(activity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(batteryState -> {
            if (batteryState.component4()>75){
                setImage(R.drawable.ic_battery1);
            }else if(batteryState.component4() < 75 && batteryState.component4() > 50){
                setImage(R.drawable.ic_battery2);
            }else if(batteryState.component4() < 50 && batteryState.component4() > 25){
                setImage(R.drawable.ic_battery3);
            }else if(batteryState.component4() < 25 && batteryState.component4() > 0){
                setImage(R.drawable.ic_battery4);
            }
            batterystatsBinding.txtChargePercentage.setText("Charge Percentage : " + batteryState.component4());
            batterystatsBinding.txtTemp.setText("Temperature : " + (float) batteryState.component5() / 10 + "°C");
            batterystatsBinding.txtVoltage.setText("Voltage : " + batteryState.component6());
        });
    }

    void setImage(int image){
        batterystatsBinding.imgBattery.setImageResource(image);
    }

    public void stats() {
        mBatteryManager = (BatteryManager) activity.getSystemService(Context.BATTERY_SERVICE);
        int level = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        mAh = (2300 * level * 0.01);
        Log.i("mAh", "mAh: " + mAh);
    }


}




