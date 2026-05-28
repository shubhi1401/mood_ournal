package com.jp.moodjournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextView streakText, entryCountText, greetingText;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved theme BEFORE super.onCreate
        prefs = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        // Theme Toggle
        findViewById(R.id.themeToggle).setOnClickListener(v -> {
            boolean currentMode = prefs.getBoolean("isDarkMode", false);
            SharedPreferences.Editor editor = prefs.edit();
            if (currentMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("isDarkMode", false);
                Toast.makeText(this, "Light Mode ☀️", Toast.LENGTH_SHORT).show();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("isDarkMode", true);
                Toast.makeText(this, "Dark Mode 🌙", Toast.LENGTH_SHORT).show();
            }
            editor.apply();
        });

        // Stats & Greeting
        streakText = findViewById(R.id.streakText);
        entryCountText = findViewById(R.id.entryCountText);
        greetingText = findViewById(R.id.greetingText);
        updateGreeting();
        updateStats();

        // Buttons
        Button addMood = findViewById(R.id.addMood);
        Button viewHistory = findViewById(R.id.viewHistory);
        Button graphBtn = findViewById(R.id.viewGraph);
        Button calendarBtn = findViewById(R.id.viewCalendar);

        graphBtn.setOnClickListener(v ->
                startActivity(new Intent(this, GraphActivity.class)));

        calendarBtn.setOnClickListener(v ->
                startActivity(new Intent(this, CalendarActivity.class)));

        // Mood emojis
        TextView happy = findViewById(R.id.moodHappy);
        TextView calm = findViewById(R.id.moodCalm);
        TextView meh = findViewById(R.id.moodMeh);
        TextView sad = findViewById(R.id.moodSad);
        TextView angry = findViewById(R.id.moodAngry);

        // Click listener
        View.OnClickListener moodClick = v -> {

            String mood = ((TextView) v).getText().toString();

            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            db.insertMood(mood, "", date, time);
            updateStats();

            // 💡 Suggestion
            String suggestion;
            if (mood.contains("😢")) {
                suggestion = "Take a short walk 🌿";
            } else if (mood.contains("😡")) {
                suggestion = "Try deep breathing 🧘";
            } else if (mood.contains("😌")) {
                suggestion = "Enjoy the peace ✨";
            } else {
                suggestion = "Keep smiling 😄";
            }

            Toast.makeText(this, "Saved: " + mood + "\n" + suggestion, Toast.LENGTH_LONG).show();

            // 🎵 NEW FLOW → Open PlaylistActivity
            Intent intent = new Intent(this, PlaylistActivity.class);
            intent.putExtra("mood", mood);
            startActivity(intent);
        };

        // Attach listeners
        happy.setOnClickListener(moodClick);
        calm.setOnClickListener(moodClick);
        meh.setOnClickListener(moodClick);
        sad.setOnClickListener(moodClick);
        angry.setOnClickListener(moodClick);

        addMood.setOnClickListener(v ->
                startActivity(new Intent(this, AddMoodActivity.class)));

        viewHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
    }

    private void updateGreeting() {
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        String greeting;

        if (hour < 12) greeting = "Good Morning ☀️";
        else if (hour < 17) greeting = "Good Afternoon 🌤️";
        else if (hour < 21) greeting = "Good Evening 🌆";
        else greeting = "Good Night 🌙";

        greetingText.setText(greeting);
    }

    private void updateStats() {
        int count = db.getMoodCount();
        int streak = db.getStreak();

        entryCountText.setText(String.valueOf(count));
        streakText.setText(streak + " days");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
        updateGreeting();
    }
}