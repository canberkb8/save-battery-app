package com.canberkbbc.savebattery.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.adapter.InstalledAppAdapter;
import com.canberkbbc.savebattery.databinding.FragmHomepageBinding;

import java.io.File;
import java.util.List;

public class HomeFragment extends BaseFragment {
    View view;
    private FragmHomepageBinding homepageBinding;
    private InstalledAppAdapter installedAppAdapter;

    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;

    private PackageManager packageManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        homepageBinding = DataBindingUtil.inflate(inflater, R.layout.fragm_homepage, container, false);
        view = homepageBinding.getRoot();

        packageManager = activity.getPackageManager();
        setInstalledAppAdapter(getInstalledAppList());
        click();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            deleteCache(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setInstalledAppAdapter(List<PackageInfo> installedAppList) {
        installedAppAdapter = new InstalledAppAdapter(activity, installedAppList);
        homepageBinding.recyclerHomepage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        homepageBinding.recyclerHomepage.setHasFixedSize(true);
        homepageBinding.recyclerHomepage.setAdapter(installedAppAdapter);
    }

    private List<PackageInfo> getInstalledAppList() {
        List<PackageInfo> infos = packageManager.getInstalledPackages(0);
        return infos;
    }

    private void click() {
        homepageBinding.btnPermisson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });
    }

    //clear cache
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
