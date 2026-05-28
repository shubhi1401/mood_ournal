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

        TextView mood, note, date, time;
        Button deleteBtn;

        public ViewHolder(View view) {
            super(view);

            mood = view.findViewById(R.id.moodText);
            note = view.findViewById(R.id.noteText);
            date = view.findViewById(R.id.dateText);
            time = view.findViewById(R.id.timeText);
            deleteBtn = view.findViewById(R.id.deleteBtn);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_mood, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (cursor.moveToPosition(position)) {

            int id = cursor.getInt(0);
            String mood = cursor.getString(1);
            String note = cursor.getString(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);

            holder.mood.setText(mood);
            holder.note.setText(note);
            holder.date.setText(date);
            holder.time.setText(time);

            // 🔥 Delete feature
            holder.deleteBtn.setOnClickListener(v -> {
                db.deleteMood(id);
                ((HistoryActivity) context).refresh();
            });

            // 👆 Click for details
            holder.itemView.setOnClickListener(v ->
                    Toast.makeText(context,
                            mood + "\n" + date + " " + time,
                            Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}