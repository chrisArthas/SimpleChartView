package com.chartsdemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.chartsdemo.R;
import com.chartsdemo.util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2016/10/17.
 *
 * mail: chris.wsy@hotmail.com
 */
public class LineChartsView extends View {

    private String TAG = "LineChartsView";

    private Context context;

    //整个view框高
    private int vWidth;

    private int vHeight;

    private float scaledDensity;

    private final float DEFAULT_MARGIN = 50;

    //坐标系最高、最低、最左、最右位置
    private float TOP;

    private float BOTTOM;

    private float LEFT;

    private float RIGHT;

    private Paint axisPaint;

    private Paint numPaint;

    private Paint pointPaint;

    private Paint linePaint;

    private Paint backLinePaint;

    private int axisColor;

    private int numColor;

    private int pointColor;

    private int backLineColor;

    private int lineColor;

    private float numTextSize;

    private int axisStrokeWidth = 3;

    private int pointRadius = 8;

    private float XAxisWidth;

    private float YAxisHeight;

    private float YUnit = 50;

    private float XUnit;

    private List<Integer> dataList;

    private List<String> XAxisNames;

    private ArrayList<Map<String,Float>> points = new ArrayList<>();

    public LineChartsView(Context context) {
        this(context, null, 0);
    }

    public LineChartsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initData(attrs);
    }

    private void initData(AttributeSet attrs)
    {
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.LineChartsView);
        if (ta != null) {
            axisColor = ta.getColor(R.styleable.LineChartsView_axisColor, 0);
            ta.recycle();
        }

        scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

        numColor = context.getResources().getColor(R.color.numColor);
        pointColor = context.getResources().getColor(R.color.pointColor);
        lineColor = context.getResources().getColor(R.color.lineColor);
        backLineColor = context.getResources().getColor(R.color.backLineColor);

        numTextSize = ViewUtil.sp2px(scaledDensity,12);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        axisPaint = new Paint();
        axisPaint.setColor(axisColor);
        axisPaint.setStrokeWidth(axisStrokeWidth);
        axisPaint.setStyle(Paint.Style.STROKE);

        numPaint = new Paint();
        numPaint.setColor(numColor);
        numPaint.setTextSize(numTextSize);

        pointPaint = new Paint();
        pointPaint.setColor(pointColor);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(5);

        backLinePaint = new Paint();
        backLinePaint.setColor(backLineColor);
        backLinePaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // X 轴 间距
        XUnit = XAxisWidth/dataList.size();

        drawBackLines(canvas);

        drawAxis(canvas);

        drawChartLine(canvas);

        drawPoint(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vWidth = w;
        vHeight = h;
        TOP = DEFAULT_MARGIN;

        BOTTOM = h - DEFAULT_MARGIN;

        YAxisHeight = (int)(h - DEFAULT_MARGIN*2);

        RIGHT = w - DEFAULT_MARGIN;

        XAxisWidth = (int)(w - DEFAULT_MARGIN*2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                int i = checkPoint(event);
                if(i != -1)
                {
                    Toast.makeText(context,"click: " + (i+1) +" num is: " + dataList.get(i),Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 判断点击位置是否属于 数据中的点
     * @param event
     * @return true or false
     */
    private int checkPoint(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        for(int i = 0;i <points.size();i++)
        {
            float tmpX = points.get(i).get("X");
            float tmpY = points.get(i).get("Y");
            if(tmpX<(x+20) && tmpX >(x-20))
            {
                if(tmpY<(y+20) && tmpY>(y-20))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    private void drawAxis(Canvas canvas) {
        //Y 轴
        canvas.drawLine(DEFAULT_MARGIN, BOTTOM, DEFAULT_MARGIN, DEFAULT_MARGIN, axisPaint);
        float YNums = YAxisHeight/YUnit;
        Log.i(TAG,"YNums: " + YNums);

        for(int i = 0 ;i < YNums-1;i++)
        {
            String tmp = (int)YUnit + i*(int)YUnit +"";

            canvas.drawText(tmp,DEFAULT_MARGIN-40,BOTTOM-(YUnit*(i+1))+5, numPaint);
        }


        //X 轴
        canvas.drawLine(DEFAULT_MARGIN, BOTTOM, RIGHT, BOTTOM, axisPaint);



        for(int i = 0 ; i< dataList.size();i++)
        {
            canvas.drawText(XAxisNames.get(i),XUnit*(i+1)+30,BOTTOM+30, numPaint);
        }


    }

    private void drawBackLines(Canvas canvas)
    {
        int nums = (int)(YAxisHeight/YUnit)*4;

        float[] xPts = new float[nums];

//                {
//        DEFAULT_MARGIN-15, BOTTOM-YUnit, RIGHT, BOTTOM-YUnit,
//                DEFAULT_MARGIN-15, BOTTOM-YUnit*2, RIGHT, BOTTOM-YUnit*2,
//                DEFAULT_MARGIN-15, BOTTOM-YUnit*3, RIGHT, BOTTOM-YUnit*3,
//                DEFAULT_MARGIN-15, BOTTOM-YUnit*4, RIGHT, BOTTOM-YUnit*4,
//                DEFAULT_MARGIN-15, BOTTOM-YUnit*5, RIGHT, BOTTOM-YUnit*5,
//                DEFAULT_MARGIN-15, BOTTOM-YUnit*6, RIGHT, BOTTOM-YUnit*6,
//                DEFAULT_MARGIN-15, BOTTOM-YUnit*7, RIGHT, BOTTOM-YUnit*7,
//        };
        for(int i = 1;i < nums+1;i++)
        {
            if(i%4 == 1)
            {
                xPts[i-1] = DEFAULT_MARGIN;
            }else if(i%4 == 2)
            {
                xPts[i-1] = BOTTOM-YUnit*((i/4)+1);
            }else if(i%4 == 3)
            {
                xPts[i-1] = RIGHT;
            }else
            {
                xPts[i-1] = BOTTOM-YUnit*(i/4);
            }

        }

        canvas.drawLines(xPts,backLinePaint);

        float[] yPts = new float[XAxisNames.size()*4];
//                {
//                        XUnit+DEFAULT_MARGIN, BOTTOM,   XUnit+DEFAULT_MARGIN, DEFAULT_MARGIN,
//                        XUnit*2+DEFAULT_MARGIN, BOTTOM, XUnit*2+DEFAULT_MARGIN, DEFAULT_MARGIN,
//                        XUnit*3+DEFAULT_MARGIN, BOTTOM, XUnit*3+DEFAULT_MARGIN, DEFAULT_MARGIN,
//                        XUnit*4+DEFAULT_MARGIN, BOTTOM, XUnit*4+DEFAULT_MARGIN, DEFAULT_MARGIN,
//                        XUnit*5+DEFAULT_MARGIN, BOTTOM, XUnit*5+DEFAULT_MARGIN, DEFAULT_MARGIN,
//                        XUnit*6+DEFAULT_MARGIN, BOTTOM, XUnit*6+DEFAULT_MARGIN, DEFAULT_MARGIN,
//                };
        for(int i = 1;i<(XAxisNames.size()*4+1);i++)
        {
            if(i%4 == 1)
            {
                yPts[i-1] = XUnit*((i/4)+1)+DEFAULT_MARGIN;
            }else if(i%4 == 2)
            {
                yPts[i-1] = BOTTOM;
            }else if(i%4 == 3)
            {
                yPts[i-1] = XUnit*((i/4)+1)+DEFAULT_MARGIN;
            }else
            {
                yPts[i-1] = TOP;
            }
        }
        canvas.drawLines(yPts,backLinePaint);
    }

    private void drawPoint(Canvas canvas)
    {
        points.clear();

        for(int i = 0;i<dataList.size();i++)
        {
            canvas.drawCircle(XUnit*(i+1)+DEFAULT_MARGIN,BOTTOM-dataList.get(i),pointRadius,pointPaint);


            Map<String,Float> map = new HashMap<>();
            map.put("X",XUnit*(i+1)+DEFAULT_MARGIN);
            map.put("Y",BOTTOM-dataList.get(i));

            points.add(map);
        }

    }

    private void drawChartLine(Canvas canvas)
    {
        for(int i = 0;i<dataList.size()-1;i++)
        {
            canvas.drawLine(XUnit*(i+1)+50,BOTTOM-dataList.get(i),XUnit*(i+2)+50,BOTTOM-dataList.get(i+1),linePaint);
        }
    }

    /**
     * 添加数据
     * @param list
     */
    public void setData(List<Integer> list)
    {
        dataList = list;
    }

    /**
     * X轴 刻度
     * @param list
     */
    public void setXAxisNames(List<String> list)
    {
        XAxisNames = list;
    }

    /**
     * 设置Y轴 单位大小
     * @param unit
     */
    public void setYUnit(int unit)
    {
        YUnit = unit;
    }

    /**
     * 设置XY轴 提示字体大小
     * @param textSize
     */
    public void setNumTextSize(int textSize)
    {
        numTextSize = ViewUtil.sp2px(scaledDensity,textSize);
    }


    public void setDataChange()
    {
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 设置XY轴 提示字体颜色
     * @param color
     */
    public void setNumColor(int color)
    {
        numColor = color;
    }


    /**
     * 设置 数据点颜色
     * @param color
     */
    public void setPointColor(int color)
    {
        pointColor = color;
    }

    /**
     * 设置 数据点 圆形半径
     * @param radius
     */
    public void setPointRadius(int radius)
    {
        pointRadius = radius;
    }

    /**
     * 设置 折线颜色
     * @param color
     */
    public void setLineColor(int color)
    {
        lineColor = color;
    }

    /**
     * 设置 背景网格线颜色
     * @param color
     */
    public void setBackLineColor(int color)
    {
        backLineColor = color;
    }




    private int getMaxItem(List<Integer> list)
    {
        int max = 0;
        for(int tmp : list)
        {
            if(tmp> max)
            {
                max = tmp;
            }
        }
        return max;
    }

}
