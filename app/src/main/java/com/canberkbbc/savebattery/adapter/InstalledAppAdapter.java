package com.canberkbbc.savebattery.adapter;

import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.canberkbbc.savebattery.MainActivity;
import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.databinding.CardInstalledAppListItemBinding;

import java.util.Arrays;
import java.util.List;

public class InstalledAppAdapter extends RecyclerView.Adapter<InstalledAppAdapter.ViewHolderInstalledApp> {
    private MainActivity activity;
    private List<PackageInfo> installedAppList;
    private CardInstalledAppListItemBinding binding;

    public InstalledAppAdapter (MainActivity activity, List<PackageInfo> installedAppList){
        this.activity = activity;
        this.installedAppList = installedAppList;
    }

    @NonNull
    @Override
    public ViewHolderInstalledApp onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.card_installed_app_list_item, parent, false);
        return new ViewHolderInstalledApp(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInstalledApp holder, int position) {
        PackageInfo installedApp=installedAppList.get(position);
        holder.bind(installedApp,position);
    }

    @Override
    public int getItemCount() {
        return installedAppList.size();
    }

    public class ViewHolderInstalledApp extends RecyclerView.ViewHolder {
        private CardInstalledAppListItemBinding rowBinding;
        public ViewHolderInstalledApp(CardInstalledAppListItemBinding rowBinding) {
            super(binding.getRoot());
            this.rowBinding = rowBinding;
        }
        private void bind(final PackageInfo installedApp, int position){
            rowBinding.txtTitle.setText(installedApp.applicationInfo.loadLabel(activity.getPackageManager()).toString());
            rowBinding.txtLastUsed.setText(installedApp.versionName);
            rowBinding.imgAppIcon.setImageDrawable(installedApp.applicationInfo.loadIcon(activity.getPackageManager()));
        }
    }
}