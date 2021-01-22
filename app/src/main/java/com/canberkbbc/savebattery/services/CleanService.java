package com.canberkbbc.savebattery.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.canberkbbc.savebattery.utils.CleanCacheUtil;
import com.canberkbbc.savebattery.utils.PackageUtil;
import com.canberkbbc.savebattery.utils.TaskInfo;

import java.util.ArrayList;


public class CleanService extends IntentService {
    private static final String TAG = "CleanService";
    public CleanService() {
        super("CleanService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<TaskInfo> infos = PackageUtil.getTaskInfos(getApplicationContext());
        if (infos != null) {
            for (TaskInfo info : infos) {
                CleanCacheUtil.delAppCache(getApplicationContext(), info.getPackageName(),
                        new CleanCacheUtil.ClearCacheObserver() {
                            @Override
                            public void onRemoveCompleted(String packageName, boolean succeeded) {
                                super.onRemoveCompleted(packageName, succeeded);
                                Log.d(TAG, "Clean:packageName=" + packageName + ";succeeded=" + succeeded);
                            }
                        });
            }
        }
    }

}
