package com.canberkbbc.savebattery.utils;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.canberkbbc.savebattery.model.SaveBatteryAppData;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class NsdHelper {
    Context mContext;

    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;
    private ServerSocket serverSocket;

    private final String SERVICE_TYPE = "_nsdchat._tcp";

    private static final String TAG = "NsdHelper";
    private String mServiceName = "NsdChat";

    public List<NsdServiceInfo> mDiscoveryServices;

    // First step : register a NsdServiceInfo object to advertise your service
    NsdServiceInfo mService;

    public NsdHelper(Context context) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }


    public void initializeNsd() {
        initializeRegistrationListener();
        initializeDiscoveryListener();
        initializeResolveListener();
        initializeServerSocket();
    }


    // Check success of service registration
    private void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
                mServiceName = nsdServiceInfo.getServiceName();
                Log.d(TAG, "Registered name : " + mServiceName);
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int arg1) {
                Log.d(TAG, "Registration Failed");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
                Log.d(TAG, "Unregistered");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            }

        };
    }

    public void registerService(int port) {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);

        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);

    }

    private void initializeServerSocket() {
        // Initialize a server socket on the next available port.
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Store the chosen port.
        int localPort = serverSocket.getLocalPort();
        SaveBatteryAppData.getInstance().setPortNumber(localPort);
    }

    // To discover services of the type you're looking for on the network
    private void initializeDiscoveryListener() {
        mDiscoveryServices = new ArrayList<NsdServiceInfo>();
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success : " + service);
                Log.d(TAG, "Host = " + service.getServiceName());
                Log.d(TAG, "port = " + service.getPort());

                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same machine: " + mServiceName);
                }else if (service.getServiceName().contains(mServiceName)){
                    mNsdManager.resolveService(service, mResolveListener);
                }else{
                    mDiscoveryServices.add(service);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                if (mService == service) {
                    mService = null;
                    Log.e(TAG, "service lost" + service);
                }
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void discoverServices() {
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }



    // Connection handshake
    private void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

                mService = serviceInfo;
                int port = mService.getPort();
                InetAddress host = mService.getHost();

                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
            }
        };
    }

    public void stopDiscovery() {
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }

    public void tearDown() {
        if (mNsdManager != null) {
            if (mRegistrationListener != null) {
                mNsdManager.unregisterService(mRegistrationListener);
            }
            if (mDiscoveryListener != null) {
                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            }
        }
    }
}
