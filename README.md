# SimpleLineChartView

Android 简单的折现图实现

![预览效果](https://github.com/chrisArthas/SimpleLineChartView/raw/master/preview2.png)

##V1.0 
    支持输入数据（废话）以及横坐标

    支持自定义 折线颜色字体颜色等

    尚未支持点击效果，后续会加上
    
    
##V1.1 
     加了点击相应的数据点的提示效果 
     增加了一个动态增加数据的按钮
	 
	 优化了Y轴单位距离的计算，并提供了设置Y轴单位数量方法，方便根据数据的不同来设置Y轴
	 
	 
	```
	lineChartsView.setYCounts(20);
	```


使用方式：

xml中
```
    <com.chartsdemo.views.LineChartsView
        android:id="@+id/linechartview"
        android:layout_width="300dp"
        android:layout_height="300dp"
        color:axisColor="#DFDFDF"/>
      
      
      
```      
activity中
```
        lineChartsView = (LineChartsView)findViewById(R.id.linechartview);
        List<Integer> list = new ArrayList<>();
        list.add(170);
        list.add(100);
        list.add(350);
        lineChartsView.setData(list);
        
        List<String> namesList = new ArrayList<>();
        namesList.add("一月");
        namesList.add("二月");
        namesList.add("三月");
        lineChartsView.setXAxisNames(namesList);

        lineChartsView.setYUnit(100);
        lineChartsView.setLineColor(this.getResources().getColor(R.color.colorPrimaryDark));
```
 
