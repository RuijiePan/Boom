package com.jiepier.boom.icon;

import android.graphics.drawable.Drawable;

import com.jiepier.boom.bean.AppProcessInfo;

/**
 * Created by JiePier on 16/11/22.
 */

public class AppIcon {

    private float x;
    private float y;
    //振幅
    private StartType type;
    private int rotateAngle;
    //旋转方向:顺时针为0，逆时针为1
    private int rotateDirection;
    //logo移动方向：顺时针为0.逆时针为1
    private int moveDirection;
    //起始时间
    private long startTime;
    private AppProcessInfo info;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public StartType getType() {
        return type;
    }

    public void setType(StartType type) {
        this.type = type;
    }

    public int getRotateAngle() {
        return rotateAngle;
    }

    public void setRotateAngle(int rotateAngle) {
        this.rotateAngle = rotateAngle;
    }

    public int getRotateDirection() {
        return rotateDirection;
    }

    public void setRotateDirection(int rotateDirection) {
        this.rotateDirection = rotateDirection;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public AppProcessInfo getInfo() {
        return info;
    }

    public void setInfo(AppProcessInfo info) {
        this.info = info;
    }

    public int getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }
}
