package com.patang.agora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Database extends AppCompatActivity {

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        mChart = (LineChart) findViewById(R.id.linechart);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 50f));
        yValues.add(new Entry(2, 70f));
        yValues.add(new Entry(3, 30f));
        yValues.add(new Entry(4, 50f));
        yValues.add(new Entry(5, 60f));
        yValues.add(new Entry(5, 65f));

        LineDataSet set1 = new LineDataSet(yValues, "Data Set");
        set1.setFillAlpha(110);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        mChart.setData(data);

    }
}
