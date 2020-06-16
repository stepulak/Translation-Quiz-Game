package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Label extends UIElement {
    private String label;
    private float topLeftX;
    private float topLeftY;
    private float fontSize;

    public Label(String label, float topLeftX, float topLeftY, float fontSize) {
        this.label = label;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.fontSize = fontSize;
    }

    @Override
    void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(fontSize);
        paint.setColor(MyColors.LABEL_COLOR);
        canvas.drawText(label, topLeftX, topLeftY - paint.ascent(), paint);
    }
}
