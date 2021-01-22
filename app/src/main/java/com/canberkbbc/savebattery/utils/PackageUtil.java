package com.canberkbbc.savebattery.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.List;

public class PackageUtil {

    public static ArrayList<TaskInfo> getTaskInfos(Context context) {

        ActivityManager am = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);

        PackageManager pm = context.getPackageManager();

        List<AndroidAppProcess> processInfos = AndroidProcesses.getRunningAppProcesses();
        ArrayList<TaskInfo> taskinfos = new ArrayList<>();
        for (AndroidAppProcess processInfo : processInfos) {
            Log.i("---------------------","---------------------");
            Log.i("processInfo:","name : "+processInfo.name);
            Log.i("processInfo:","foreground : "+processInfo.foreground);
            Log.i("processInfo:","pid : "+processInfo.pid);
            Log.i("processInfo:","uid : "+processInfo.uid);
            TaskInfo taskinfo = new TaskInfo();
            String packname = processInfo.name;
            taskinfo.setPackageName(processInfo.name);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(
                        packname, 0);
                String name = applicationInfo.loadLabel(pm).toString();
                taskinfo.setName(name);
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    taskinfo.setUserTask(true);
                } else {
                    taskinfo.setUserTask(false);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskinfo.setName(packname);
            }
            if (!taskinfo.getPackageName().equals(context.getPackageName())){
                taskinfos.add(taskinfo);
            }

        }
        return taskinfos;
    }
}

