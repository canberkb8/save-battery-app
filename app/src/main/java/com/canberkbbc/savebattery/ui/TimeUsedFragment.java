package com.canberkbbc.savebattery.ui;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.pm.PackageInfo;
import android.graphics.Color;
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
import com.canberkbbc.savebattery.model.SaveBatteryAppData;
import com.canberkbbc.savebattery.model.TimeUsedModel;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


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
    private long timeInforground, lastTime, start, end;

    ArrayList<BarEntry> dataValues;
    String[] graphAppNameList;

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
                        calendarInterval = Calendar.DAY_OF_YEAR;
                        setAdapter();
                        Log.i("TAG", "TabLayout: case INTERVAL_DAILY");
                        break;
                    case 1:
                        interval = usageStatsManager.INTERVAL_WEEKLY;
                        calendarInterval = Calendar.WEEK_OF_YEAR;
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

    private void calendar(int calendarInterval) {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+03"));
        end = System.currentTimeMillis();
        calendar.add(calendarInterval, -1);
        start = calendar.getTimeInMillis();
        Log.i("start", "start: " + start);
        Log.i("end", "end: " + end);
    }

    private List<UsageStats> getStats(int interval, int calendarInterval) {
        calendar(calendarInterval);
        return usageStatsManager.queryUsageStats(interval, start, end);
    }

    private Map<String, UsageStats> getMapStats() {
        return usageStatsManager.queryAndAggregateUsageStats(start, end);
    }

    private ArrayList<BarEntry> getGraphList(List<TimeUsedModel> arrayList) {
        int graphSize = 5;
        graphAppNameList = new String[graphSize];
        dataValues = new ArrayList<BarEntry>();
        for (int i = 0; i < graphSize; i++) {
            for (PackageInfo info : SaveBatteryAppData.getInstance().getInstalledApp()) {
                if (arrayList.get(i).getPackageName().equals(info.packageName)) {
                    graphAppNameList[i] = info.applicationInfo.loadLabel(activity.getPackageManager()).toString();
                }
            }
            dataValues.add(new BarEntry(i, arrayList.get(i).getTime()));
        }
        return dataValues;
    }

    private void createGraph() {
        BarDataSet barDataSet = new BarDataSet(getGraphList(getTimeUsedModelStatsList), "Applications");
        barDataSet.setValueTextColor(Color.BLACK);
        int[] colors = new int[]{Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED, Color.GRAY};
        barDataSet.setColors(colors);

        BarData barData = new BarData(barDataSet);

        timeusedBinding.graph.getXAxis().setValueFormatter(new IndexAxisValueFormatter(graphAppNameList));
        timeusedBinding.graph.getXAxis().setTextSize(8f);
        timeusedBinding.graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        timeusedBinding.graph.getAxisRight().setDrawLabels(false);
        timeusedBinding.graph.getLegend().setEnabled(false);
        timeusedBinding.graph.setFitBars(true);
        timeusedBinding.graph.setData(barData);
        timeusedBinding.graph.getDescription().setText("");
        timeusedBinding.graph.animateY(2000);
        timeusedBinding.graph.invalidate();
    }


    private void createModelList() {
        boolean lastTimeBiggerThan = true;
        getTimeUsedModelStatsList = new ArrayList<>();
        for (int i = 0; i < getStatsList.size(); i++) {
            timeInforground = getStatsList.get(i).getTotalTimeInForeground();
            lastTime = getStatsList.get(i).getLastTimeUsed();
            if (timeInforground > 0) {
                packageName = getStatsList.get(i).getPackageName();
                for (int j = 0; j < getTimeUsedModelStatsList.size(); j++) {
                    if (packageName.equals(getTimeUsedModelStatsList.get(j).getPackageName())) {
                        if (lastTime > getTimeUsedModelStatsList.get(j).getLastTime()) {
                            getTimeUsedModelStatsList.remove(j);
                        } else {
                            lastTimeBiggerThan = false;
                        }
                    }
                }
                if (lastTimeBiggerThan) {
                    timeUsedModel = new TimeUsedModel(packageName, timeInforground, lastTime);
                    getTimeUsedModelStatsList.add(timeUsedModel);
                }
            }
        }
    }

    private void setAdapter() {
        getStatsList = new ArrayList<>();
        getStatsList = getStats(interval, calendarInterval);
        createModelList();
        getTimeUsedModelStatsList = selectionSort(getTimeUsedModelStatsList);
        createGraph();
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