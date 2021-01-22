// IPackageStatsObserver.aidl
package com.canberkbbc.savebattery;

// Declare any non-default types here with import statements

oneway interface IPackageStatsObserver {

    void onGetStatsCompleted(in PackageStats pStats, boolean succeeded);
}