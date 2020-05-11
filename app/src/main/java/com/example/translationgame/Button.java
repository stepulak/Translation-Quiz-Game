package com.example.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;

public class Button {
    private Bitmap bitmap;
    private RectF rect;
    private String text;

    public Button(Bitmap b, float x, float y, float width, float height) {
        bitmap = b;
        rect = new RectF(x, y, x + width, y + height);
        text = "?";
    }

    public void setText(String txt) {
        text = txt;
    }

    public void update(float deltaTime) {

    }

    public boolean click(float x, float y) {
        return rect.contains(x, y);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, null, rect, paint);

        float textX = rect.centerX() - (paint.measureText(text) / 2);
        float textY = rect.centerY() - (paint.descent() + paint.ascent() / 2);

        canvas.drawText(text, textX, textY, paint);
    }
}
