package com.jp.moodjournal;

import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class MoodJournalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
