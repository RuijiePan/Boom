package com.jiepier.boom.icon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.VectorDrawable;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jiepier.boom.R;
import com.jiepier.boom.base.App;
import com.jiepier.boom.bean.AppProcessInfo;
import com.jiepier.boom.util.BitmapUtil;
import com.jiepier.boom.util.ImageTools;
import com.jiepier.boom.util.RectCollisionUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by panruijiesx on 2016/11/23.
 */

public class AppIconView extends View {

    public static final int APP_FLOAT_TIME = 5000;
    public static final int FADE_OUT_TIME = 1000;
    public static final int DISTANCE = 5;
    private List<AppIcon> mList = new ArrayList<>();
    private List<AppProcessInfo> infoList = new ArrayList<>();
    private Bitmap[] mAppBitmap;
    private Paint mBitmapPaint;
    private int[] mAppWidth;
    private int[] mAppHeight;
    private KillProcessListener mListener;

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

    private void getAppIconLocation(AppIcon appIcon){
        appIcon.changePoint();
    }

    private void drawAppIcon(Canvas canvas){
        long currentTime = System.currentTimeMillis();
        for(int i = 0 ; i < mList.size();i++){
            AppIcon appIcon = mList.get(i);
                if (currentTime > appIcon.getStartTime()&&appIcon.getStartTime()!=0){
                    getAppIconLocation(appIcon);
                    canvas.save();

                    Matrix matrix = new Matrix();
                    float transX = appIcon.getX();
                    float transY = appIcon.getY();
                    matrix.postTranslate(transX,transY);

                    float rotateFraction = ((currentTime-appIcon.getStartTime())%APP_FLOAT_TIME)/(float)APP_FLOAT_TIME;
                    int angle = (int)(rotateFraction * 360);
                    int rotate = appIcon.getRotateDirection() == 0? angle + appIcon.getRotateAngle(): -angle+appIcon.getRotateAngle();
                    matrix.postRotate(rotate,transX + mAppWidth[i]/2 ,transY +mAppHeight[i]/2);

                    if (!appIcon.isKilled())
                        canvas.drawBitmap(mAppBitmap[i],matrix,mBitmapPaint);
                    else {
                        long time =  currentTime - appIcon.getKilledTime();
                        if (time<FADE_OUT_TIME) {
                            int percent = (int) (time * 100 / FADE_OUT_TIME) > 100 ? 0 : 100-(int) (time * 100 / FADE_OUT_TIME);
                            canvas.drawBitmap(BitmapUtil.adjustOpacity(mAppBitmap[i], (int) (percent*255/100.0)), matrix, mBitmapPaint);
                        }
                    }
                    canvas.restore();
                }
        }
        checkCollision();
    }

