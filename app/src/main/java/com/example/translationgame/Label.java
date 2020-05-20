package com.example.translationgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Label {
    private String label;
    private RectF rect;
    private float fontSize;
    private float textWidth;
    private float textHeight;

    public Label(String lbl, Paint paint, float x, float y, float width, float height) {
        label = lbl;
        rect = new RectF(x, y, x + width, y + height);

        for (fontSize = 10.f; fontSize < 400f; fontSize++) {
            paint.setTextSize(fontSize);
            textWidth = paint.measureText(label);
            textHeight = paint.descent() - paint.ascent();
            if (textWidth >= width || textHeight >= height) {
                break;
            }
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(fontSize);

        float textX = rect.centerX() - textWidth / 2.f;
        float textY = rect.centerY() - textHeight / 2.f;

        paint.setColor(Color.WHITE);
        canvas.drawText(label, textX, textY, paint);
    }
}
