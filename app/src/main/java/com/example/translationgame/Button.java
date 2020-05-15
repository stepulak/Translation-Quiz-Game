package com.example.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;

public class Button {
    private static final float SIZE_RATIO = 0.96f;

    private Bitmap bitmap;
    private String text;
    private RectF rect;

    public Button(Bitmap b, float x, float y, float width, float height) {
        bitmap = b;
        text = "X";

        float newWidth = width * SIZE_RATIO;
        float newHeight = height * SIZE_RATIO;
        float offsetX = (width - newWidth) / 2;
        float offsetY = (height - newHeight) / 2;

        rect = new RectF(x + offsetX, y + offsetY, x + offsetX + newWidth, y + offsetY + newHeight);
    }

    public float getX() {
        return rect.left;
    }

    public float getY() {
        return rect.top;
    }

    public float getWidth() {
        return rect.right - rect.left;
    }

    public float getHeight() {
        return rect.bottom - rect.top;
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
        // TODO MOVE COLORS TO SEPARATE CLASS
        paint.setColor(Game.BUTTON_COLOR);
        canvas.drawBitmap(bitmap, null, rect, paint);

        paint.setTextSize(50);

        float textX = rect.centerX() - (paint.measureText(text) / 2);
        float textY = rect.centerY() - (paint.descent() + paint.ascent() / 2);

        paint.setColor(Game.BUTTON_TEXT_COLOR);
        canvas.drawText(text, textX, textY, paint);
    }
}
