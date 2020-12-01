package com.canberkbbc.savebattery.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.canberkbbc.savebattery.MainActivity;
import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.databinding.CardTimeUsedListItemBinding;

import java.util.List;

import com.canberkbbc.savebattery.model.TimeUsedModel;

public class TimeUsedAdapter extends RecyclerView.Adapter<TimeUsedAdapter.ViewHolderTimeUsed> {
    private MainActivity activity;
    private List<TimeUsedModel> timeUsedList;
    private CardTimeUsedListItemBinding binding;
    private int minutes,seconds,hours,days;

    public TimeUsedAdapter (MainActivity activity, List<TimeUsedModel> timeUsedList){
        this.activity = activity;
        this.timeUsedList = timeUsedList;
    }

    @NonNull
    @Override
    public TimeUsedAdapter.ViewHolderTimeUsed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.card_time_used_list_item, parent, false);
        return new TimeUsedAdapter.ViewHolderTimeUsed(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeUsedAdapter.ViewHolderTimeUsed holder, int position) {
        TimeUsedModel timeUsedModel=timeUsedList.get(position);
        holder.bind(timeUsedModel,position);
    }

    @Override
    public int getItemCount() {
        return timeUsedList.size();
    }

    public class ViewHolderTimeUsed extends RecyclerView.ViewHolder {
        private CardTimeUsedListItemBinding rowBinding;
        public ViewHolderTimeUsed(CardTimeUsedListItemBinding rowBinding) {
            super(binding.getRoot());
            this.rowBinding = rowBinding;
        }
        @SuppressLint("SetTextI18n")
        private void bind(final TimeUsedModel timeUsedModel, int position){

            long time = timeUsedModel.getTime();
            seconds = (int) (time / 1000) % 60 ;
            minutes = (int) ((time / (1000*60)) % 60);
            hours   = (int) ((time / (1000*60*60)) % 24);
            days   = (int) ((time / (1000*60*60*24)) % 360);

            rowBinding.txtPackageName.setText(timeUsedModel.getPackageName());
            rowBinding.txtUsedTime.setText(days + "d " + hours + "h " + minutes + "m " + seconds +"s " );
            Log.i("adapter count", "bind: "+ getItemCount());

            rowBinding.txtPackageName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int version = android.os.Build.VERSION.SDK_INT;
                    if (version >= 29) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", timeUsedModel.getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }
}