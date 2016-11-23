package com.jiepier.boom.icon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jiepier.boom.R;
import com.jiepier.boom.base.App;
import com.jiepier.boom.bean.AppProcessInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by panruijiesx on 2016/11/23.
 */

public class AppIconView extends View {

    public static final int APP_FLOAT_TIME = 3000;
    // 中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 300;
    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 100;
    //logo移动速率
    private static final float MOVE_SPEED = 0.2f;
    // 中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    // 振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;

    private List<AppIcon> mList = new ArrayList<>();
    private List<AppProcessInfo> infoList = new ArrayList<>();
    private Bitmap[] mAppBitmap;
    private Paint mBitmapPaint;
    private int[] mAppWidth;
    private int[] mAppHeight;

    public AppIconView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
    }

    // 通过叶子信息获取当前叶子的Y值
    private int getLocationY(AppIcon appIcon) {
        // y = A(wx+Q)+h
        float w = (float) ((float) 2 * Math.PI / App.sScreenWidth);
        float a = mMiddleAmplitude;
        switch (appIcon.getType()) {
            case LITTLE:
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                // 小振幅 ＝ 中等振幅 + 振幅差
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }
        //Log.i(TAG, "---a = " + a + "---w = " + w + "--leaf.x = " + leaf.x);
        return (int) (a * Math.sin(w * appIcon.getX())) + App.sScreenHeight /2;
    }

    private void getAppIconLocation(AppIcon appIcon,long currentTime){
       /* long intervalTime = currentTime - appIcon.getStartTime();
        if (intervalTime < 0){
            return;
        }else if (intervalTime > APP_FLOAT_TIME){
            appIcon.setStartTime(System.currentTimeMillis() + new Random().nextInt((int)APP_FLOAT_TIME));
        }

        float fraction = (float) intervalTime / APP_FLOAT_TIME;
        appIcon.setX(App.sScreenWidth - App.sScreenWidth*fraction);*/
        if (appIcon.getX()<100){

        }
        appIcon.setY(getLocationY(appIcon));
    }

    private void drawAppIcon(Canvas canvas){
        long currentTime = System.currentTimeMillis();
        for(int i = 0 ; i < mList.size();i++){
            AppIcon appIcon = mList.get(i);
                if (currentTime > appIcon.getStartTime()&&appIcon.getStartTime()!=0){
                    getAppIconLocation(appIcon,currentTime);
                    canvas.save();

                    Matrix matrix = new Matrix();
                    float transX = appIcon.getX();
                    float transY = appIcon.getY();
                    matrix.postTranslate(transX,transY);

                    float rotateFraction = ((currentTime-appIcon.getStartTime())%APP_FLOAT_TIME)/(float)APP_FLOAT_TIME;
                    int angle = (int)(rotateFraction * 360);
                    int rotate = appIcon.getRotateDirection() == 0? angle + appIcon.getRotateAngle(): -angle+appIcon.getRotateAngle();
                    matrix.postRotate(rotate,transX + mAppWidth[i]/2 ,transY +mAppHeight[i]/2);
                    canvas.drawBitmap(mAppBitmap[i],matrix,mBitmapPaint);
                    canvas.restore();
                }
        }
        invalidate();
    }

    public void setAppInfoList(List<AppProcessInfo> infoList){
        this.infoList = infoList;
        mList = new IconFactory(infoList).generateAppIcons();
        initBitmap();
    }

    private void initBitmap() {

        mAppBitmap = new Bitmap[infoList.size()];
        mAppWidth = new int[infoList.size()];
        mAppHeight = new int[infoList.size()];

        for (int i = 0;i<infoList.size();i++){
            //mAppBitmap[i] = ((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
            mAppBitmap[i] = ((BitmapDrawable)infoList.get(i).getIcon()).getBitmap();
            mAppWidth[i] = mAppBitmap[i].getWidth();
            mAppHeight[i] = mAppBitmap[i].getHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAppBitmap!=null){
            drawAppIcon(canvas);
        }
    }

    private class IconFactory {

        private List<AppProcessInfo> mList = new ArrayList<>();
        private Random random = new Random();

        public IconFactory(List<AppProcessInfo> list){
            this.mList = list;
        }

        public AppIcon generateAppIcon(AppProcessInfo info){
            AppIcon appIcon = new AppIcon();
            int randomType = random.nextInt(3);
            StartType type = StartType.MIDDLE;

            switch (randomType){
                case 0:
                    break;
                case 1:
                    type = StartType.LITTLE;
                    break;
                case 2:
                    type = StartType.BIG;
                    break;
            }
            appIcon.setType(type);
            appIcon.setRotateAngle(random.nextInt(360));
            appIcon.setRotateDirection(random.nextInt(2));
            appIcon.setMoveDirection(random.nextInt(2));
            appIcon.setStartTime(System.currentTimeMillis()+random.nextInt((int)(APP_FLOAT_TIME*1.5)));
            appIcon.setInfo(info);
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
}
