package com.jiepier.boom.ui;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.jiepier.boom.R;
import com.jiepier.boom.base.BaseActivity;
import com.jiepier.boom.bean.AppProcessInfo;
import com.jiepier.boom.icon.AppIconView;
import com.jiepier.boom.icon.BoomIconView;
import com.jiepier.boom.icon.BoomRect;
import com.jiepier.boom.service.CleanService;
import com.jiepier.boom.util.ToastUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements CleanService.OnPeocessActionListener {

    @BindView(R.id.appIconView)
    AppIconView appIconView;
    @BindView(R.id.boomIconView)
    BoomIconView boomIconView;
    private ProgressDialog mProgressDialog;
    private CleanService mCleanService;
    private Set<Integer> set = new HashSet<>();
    public int mPosition;

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
        mProgressDialog = ProgressDialog.show(this, null, "  正在扫描");

        boomIconView.setMoveListener(new BoomIconView.OnMoveListener() {
            @Override
            public void onMove(BoomRect rect) {
                appIconView.checkCollisionWithApp(rect);
            }
        });

        appIconView.setKillProcessListener(new AppIconView.KillProcessListener() {
            @Override
            public void killProcess(String packageName,int position) {
                //Log.w("haha","666666666========="+packageName);
                    mCleanService.killProcess(packageName);
                    mPosition = position;
            }
        });
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
        mProgressDialog.setMessage("    " + current + "/" + max);
    }

    @Override
    public void onScanCompleted(Context context, List<AppProcessInfo> apps) {
        mProgressDialog.dismiss();
        appIconView.setAppInfoList(apps);
        appIconView.invalidate();
    }

    @Override
    public void onCleanStarted(Context context) {

    }

    @Override
    public void onCleanCompleted(Context context, long cacheSize) {
        //清理完，如果可用内存变小，就显示清理的内存，否则显示APP占用的内存
        if (!set.contains(mPosition)) {
            if (cacheSize>0) {
                ToastUtil.showToast(context, "清理应用:" + appIconView.getmList().get(mPosition).getInfo().getAppName() + ",回收内存:" +
                        cacheSize/1024 + "KB");
            }else {
                ToastUtil.showToast(context, "清理应用:" + appIconView.getmList().get(mPosition).getInfo().getAppName() + ",回收内存:" +
                        appIconView.getmList().get(mPosition).getInfo().getMemory()/1024 + "KB");
            }
            set.add(mPosition);
        }
        //Toast.makeText(context,"杀死应用:"+appIconView.getmList().get(mPosition).getInfo().getAppName(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

}
