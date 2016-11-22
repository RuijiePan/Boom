package com.jiepier.boom.ui;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.jiepier.boom.R;
import com.jiepier.boom.base.BaseActivity;
import com.jiepier.boom.bean.AppProcessInfo;
import com.jiepier.boom.service.CleanService;

import java.util.List;

public class MainActivity extends BaseActivity implements CleanService.OnPeocessActionListener{

    private ProgressDialog mProgressDialog;
    private CleanService mCleanService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mCleanService = ((CleanService.ProcessServiceBind) iBinder).getService();
            mCleanService.setOnActionListener(MainActivity.this);
            mCleanService.scanRunProcess();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mCleanService.setOnActionListener(null);
            mCleanService = null;
        }
    };

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initUiAndListener() {
        bindService(new Intent(this, CleanService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
        mProgressDialog = ProgressDialog.show(this,null,"  正在扫描");
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @Override
    public void onScanStarted(Context context) {
        mProgressDialog.show();
    }

    @Override
    public void onScanProgressUpdated(Context context, int current, int max) {
        mProgressDialog.setMessage("    "+current+"/"+max);
    }

    @Override
    public void onScanCompleted(Context context, List<AppProcessInfo> apps) {
        mProgressDialog.dismiss();
    }

    @Override
    public void onCleanStarted(Context context) {

    }

    @Override
    public void onCleanCompleted(Context context, long cacheSize) {

    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}