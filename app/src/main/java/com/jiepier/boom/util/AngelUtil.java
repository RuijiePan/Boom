package com.jiepier.boom.util;

import android.util.Log;

/**
 * Created by JiePier on 16/11/27.
 */

public class AngelUtil {

    public static double CalulateXYAnagle(double startx, double starty, double endx, double endy)
    {
        double tan = Math.atan(Math.abs((endy - starty) / (endx - startx))) * 180 / Math.PI;
        if (endx > startx && endy > starty)//第一象限
        {
            //Log.w("haha1","1="+tan);
            return 180-tan;
        }
        else if (endx > startx && endy < starty)//第二象限
        {
            //Log.w("haha2","2="+tan);
            return tan+180;
        }
        else if (endx < startx && endy > starty)//第三象限
        {
            //Log.w("haha",tan-180+"");
            //Log.w("haha3","3="+tan);
            return tan;
        }
        else
        {
            //Log.w("haha",180-tan+"");
            //Log.w("haha4","4="+tan);
            return -tan;
        }

    }
}
