package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public class StatsElement extends UIElement {
    private String counter;
    private String label;
    private float x;
    private float y;
    private float counterTextSize;
    private float labelTextSize;

    StatsElement(float x, float y, float counterTextSize, float labelTextSize) {
        this.x = x;
        this.y = y;
        this.counterTextSize = counterTextSize;
        this.labelTextSize = labelTextSize;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(counterTextSize);
        canvas.drawText(counter, x, y + counterTextSize, paint);
        float counterTextWidth = paint.measureText(counter);
        paint.setTextSize(labelTextSize);
        canvas.drawText(label, x + counterTextWidth, y + counterTextSize, paint);
    }
}
