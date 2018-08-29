package com.chartsdemo.views;

public class LivePoint {

    public LivePoint(String y,String x)
    {
        this.x =x;
        this.y = y;
    }

    //横纵坐标
    private String x;

    private String y;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}
