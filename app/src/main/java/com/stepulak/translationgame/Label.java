package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Label extends UIElement {
    private final float LABEL_MAX_FONT_SIZE = 400.f;
    private final float LABEL_MIN_FONT_SIZE = 10.f;

    private String label;
    private RectF body;
    private float fontSize;
    private float textOffsetX;
    private float textOffsetY;

    public Label(String label, Paint paint, RectF body) {
        this.label = label;
        this.body = body;
        setupProportions(paint, body.width(), body.height());
    }

    public void draw(Canvas canvas, Paint paint) {
        float textX = body.centerX() + textOffsetX;
        float textY = body.centerY() + textOffsetY;
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
