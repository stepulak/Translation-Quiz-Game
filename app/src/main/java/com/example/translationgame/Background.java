package com.example.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Background {
    private RectF rect;

    public Background(float screenWidth, float screenHeight) {
        rect = new RectF(0.f, 0.f, screenWidth, screenHeight);
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(MyColors.BACKGROUND_COLOR);
        canvas.drawRect(rect, paint);
    }
}
