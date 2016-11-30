package com.jiepier.boom.util;

import android.util.Log;

import com.jiepier.boom.icon.AppIcon;
import com.jiepier.boom.icon.BoomRect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panruijiesx on 2016/11/23.
 */

public class RectCollisionUtil {

    public static boolean isCollision(AppIcon icon1,AppIcon icon2){
        long currentTime = System.currentTimeMillis();
        if (icon1.getStartTime() < currentTime&&icon2.getStartTime()<currentTime) {
            if (icon1.getX() > icon2.getX() && icon1.getX() > icon2.getX() + icon2.getWidth()) {
                return false;
            } else if (icon2.getX() > icon1.getX() && icon2.getX() > icon1.getX() + icon1.getWidth()) {
                return false;
            } else if (icon1.getY() > icon2.getY() && icon1.getY() > icon2.getY() + icon2.getHeight()) {
                return false;
            } else if (icon2.getY() > icon1.getY() && icon2.getY() > icon1.getY() + icon1.getHeight()) {
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    public static boolean isCollision(BoomRect rect, AppIcon icon){

        if (rect.getX() > icon.getX() && rect.getX() > icon.getX() + icon.getWidth()) {
            return false;
        } else if (icon.getX() > rect.getX() && icon.getX() > rect.getX() + rect.getWidth()) {
            return false;
        } else if (rect.getY() > icon.getY() && rect.getY() > icon.getY() + icon.getHeight()) {
            return false;
        } else if (icon.getY() > rect.getY() && icon.getY() > rect.getY() + rect.getHeight()) {
            return false;
        }
        return true;

    }

    public static List<AppIcon> solverCollison(List<AppIcon> mList){

        List<Integer> list = new ArrayList<>();

        do {
            list.clear();
            for (int i = 0;i< mList.size();i++)
                for (int j =i+1 ;j<mList.size();j++){
                    if (i!=j && RectCollisionUtil.isCollision(mList.get(i),mList.get(j))){
                        if (!list.contains(i))
                            list.add(i);
                        if (!list.contains(j))
                            list.add(j);
                    }
                }

            for (int i=0;i<list.size();i++){
                mList.get(i).setDegree((int) (Math.PI-mList.get(i).getDegree()));
                mList.get(i).changePoint();
            }
        }while (list.size()!=0);

        return mList;
    }

    public static float[] getDxDy(float x1,float y1,int width1,
                                  float x2,float y2,int width2){
        float[] d = new float[2];
        if(x1<x2){
            d[0] = width1/2+width2/2-(x2 - x1);
        }else {
            d[0] = x1 - x2 - (width1/2+width2/2);
        }

        if (y1<y2){
            d[1] = width1/2+width2/2-(y2 - y1);
        }else{
            d[1] = y1 - y2- (width1/2+width2/2);
        }
        return d;
    }
}
