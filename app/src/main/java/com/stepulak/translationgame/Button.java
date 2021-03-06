package com.stepulak.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.core.util.Consumer;


public class Button extends ClickableElement {
    private enum AnimationType {
        NONE,
        CLICK,
    }
    private static final float SIZE_RATIO = 0.9f;
    private static final float CLICK_ANIMATION_TIME = 0.1f;

    private Bitmap bitmap;
    private RectF body;
    // Use String, not Character for single letter text because of canvas.drawText
    private String text;
    private AnimationType animationType = AnimationType.NONE;
    private float animationTimer = 0.f;

    public Button(Bitmap b, RectF body) {
        bitmap = b;

        float oldWidth = body.width();
        float oldHeight = body.height();
        float newWidth = oldWidth * SIZE_RATIO;
        float newHeight = oldHeight * SIZE_RATIO;
        float offsetX = (oldWidth - newWidth) / 2.f;
        float offsetY = (oldHeight - newHeight) / 2.f;

        this.body = new RectF(
                body.left + offsetX,
                body.top + offsetY,
                body.left + offsetX + newWidth,
                body.top + offsetY + newHeight
        );
    }

    public void setCharacter(Character c) {
        text = c != null ? Character.toString(c) : null;
    }

    public Character getCharacter() {
        return text != null && text.length() > 0 ? text.charAt(0) : null;
    }

    @Override
    public boolean click(float x, float y) {
        if(!body.contains(x, y)) {
            return false;
        }
        animationType = AnimationType.CLICK;
        animationTimer = CLICK_ANIMATION_TIME;
        invokeClickCallback();
        return true;
    }

    @Override
    public void update(float deltaTime) {
        if (animationType == AnimationType.CLICK) {
            animationTimer -= deltaTime;
            if (animationTimer <= 0f) {
                animationTimer = 0f;
                animationType = AnimationType.NONE;
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        draw(canvas, paint, 1.f);
    }

    public void draw(Canvas canvas, Paint paint, float globalAlpha) {
        paint.setColor(MyColors.LABEL_COLOR);

        float localAlpha = (animationType == AnimationType.CLICK) ? 0.2f + 0.8f * animationTimer/CLICK_ANIMATION_TIME : 1.f;
        paint.setAlpha((int) (255 * globalAlpha * localAlpha));

        canvas.drawBitmap(bitmap, null, body, paint);
        paint.setAlpha(255);

        if (text != null) {
            paint.setTextSize(body.height() * 0.7f);

            float textX = body.centerX() - paint.measureText(text) / 2.f;
            float textY = body.centerY() - (paint.descent() + paint.ascent()) / 2.f;

            paint.setColor(MyColors.BUTTON_LABEL_COLOR);
            canvas.drawText(text, textX, textY, paint);
        }
    }
}
