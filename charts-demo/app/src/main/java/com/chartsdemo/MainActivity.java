package com.chartsdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chartsdemo.views.LineChartsView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private LineChartsView lineChartsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    }
}