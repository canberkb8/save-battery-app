package com.canberkbbc.savebattery.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.canberkbbc.savebattery.utils.PackageUtil;
import com.canberkbbc.savebattery.utils.TaskInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SpeedService extends IntentService {

    public SpeedService() {
        super("SpeedService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<TaskInfo> infos = PackageUtil.getTaskInfos(getApplicationContext());
        if (infos != null){
            for (TaskInfo info:infos){
                killAppByPackage(getApplicationContext(),info.getPackageName());
            }
        }
    }

    private void killAppByPackage(Context context, String packageToKill) {

        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //killBackgroundProcesses(API > 8)
        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }
            if (packageInfo.packageName.equals(packageToKill)
                    && mActivityManager != null) {
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);
            }
        }
        try {
            Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(mActivityManager, packageToKill);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
