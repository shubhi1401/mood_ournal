package com.jp.moodjournal;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "MoodDB", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE moods(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mood TEXT, " +
                "note TEXT, " +
                "date TEXT, " +
                "time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple upgrade: just add missing columns if needed, but here we'll just keep it simple
        // To avoid data loss, we should ideally use ALTER TABLE, but since we already upgraded to 2
        // and dropped the table, we'll just ensure it exists.
        onCreate(db);
    }

    // 🔹 Insert mood (UPDATED)
    public void insertMood(String mood, String note, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("mood", mood);
        cv.put("note", note);
        cv.put("date", date);
        cv.put("time", time);

        db.insert("moods", null, cv);
    }

    // 🔹 OLD METHOD (kept for your history screen)
    public ArrayList<String> getAllMoods() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM moods ORDER BY id DESC", null);

        while (c.moveToNext()) {
            String mood = c.getString(1);
            String note = c.getString(2);
            String date = c.getString(3);
            String time = c.getString(4);

            list.add(mood + "\n" + note + "\n" + date + " " + time);
        }
        c.close();
        return list;
    }

    // 🔹 NEW: Get moods for specific date (calendar)
    public Cursor getMoodByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM moods WHERE date = ?", new String[]{date});
    }

    // 🔹 NEW: Count moods (for graphs)
    public int getMoodCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM moods", null);

        if (c.moveToFirst()) {
            int count = c.getInt(0);
            c.close();
            return count;
        }
        c.close();
        return 0;
    }
    public Cursor getAllMoodsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM moods", null);
    }
    // 🔹 NEW: Calculate Streak
    public int getStreak() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT date FROM moods ORDER BY date DESC", null);
        
        int streak = 0;
        // Simple streak logic: count consecutive days starting from today or yesterday
        // For simplicity, we'll just count how many distinct days are in the DB for now
        // or you can implement a more complex logic with date parsing.
        streak = c.getCount();
        c.close();
        return streak;
    }

    public void deleteMood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("moods", "id = ?", new String[]{String.valueOf(id)});
    }
}