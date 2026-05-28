package com.jp.moodjournal;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        db = new DatabaseHelper(this);

        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.setAlpha(0f);
        calendarView.animate().alpha(1f).setDuration(600);

        loadMoodDates();

        // Click on date
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            String selectedDate = formatDate(date);
            Cursor c = db.getMoodByDate(selectedDate);

            if (c.moveToFirst()) {
                String mood = c.getString(1);
                String note = c.getString(2);
                String time = c.getString(4);

                Toast.makeText(this,
                        "Mood: " + mood + "\n" +
                                "Time: " + time + "\n" +
                                "Note: " + note,
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No mood logged", Toast.LENGTH_SHORT).show();
            }
            c.close();
        });
    }

    private void loadMoodDates() {
        Cursor c = db.getAllMoodsCursor();
        if (c == null) return;

        while (c.moveToNext()) {
            String dateStr = c.getString(3); // date
            String mood = c.getString(1);

            CalendarDay day = convertToCalendarDay(dateStr);

            if (day != null) {
                calendarView.addDecorator(new MoodDecorator(day, mood));
            }
        }
        c.close();
    }

    private CalendarDay convertToCalendarDay(String dateStr) {
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(dateStr);
            if (date == null) return null;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return CalendarDay.from(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
            );

        } catch (Exception e) {
            return null;
        }
    }

    private String formatDate(CalendarDay day) {
        return String.format(Locale.getDefault(), "%02d-%02d-%04d",
                day.getDay(), day.getMonth(), day.getYear());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
    }
}
