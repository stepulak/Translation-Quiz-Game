package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Delimiter extends UIElement {
    private RectF area;

    public Delimiter(RectF area) {
        this.area = area;
    }

    @Override
    void draw(Canvas canvas, Paint paint) {
        paint.setColor(MyColors.DELIMITER_COLOR);
        canvas.drawRect(area, paint);
    }
}
