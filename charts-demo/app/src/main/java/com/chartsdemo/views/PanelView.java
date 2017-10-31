package com.chartsdemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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

    private int mRadius = 100;

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

        int wrap_Len = 400;
        int width = measureDimension(wrap_Len, widthMeasureSpec);
        int height = measureDimension(wrap_Len, heightMeasureSpec);
        int len=Math.min(width,height);
        //保证是一个正方形
        setMeasuredDimension(len,len);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRadius = (getWidth()-getPaddingLeft()-getPaddingRight())/2;//半径

        drawBack(canvas);

//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        canvas.translate(canvas.getWidth()/2, 200); //将位置移动画纸的坐标点:150,150
//        canvas.drawCircle(0, 0, 100, paint); //画圆圈
//
//        //使用path绘制路径文字
//        canvas.save();
//        canvas.translate(-75, -75);
//        Path path = new Path();
//        path.addArc(new RectF(0,0,150,150), -180, 180);
//        Paint citePaint = new Paint(paint);
//        citePaint.setTextSize(14);
//        citePaint.setStrokeWidth(1);
//        canvas.drawTextOnPath("1111111", path, 28, 0, citePaint);
//        canvas.restore();
//
//        Paint tmpPaint = new Paint(paint); //小刻度画笔对象
//        tmpPaint.setStrokeWidth(1);
//
//        float  y=100;
//        int count = 60; //总刻度数
//
//        for(int i=0 ; i <count ; i++){
//            if(i%5 == 0){
//                canvas.drawLine(0f, y, 0, y+12f, paint);
//                canvas.drawText(String.valueOf(i/5+1), -4f, y+25f, tmpPaint);
//
//            }else{
//                canvas.drawLine(0f, y, 0f, y +5f, tmpPaint);
//            }
//            canvas.rotate(360/count,0f,0f); //旋转画纸
//        }
//
//        //绘制指针
//        tmpPaint.setColor(Color.GRAY);
//        tmpPaint.setStrokeWidth(4);
//        canvas.drawCircle(0, 0, 7, tmpPaint);
//        tmpPaint.setStyle(Paint.Style.FILL);
//        tmpPaint.setColor(Color.YELLOW);
//        canvas.drawCircle(0, 0, 5, tmpPaint);
//        canvas.drawLine(0, 10, 0, -65, paint);
    }

    private void initPaint()
    {
        circlePaint = new Paint();
        circlePaint.setStrokeWidth(2);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(getResources().getColor(R.color.panel_light_green));

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.panel_light_green));

    }

    private void drawBack(Canvas canvas)
    {
        canvas.translate(getWidth()/2,getHeight()/2);
        canvas.save();

        RectF mRect=new RectF(-mRadius-10,-mRadius-10,mRadius+10,mRadius+10);
//        canvas.drawRect(mRect,circlePaint);
        canvas.drawArc(mRect,-240,300,false,circlePaint);
    }

    public int measureDimension(int defaultSize, int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = defaultSize;   //UNSPECIFIED
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
