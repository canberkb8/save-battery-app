package com.canberkbbc.savebattery.ui;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.canberkbbc.savebattery.R;
import com.canberkbbc.savebattery.adapter.TimeUsedAdapter;
import com.canberkbbc.savebattery.databinding.FragmTimeusedBinding;
import com.canberkbbc.savebattery.model.TimeUsedModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class TimeUsedFragment extends BaseFragment {

    View view;
    FragmTimeusedBinding timeusedBinding;
    private TimeUsedAdapter timeUsedAdapter;
    private TimeUsedModel timeUsedModel;
    private UsageStatsManager usageStatsManager;
    Calendar calendar;

    List<UsageStats> getStatsList;
    List<TimeUsedModel> getTimeUsedModelStatsList;

    private int interval = usageStatsManager.INTERVAL_DAILY;
    private int calendarInterval = Calendar.DATE;
    private String packageName;
    private long timeInforground, start, end;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        timeusedBinding = DataBindingUtil.inflate(inflater, R.layout.fragm_timeused, container, false);
        view = timeusedBinding.getRoot();

        usageStatsManager = (UsageStatsManager) activity.getSystemService(activity.USAGE_STATS_SERVICE);

        tabListener();
        setAdapter();
        return view;
    }


    private void tabListener() {
        timeusedBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        interval = usageStatsManager.INTERVAL_DAILY;
                        calendarInterval = Calendar.DAY_OF_WEEK;
                        setAdapter();
                        Log.i("TAG", "TabLayout: case INTERVAL_DAILY");
                        break;
                    case 1:
                        interval = usageStatsManager.INTERVAL_WEEKLY;
                        calendarInterval = Calendar.WEEK_OF_MONTH;
                        setAdapter();
                        Log.i("TAG", "TabLayout: case INTERVAL_WEEKLY");
                        break;
                    case 2:
                        interval = usageStatsManager.INTERVAL_MONTHLY;
                        calendarInterval = Calendar.MONTH;
                        setAdapter();
                        Log.i("TAG", "TabLayout: case INTERVAL_MONTHLY");
                        break;
                    case 3:
                        interval = usageStatsManager.INTERVAL_YEARLY;
                        calendarInterval = Calendar.YEAR;
                        setAdapter();
                        Log.i("TAG", "TabLayout: case INTERVAL_YEARLY");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void calender(int calendarInterval) {
        calendar = Calendar.getInstance();
        calendar.add(calendarInterval, -1);
        start = calendar.getTimeInMillis();
        end = System.currentTimeMillis();
    }

    private List<UsageStats> getStats(int interval, int calendarInterval) {
        calender(calendarInterval);
        List<UsageStats> stats = usageStatsManager.queryUsageStats(interval, start, end);
        return stats;
    }

    private Map<String, UsageStats> getMapStats() {
        Map<String, UsageStats> queryUsageStats = usageStatsManager.queryAndAggregateUsageStats(start, end);
        return queryUsageStats;
    }

    private void createModelList() {
        getTimeUsedModelStatsList = new ArrayList<>();
        for (int i = 0; i < getStatsList.size(); i++) {
            timeInforground = getStatsList.get(i).getTotalTimeInForeground();
            packageName = getStatsList.get(i).getPackageName();
            timeUsedModel = new TimeUsedModel(packageName, timeInforground);
            getTimeUsedModelStatsList.add(timeUsedModel);
        }
    }

    private void setAdapter() {
        getStatsList = new ArrayList<>();
        getStatsList = getStats(interval, calendarInterval);
        createModelList();
        getTimeUsedModelStatsList = selectionSort(getTimeUsedModelStatsList);
        timeUsedAdapter = new TimeUsedAdapter(activity, getTimeUsedModelStatsList);
        timeusedBinding.recyclerTimeused.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        timeusedBinding.recyclerTimeused.setHasFixedSize(true);
        timeusedBinding.recyclerTimeused.setAdapter(timeUsedAdapter);
    }

    public static List<TimeUsedModel> selectionSort(List<TimeUsedModel> arrayList) {
        TimeUsedModel model;
        int min;
        for (int i = 0; i < arrayList.size() - 1; i++) {
            min = i;
            for (int j = i; j < arrayList.size(); j++) {
                if (arrayList.get(j).getTime() < arrayList.get(min).getTime()) {
                    min = j;
                }
            }
            model = arrayList.get(i);
            arrayList.set(i, arrayList.get(min));
            arrayList.set(min, model);
        }
        Collections.reverse(arrayList);
        return arrayList;
    }
}


/**
 * for(Map.Entry<String, UsageStats> entry : getMapStats().entrySet()) {
 * String key = entry.getKey();
 * UsageStats value = entry.getValue();
 * long timeInforground = value.getTotalTimeInForeground();
 * long getLastTimeUsed = value.getLastTimeUsed();
 * long describeContents = value.describeContents();
 * Log.i(TAG, "getLastTimeUsed : "+ getLastTimeUsed +" : "+"describeContents: "+describeContents);
 * String packageName=value.getPackageName();
 * int minutes = (int) ((timeInforground / (1000*60)) % 60);
 * int seconds = (int) (timeInforground / 1000) % 60 ;
 * int hours   = (int) ((timeInforground / (1000*60*60)) % 24);
 * Log.i(TAG, "getMapStats : "+ packageName +" : "+"Time is: "+hours+"h"+":"+minutes+"m"+seconds+"s");
 * }
 */
