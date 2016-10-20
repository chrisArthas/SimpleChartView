package com.chartsdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chartsdemo.views.LineChartsView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Button btn1;

    private LineChartsView lineChartsView;

    private EditText editTextYnum;

    private EditText editTextXnum;

    List<Integer> list;

    List<String> namesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChartsView = (LineChartsView)findViewById(R.id.linechartview);

        btn1 = (Button)findViewById(R.id.btn1);

        editTextYnum= (EditText)findViewById(R.id.ynum);
        editTextXnum = (EditText)findViewById(R.id.xnum);

         list = new ArrayList<>();

        list.add(170);
        list.add(100);
        list.add(350);
        list.add(140);
        list.add(200);
        list.add(300);
        list.add(300);

        lineChartsView.setData(list);

        namesList = new ArrayList<>();
        namesList.add("一");
        namesList.add("二");
        namesList.add("三");
        namesList.add("四");
        namesList.add("五");
        namesList.add("六");
        namesList.add("七");

        lineChartsView.setXAxisNames(namesList);


        lineChartsView.setYUnit(50);

        lineChartsView.setLineColor(this.getResources().getColor(R.color.colorPrimaryDark));


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ynum = editTextYnum.getText().toString();
                String xnum = editTextXnum.getText().toString();
                if(!ynum.equals("")&& !xnum.equals(""))
                {

                    list.add(Integer.parseInt(ynum));
                    namesList.add(xnum);
                }else
                {
                    Toast.makeText(MainActivity.this,"x value or y value can not be null",Toast.LENGTH_SHORT).show();
                }


                lineChartsView.setDataChange();
            }
        });
    }
}
