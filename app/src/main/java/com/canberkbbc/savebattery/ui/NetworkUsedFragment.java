package com.canberkbbc.savebattery.ui;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.canberkbbc.savebattery.MainActivity;
import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.databinding.FragmNetworkusedBinding;

import java.util.Arrays;
import java.util.List;


public class NetworkUsedFragment extends BaseFragment {
    View view;
    private FragmNetworkusedBinding networkusedBinding;

    private static final String DEBUG_TAG = "NetworkStatusExample";
    private ConnectivityManager connMgr;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        networkusedBinding = DataBindingUtil.inflate(inflater, R.layout.fragm_networkused, container, false);
        view = networkusedBinding.getRoot();

        connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        checkNetworkConnetion();
        Log.i("isOnline", "isOnline: " +isOnline());

        return view;
    }

    //Wifi nin açık olup olmadını ve telefonun baglı olup olmadıgını donurur.
    private void checkNetworkConnetion(){
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
        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);
    }

    //Telefon internete bağlı olup olmadığını döndürür.
    public boolean isOnline() {
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    //network kullanımı deneme fonksıyonları
    public void getAllAppList() {

        PackageManager p = activity.getPackageManager();
        List<ApplicationInfo> packages = p.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = p.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
                String[] permissions = packageInfo.requestedPermissions;
                if (permissions != null) {
                    for (String permissionName : permissions) {
                        if (permissionName.equals("android.permission.INTERNET")) {
                            ApplicationInfo appInfo = packageInfo.applicationInfo;
                            Log.i("getApplicationIcon", "getApplicationIcon: " + activity.getPackageManager().getApplicationIcon(appInfo));
                            Log.i("getApplicationLabel", "getApplicationLabel: " + activity.getPackageManager().getApplicationLabel(appInfo));
                            Log.i("getDataUsage", "getDataUsage: " + getDataUsage(appInfo));
                            Log.i("permissionName", "permissionName: " + permissionName);
                            Log.i("permissionsList", "permissionsList: " + Arrays.toString(permissions));
                            Log.i("getApplicationLabel", "getApplicationLabel: " + activity.getPackageManager().getApplicationLabel(appInfo));
                            Log.i("getDataUsage", "getDataUsage: " + getDataUsage(appInfo));
                            Log.i("permissionsList", "permissionsList: " + Arrays.toString(permissions));
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public String getDataUsage(ApplicationInfo appInfo) {

        int uid = appInfo.uid;
        Log.i("getUidRxBytes", "getUidRxBytes: " + (double) TrafficStats.getUidRxBytes(uid) / (1024 * 1024));
        Log.i("getUidTxBytes", "getUidTxBytes: " + (double) TrafficStats.getUidTxBytes(uid) / (1024 * 1024));
        Log.i("getUidRxPackets", "getUidRxPackets: " + (double) TrafficStats.getUidRxPackets(uid) / (1024 * 1024));
        Log.i("getUidTxPackets", "getUidTxPackets: " + (double) TrafficStats.getUidTxPackets(uid) / (1024 * 1024));

        double received = (double) TrafficStats.getUidRxBytes(uid) / (1024 * 1024);
        double sent = (double) TrafficStats.getUidTxBytes(uid) / (1024 * 1024);

        double total = received + sent;

        return String.format("%.2f", total) + " MB";

    }
}
