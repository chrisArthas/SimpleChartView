package com.chartsdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chartsdemo.R;
import com.chartsdemo.activitys.MainActivity;
import com.chartsdemo.views.LineChartView;
import com.chartsdemo.views.LineChartsView;
import com.chartsdemo.views.LivePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win on 2017/4/20.
 */

public class LineChartFragment extends Fragment{

//    private Button btn1;
//
//    private LineChartsView lineChartsView;
//
//    private EditText editTextYnum;
//
//    private EditText editTextXnum;
//
//    List<Integer> list;
//
//    List<String> namesList;

    private LineChartView lineChartView;

    public LineChartFragment() {
    }

    static public LineChartFragment getInstance()
    {
        return new LineChartFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.linechar_layout,null,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChartView = (LineChartView) view.findViewById(R.id.line_view);
        List<LivePoint> livePoints = new ArrayList<>();
        livePoints.add(new LivePoint("10","00:00"));
        livePoints.add(new LivePoint("43","01:00"));
        livePoints.add(new LivePoint("88","02:00"));
        livePoints.add(new LivePoint("104","03:00"));
        livePoints.add(new LivePoint("76","04:00"));
        livePoints.add(new LivePoint("55","05:00"));
        livePoints.add(new LivePoint("70","06:00"));
        livePoints.add(new LivePoint("88","07:00"));
        livePoints.add(new LivePoint("110","08:00"));
        livePoints.add(new LivePoint("120","09:00"));
        livePoints.add(new LivePoint("130","10:00"));
        livePoints.add(new LivePoint("100","11:00"));
        livePoints.add(new LivePoint("40","12:00"));
        livePoints.add(new LivePoint("20","13:00"));
        livePoints.add(new LivePoint("10","14:00"));
        livePoints.add(new LivePoint("80","15:00"));
        livePoints.add(new LivePoint("100","16:00"));
        livePoints.add(new LivePoint("150","17:00"));
        livePoints.add(new LivePoint("90","18:00"));
        livePoints.add(new LivePoint("60","19:00"));
        livePoints.add(new LivePoint("50","20:00"));
        livePoints.add(new LivePoint("30","21:00"));
        livePoints.add(new LivePoint("30","22:00"));
        livePoints.add(new LivePoint("10","23:00"));


        lineChartView.setData(livePoints);
//        initView(view);
//        initData();
    }

//    private void initView(View view)
//    {
//        lineChartsView = (LineChartsView)view.findViewById(R.id.linechartview);
//
//        btn1 = (Button)view.findViewById(R.id.btn1);
//
//        editTextYnum= (EditText)view.findViewById(R.id.ynum);
//        editTextXnum = (EditText)view.findViewById(R.id.xnum);
//    }
//
//    private void initData()
//    {
//        list = new ArrayList<>();
//
//        list.add(17);
//        list.add(10);
//        list.add(35);
//        list.add(14);
//        list.add(20);
//        list.add(30);
//        list.add(30);
//
//        lineChartsView.setData(list);
//
//        namesList = new ArrayList<>();
//        namesList.add("一");
//        namesList.add("二");
//        namesList.add("三");
//        namesList.add("四");
//        namesList.add("五");
//        namesList.add("六");
//        namesList.add("七");
//
//        lineChartsView.setXAxisNames(namesList);
//
//
//        lineChartsView.setYUnit(5);
//
//        lineChartsView.setYCounts(20);
//
//
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String ynum = editTextYnum.getText().toString();
//                String xnum = editTextXnum.getText().toString();
//                if(!ynum.equals("")&& !xnum.equals(""))
//                {
//
//                    list.add(Integer.parseInt(ynum));
//                    namesList.add(xnum);
//                }else
//                {
//                    Toast.makeText(getActivity(),"x value or y value can not be null",Toast.LENGTH_SHORT).show();
//                }
//
//
//                lineChartsView.setDataChange();
//            }
//        });
//    }
}