    private void checkCollision() {

        for (int i = 0;i< mList.size();i++)
            for (int j =i ;j<mList.size();j++){
                if (i!=j && !mList.get(i).isKilled() && !mList.get(j).isKilled()
                        &&RectCollisionUtil.isCollision(mList.get(i),mList.get(j))){
                    AppIcon app1 = mList.get(i);
                    AppIcon app2 = mList.get(j);
                    int degree = app1.getDegree();
                    app1.setDegree(app2.getDegree());
                    app2.setDegree(degree);

                    while (RectCollisionUtil.isCollision(mList.get(i),mList.get(j))){
                        /*app1.changePoint();
                        app2.changePoint();*/
                        if (app1.getX()<app2.getX()){
                            app1.setX((int) (app1.getX()-1));
                            app2.setX((int) (app2.getX()+1));
                            if (app1.getY()<app2.getY()){
                                app1.setY((int) (app1.getY()-1));
                                app2.setY((int) (app2.getY()+1));
                            }else {
                                app1.setY((int) (app1.getY()+1));
                                app2.setY((int) (app2.getY()-1));
                            }
                        }else {
                            app1.setX((int) (app1.getX()+1));
                            app2.setX((int) (app2.getX()-1));
                            if (app1.getY()<app2.getY()){
                                app1.setY((int) (app1.getY()-1));
                                app2.setY((int) (app2.getY()+1));
                            }else {
                                app1.setY((int) (app1.getY()+1));
                                app2.setY((int) (app2.getY()-1));
                            }
                        }
                    }
                    /*app1.changePoint();
                    app2.changePoint();*/
                    /*float[] d = RectCollisionUtil.getDxDy(app1.getX(),app1.getY(),app1.getWidth(),app2.getX(),app2.getY(),app2.getWidth());
                    float dx = app2.getX() - app1.getX();
                    float dy = app2.getY() - app1.getY();

                    *//*if (dx>0)
                        dx = app2.getWidth()-dx;
                    else
                        dx = -(app2.getWidth()+dx);

                    if(dy>0)
                        dy = app2.getHeight()-dy;
                    else
                        dy = -(app2.getHeight()+dy);*//*
                    app2.setX((int) (app1.getX() + dx));
                    app2.setY((int) (app2.getY() + dy));*/
                }
            }

        /*for (int i=0;i<list.size();i++){
            mList.get(i).setDegree((int)(Math.random()*Math.PI*2));
            mList.get(i).changePoint();
        }*/
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
            mAppBitmap[i] = BitmapUtil.zoomImage(BitmapUtil.getBitmapFromDrawable(infoList.get(i).getIcon()),128,128);
            //mAppBitmap[i] = ((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
            /*if (infoList.get(i).getIcon() instanceof BitmapDrawable) {
                mAppBitmap[i] = ((BitmapDrawable) infoList.get(i).getIcon()).getBitmap();
            }else {
                //drawable instanceof VectorDrawable
                Bitmap bitmap = Bitmap.createBitmap(infoList.get(i).getIcon().getIntrinsicWidth(), infoList.get(i).getIcon().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                infoList.get(i).getIcon().setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                infoList.get(i).getIcon().draw(canvas);
                mAppBitmap[i] = bitmap;
            }*/
            mAppWidth[i] = mAppBitmap[i].getWidth();
            mAppHeight[i] = mAppBitmap[i].getHeight();
            mList.get(i).setWidth(mAppWidth[i]);
            mList.get(i).setHeight(mAppHeight[i]);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAppBitmap!=null){
            drawAppIcon(canvas);
        }
    }

    //工厂类，创建一大波的APP
    private class IconFactory {

        private List<AppProcessInfo> mList = new ArrayList<>();
        private Random random = new Random();

        public IconFactory(List<AppProcessInfo> list){
            this.mList = list;
        }

        public AppIcon generateAppIcon(AppProcessInfo info,int i){
            AppIcon appIcon = new AppIcon();

            appIcon.setRotateAngle(random.nextInt(360));
            appIcon.setRotateDirection(random.nextInt(2));
            appIcon.setDegree((int)(Math.random()*Math.PI*2));
            appIcon.setSpeed(10);
            appIcon.setX(App.sScreenWidth/2);
            appIcon.setY(App.sScreenHeight/2);
            appIcon.setStartTime(System.currentTimeMillis()+i*500);
            appIcon.setInfo(info);
            appIcon.setKilled(false);
            return appIcon;
        }

        public List<AppIcon> generateAppIcons(){
            return generateAppIcons(mList.size());
        }

        private List<AppIcon> generateAppIcons(int appSize){
            List<AppIcon> apps = new LinkedList<>();
            for (int i = 0 ;i < appSize;i++){
                apps.add(generateAppIcon(mList.get(i),i));
            }
            return apps;
        }

    }

    public void checkCollisionWithApp(BoomRect rect){

        for (int i=0;i<mList.size();i++){
            if (!mList.get(i).isKilled()&&RectCollisionUtil.isCollision(rect,mList.get(i))){
                mListener.killProcess(mList.get(i).getInfo().getProcessName(),i);
                mList.get(i).setKilled(true);
                mList.get(i).setKilledTime(System.currentTimeMillis());
            }
        }
    }

    public void setKillProcessListener(KillProcessListener listener){
        this.mListener = listener;
    }

    public interface KillProcessListener{
        void killProcess(String packageName,int position);
    }

    public List<AppIcon> getmList() {
        return mList;
    }

}
