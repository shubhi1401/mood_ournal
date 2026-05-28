package com.jp.moodjournal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class PlaylistActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> songs = new ArrayList<>();
    ArrayList<String> links = new ArrayList<>();
    int clickCount = 0;
    String currentMood;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        listView = findViewById(R.id.listView);
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());
        currentMood = getIntent().getStringExtra("mood");
        if (currentMood == null) currentMood = "😄"; // Default to happy if null

        loadSongs(currentMood);

        adapter = new ArrayAdapter<String>(this, R.layout.item_song, R.id.songName, songs);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = links.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

            clickCount++;
            // Refresh logic: every 2 clicks, shuffle and pick new songs for the NEXT view
            if (clickCount % 2 == 0) {
                loadSongs(currentMood);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadSongs(String mood) {
        songs.clear();
        links.clear();

        ArrayList<SongItem> pool = new ArrayList<>();
        String ytBase = "https://www.youtube.com/results?search_query=";

        if (mood.contains("😄")) {
            pool.add(new SongItem("Happy - Pharrell Williams 🇺🇸", ytBase + "Pharrell+Williams+Happy"));
            pool.add(new SongItem("Zinda - Bhaag Milkha Bhaag 🇮🇳", ytBase + "Zinda+Bhaag+Milkha+Bhaag"));
            pool.add(new SongItem("Proper Patola - Diljit Dosanjh 🌾", ytBase + "Proper+Patola+Diljit+Dosanjh"));
            pool.add(new SongItem("Mauja Hi Mauja - Jab We Met 💃", ytBase + "Mauja+Hi+Mauja"));
            pool.add(new SongItem("Walking on Sunshine - Katrina ☀️", ytBase + "Walking+on+Sunshine"));
            pool.add(new SongItem("Levitating - Dua Lipa ✨", ytBase + "Levitating+Dua+Lipa"));
            pool.add(new SongItem("Gallan Goodiyaan 🥳", ytBase + "Gallan+Goodiyaan"));
            pool.add(new SongItem("Lover - Diljit Dosanjh 💖", ytBase + "Lover+Diljit+Dosanjh"));
        } else if (mood.contains("😢")) {
            pool.add(new SongItem("Someone Like You - Adele 🇬🇧", ytBase + "Adele+Someone+Like+You"));
            pool.add(new SongItem("Channa Mereya - Arijit Singh 💔", ytBase + "Channa+Mereya"));
            pool.add(new SongItem("Qismat - Ammy Virk 🌧️", ytBase + "Qismat+Ammy+Virk"));
            pool.add(new SongItem("Fix You - Coldplay 🕯️", ytBase + "Fix+You+Coldplay"));
            pool.add(new SongItem("Agar Tum Saath Ho 🥀", ytBase + "Agar+Tum+Saath+Ho"));
            pool.add(new SongItem("Stay - Justin Bieber 🌊", ytBase + "Stay+Justin+Bieber"));
            pool.add(new SongItem("Tujhe Kitna Chahne Lage 🎻", ytBase + "Tujhe+Kitna+Chahne+Lage"));
        } else if (mood.contains("😡")) {
            pool.add(new SongItem("Weightless - Marconi Union 🧘", ytBase + "Weightless+Marconi+Union"));
            pool.add(new SongItem("Iktara - Wake Up Sid ✨", ytBase + "Iktara+Wake+Up+Sid"));
            pool.add(new SongItem("Kinni Kinni - Diljit Dosanjh 🌊", ytBase + "Kinni+Kinni+Diljit"));
            pool.add(new SongItem("River Flows in You - Yiruma 🎹", ytBase + "River+Flows+in+You+Yiruma"));
            pool.add(new SongItem("Kun Faya Kun - Rockstar 🕊️", ytBase + "Kun+Faya+Kun"));
            pool.add(new SongItem("Pasoori - Ali Sethi 🎶", ytBase + "Pasoori+Ali+Sethi"));
            pool.add(new SongItem("Clair de Lune 🌙", ytBase + "Clair+de+Lune"));
        } else {
            pool.add(new SongItem("Sunflower - Post Malone 🌻", ytBase + "Sunflower+Post+Malone"));
            pool.add(new SongItem("Tum Se Hi - Jab We Met 💕", ytBase + "Tum+Se+Hi"));
            pool.add(new SongItem("Lemonade - Diljit Dosanjh 🍋", ytBase + "Lemonade+Diljit+Dosanjh"));
            pool.add(new SongItem("Peaches - Justin Bieber 🍑", ytBase + "Peaches+Justin+Bieber"));
            pool.add(new SongItem("Brown Munde - AP Dhillon 🔥", ytBase + "Brown+Munde+AP+Dhillon"));
            pool.add(new SongItem("Excuses - AP Dhillon 🕶️", ytBase + "Excuses+AP+Dhillon"));
        }

        Collections.shuffle(pool);
        
        // Pick a subset of 4 random songs from the larger pool
        int count = Math.min(pool.size(), 4);
        for (int i = 0; i < count; i++) {
            songs.add(pool.get(i).name);
            links.add(pool.get(i).link);
        }
    }

    private static class SongItem {
        String name, link;
        SongItem(String name, String link) {
            this.name = name;
            this.link = link;
        }
    }
}
