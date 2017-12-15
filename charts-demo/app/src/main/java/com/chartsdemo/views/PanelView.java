package com.chartsdemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.chartsdemo.R;

/**
 * 自定义仪表盘View
 *
 * Created by Chris on 2016/10/21.
 *
 * chris.wsy@hotmail.com
 */
public class PanelView extends View{

    private String TAG = "PanelView";

    private Paint circlePaint;

    private Paint textPaint;

    private Paint scalePaint;

    private Paint hourNeedlePaint;

    private Paint minuteNeedlePaint;

    private Paint secondNeedlePaint;

    private int mRadius = 100;

    private int DEFAULT_HOUR_NEEDLE_LENGTH = 25;

    private int DEFAULT_SECOND_NEEDLE_LENGTH = 15;

    public PanelView(Context context) {
        this(context, null, 0);
    }

    public PanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wrapLen = 400;
        int width = measureDimension(wrapLen, widthMeasureSpec);
        int height = measureDimension(wrapLen, heightMeasureSpec);
        int len=Math.min(width,height);
        //保证是一个正方形
        setMeasuredDimension(len,len);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //半径
        mRadius = (getWidth()-getPaddingLeft()-getPaddingRight())/2 - 5;

        drawBack(canvas);

        drawScale(canvas);
    }

    private void initPaint()
    {
        circlePaint = new Paint();
        circlePaint.setStrokeWidth(2);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(getResources().getColor(R.color.panel_light_green));
        circlePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.panel_light_green));

        scalePaint = new Paint();
        scalePaint.setStrokeWidth(2);
        scalePaint.setStyle(Paint.Style.STROKE);
        scalePaint.setColor(getResources().getColor(R.color.numColor));
        scalePaint.setAntiAlias(true);

        hourNeedlePaint = new Paint();
        hourNeedlePaint.setStrokeWidth(10);
        hourNeedlePaint.setStyle(Paint.Style.STROKE);
        hourNeedlePaint.setColor(getResources().getColor(R.color.numColor));
        hourNeedlePaint.setAntiAlias(true);

        minuteNeedlePaint = new Paint();
        minuteNeedlePaint.setStrokeWidth(7);
        minuteNeedlePaint.setStyle(Paint.Style.STROKE);
        minuteNeedlePaint.setColor(getResources().getColor(R.color.numColor));
        minuteNeedlePaint.setAntiAlias(true);

        secondNeedlePaint = new Paint();
        secondNeedlePaint.setStrokeWidth(5);
        secondNeedlePaint.setStyle(Paint.Style.STROKE);
        secondNeedlePaint.setColor(getResources().getColor(R.color.numColor));
        secondNeedlePaint.setAntiAlias(true);


    }

    private void drawBack(Canvas canvas)
    {
        canvas.translate(getWidth()/2,getHeight()/2);
        canvas.save();

        canvas.drawCircle(0,0,mRadius,circlePaint);
    }


    private void drawScale(Canvas canvas)
    {
        for(int i =0;i<60;i++)
        {
            if(i%5 == 0)
            {
                scalePaint.setStrokeWidth(5);
                canvas.drawLine(0,-mRadius+DEFAULT_HOUR_NEEDLE_LENGTH,0,-mRadius, scalePaint);
            }else
            {
                scalePaint.setStrokeWidth(2);
                canvas.drawLine(0,-mRadius+DEFAULT_SECOND_NEEDLE_LENGTH,0,-mRadius, scalePaint);
            }

            canvas.rotate(6);
        }

    }



    public int measureDimension(int defaultSize, int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            //UNSPECIFIED
            result = defaultSize;
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
