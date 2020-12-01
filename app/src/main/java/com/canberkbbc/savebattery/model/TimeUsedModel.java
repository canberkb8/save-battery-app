package com.canberkbbc.savebattery.model;

public class TimeUsedModel {
    String packageName;
    long time;

    public TimeUsedModel(String packageName, long time) {
        this.packageName = packageName;
        this.time = time;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
