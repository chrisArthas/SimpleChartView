package com.chartsdemo.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chartsdemo.R;
import com.chartsdemo.fragments.LineChartFragment;
import com.chartsdemo.fragments.PaneFragment;
import com.chartsdemo.views.LineChartsView;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPanelView();

    }

    private void getLineChartFragment()
    {
        LineChartFragment fragment = (LineChartFragment)getSupportFragmentManager().findFragmentById(R.id.content);

        if(fragment == null)
        {
            fragment = LineChartFragment.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    private void getPanelView()
    {
        PaneFragment fragment = (PaneFragment)getSupportFragmentManager().findFragmentById(R.id.content);

        if(fragment == null)
        {
            fragment = PaneFragment.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
