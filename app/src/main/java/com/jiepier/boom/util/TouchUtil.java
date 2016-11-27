package com.jiepier.boom.util;

/**
 * Created by JiePier on 16/11/27.
 */

public class TouchUtil {

    public static boolean isTouchView(int ponitX,int ponitY,int leftX,int leftY,int width,int height){
        if (ponitX<leftX)
            return false;
        if (ponitX>leftX+width)
            return false;
        if (ponitY<leftY)
            return false;
        if (ponitY>leftY+height)
            return false;
        return true;
    }
}
