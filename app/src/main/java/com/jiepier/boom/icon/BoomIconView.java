package com.jiepier.boom.icon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
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

/**
 * Created by panruijiesx on 2016/11/24.
 */

public class BoomIconView extends View implements View.OnTouchListener{

    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;
    private int mLastX;
    private int mLastY;
    private int dx;
    private int dy;
    private OnMoveListener mListener;

    public BoomIconView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        initBitmap();
        setOnTouchListener(this);
    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
    }

    private void initBitmap() {
        mBitmap = ((BitmapDrawable)getResources()
                .getDrawable(R.drawable.ic_airplanemode_active_indigo_a200_36dp))
                .getBitmap();
        mWidth = mBitmap.getWidth();
        mHeight = mBitmap.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        Matrix matrix = new Matrix();
        matrix.postTranslate(App.sScreenWidth/2-mWidth+dx,App.sScreenHeight/2-mHeight+dy);
        canvas.drawBitmap(mBitmap,matrix,mBitmapPaint);
        canvas.restore();

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
                int dy = mLastY - y;

                if (App.sScreenWidth/2-mWidth+this.dx-dx<0)
                    dx = 0;
                if (App.sScreenWidth/2+this.dx-dx>App.sScreenWidth-mWidth)
                    dx = 0;
                if (App.sScreenHeight/2-mHeight+this.dy-dy<0)
                    dy = 0;
                if (App.sScreenHeight/2+this.dy-dy>App.sScreenHeight-mHeight*1.5)
                    dy = 0;
                changePoint(dx,dy);
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void changePoint(int dx,int dy){
        this.dx -= dx;
        this.dy -= dy;
        invalidate();
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
