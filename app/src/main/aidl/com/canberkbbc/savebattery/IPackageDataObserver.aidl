// IPackageDataObserver.aidl
package com.canberkbbc.savebattery;

// Declare any non-default types here with import statements

oneway interface IPackageDataObserver {
    void onRemoveCompleted(in String packageName, boolean succeeded);
}