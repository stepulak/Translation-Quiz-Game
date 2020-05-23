package com.example.translationgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Label {
    private final float LABEL_MAX_FONT_SIZE = 400.f;
    private final float LABEL_MIN_FONT_SIZE = 10.f;

    private String label;
    private RectF rect;
    private float fontSize;
    private float textOffsetX;
    private float textOffsetY;

    public Label(String lbl, Paint paint, float x, float y, float width, float height) {
        label = lbl;
        rect = new RectF(x, y, x + width, y + height);

        setupProportions(paint, width, height);
    }

    public void draw(Canvas canvas, Paint paint) {
        float textX = rect.centerX() + textOffsetX;
        float textY = rect.centerY() + textOffsetY;
        paint.setTextSize(fontSize);
        paint.setColor(MyColors.LABEL_COLOR);
        canvas.drawText(label, textX, textY, paint);
    }

    private void setupProportions(Paint paint, float width, float height) {
        // Find out good fontSize, textOffsetX, textOffsetY
        for (fontSize = LABEL_MIN_FONT_SIZE; fontSize < LABEL_MAX_FONT_SIZE; fontSize++) {
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
}
