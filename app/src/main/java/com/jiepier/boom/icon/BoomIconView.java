package com.jiepier.boom.icon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.jiepier.boom.R;
import com.jiepier.boom.base.App;
import com.jiepier.boom.util.AngelUtil;
import com.jiepier.boom.util.ImageTools;
import com.jiepier.boom.util.TouchUtil;

/**
 * Created by panruijiesx on 2016/11/24.
 */

public class BoomIconView extends View implements View.OnTouchListener{

    public static final int APP_FLOAT_TIME = 5000;
    public static final int ARROW_ANGLE1 = -15;
    public static final int ARROW_ANGLE2 = 15;
    public static final int ARROW_LENGTH = 30;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;
    private int mLastX;
    private int mLastY;
    private int dx;
    private int dy;
    private int pointDownX;
    private int pointDownY;
    private double speed;
    private double degree;
    private boolean isMove;
    private boolean isStrat;
    private boolean isTouchView;
    private long startTime;
    private OnMoveListener mListener;

    public BoomIconView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        initBitmap();
        initData();
        setOnTouchListener(this);
    }

    private void initData() {
        isMove = true;
        isTouchView = false;
        isStrat = false;
    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setStrokeWidth(5);
        mBitmapPaint.setStyle(Paint.Style.STROKE);
    }

    private void initBitmap() {
        mBitmap = ImageTools.createBitmapBySize(
                ((BitmapDrawable)getResources()
                .getDrawable(R.drawable.ic_airplancemode_active_indigo_a200_36dp))
                .getBitmap(),
                128,128);
        mWidth = mBitmap.getWidth();
        mHeight = mBitmap.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        //画boom图像
        Matrix matrix = new Matrix();
        matrix.postTranslate(App.sScreenWidth/2-mWidth+dx,App.sScreenHeight/2-mHeight+dy);
        long currentTime = System.currentTimeMillis();
        float transX = App.sScreenWidth/2-mWidth/2+dx;
        float transY = App.sScreenHeight/2-mHeight/2+dy;
        if (!isMove) {
            matrix.postRotate((currentTime - startTime) % APP_FLOAT_TIME / (float) APP_FLOAT_TIME * 360, transX, transY);
        }
        canvas.drawBitmap(mBitmap,matrix,mBitmapPaint);

        //画箭头
        if (isMove&&isStrat){
            //先画直线
            int mLastX = App.sScreenWidth/2-mWidth/2+dx;
            int mLastY = App.sScreenHeight/2-mHeight/2+dy;
            double dx1 = (mLastX-pointDownX)*Math.cos(ARROW_ANGLE1) -
                    (mLastY-pointDownY)*Math.sin(ARROW_ANGLE1) ;
            double dy1 = (mLastY-pointDownY)*Math.cos(ARROW_ANGLE1) +
                    (mLastX-pointDownX)*Math.sin(ARROW_ANGLE1);
            double dx2 = (mLastX-pointDownX)*Math.cos(ARROW_ANGLE2) -
                    (mLastY-pointDownY)*Math.sin(ARROW_ANGLE2) ;
            double dy2 = (mLastY-pointDownY)*Math.cos(ARROW_ANGLE2) +
                    (mLastX-pointDownX)*Math.sin(ARROW_ANGLE2);
            double hypoLen = Math.sqrt(dx1*dx1+dy1*dy1);
            double x1 = pointDownX-ARROW_LENGTH/hypoLen*dx1;
            double y1 = pointDownY-ARROW_LENGTH/hypoLen*dy1;
            double x2 = pointDownX-ARROW_LENGTH/hypoLen*dx2;
            double y2 = pointDownY-ARROW_LENGTH/hypoLen*dy2;
            canvas.drawLine(pointDownX,pointDownY, mLastX,mLastY, mBitmapPaint);
            canvas.drawLine(pointDownX,pointDownY,(float)x1,(float)y1,mBitmapPaint);
            canvas.drawLine(pointDownX,pointDownY,(float)x2,(float)y2,mBitmapPaint);
            canvas.drawCircle(App.sScreenWidth/2-mWidth/2+dx,
                    App.sScreenHeight/2-mHeight/2+dy,mWidth/4,mBitmapPaint);
        }

        if (!isMove){
            Moving();
        }
        canvas.restore();
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (TouchUtil.isTouchView(x,y,App.sScreenWidth/2-mWidth+dx,App.sScreenHeight/2-mHeight+dy,mWidth,mHeight)) {
                    isMove = true;
                    isStrat = true;
                    isTouchView = true;
                    pointDownX = x;
                    pointDownY = y;
                    speed = 0;
                }else {
                    isTouchView = false;
                }
                    break;
            case MotionEvent.ACTION_MOVE:
                if (isTouchView) {
                    int dx = mLastX - x;
                    int dy = mLastY - y;

                    if (App.sScreenWidth / 2 -mWidth + this.dx - dx < 0)
                        dx = 0;
                    if (App.sScreenWidth / 2 + this.dx - dx > App.sScreenWidth )
                        dx = 0;
                    if (App.sScreenHeight / 2 -mHeight + this.dy - dy < 0)
                        dy = 0;
                    if (App.sScreenHeight / 2 + this.dy - dy > App.sScreenHeight )
                        dy = 0;
                    changePoint(dx, dy);
                }
                    break;
            case MotionEvent.ACTION_UP:
                if (isTouchView) {
                    isMove = false;
                    speed = Math.sqrt(
                            (pointDownX - mLastX) * (pointDownX - mLastX)
                                    + (pointDownY - mLastY) * (pointDownY - mLastY)
                    ) / 20;
                    startTime = System.currentTimeMillis();
                    degree = AngelUtil.CalulateXYAnagle(pointDownX, pointDownY, x, y) / 180 * Math.PI;
                    //Log.w("haha",degree+"");
                    Moving();
                }
                break;

        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void Moving() {

        double dx = -speed * Math.cos(degree);
        double dy = speed * Math.sin(degree);

        /*if (App.sScreenWidth/2-mWidth+this.dx-dx<0)
            degree = -degree;
        if (App.sScreenWidth/2+dx>App.sScreenWidth-mWidth)
            degree = -degree;
        if (App.sScreenHeight/2-mHeight+dy<0)
            degree = Math.PI-degree;
        if (App.sScreenHeight/2+dy>App.sScreenHeight-mHeight)
            degree = Math.PI-degree;*/

        //Log.w("haha",Math.cos(degree)+"!!"+Math.sin(degree));
        if (App.sScreenWidth/2-mWidth+this.dx-dx<0) {
            dx = 0;
            degree = Math.PI - degree;
        }
        if (App.sScreenWidth/2+this.dx-dx>App.sScreenWidth) {
            dx = 0;
            degree = Math.PI - degree;
        }
        if (App.sScreenHeight/2-mHeight+this.dy-dy<0) {
            dy = 0;
            degree = -degree;
        }
        if (App.sScreenHeight/2+this.dy-dy>App.sScreenHeight){
            dy = 0;
            degree = - degree;
        }
        changePoint(dx,dy);

        /*if (dx<-(App.sScreenWidth/2-mWidth)) {
            dx = -(App.sScreenWidth/2-mWidth);
        }else if (dx > App.sScreenWidth/2){
            dx = App.sScreenWidth/2;
        }

        if (dy<-(App.sScreenHeight/2-mHeight)){
            dy = -(App.sScreenHeight/2-mHeight);
        }else if (dy > App.sScreenHeight/2){
            dy = App.sScreenHeight/2;
        }*/
    }

    private void changePoint(double dx,double dy){
        this.dx -= dx;
        this.dy -= dy;
        if (!isMove)
        mListener.onMove(getRect());
    }

    public BoomRect getRect(){
        BoomRect rect= new BoomRect();
        rect.setX(App.sScreenWidth/2-mWidth+dx)
                .setY(App.sScreenHeight/2-mHeight+dy)
                .setWidth(mWidth)
                .setHeight(mHeight);
        return rect;
    }

    public void setMoveListener(OnMoveListener listener){
        this.mListener = listener;
    }

    public interface OnMoveListener{
        void onMove(BoomRect rect);
    }

}
