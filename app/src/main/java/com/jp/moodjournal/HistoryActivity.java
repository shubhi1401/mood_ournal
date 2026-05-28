package com.jp.moodjournal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        db = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        findViewById(R.id.calendarBtnCard).setOnClickListener(v -> {
            startActivity(new Intent(this, CalendarActivity.class));
        });

        loadData();
    }

    public void loadData() {
        Cursor cursor = db.getAllMoodsCursor();
        MoodAdapter adapter = new MoodAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
    }

    public void refresh() {
        loadData();
    }
}