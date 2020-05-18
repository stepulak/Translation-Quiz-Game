package com.example.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;


public class Button {
    private enum AnimationType {
        NONE,
        CLICK,
    }

    private static final float SIZE_RATIO = 0.96f;
    private static final float CLICK_ANIMATION_TIME = 0.1f;

    private Bitmap bitmap;
    private RectF rect;
    // Use String, not Character for single letter text because of canvas.drawText
    private String text;
    private AnimationType animationType = AnimationType.NONE;
    private float animationTimer = 0f;

    public Button(Bitmap b, float x, float y, float width, float height) {
        bitmap = b;

        float newWidth = width * SIZE_RATIO;
        float newHeight = height * SIZE_RATIO;
        float offsetX = (width - newWidth) / 2;
        float offsetY = (height - newHeight) / 2;

        rect = new RectF(x + offsetX, y + offsetY, x + offsetX + newWidth, y + offsetY + newHeight);
    }

    public void setCharacter(Character c) {
        text = c != null ? Character.toString(c) : null;
    }

    public Character getCharacter() {
        return text != null && text.length() > 0 ? text.charAt(0) : null;
    }

    public void update(float deltaTime) {
        if (animationType == AnimationType.CLICK) {
            animationTimer -= deltaTime;
            if (animationTimer <= 0f) {
                animationTimer = 0f;
                animationType = AnimationType.NONE;
            }
        }
    }

    public boolean click(float x, float y) {
        if(!rect.contains(x, y)) {
            return false;
        }
        animationType = AnimationType.CLICK;
        animationTimer = CLICK_ANIMATION_TIME;
        return true;
    }

    public void draw(Canvas canvas, Paint paint) {
        // TODO MOVE COLORS TO SEPARATE CLASS
        paint.setColor(Game.BUTTON_COLOR);
        if (animationType == AnimationType.CLICK) {
            paint.setAlpha(55 + (int) (200 * animationTimer/CLICK_ANIMATION_TIME));
        }
        canvas.drawBitmap(bitmap, null, rect, paint);
        paint.setAlpha(255);

        if (text != null) {
            paint.setTextSize(50);

            float textX = rect.centerX() - (paint.measureText(text) / 2);
            float textY = rect.centerY() - (paint.descent() + paint.ascent() / 2);

            paint.setColor(Game.BUTTON_TEXT_COLOR);
            canvas.drawText(text, textX, textY, paint);
        }
    }
}
