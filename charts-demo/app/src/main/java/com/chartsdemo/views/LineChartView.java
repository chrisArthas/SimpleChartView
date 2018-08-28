package com.chartsdemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chartsdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 *  @author Chris
 *
 */
public class LineChartView extends View {

    private static final String TAG = "wsy-LineChartView";

    private int mWidth;

    private int mHeight;

    /**
     * Y 轴 单位数量 默认 5
     */
    int YAXIS_NUM = 5;

    /**
     * X轴单位数量 默认5
     */
    int XAXIS_NUM = 5;

    /**
     * X轴第一个以及最后一个坐标点的padding
     */
    int X_CONTENT_PADDING = 50;

    /**
     * x轴画笔
     */
    private Paint axisPaint;

    /**
     * 单位文字 画笔
     */
    private Paint textPaint;

    /**
     *  虚线画笔
     */
    private Paint dottedLinePaint;

    /**
     * 曲线画笔
     */
    private Paint linePaint;

    /**
     * 数据点画笔
     */
    private Paint pointPaint;

    private Context mContext;

    private List<Point> points;

    private List<PointModel> dataList;

    //曲线弯曲率
    private float curveRadius = 0.2f;

    //曲线
    private Path mPath;

    //x轴单位宽度
    private int unitWidth;

    //padding
    int paddingTop = 50;
    int paddingLeft = 50;
    int paddingRight = 50;
    int paddingBottom = 50;

    //坐标轴原点
    int x0,y0;

    public LineChartView(Context context) {
        this(context,null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }


    private void initPaint()
    {
        linePaint = new Paint();
        linePaint.setColor(mContext.getResources().getColor(R.color.new_line_red));
        linePaint.setStrokeWidth(5);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(mContext.getResources().getColor(R.color.new_line_red));
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeWidth(3);
        pointPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);


        axisPaint = new Paint();
        axisPaint.setColor(mContext.getResources().getColor(R.color.new_line_red));
        axisPaint.setAntiAlias(true);
        axisPaint.setStrokeWidth(3);

        dottedLinePaint = new Paint();
        dottedLinePaint.setColor(Color.GRAY);
        dottedLinePaint.setStyle(Paint.Style.STROKE);
        dottedLinePaint.setStrokeWidth(3);
        PathEffect pathEffect = new DashPathEffect(new float[]{5,5},0);
        dottedLinePaint.setPathEffect(pathEffect);
    }

