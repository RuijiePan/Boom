package com.jiepier.boom.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jiepier.boom.R;
import com.jiepier.boom.bean.AppProcessInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import processes.ProcessManager;
import processes.models.AndroidAppProcess;
import processes.models.AndroidProcess;

/**
 * Created by JiePier on 16/11/22.
 */

public class CleanService extends Service {

    private static String TAG = "CleanService";
    private boolean mIsScanning = false;
    private boolean mIsCleaning = false;

    private OnPeocessActionListener mOnActionListener;
    ActivityManager activityManager = null;
    List<AppProcessInfo> list = null;
    PackageManager packageManager = null;
    Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class ProcessServiceBind extends Binder{
        public CleanService getService(){
            return CleanService.this;
        }
    }

    private ProcessServiceBind mBinder = new ProcessServiceBind();

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        try {
            activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            packageManager = mContext.getPackageManager();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class TaskScan extends AsyncTask<Void,Integer,List<AppProcessInfo>>{

        private int mAppCount = 0;

        @Override
        protected void onPreExecute() {
            if (mOnActionListener != null) {
                mOnActionListener.onScanStarted(CleanService.this);
            }
        }

        @Override
        protected List<AppProcessInfo> doInBackground(Void... voids) {
            list = new ArrayList<>();
            ApplicationInfo appInfo = null;
            AppProcessInfo abAppProcessInfo = null;

            List<AndroidAppProcess> appProcessList = ProcessManager.getRunningAppProcesses();
            publishProgress(0,appProcessList.size());

            String lastAppProcessName = "";
            for (AndroidAppProcess appProcessInfo : appProcessList){
                publishProgress(++mAppCount, appProcessList.size());
                abAppProcessInfo = new AppProcessInfo(
                        appProcessInfo.getPackageName(), appProcessInfo.pid,
                        appProcessInfo.uid);

                try {
                    appInfo = packageManager.getApplicationInfo(appProcessInfo.getPackageName(), 0);
                    if ((appInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
                        abAppProcessInfo.setSystem(true);
                    }else {
                        abAppProcessInfo.setSystem(false);
                    }
                    Drawable icon = appInfo.loadIcon(packageManager);
                    String name = appInfo.loadLabel(packageManager).toString();
                    abAppProcessInfo.setIcon(icon);
                    abAppProcessInfo.setAppName(name);
                }catch (PackageManager.NameNotFoundException e){

                    //服务的命名
                    if (appProcessInfo.getPackageName().indexOf(":") != -1){
                        appInfo = getApplicationInfo(appProcessInfo.getPackageName().split(":")[0]);
                        if (appInfo!=null){
                            Drawable icon = appInfo.loadIcon(packageManager);
                            abAppProcessInfo.setIcon(icon);
                        }else {
                            abAppProcessInfo.setIcon(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
                        }
                    }
                    abAppProcessInfo.setSystem(true);
                    abAppProcessInfo.setAppName(appProcessInfo.getPackageName());
                }

                long memsize = activityManager.getProcessMemoryInfo(new int[]{appProcessInfo.pid})[0].getTotalPrivateDirty() * 1024;
                abAppProcessInfo.setMemory(memsize);

                list.add(abAppProcessInfo);
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (mOnActionListener != null){
                mOnActionListener.onScanProgressUpdated(CleanService.this,values[0],values[1]);
            }
        }

        @Override
        protected void onPostExecute(List<AppProcessInfo> appProcessInfos) {
            if (mOnActionListener != null){
                mOnActionListener.onScanCompleted(CleanService.this,appProcessInfos);
            }
            mIsScanning = false;
        }
    }

    public void scanRunProcess() {
        // mIsScanning = true;

        new TaskScan().execute();
    }

    public ApplicationInfo getApplicationInfo( String processName) {
        if (processName == null) {
            return null;
        }
        List<ApplicationInfo> appList = packageManager
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo appInfo : appList) {
            if (processName.equals(appInfo.processName)) {
                return appInfo;
            }
        }
        return null;
    }

    public void killBackgroundProcesses(String processName){

        String packageName = null;
        try {
            if (processName.indexOf(":")== -1){
                packageName = processName;
            }else {
                packageName = processName.split(":")[0];
            }

            activityManager.killBackgroundProcesses(packageName);

            Method forceStopPackage = activityManager.getClass()
                    .getDeclaredMethod("forceStopPackage",String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(activityManager,packageName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class TaskClean extends AsyncTask<String,Void,Long>{

        @Override
        protected Long doInBackground(String... processName) {
            long beforeMemory = 0;
            long endMemory = 0;

            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

            activityManager.getMemoryInfo(memoryInfo);
            beforeMemory = memoryInfo.availMem;
            killBackgroundProcesses(processName[0]);
            activityManager.getMemoryInfo(memoryInfo);
            endMemory = memoryInfo.availMem;

            return endMemory - beforeMemory;
        }

        @Override
        protected void onPreExecute() {
            if (mOnActionListener != null){
                mOnActionListener.onCleanStarted(CleanService.this);
            }
        }

        @Override
        protected void onPostExecute(Long result) {
            if (mOnActionListener != null) {
                mOnActionListener.onCleanCompleted(CleanService.this, result);
            }
        }
    }

    public long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        // 当前系统可用内存 ,将获得的内存大小规格化

        return memoryInfo.availMem;
    }

    public void killProcess(String processName) {
        //  mIsCleaning = true;

        new TaskClean().execute(processName);
    }

    public boolean isScanning() {
        return mIsScanning;
    }

    public boolean isCleaning() {
        return mIsCleaning;
    }

    public static interface OnPeocessActionListener {
        public void onScanStarted(Context context);

        public void onScanProgressUpdated(Context context, int current, int max);

        public void onScanCompleted(Context context, List<AppProcessInfo> apps);

        public void onCleanStarted(Context context);

        public void onCleanCompleted(Context context, long cacheSize);
    }
}
