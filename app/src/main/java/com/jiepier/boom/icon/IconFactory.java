package com.jiepier.boom.icon;

import android.graphics.drawable.Icon;

import com.jiepier.boom.base.App;
import com.jiepier.boom.bean.AppProcessInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by panruijiesx on 2016/11/23.
 */

public class IconFactory {

    public static final int APP_FLOAT_TIME = 3000;
    // 中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 13;
    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 5;
    // 中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    // 振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;

    private List<AppProcessInfo> mList = new ArrayList<>();
    private Random random = new Random();

    public IconFactory(List<AppProcessInfo> list){
        this.mList = list;
    }

    public AppIcon generateAppIcon(AppProcessInfo info){

        AppIcon appIcon = new AppIcon();
        appIcon.setSpeed(3);
        appIcon.setRotateAngle(random.nextInt(360));
        appIcon.setRotateDirection(random.nextInt(2));
        appIcon.setStartTime(System.currentTimeMillis()+random.nextInt((int)(APP_FLOAT_TIME*1.5)));
        appIcon.setInfo(info);
        appIcon.setDegree((int)(Math.random()*Math.PI*2));
        return appIcon;
    }

    public List<AppIcon> generateAppIcons(){
        return generateAppIcons(mList.size());
    }

    private List<AppIcon> generateAppIcons(int appSize){
        List<AppIcon> apps = new LinkedList<>();
        for (int i = 0 ;i < appSize;i++){
            apps.add(generateAppIcon(mList.get(i)));
        }
        return apps;
    }

}
