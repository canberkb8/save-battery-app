package com.canberkbbc.savebattery.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.adapter.InstalledAppAdapter;
import com.canberkbbc.savebattery.adapter.NetworkAdapter;
import com.canberkbbc.savebattery.databinding.FragmNetworkusedBinding;
import com.canberkbbc.savebattery.model.SaveBatteryAppData;
import com.canberkbbc.savebattery.utils.NsdHelper;

import java.util.ArrayList;
import java.util.List;


public class NetworkUsedFragment extends BaseFragment {
    View view;
    private FragmNetworkusedBinding networkusedBinding;
    private ConnectivityManager connMgr;
    private WifiManager wifiManager;
    private WifiInfo info;
    private NsdHelper mNsdHelper;
    private final int NSDCHAT_PORT = 9000;

    public int speed = 0;
    public String ssid,bssid,macAddress,netmaskIp,gatewayIp = null;

    String[] PERMS_INITIAL = {
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    List<ScanResult> results = new ArrayList<>();
    private NetworkAdapter networkAdapter;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        networkusedBinding = DataBindingUtil.inflate(inflater, R.layout.fragm_networkused, container, false);
        view = networkusedBinding.getRoot();

        connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        getWifiInfo();
        checkNetworkConnetion();
        scanWifiReceiver();
        createNsdHelper();
        setView();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNsdHelper != null) {
            mNsdHelper.initializeNsd();
            mNsdHelper.registerService(NSDCHAT_PORT);
        }
    }

    void createNsdHelper(){
        mNsdHelper = new NsdHelper(activity);
        mNsdHelper.initializeNsd();
        mNsdHelper.registerService(NSDCHAT_PORT);
        mNsdHelper.discoverServices();
    }

    void setView(){
        networkusedBinding.txtDeviceName.setText(getDeviceName());
        networkusedBinding.txtPortnumber.setText("Port Number : "+ SaveBatteryAppData.getInstance().getPortNumber());
        networkusedBinding.txtConnectionMobile.setText("Mobile Connection : "+ isOnline());
    }


    //Wifi alanımızdaki wifilerin listesi ve bilgileri
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void scanWifiReceiver() {
        ActivityCompat.requestPermissions(activity, PERMS_INITIAL, 127);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                } else {
                    scanFailure();
                }
            }
        };
        activity.registerReceiver(wifiScanReceiver, intentFilter);
        boolean success = wifiManager.startScan();
        if (!success) {
            scanFailure();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void scanSuccess() {
        if (results != null) {
            results.clear();
        }
        results = wifiManager.getScanResults();
        setNetworkAdapter(results);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void scanFailure() {
        results = wifiManager.getScanResults();
    }

    private void setNetworkAdapter(List<ScanResult> wifiList) {
        networkAdapter = new NetworkAdapter(activity, wifiList);
        networkusedBinding.recyclerWifi.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        networkusedBinding.recyclerWifi.setHasFixedSize(true);
        networkusedBinding.recyclerWifi.setAdapter(networkAdapter);
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    //Wifi nin açık olup olmadını ve telefonun baglı olup olmadıgını donurur.
    private void checkNetworkConnetion() {
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
    }

    //Telefon internete bağlı olup olmadığını döndürür.
    public boolean isOnline() {
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean getWifiInfo() {
        WifiManager wifi = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            info = wifi.getConnectionInfo();
            speed = info.getLinkSpeed();
            ssid = info.getSSID();
            bssid = info.getBSSID();
            macAddress = info.getMacAddress();
            gatewayIp = getIpFromIntSigned(wifi.getDhcpInfo().gateway);
            netmaskIp = getIpFromIntSigned(wifi.getDhcpInfo().netmask);
            return true;
        }
        return false;
    }

    public static String getIpFromIntSigned(int ip_int) {
        String ip = "";
        for (int k = 0; k < 4; k++) {
            ip = ip + ((ip_int >> k * 8) & 0xFF) + ".";
        }
        return ip.substring(0, ip.length() - 1);
    }

}

