package com.canberkbbc.savebattery.model;

public class TimeUsedModel {
    String packageName;
    long time;
    long lastTime;

    public TimeUsedModel(String packageName, long time, long lastTime) {
        this.packageName = packageName;
        this.time = time;
        this.lastTime = lastTime;
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

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

}
