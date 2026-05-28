package com.jp.moodjournal;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class MoodDecorator implements DayViewDecorator {

    private final CalendarDay date;
    private final String mood;

    public MoodDecorator(CalendarDay date, String mood) {
        this.date = date;
        this.mood = mood;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        int color;
        String emoji = "";

        if (mood.contains("😄")) {
            color = Color.parseColor("#FBC02D"); // Vibrant Yellow
            emoji = "😄";
        } else if (mood.contains("😌")) {
            color = Color.parseColor("#4CAF50"); // Green
            emoji = "😌";
        } else if (mood.contains("😐")) {
            color = Color.parseColor("#9E9E9E"); // Grey
            emoji = "😐";
        } else if (mood.contains("😢")) {
            color = Color.parseColor("#1976D2"); // Blue
            emoji = "😢";
        } else if (mood.contains("😡")) {
            color = Color.parseColor("#D32F2F"); // Red
            emoji = "😡";
        } else {
            color = Color.GRAY;
        }

        // 1. Remove the full background box. Instead, we can use a subtle DotSpan or just the emoji.
        // We'll use a small dot at the bottom to indicate a mood is logged, in addition to the emoji.
        view.addSpan(new DotSpan(8, color));

        // 2. Add the emoji using our custom span that draws it below the date
        if (!emoji.isEmpty()) {
            view.addSpan(new EmojiSpan(emoji));
        }
        
        // 3. Make the date text white to be visible on purple
        view.addSpan(new ForegroundColorSpan(Color.WHITE));
    }
}
