package com.canberkbbc.savebattery.adapter;

import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.canberkbbc.savebattery.MainActivity;
import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.databinding.CardWifiItemBinding;

import java.util.List;

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.ViewHolderInfo> {
    private MainActivity activity;
    private List<ScanResult> wifiList;
    private CardWifiItemBinding binding;

    public NetworkAdapter(MainActivity activity, List<ScanResult> wifiList) {
        this.activity = activity;
        this.wifiList = wifiList;
    }

    @NonNull
    @Override
    public NetworkAdapter.ViewHolderInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.card_wifi_item, parent, false);
        return new NetworkAdapter.ViewHolderInfo(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkAdapter.ViewHolderInfo holder, int position) {
        ScanResult wifiItem = wifiList.get(position);
        holder.bind(wifiItem, position);
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    public class ViewHolderInfo extends RecyclerView.ViewHolder {
        private CardWifiItemBinding rowBinding;

        public ViewHolderInfo(CardWifiItemBinding rowBinding) {
            super(binding.getRoot());
            this.rowBinding = rowBinding;
        }

        private void bind(final ScanResult wifiItem, int position) {

            rowBinding.txtWifiName.setText(wifiItem.SSID);
            rowBinding.txtBssid.setText(wifiItem.BSSID);
            rowBinding.txtCapabilites.setText(wifiItem.capabilities);
            String ghz = String.valueOf(wifiItem.frequency);
            rowBinding.txtGhz.setText("Ghz : " + ghz.substring(0,1)+","+ghz.substring(2,3));
            int wifilevel = wifiItem.level + 100;
            if (wifilevel > 50) {
                rowBinding.imgWifiIcon.setImageResource(R.drawable.ic_wifi4);
            } else if (wifilevel < 50 && wifilevel > 35) {
                rowBinding.imgWifiIcon.setImageResource(R.drawable.ic_wifi3);
            } else if (wifilevel < 35 && wifilevel > 15) {
                rowBinding.imgWifiIcon.setImageResource(R.drawable.ic_wifi2);
            } else {
                rowBinding.imgWifiIcon.setImageResource(R.drawable.ic_wifi1);
            }
        }
    }
}