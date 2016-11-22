package com.jiepier.boom.bean;

import java.util.Comparator;

/**
 * Created by panruijiesx on 2016/11/22.
 */

public class ComparatorAppInfo implements Comparator {

    @Override
    public int compare(Object o, Object t1) {
        AppProcessInfo info1 = (AppProcessInfo) o;
        AppProcessInfo info2 = (AppProcessInfo) t1;

        int flag = info1.getAppName().compareTo(info1.getAppName());
        if (flag == 0){
            return (int)(info1.getMemory() - info2.getMemory());
        }else {
            return flag;
        }
    }
}
