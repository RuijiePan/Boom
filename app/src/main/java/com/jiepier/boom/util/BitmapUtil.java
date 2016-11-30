package com.jiepier.boom.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by panruijiesx on 2016/11/24.
 */

public class BitmapUtil {

    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number){
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg
                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {

            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);

        }

        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg
                .getHeight(), Bitmap.Config.ARGB_8888);

        return sourceImg;
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable){

        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }else {
            //drawable instanceof VectorDrawable
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

    public static Bitmap adjustOpacity(Bitmap bitmap, int opacity)
    {
        Bitmap mutableBitmap = bitmap.isMutable()
                ? bitmap
                : bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        int colour = (opacity & 0xFF) << 24;
        canvas.drawColor(colour, PorterDuff.Mode.DST_IN);
        return mutableBitmap;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap oldbmp = drawableToBitmap(drawable);

        Matrix matrix = new Matrix();

        float sx = ((float) w / width);
        float sy = ((float) h / height);

        matrix.postScale(sx, sy);

        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newbmp);

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;

        Bitmap bitmap = Bitmap.createBitmap(w, h, config);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);

        drawable.draw(canvas);
        return bitmap;

    }


}
