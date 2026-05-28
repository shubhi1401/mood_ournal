package com.jp.moodjournal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EmojiSpan extends ReplacementSpan {

    private final String emoji;

    public EmojiSpan(String emoji) {
        this.emoji = emoji;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        // Return 0 so it doesn't take up horizontal space in the text flow,
        // allowing it to be drawn "on top" or "below" the existing text.
        return 0;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, @NonNull Paint paint) {
        
        // Save original text size
        float oldSize = paint.getTextSize();
        
        // Make emoji slightly smaller to fit better
        paint.setTextSize(oldSize * 0.8f);
        
        // Draw emoji below the date number. 
        // 'y' is the baseline of the date. We move down from there.
        canvas.drawText(emoji, x - (paint.measureText(emoji) / 2), y + (oldSize * 0.8f), paint);
        
        // Restore original text size
        paint.setTextSize(oldSize);
    }
}
