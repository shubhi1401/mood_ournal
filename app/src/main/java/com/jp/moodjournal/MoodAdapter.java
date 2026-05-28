package com.jp.moodjournal;

import android.content.Context;
import android.database.Cursor;
import android.view.*;
import android.widget.*;

import androidx.recyclerview.widget.RecyclerView;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.ViewHolder> {
    Context context;
    Cursor cursor;
    DatabaseHelper db;
    public MoodAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        db = new DatabaseHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mood, note, date;
        View deleteBtn;

        public ViewHolder(View view) {
            super(view);

            mood = view.findViewById(R.id.moodText);
            note = view.findViewById(R.id.noteText);
            date = view.findViewById(R.id.dateText);
            deleteBtn = view.findViewById(R.id.deleteBtn);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.mood_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (cursor.moveToPosition(position)) {

            int id = cursor.getInt(0);
            String moodStr = cursor.getString(1);
            String note = cursor.getString(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);

            // Robust emoji display: use the saved string, but fallback if it was the old format
            String displayEmoji = moodStr;
            if (moodStr.contains("Happy")) displayEmoji = "😄";
            else if (moodStr.contains("Neutral")) displayEmoji = "😐";
            else if (moodStr.contains("Sad")) displayEmoji = "😢";
            else if (moodStr.contains("Angry")) displayEmoji = "😡";
            else if (moodStr.contains("Calm")) displayEmoji = "😌";
            
            // If it's just the emoji now, it will just use moodStr as is.

            holder.mood.setText(displayEmoji);
            holder.note.setText(note);
            holder.date.setText(date + " • " + time);

            // 🔥 Delete feature (safer cast)
            holder.deleteBtn.setOnClickListener(v -> {
                db.deleteMood(id);
                if (context instanceof HistoryActivity) {
                    ((HistoryActivity) context).refresh();
                }
            });

            // 👆 Click for details
            holder.itemView.setOnClickListener(v ->
                    Toast.makeText(context,
                            moodStr + "\n" + date + " " + time,
                            Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}