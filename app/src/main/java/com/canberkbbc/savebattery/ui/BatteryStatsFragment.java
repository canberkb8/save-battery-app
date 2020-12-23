package com.canberkbbc.savebattery.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.databinding.FragmBatterystatsBinding;
import com.github.pwittchen.rxbattery.library.RxBattery;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BatteryStatsFragment extends BaseFragment {
    View view;
    private FragmBatterystatsBinding batterystatsBinding;

    BatteryManager mBatteryManager;
    int status;
    double mAh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        batterystatsBinding = DataBindingUtil.inflate(inflater, R.layout.fragm_batterystats, container, false);
        view = batterystatsBinding.getRoot();

        stats();
        rxBattery();

        activity.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        return view;
    }



    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            batterystatsBinding.textView3.setText("Şarj durumu : " + getStatusString(status));
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
            batterystatsBinding.textView.setText("Şarj yüzdesi : " + batteryState.component4());
            batterystatsBinding.textView2.setText("Sıcaklık değeri : " + (float) batteryState.component5() / 10 + "°C");
            batterystatsBinding.textView4.setText("Voltaj değeri : " + batteryState.component6());
        });
    }

    public void stats() {
        mBatteryManager = (BatteryManager) activity.getSystemService(Context.BATTERY_SERVICE);
        int level = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        mAh = (2300 * level * 0.01);
        Log.i("mAh", "mAh: " + mAh);
    }
}
