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
        // Return the size of the original text so it still takes up space
        return (int) paint.measureText(text, start, end);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, @NonNull Paint paint) {
        
        // 1. Draw original date text
        canvas.drawText(text, start, end, x, y, paint);

        // 2. Draw emoji below it
        float oldSize = paint.getTextSize();
        paint.setTextSize(oldSize * 0.8f);
        
        // Adjust x to center the emoji under the text
        float textWidth = paint.measureText(text, start, end);
        float emojiWidth = paint.measureText(emoji);
        canvas.drawText(emoji, x + (textWidth / 2) - (emojiWidth / 2), y + (oldSize * 0.9f), paint);
        
        paint.setTextSize(oldSize);
    }
}
