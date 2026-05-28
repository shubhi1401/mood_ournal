package com.jp.moodjournal;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.*;
import com.github.mikephil.charting.data.*;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    PieChart chart;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        chart = findViewById(R.id.chart);
        db = new DatabaseHelper(this);

        setupChart();
        loadGraph();
    }

    private void setupChart() {
        chart.setBackgroundColor(Color.parseColor("#F3E5F5")); // Pastel Purple Background
        chart.setUsePercentValues(true);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(45f);
        chart.setTransparentCircleRadius(50f);
        chart.setHoleColor(Color.WHITE);

        chart.setCenterText("Mood Stats 🌸");
        chart.setCenterTextSize(18f);
        chart.setCenterTextColor(Color.parseColor("#7B1FA2")); // Deep Purple for text

        chart.getDescription().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false); // 🔥 Hide the legend (labels & colors at bottom)
    }

    private void loadGraph() {

        int happy = 0, sad = 0, angry = 0, calm = 0, meh = 0;

        Cursor c = db.getAllMoodsCursor();

        while (c.moveToNext()) {
            String mood = c.getString(1);

            if (mood.contains("😄")) happy++;
            else if (mood.contains("😢")) sad++;
            else if (mood.contains("😡")) angry++;
            else if (mood.contains("😌")) calm++;
            else if (mood.contains("😐")) meh++;
        }
        c.close();

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(happy, "Happy 😄"));
        entries.add(new PieEntry(sad, "Sad 😢"));
        entries.add(new PieEntry(angry, "Angry 😡"));
        entries.add(new PieEntry(calm, "Calm 😌"));
        entries.add(new PieEntry(meh, "Meh 😐"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        // 🎨 Purple & Pink Pastel Theme Colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#CE93D8")); // Pastel Purple (Happy)
        colors.add(Color.parseColor("#F48FB1")); // Pastel Pink (Sad)
        colors.add(Color.parseColor("#BA68C8")); // Medium Purple (Angry)
        colors.add(Color.parseColor("#F06292")); // Medium Pink (Calm)
        colors.add(Color.parseColor("#E1BEE7")); // Lightest Purple (Meh)

        dataSet.setColors(colors);
        dataSet.setSliceSpace(4f);
        dataSet.setSelectionShift(12f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.parseColor("#4A148C")); // Deep Purple for values

        chart.setData(data);

        // ✨ Animation
        chart.animateY(1200);

        chart.invalidate();
    }
}