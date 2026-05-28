package com.jp.moodjournal;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.*;
import com.github.mikephil.charting.data.*;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    PieChart chart;
    DatabaseHelper db;
    TextView moodQuote, quoteEmoji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        chart = findViewById(R.id.chart);
        moodQuote = findViewById(R.id.moodQuote);
        quoteEmoji = findViewById(R.id.quoteEmoji);
        db = new DatabaseHelper(this);

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        setupChart();
        loadGraph();
    }

    private void setupChart() {
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setUsePercentValues(true);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(62f);
        
        // Theme-aware colors
        android.util.TypedValue typedValue = new android.util.TypedValue();
        getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        int textColor = typedValue.data;

        chart.setHoleColor(Color.TRANSPARENT);
        chart.setCenterText("Mood\nSummary 🌸");
        chart.setCenterTextSize(16f);
        chart.setCenterTextColor(textColor);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }

    private void loadGraph() {
        int happy = 0, sad = 0, angry = 0, calm = 0, meh = 0;

        Cursor c = db.getAllMoodsCursor();
        if (c == null) return;

        while (c.moveToNext()) {
            String mood = c.getString(1);
            if (mood.contains("😄")) happy++;
            else if (mood.contains("😢")) sad++;
            else if (mood.contains("😡")) angry++;
            else if (mood.contains("😌")) calm++;
            else if (mood.contains("😐")) meh++;
        }
        c.close();

        // Quote logic
        int max = Math.max(happy, Math.max(sad, Math.max(angry, Math.max(calm, meh))));
        String quote;
        String emoji;

        if (max == 0) {
            quote = "No entries yet. Start logging your mood! ✍️";
            emoji = "✨";
        } else if (max == happy) {
            emoji = "😄";
            quote = "You've been mostly happy! Keep spreading that joy. ✨";
        } else if (max == calm) {
            emoji = "😌";
            quote = "Inner peace is your superpower lately. Stay mindful. 🧘";
        } else if (max == sad) {
            emoji = "😢";
            quote = "It's okay to feel down. This too shall pass. Take care of yourself. 🌿";
        } else if (max == angry) {
            emoji = "😡";
            quote = "Take a deep breath. Channel that energy into something positive. 🌪️";
        } else {
            emoji = "😐";
            quote = "Finding balance is key. Tomorrow is a new day! ⛅";
        }

        moodQuote.setText(quote);
        quoteEmoji.setText(emoji);
        
        findViewById(R.id.quoteCard).startAnimation(
                AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (happy > 0) entries.add(new PieEntry(happy, "Happy"));
        if (sad > 0) entries.add(new PieEntry(sad, "Sad"));
        if (angry > 0) entries.add(new PieEntry(angry, "Angry"));
        if (calm > 0) entries.add(new PieEntry(calm, "Calm"));
        if (meh > 0) entries.add(new PieEntry(meh, "Meh"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#CE93D8")); // Happy
        colors.add(Color.parseColor("#F48FB1")); // Sad
        colors.add(Color.parseColor("#BA68C8")); // Angry
        colors.add(Color.parseColor("#F06292")); // Calm
        colors.add(Color.parseColor("#E1BEE7")); // Meh

        dataSet.setColors(colors);
        dataSet.setSliceSpace(4f);
        dataSet.setSelectionShift(12f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        
        // Use theme-aware color for values on slices
        android.util.TypedValue typedValueVal = new android.util.TypedValue();
        getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValueVal, true);
        data.setValueTextColor(typedValueVal.data);

        chart.setData(data);
        chart.animateY(1200);
        chart.invalidate();
    }
}
