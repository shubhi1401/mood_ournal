package com.jp.moodjournal;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddMoodActivity extends AppCompatActivity {

    String mood = "";
    EditText note;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        note = findViewById(R.id.note);
        db = new DatabaseHelper(this);

        // Update to match new LinearLayout IDs
        findViewById(R.id.happy).setOnClickListener(v -> {
            mood = "Happy 😊";
            Toast.makeText(this, "Selected: Happy", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.neutral).setOnClickListener(v -> {
            mood = "Neutral 😐";
            Toast.makeText(this, "Selected: Neutral", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.sad).setOnClickListener(v -> {
            mood = "Sad 😢";
            Toast.makeText(this, "Selected: Sad", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.saveBtn).setOnClickListener(v -> {
            if (mood.isEmpty()) {
                Toast.makeText(this, "Select mood", Toast.LENGTH_SHORT).show();
                return;
            }

            String text = note.getText().toString();
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            db.insertMood(mood, text, date, time);

            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
