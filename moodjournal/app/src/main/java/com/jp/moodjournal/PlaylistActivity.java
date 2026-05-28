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
        currentMood = getIntent().getStringExtra("mood");
        if (currentMood == null) currentMood = "😄"; // Default to happy if null

        loadSongs(currentMood);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songs) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(android.graphics.Color.parseColor("#4A148C")); // Deep Purple
                text.setTextSize(16f);
                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            clickCount++;
            
            // Refresh logic: every 2 clicks, shuffle and pick new songs
            if (clickCount % 2 == 0) {
                loadSongs(currentMood);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Shuffling playlist... 🔄", Toast.LENGTH_SHORT).show();
            }

            String url = links.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }

    private void loadSongs(String mood) {
        songs.clear();
        links.clear();

        ArrayList<SongItem> pool = new ArrayList<>();

        // Multi-genre Pool: Bollywood + Hollywood + Punjabi
        if (mood.contains("😄")) {
            pool.add(new SongItem("Happy - Pharrell Williams 🇺🇸", "https://open.spotify.com/track/60nZcImuRpwUvU6ZDCYvYG"));
            pool.add(new SongItem("Zinda - Bhaag Milkha Bhaag 🇮🇳", "https://open.spotify.com/track/4C68KqZ6hP3G6pYVf6D9Rz"));
            pool.add(new SongItem("Proper Patola - Diljit Dosanjh 🌾", "https://open.spotify.com/track/6Nne8xJv5Eov6VjLdYlGq6"));
            pool.add(new SongItem("Mauja Hi Mauja - Jab We Met 💃", "https://open.spotify.com/track/6uS0V8m6D9BfI6Z8L8R1Y1"));
            pool.add(new SongItem("Walking on Sunshine - Katrina ☀️", "https://open.spotify.com/track/05wIrZqcneXhEBJsqS3vXv"));
            pool.add(new SongItem("Levitating - Dua Lipa ✨", "https://open.spotify.com/track/39Yp9IuGZ16SshvS9ABuO4"));
            pool.add(new SongItem("Gallan Goodiyaan 🥳", "https://open.spotify.com/track/4vUmDws77QSTbbZ2STv2S8"));
            pool.add(new SongItem("Lover - Diljit Dosanjh 💖", "https://open.spotify.com/track/2S5k6V8m6D9BfI6Z8L8R1Y1"));
        } else if (mood.contains("😢")) {
            pool.add(new SongItem("Someone Like You - Adele 🇬🇧", "https://open.spotify.com/track/4vUmDws77QSTbbZ2STv2S8"));
            pool.add(new SongItem("Channa Mereya - Arijit Singh 💔", "https://open.spotify.com/track/1LInZ5K7S8J7I9V7S7S7S7"));
            pool.add(new SongItem("Qismat - Ammy Virk 🌧️", "https://open.spotify.com/track/2S5k6V8m6D9BfI6Z8L8R1Y1"));
            pool.add(new SongItem("Fix You - Coldplay 🕯️", "https://open.spotify.com/track/7LVHVU3tWfc0emwYvO9gyC"));
            pool.add(new SongItem("Agar Tum Saath Ho 🥀", "https://open.spotify.com/track/3ylXp7S8J7I9V7S7S7S7S7"));
            pool.add(new SongItem("Stay - Justin Bieber 🌊", "https://open.spotify.com/track/5HCyWvSTE6vR9nSZZ9S6S0"));
            pool.add(new SongItem("Tujhe Kitna Chahne Lage 🎻", "https://open.spotify.com/track/6kf769LpIC8X7Z2nd80Eny"));
        } else if (mood.contains("😡")) {
            pool.add(new SongItem("Weightless - Marconi Union 🧘", "https://open.spotify.com/track/6k9mgoS7pT9uSjS9S6S0p4"));
            pool.add(new SongItem("Iktara - Wake Up Sid ✨", "https://open.spotify.com/track/4S5k6V8m6D9BfI6Z8L8R1Y1"));
            pool.add(new SongItem("Kinni Kinni - Diljit Dosanjh 🌊", "https://open.spotify.com/track/5S5k6V8m6D9BfI6Z8L8R1Y1"));
            pool.add(new SongItem("River Flows in You - Yiruma 🎹", "https://open.spotify.com/track/649SBr97vTFTp9v99mZpYm"));
            pool.add(new SongItem("Kun Faya Kun - Rockstar 🕊️", "https://open.spotify.com/track/2S5k6V8m6D9BfI6Z8L8R1Y1"));
            pool.add(new SongItem("Pasoori - Ali Sethi 🎶", "https://open.spotify.com/track/59v6V8m6D9BfI6Z8L8R1Y1"));
            pool.add(new SongItem("Clair de Lune 🌙", "https://open.spotify.com/track/6kf769LpIC8X7Z2nd80Eny"));
        } else {
            pool.add(new SongItem("Sunflower - Post Malone 🌻", "https://open.spotify.com/track/3G69vJMWsX6ZgfwYCNCcgk"));
            pool.add(new SongItem("Tum Se Hi - Jab We Met 💕", "https://open.spotify.com/track/7S5k6V8m6D9BfI6Z8L8R1Y1"));
            pool.add(new SongItem("Lemonade - Diljit Dosanjh 🍋", "https://open.spotify.com/track/1S5k6V8m6D9BfI6Z8L8R1Y1"));
            pool.add(new SongItem("Peaches - Justin Bieber 🍑", "https://open.spotify.com/track/4iJyoH6vR9nSZZ9S6S0"));
            pool.add(new SongItem("Brown Munde - AP Dhillon 🔥", "https://open.spotify.com/track/59v6V8m6D9BfI6Z8L8R1Y1"));
            pool.add(new SongItem("Excuses - AP Dhillon 🕶️", "https://open.spotify.com/track/7MXvBYY3UisS0D0pC6S8D4"));
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
