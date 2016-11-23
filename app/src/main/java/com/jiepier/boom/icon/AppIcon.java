package com.jiepier.boom.icon;

import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;

import com.jiepier.boom.base.App;
import com.jiepier.boom.bean.AppProcessInfo;

/**
 * Created by JiePier on 16/11/22.
 */

public class AppIcon {

    private int x;
    private int y;

    private int rotateAngle;
    //旋转方向:顺时针为0，逆时针为1
    private int rotateDirection;
    //起始时间
    private long startTime;
    private AppProcessInfo info;
    private int degree;
    private int speed;
    private int width;
    private int height;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDegree() {
        return degree;
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


    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void changePoint(){
        x += speed*Math.cos(degree);
        y += speed*Math.sin(degree);

        if (y >= App.sScreenHeight - height*2||y<= 0)
            degree = -degree;

        if (x >= App.sScreenWidth - width*1.5||x <= 0)
            degree = (int) (Math.PI-degree);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
