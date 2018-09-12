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
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

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
    private int YAXIS_NUM = 6;

    /**
     * X轴单位数量 默认5
     */
    private int XAXIS_NUM = 5;

    /**
     * 绘制时，只绘制当前可见的位置
     */
    private int startNum = 0;

    private int endNum = XAXIS_NUM;

    /**
     * X轴第一个以及最后一个坐标点的padding
     */
    private int X_CONTENT_PADDING = 50;

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

    //图表 X轴 显示的宽度
    private  int contentWidth;

    //x轴单位宽度
    private int unitWidth;

    //x轴总长
    private int xWidth;

    //Y 轴单元高度
    private float unitHeight;

    //padding
    private int paddingTop = 50;
    private int paddingLeft = 50;
    private int paddingRight = 50;
    private int paddingBottom = 50;

    //坐标轴原点
    private int x0,y0;


    //Y 轴最大的数值
    private int maxY = 0;

    //图表内容 总高度
    private int contentHeight;

    private int unitY;

    /**
     * 当前点击位置 默认-1
     */
    private int currentClickPosition = -1;

    /**
     * 手指落下位置
     */
    private float xDown = -1;

    private float yDown = -1;

    private int touchSlop;

    private int moveDistance = 0;

    private int lastMoveDistance = 0;

    /**
     * 在点击时 是否需要计算移动距离
     */
    private boolean needCaculateDistance = false;

    /**
     * 内容 刷新区域
     */
    private Rect contentRect;



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
        initXY();
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
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        pointPaint.setStrokeWidth(15);
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
        bubbleTextPaint.setTextSize(26);
        bubbleTextPaint.setFakeBoldText(true);
        bubbleTextPaint.setAntiAlias(true);
        bubbleTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    /**
     * 初始化坐标原点
     */
    private void initXY()
    {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(MEASURE_WIDTH, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(MEASURE_HEIGHT, View.MeasureSpec.EXACTLY);
        measure(widthMeasureSpec,heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        //X轴原点
        x0 = paddingLeft + 50;

        y0 = mHeight- paddingBottom - 30;

        //图表内容 总高度
        contentHeight = y0 - 120;

        //X轴固定单位长
        unitWidth = 200;

        contentWidth  = mWidth - paddingRight - x0;

        touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

        contentRect = new Rect(paddingLeft + 50,y0-contentHeight,mWidth-paddingRight,y0+30);

    }

    public void setData(List<LivePoint> list)
    {
        points = new ArrayList<>();
        if(list == null || list.size() == 0)
        {
            return;
        }

        dataList = list;

         XAXIS_NUM = dataList.size();
         if(XAXIS_NUM > 9)
         {
             endNum = 9;
         }

        maxY = getMaxY();

        //Y轴 单位数值
        unitY = getUnitY();

        //比列
        float yUnitRadio = (float)contentHeight/(YAXIS_NUM*unitY);



//        //x 轴单位宽度
//        if(XAXIS_NUM >1)
//        {
//            unitWidth =  (contentWidth-X_CONTENT_PADDING*2)/(XAXIS_NUM-1);
//        }else
//        {
//            unitWidth =  (contentWidth-X_CONTENT_PADDING*2);
//        }

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
        if(points == null || points.size() == 0 || dataList == null ||dataList.size() == 0)
        {
            //无数据时，只展示坐标轴
            YAXIS_NUM = 6;
            unitY = 100;
            endNum = XAXIS_NUM = 0;

            drawYAxis(canvas);
            drawDottedLine(canvas);
            drawXAxis(canvas);

        }else
        {

            drawYAxis(canvas);

            canvas.save();

            canvas.clipRect(contentRect);

            drawDottedLine(canvas);

            Log.i(TAG,"onDraw: lastMoveDistance: " + lastMoveDistance +" moveDistance: "+moveDistance);

            canvas.translate(lastMoveDistance + moveDistance,0);

            drawXAxis(canvas);

            measureControlPoint();

            canvas.drawPath(mPath,linePaint);

            drawShadow(canvas);

            drawPoint(canvas);

            drawBubble(canvas);

            canvas.restore();

        }


    }




    /**
     * 绘制横纵坐标
     * @param canvas
     */
    private void drawYAxis(Canvas canvas)
    {


        //绘制Y 轴单位
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("人数",paddingLeft,paddingTop+20,textPaint);

        canvas.drawText("0",paddingLeft,y0+7,textPaint);

         unitHeight =(float)  contentHeight/YAXIS_NUM;

        for(int i = 0;i <YAXIS_NUM;i++)
        {
            //Y轴 单位
            canvas.drawText(unitY*(i+1)+"",paddingLeft,y0+7-unitHeight*(i+1),textPaint);
        }
    }

    /**
     * 虚线
     */
    private void drawDottedLine(Canvas canvas)
    {
        //X轴
        xWidth = unitWidth*points.size();
        if(xWidth < mWidth - paddingRight - paddingLeft - 50)
        {
            xWidth = mWidth - paddingRight - paddingLeft - 50;
        }

        Path path = new Path();

        for(int i = 0;i <YAXIS_NUM;i++)
        {
            path.moveTo(x0,y0 -unitHeight*(i+1));
            path.lineTo(xWidth + paddingLeft + 50,y0-unitHeight*(i+1));
            //虚线
        }
        canvas.drawPath(path,dottedLinePaint);
    }

    /**
     * X轴
     * @param canvas
     */
    private void drawXAxis(Canvas canvas)
    {
        canvas.drawLine(x0,y0,xWidth + paddingLeft + 50,y0,axisPaint);

        float[] circleXY = new float[(endNum - startNum) * 2];
        float[] lines = new float[(endNum - startNum) * 4];
        for(int i = startNum;i<endNum;i++)
        {
            linePaint.setStyle(Paint.Style.FILL);
            //x轴上的点
//            canvas.drawCircle(x0+X_CONTENT_PADDING+unitWidth*i,y0,5,linePaint);
            circleXY[(i - startNum)*2 ] = x0+X_CONTENT_PADDING+unitWidth*i;
            circleXY[(i - startNum)*2 + 1] = y0;


            //x轴与数据点连接线
//            canvas.drawLine(x0+X_CONTENT_PADDING+unitWidth*i,y0,points.get(i).x,points.get(i).y,verticalPaint);

            lines[(i - startNum)*4 ] = x0+X_CONTENT_PADDING+unitWidth*i;
            lines[(i - startNum)*4 +1 ] = y0;
            lines[(i - startNum)*4 +2 ] = points.get(i).x;
            lines[(i - startNum)*4 +3 ] = points.get(i).y;


            //x轴下的数值
            canvas.drawText(dataList.get(i).getX(),x0+unitWidth*i - 15 + X_CONTENT_PADDING,y0 + 30,textPaint);
        }
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(10);
        canvas.drawPoints(circleXY,linePaint);

        canvas.drawLines(lines,verticalPaint);

        linePaint.setStrokeWidth(5);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    /**
     *     画数据点
     */
    private void drawPoint(Canvas canvas)
    {
        float[] circleXY = new float[(endNum - startNum) * 2];


        for(int i = startNum;i<endNum;i++)
        {
            int x = points.get(i).x;
            int y = points.get(i).y;

            circleXY[(i - startNum)*2 ] = x;
            circleXY[(i - startNum)*2 +1 ] = y;

//            canvas.drawCircle(x,y,dataPointRadius,pointPaint);
        }
        canvas.drawPoints(circleXY,pointPaint);


        //圆圈内部涂白 >_<
        pointPaint.setColor(Color.WHITE);
        pointPaint.setStyle(Paint.Style.FILL);

        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        pointPaint.setStrokeWidth(10);
        canvas.drawPoints(circleXY,pointPaint);

        pointPaint.setStrokeWidth(15);
        pointPaint.setColor(getResources().getColor(R.color.line_red));
        pointPaint.setStyle(Paint.Style.STROKE);

    }

    /**
     * 绘制点击气泡
     * @param canvas
     */
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

        //气泡内文字
        String text = dataList.get(currentClickPosition).getY();
        int length = text.length();
        float textX = x-20;
        switch (length)
        {
            case 1:
                textX = x-11;
                break;
            case 2:
                textX = x-16;
                break;
            case 3:
                textX = x-21;
                break;
            default:
                break;
        }
        canvas.drawText(text,textX,y-70,bubbleTextPaint);

    }

    /**
     * 计算控制点
     *
     * 三阶贝塞尔曲线公式 https://blog.csdn.net/it_zouxiang/article/details/52667896
     */

    private void measureControlPoint( )
    {
        //数据点不够画三阶贝塞尔，直线代替
        mPath.reset();
        if(points.size()  == 2)
        {
            mPath.moveTo( points.get(0).x,points.get(0).y);
            mPath.lineTo(points.get(1).x,points.get(1).y);
        }else
        {
            for(int i = startNum; i < endNum -1;i++)
            {
                int x = points.get(i).x;
                int y = points.get(i).y;
                if(i == startNum)
                {
                    mPath.moveTo(x,y);

                    float Ax0 = x+(points.get(i+1).x - x)* curveRadius;
                    float Ay0 = y+(points.get(i+1).y - y)* curveRadius;

                    if(Ay0>y0)
                    {
                        Ay0 = y0;
                    }

                    float Bx0 = points.get(i+1).x - (points.get(i+2).x - x)* curveRadius;
                    float By0 = points.get(i+1).y - (points.get(i+2).y - y)* curveRadius;

                    if(By0 > y0)
                    {
                        By0 = y0;
                    }
                    mPath.cubicTo(Ax0,Ay0,Bx0,By0,points.get(i+1).x,points.get(i+1).y);

                }else if(i == endNum - 2)
                {

                    float Axi = points.get(i).x+(points.get(i+1).x-points.get(i-1).x)* curveRadius;
                    float Ayi = points.get(i).y+(points.get(i+1).y-points.get(i-1).y)* curveRadius;

                    if(Ayi>y0)
                    {
                        Ayi = y0;
                    }

                    float Bxi = points.get(i+1).x - (points.get(i+1).x - points.get(i).x)* curveRadius;
                    float Byi = points.get(i+1).y - (points.get(i+1).y - points.get(i).y)* curveRadius;

                    if(Byi>y0)
                    {
                        Byi = y0;
                    }

                    mPath.cubicTo(Axi,Ayi,Bxi,Byi,points.get(i+1).x,points.get(i+1).y);
                }else
                {
                    float Axi = points.get(i).x+(points.get(i+1).x-points.get(i-1).x)* curveRadius;
                    float Ayi = points.get(i).y+(points.get(i+1).y-points.get(i-1).y)* curveRadius;

                    if(Ayi>y0)
                    {
                        Ayi = y0;
                    }

                    float Bxi = points.get(i+1).x - (points.get(i+2).x - points.get(i).x)* curveRadius;
                    float Byi = points.get(i+1).y - (points.get(i+2).y - points.get(i).y)* curveRadius;

                    if(Byi>y0)
                    {
                        Byi = y0;
                    }
                    mPath.cubicTo(Axi,Ayi,Bxi,Byi,points.get(i+1).x,points.get(i+1).y);
                }
            }
        }
    }

    private void drawShadow(Canvas canvas)
    {
        if(shadowPath == null)
        {
            shadowPath = new Path();
        }else
        {
            shadowPath.reset();
        }
        shadowPath.addPath(mPath);
        shadowPath.lineTo(x0+X_CONTENT_PADDING+unitWidth*(endNum-1),y0);
        shadowPath.lineTo(x0+X_CONTENT_PADDING,y0);
        shadowPath.close();

        if(!mPath.isEmpty())
        {
            canvas.drawPath(shadowPath,shadowPaint);
        }

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
        if(length == 1 )
        {
            YAXIS_NUM = 5;
            num = 2;
        }else if(maxY == (int)Math.pow(10,length-1))
        {
            YAXIS_NUM = 5;
            num = 2 *(int)Math.pow(10,length-2);
        }else if(maxY <= 2*(int)Math.pow(10,length-1) && maxY > (int)Math.pow(10,length-1))
        {
            YAXIS_NUM = 4;
            num = 5 * (int)Math.pow(10,length-2);
        }else if(maxY <= 3*(int)Math.pow(10,length-1) && maxY > 2*(int)Math.pow(10,length-1))
        {
            YAXIS_NUM = 6;
            num = 5 * (int)Math.pow(10,length-2);
        }else if(maxY <= 4*(int)Math.pow(10,length-1) && maxY > 3*(int)Math.pow(10,length-1))
        {
            YAXIS_NUM = 5;
            num = (int)Math.pow(10,length-1);
        }else if(maxY <= 5*(int)Math.pow(10,length-1) && maxY > 4*(int)Math.pow(10,length-1))
        {
            YAXIS_NUM = 5;
            num = 1 * (int)Math.pow(10,length-1);
        }else if(maxY <= 6*(int)Math.pow(10,length-1) && maxY > 5*(int)Math.pow(10,length-1))
        {
            YAXIS_NUM = 6;
            num = 1*(int)Math.pow(10,length-1);
        }else if(maxY > 6 *(int)Math.pow(10,length-1) && maxY <= 8*(int)Math.pow(10,length-1))
        {
            YAXIS_NUM = 4;
            num = 2*(int)Math.pow(10,length-1);
        }else if(maxY > 8*(int)Math.pow(10,length-1))
        {
            YAXIS_NUM = 5;
            num = 2*(int)Math.pow(10,length-1);
        }
        return num;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(points == null || points.size() == 0)
        {
            return false;
        }
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: ACTION_DOWN");
                xDown = event.getX();
                yDown = event.getY();
                needCaculateDistance = false;
                moveDistance = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getX();
                float distance = currentX - xDown;
                if(Math.abs(distance) > touchSlop)
                {
                    //控制左侧，防止左侧过度滑动
                    if(lastMoveDistance == 0 && distance > 0)
                    {
                        break;
                    }
                    if(lastMoveDistance < 0 && (lastMoveDistance + distance) > 0)
                    {
                        distance = -lastMoveDistance;
                    }

                    //控制右侧，防止右侧过度滑动
                    if(Math.abs(lastMoveDistance) == (xWidth - contentWidth) && distance < 0)
                    {
                        break;
                    }

                    if(Math.abs(lastMoveDistance+distance) > (xWidth - contentWidth))
                    {
                        distance = -lastMoveDistance - (xWidth - contentWidth);
                    }

                    needCaculateDistance = true;
                    moveDistance = (int)distance;
                    startNum = Math.abs(lastMoveDistance + moveDistance + X_CONTENT_PADDING)/unitWidth;
                    endNum = startNum + 8;
                    if(endNum > points.size())
                    {
                        endNum = points.size();
                    }
                    Log.i(TAG,"startNum: "+startNum+" endNum: "+endNum);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                if(needCaculateDistance)
                {
                    //在手指抬起，刷新完之后再保存滑动距离
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lastMoveDistance  = lastMoveDistance + moveDistance;
                        }
                    },100);
                }else
                {
                    int lastClick = currentClickPosition;
                    int i = checkPoint(event);
                    if(i != -1)
                    {
                        currentClickPosition = i;
                    }else{
                        currentClickPosition = -1;
                    }
                    if(lastClick != currentClickPosition)
                    {
                        invalidate();
                    }
                }

                Log.i(TAG, "onTouchEvent: ACTION_UP");


                break;
        }
        return true;
    }


    /**
     * 判断点击位置是否属于 数据中的点
     * @param event
     * @return true or false
     */
    private int checkPoint(MotionEvent event)
    {
        float x = event.getX() - lastMoveDistance;
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