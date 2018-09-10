package com.chartsdemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class XfermodeView extends View{

    private static final String TAG = "XfermodeView";

    private Paint backPaint;

    private Paint contentPaint;

    int mWidth;

    int mHeight;


    public XfermodeView(Context context) {
        this(context,null);
    }

    public XfermodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint()
    {
        backPaint = new Paint();
        backPaint.setColor(Color.BLUE);

        contentPaint = new Paint();
        contentPaint.setColor(Color.WHITE);

//        Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
//        contentPaint.setXfermode(xfermode);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawRect(0,0,mWidth,mHeight, backPaint);

        canvas.save();

        canvas.clipRect(100,100,800,800);

        canvas.drawRect(0,0,mWidth,mHeight, contentPaint);

        canvas.restore();
//        contentPaint.setXfermode(null);

    }
}
