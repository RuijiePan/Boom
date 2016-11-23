package com.jiepier.boom.util;

import android.util.Log;

import com.jiepier.boom.icon.AppIcon;

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
}
