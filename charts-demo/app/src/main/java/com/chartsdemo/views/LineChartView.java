package com.chartsdemo.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chartsdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 直播数据折线图
 *  @author Wushengyang
 *
 */
public class LineChartView extends View {

    private static final String TAG = "LineChartView";

    private int mWidth;

    private int mHeight;

    private int MEASURE_WIDTH = 1250;

    private int MEASURE_HEIGHT = 850;

    /**
     * Y 轴 单位数量 默认 6
     */
    int YAXIS_NUM = 6;

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
     * X轴与数据点连线
     */
    private Paint verticalPaint;

    /**
     * 阴影
     */
    private Paint shadowPaint;

    /**
     * 数据点画笔
     */
    private Paint pointPaint;

    /**
     * 气泡内部
     */
    private Paint bubbleTextPaint;

    private Context mContext;

    private List<Point> points;

    private List<LivePoint> dataList;

    //曲线弯曲率
    private float curveRadius = 0.2f;

    //曲线
    private Path mPath;

    //阴影path
    private Path shadowPath;

    //x轴单位宽度
    private int unitWidth;

    //padding
    int paddingTop = 50;
    int paddingLeft = 50;
    int paddingRight = 50;
    int paddingBottom = 50;

    //坐标轴原点
    int x0,y0;

    //Y 轴最大的数值
    private int maxY = 0;

    private int unitY;

    /**
     * 当前点击位置 默认-1
     */
    private int currentClickPosition = -1;

