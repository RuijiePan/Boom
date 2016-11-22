package com.jiepier.boom.bean;

import android.graphics.drawable.Drawable;

import java.util.Comparator;

/**
 * Created by JiePier on 16/11/22.
 */

public class AppProcessInfo implements Comparable<AppProcessInfo>{

    private String appName;
    private String processName;
    private String process;
    private int pid;
    private int uid;
    private Drawable icon;
    private long memory;
    private String cpu;
    private String threadsCount;

    private boolean isSystem;
    public AppProcessInfo(){
        super();
    }

    public AppProcessInfo(String processName, int pid, int uid) {
        super();
        this.processName = processName;
        this.pid = pid;
        this.uid = uid;
    }

    @Override
    public int compareTo(AppProcessInfo another) {
        if (this.processName.compareTo(another.processName) == 0){
            if (this.memory < another.memory){
                return 1;
            }else if (this.memory == another.memory){
                return 0;
            }else {
                return -1;
            }
        }else {
            return this.processName.compareTo(another.processName);
        }
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getThreadsCount() {
        return threadsCount;
    }

    public void setThreadsCount(String threadsCount) {
        this.threadsCount = threadsCount;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }
}