    public void setData()
    {
        dataList = new ArrayList<>();
        dataList.add(new PointModel("198","3:00"));
        dataList.add(new PointModel("220","3:30"));
        dataList.add(new PointModel("350","4:00"));
        dataList.add(new PointModel("480","4:30"));
        dataList.add(new PointModel("503","5:00"));


        points = new ArrayList<>();

        //X轴原点
        x0 = paddingLeft + 50;

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(1200, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(700,View.MeasureSpec.EXACTLY);
        measure(widthMeasureSpec,heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        y0 = mHeight- paddingBottom - 50;
        //图表内容 总高度
        int contentHeight = y0 - 100;
        Log.i(TAG,"mWidth: "+mWidth);
        //图表 X轴宽度
        int contentWidth  = mWidth - paddingRight - x0;

        //x 轴单位宽度
        unitWidth =  (contentWidth-X_CONTENT_PADDING*2)/(XAXIS_NUM-1);

        for(int i = 0;i<dataList.size();i++)
        {
            Point point = new Point();
            String amount = dataList.get(i).getAmount();
            point.x = x0+X_CONTENT_PADDING+unitWidth*i;
            //todo
            point.y = y0 - Integer.parseInt(amount);

            points.add(point);
        }


        mPath = new Path();
        invalidate();
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

        //TODO 无数据时 暂时不处理
        if(points == null || points.size() == 0)
        {
            return;
        }

        drawAxis(canvas);

        drawPoint(canvas);
        measureControlPoint();
        canvas.drawPath(mPath,linePaint);
    }



    /**
     * 绘制横纵坐标
     * @param canvas
     */
    private void drawAxis(Canvas canvas)
    {


        int maxY = (int)Math.ceil(getMaxY());

        //图表内容 总高度
        int contentHeight = y0 - 100;

        //Y轴 单位数值
        int unitY = maxY/YAXIS_NUM;


        //X轴
        canvas.drawLine(x0,y0,mWidth-paddingRight,y0,axisPaint);

        //绘制Y 轴单位
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("人数",paddingLeft,paddingTop+20,textPaint);



        canvas.drawText("0",paddingLeft,y0+7,textPaint);



        //Y 轴单元高度
        float unitHeight =(float)  contentHeight/YAXIS_NUM;

        for(int i = 0;i <YAXIS_NUM;i++)
        {
            Path path = new Path();
            path.moveTo(x0,y0 -unitHeight*(i+1));
            path.lineTo(mWidth-paddingRight,y0-unitHeight*(i+1));
            //虚线
            canvas.drawPath(path,dottedLinePaint);
            //Y轴 单位
            canvas.drawText(dataList.get(i).getAmount()+"",paddingLeft,y0+7-unitHeight*(i+1),textPaint);

        }

        for(int i = 0;i<XAXIS_NUM;i++)
        {
            linePaint.setStyle(Paint.Style.FILL);
            //x轴上的点
            canvas.drawCircle(x0+X_CONTENT_PADDING+unitWidth*i,y0,5,linePaint);
            //x轴下的数值
            canvas.drawText(dataList.get(i).getTime(),x0+unitWidth*i - 10 + X_CONTENT_PADDING,y0 + 30,textPaint);
        }
        linePaint.setStyle(Paint.Style.STROKE);



    }

    /**
     *     画数据点
     */
    private void drawPoint(Canvas canvas)
    {
        for(int i = 0;i<points.size();i++)
        {
            int x = points.get(i).x;
            int y = points.get(i).y;
            canvas.drawCircle(x,y,10,pointPaint);
        }
    }

    /**
     * 计算控制点
     *
     * 二阶贝塞尔曲线公式 https://blog.csdn.net/it_zouxiang/article/details/52667896
     */

    private void measureControlPoint( )
    {
        for(int i = 0; i < points.size() -1;i++)
        {
            int x = points.get(i).x;
            int y = points.get(i).y;
            if(i == 0)
            {
                mPath.moveTo(x,y);

                float Ax0 = x+(points.get(i+1).x - x)* curveRadius;
                float Ay0 = y+(points.get(i+1).y - y)* curveRadius;

                Log.i(TAG,"Ax0: "+Ax0 + " Ay0: "+Ay0);


                float Bx0 = points.get(i+1).x - (points.get(i+2).x - x)* curveRadius;
                float By0 = points.get(i+1).y - (points.get(i+2).y - y)* curveRadius;

                Log.i(TAG,"Bx0: "+Bx0 + " By0: "+By0);

                mPath.cubicTo(Ax0,Ay0,Bx0,By0,points.get(i+1).x,points.get(i+1).y);

            }else if(i == points.size() - 2)
            {

                float Axi = points.get(i).x+(points.get(i+1).x-points.get(i-1).x)* curveRadius;
                float Ayi = points.get(i).y+(points.get(i+1).y-points.get(i-1).y)* curveRadius;

                Log.i(TAG,"Ax"+i+": "+Axi + " Ay"+i+": "+Ayi);

                float Bxi = points.get(i+1).x - (points.get(i+1).x - points.get(i).x)* curveRadius;
                float Byi = points.get(i+1).y - (points.get(i+1).y - points.get(i).y)* curveRadius;

                Log.i(TAG,"Bx"+i+": "+Bxi + " By"+i+": "+Byi);

                mPath.cubicTo(Axi,Ayi,Bxi,Byi,points.get(i+1).x,points.get(i+1).y);
            }else
            {
                float Axi = points.get(i).x+(points.get(i+1).x-points.get(i-1).x)* curveRadius;
                float Ayi = points.get(i).y+(points.get(i+1).y-points.get(i-1).y)* curveRadius;

                Log.i(TAG,"Ax"+i+": "+Axi + " Ay"+i+": "+Ayi);


                float Bxi = points.get(i+1).x - (points.get(i+2).x - points.get(i).x)* curveRadius;
                float Byi = points.get(i+1).y - (points.get(i+2).y - points.get(i).y)* curveRadius;

                Log.i(TAG,"Bx"+i+"i: "+Bxi + " By"+i+": "+Byi);

                mPath.cubicTo(Axi,Ayi,Bxi,Byi,points.get(i+1).x,points.get(i+1).y);

            }

        }
    }

    /**
     * 获取 数据中Y值的最高值
     * @return
     */
    private int getMaxY()
    {
        int maxY = 0;
        for(Point point:points)
        {
            if(point.y - maxY > 0)
            {
                maxY = point.y;
            }
        }
        return maxY;
    }


    public class PointModel
    {
        private String amount;
        private String time;
        public PointModel(String amount,String time)
        {
            this.amount = amount;
            this.time = time;
        }

        public String getAmount() {
            return amount;
        }


        public String getTime() {
            return time;
        }

    }
}
