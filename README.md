# SimpleLineChartView

Android 简单的折现图实现

[preview](http://github.com/chrisArthas/SimpleLineChartView/raw/master/preview.png)

##V1.0 
    支持输入数据（废话）以及横坐标

    支持自定义 折线颜色字体颜色等

    尚未支持点击效果，后续会加上


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
        list.add(140);
        list.add(200);
        list.add(300);
        list.add(300);

        lineChartsView.setData(list);

        List<String> namesList = new ArrayList<>();
        namesList.add("一月");
        namesList.add("二月");
        namesList.add("三月");
        namesList.add("四月");
        namesList.add("五月");
        namesList.add("六月");
        namesList.add("七月");

        lineChartsView.setXAxisNames(namesList);


        lineChartsView.setYUnit(100);

        lineChartsView.setLineColor(this.getResources().getColor(R.color.colorPrimaryDark));
        ```
 