    /**
     * 数据点圆圈半径
     */
    private int dataPointRadius = 7;

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
        linePaint.setColor(getResources().getColor(R.color.line_red));
        linePaint.setStrokeWidth(5);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(getResources().getColor(R.color.line_red));
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeWidth(3);
        pointPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);


        axisPaint = new Paint();
        axisPaint.setColor(getResources().getColor(R.color.line_red));
        axisPaint.setAntiAlias(true);
        axisPaint.setStrokeWidth(3);

        verticalPaint = new Paint();
        verticalPaint.setColor(getResources().getColor(R.color.line_red));
        verticalPaint.setAntiAlias(true);
        verticalPaint.setStrokeWidth(1);
        verticalPaint.setAlpha(80);

        dottedLinePaint = new Paint();
        dottedLinePaint.setColor(Color.GRAY);
        dottedLinePaint.setStyle(Paint.Style.STROKE);
        dottedLinePaint.setStrokeWidth(3);
        dottedLinePaint.setAlpha(80);
        PathEffect pathEffect = new DashPathEffect(new float[]{5,5},0);
        dottedLinePaint.setPathEffect(pathEffect);


        shadowPaint = new Paint();
        shadowPaint.setColor(getResources().getColor(R.color.light_red));
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setAlpha(50);
        shadowPaint.setAntiAlias(true);

        bubbleTextPaint = new Paint();
        bubbleTextPaint.setColor(Color.WHITE);
        bubbleTextPaint.setTextSize(22);
        bubbleTextPaint.setFakeBoldText(true);
        bubbleTextPaint.setAntiAlias(true);
        bubbleTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    public void setData(List<LivePoint> list)
    {
        dataList = list;

        XAXIS_NUM = dataList.size();

        points = new ArrayList<>();

        //X轴原点
        x0 = paddingLeft + 50;

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(MEASURE_WIDTH, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(MEASURE_HEIGHT, View.MeasureSpec.EXACTLY);
        measure(widthMeasureSpec,heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        y0 = mHeight- paddingBottom - 50;

        maxY = getMaxY();

        //图表内容 总高度
        int contentHeight = y0 - 100;

        //Y轴 单位数值
//         unitY = maxY/YAXIS_NUM;
        unitY = getUnitY();

        //比列
        float yUnitRadio = (float)contentHeight/(YAXIS_NUM*unitY);


        //图表 X轴宽度
        int contentWidth  = mWidth - paddingRight - x0;

        //x 轴单位宽度
        unitWidth =  (contentWidth-X_CONTENT_PADDING*2)/(XAXIS_NUM-1);

        for(int i = 0;i<dataList.size();i++)
        {
            Point point = new Point();
            String amount = dataList.get(i).getY();
            point.x = x0+X_CONTENT_PADDING+unitWidth*i;
            point.y = (int)(y0 - Integer.parseInt(amount)*yUnitRadio);
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
        //TODO 无数据时 暂时不处理
        if(points == null || points.size() == 0)
        {
            return;
        }

        drawAxis(canvas);

        measureControlPoint();

        canvas.drawPath(mPath,linePaint);

        drawShadow(canvas);

        drawPoint(canvas);

        drawBubble(canvas);
    }



    /**
     * 绘制横纵坐标
     * @param canvas
     */
    private void drawAxis(Canvas canvas)
    {

        //图表内容 总高度
        int contentHeight = y0 - 100;

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
            canvas.drawText(unitY*(i+1)+"",paddingLeft,y0+7-unitHeight*(i+1),textPaint);

        }

        for(int i = 0;i<XAXIS_NUM;i++)
        {
            linePaint.setStyle(Paint.Style.FILL);
            //x轴上的点
            canvas.drawCircle(x0+X_CONTENT_PADDING+unitWidth*i,y0,5,linePaint);

            //x轴与数据点连接线
            canvas.drawLine(x0+X_CONTENT_PADDING+unitWidth*i,y0,points.get(i).x,points.get(i).y,verticalPaint);

            //x轴下的数值
            canvas.drawText(dataList.get(i).getX(),x0+unitWidth*i - 15 + X_CONTENT_PADDING,y0 + 30,textPaint);
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
            canvas.drawCircle(x,y,dataPointRadius,pointPaint);
        }
        //圆圈内部涂白 >_<
        pointPaint.setColor(Color.WHITE);
        pointPaint.setStyle(Paint.Style.FILL);
        for(int i = 0;i<points.size();i++)
        {
            int x = points.get(i).x;
            int y = points.get(i).y;
            canvas.drawCircle(x,y,dataPointRadius-2,pointPaint);
        }

        pointPaint.setColor(getResources().getColor(R.color.line_red));
        pointPaint.setStyle(Paint.Style.STROKE);

    }

    private void drawBubble(Canvas canvas)
    {
        if(currentClickPosition == -1 || currentClickPosition > points.size()-1)
        {
            return;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.popup);
        int x = points.get(currentClickPosition).x;
        int y = points.get(currentClickPosition).y;
        canvas.drawBitmap(bitmap,x-40,y-120,null);

        canvas.drawText(dataList.get(currentClickPosition).getY(),x-20,y-70,bubbleTextPaint);
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

    private void drawShadow(Canvas canvas)
    {
        if(shadowPath == null)
        {
            shadowPath = new Path();
            shadowPath.addPath(mPath);
            shadowPath.lineTo(x0+X_CONTENT_PADDING+unitWidth*(XAXIS_NUM-1),y0);
            shadowPath.lineTo(x0+X_CONTENT_PADDING,y0);
            shadowPath.close();
        }
        canvas.drawPath(shadowPath,shadowPaint);

//        int save = canvas.save();
//        canvas.clipPath(shadowPath);
//        canvas.restoreToCount(save);
    }

    /**
     * 获取 数据中Y值的最高值
     * @return
     */
    private int getMaxY()
    {
        int maxY = 0;
        for(LivePoint point:dataList)
        {
            if(Integer.parseInt(point.getY()) - maxY > 0)
            {
                maxY = Integer.parseInt(point.getY());
            }
        }
        if(maxY%100 > 0)
        {
            maxY = maxY - maxY%100 + 100;
        }
        return maxY;
    }

    /**
     * 计算适合的Y轴单位数值
     * @return
     */
    private int getUnitY() {
        int num = 100;
        int length = Integer.toString(maxY).length();
        if (maxY == 0) {
            YAXIS_NUM = 6;
            return num;
        }

        if(maxY > 6*Math.pow(10,length-1))
        {
            YAXIS_NUM = 5;
            num = 2*(int)Math.pow(10,length-1);
        }else
        {
            YAXIS_NUM = 6;
            num = 1*(int)Math.pow(10,length-1);
        }
        return num;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                int lastClick = currentClickPosition;
                int i = checkPoint(event);
                if(i != -1)
                {
//                    showPop(event,dataList.get(i).getY(),i);
                    currentClickPosition = i;
                }else{
                    currentClickPosition = -1;
                }
                if(lastClick != currentClickPosition)
                {
                    invalidate();
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
            float tmpX = points.get(i).x;
            float tmpY = points.get(i).y;
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
}