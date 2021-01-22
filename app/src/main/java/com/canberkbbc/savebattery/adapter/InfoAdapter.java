package com.canberkbbc.savebattery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.canberkbbc.savebattery.MainActivity;
import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.databinding.CardInfoItemBinding;
import com.canberkbbc.savebattery.model.InfoModel;
import com.canberkbbc.savebattery.model.SaveBatteryAppData;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolderInfo> {
    private MainActivity activity;
    private ArrayList<InfoModel> infoList;
    private CardInfoItemBinding binding;

    public InfoAdapter(MainActivity activity, ArrayList<InfoModel> infoList) {
        this.activity = activity;
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public ViewHolderInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.card_info_item, parent, false);
        return new ViewHolderInfo(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInfo holder, int position) {
        InfoModel infoModel = infoList.get(position);
        holder.bind(infoModel, position);
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public class ViewHolderInfo extends RecyclerView.ViewHolder {
        private CardInfoItemBinding rowBinding;

        public ViewHolderInfo(CardInfoItemBinding rowBinding) {
            super(binding.getRoot());
            this.rowBinding = rowBinding;
        }

        private void bind(final InfoModel infoModel, int position) {
            rowBinding.txtTitle.setText(infoModel.getTitle());
            rowBinding.txtContent.setText(infoModel.getContentinfo());
            rowBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SaveBatteryAppData.getInstance().setInfoModel(infoModel);
                    activity.changeFragment(R.id.infoDetail);
                }
            });
        }
    }
}
