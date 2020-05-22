package com.example.translationgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Label {
    private String label;
    private RectF rect;
    private float fontSize;
    private float textOffsetX;
    private float textOffsetY;

    public Label(String lbl, Paint paint, float x, float y, float width, float height) {
        label = lbl;
        rect = new RectF(x, y, x + width, y + height);

        for (fontSize = 10.f; fontSize < 400f; fontSize++) {
            paint.setTextSize(fontSize);
            float textWidth = paint.measureText(label);
            float textHeight = paint.descent() - paint.ascent();
            if (textWidth >= width || textHeight >= height) {
                textOffsetX = -.5f * textWidth;
                textOffsetY = -.5f * (paint.descent() + paint.ascent());
                break;
            }
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(fontSize);

        float textX = rect.centerX() + textOffsetX;
        float textY = rect.centerY() + textOffsetY;

        paint.setColor(Color.WHITE);
        canvas.drawText(label, textX, textY, paint);
    }
}
