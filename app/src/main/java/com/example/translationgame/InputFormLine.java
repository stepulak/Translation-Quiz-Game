package com.example.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class InputFormLine {
    public static final float VELOCITY_BASE = 100.f;
    public static final float ACCELERATION_BASE = 7000.f;
    public static final float ACCELERATION_INCREASE = 5000.f;

    private enum AnimationStatus {
        IDLE,
        ENTERING,
        EXITING
    };

    private AnimationStatus animationStatus;
    private float offsetX;
    private float maxOffsetX;
    private float velocity;
    private float acceleration;

    private Button[] buttons;
    private Dash dash = null;

    InputFormLine(String word, boolean endingWithDash, Bitmap buttonBitmap, Bitmap dashBitmap, float x, float y, float buttonWidth, float buttonHeight) {
        offsetX = 0;
        buttons = new Button[word.length()];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(buttonBitmap, x, y, buttonWidth, buttonHeight);
            x += buttonWidth;
        }
        if (endingWithDash) {
            dash = new Dash(dashBitmap, x, y, buttonWidth / 2.5f, buttonHeight);
        }
    }

    public boolean isAnimating() {
        return animationStatus != AnimationStatus.IDLE;
    }

    public void startEnterAnimation(float offset, float acc) {
        offsetX = offset;
        acceleration = acc;
        maxOffsetX = 0;
        velocity = VELOCITY_BASE;
        animationStatus = AnimationStatus.ENTERING;
    }

    public void startExitAnimation(float maxOffset, float acc) {
        maxOffsetX = maxOffset;
        acceleration = acc;
        offsetX = 0;
        velocity = VELOCITY_BASE;
        animationStatus = AnimationStatus.EXITING;
    }

    public boolean click(float x, float y) {
        return false;
    }

    public void update(float deltaTime) {
        if (animationStatus != AnimationStatus.IDLE) {
            offsetX += velocity * deltaTime;
            velocity += acceleration * deltaTime;

            if (offsetX >= maxOffsetX) {
                offsetX = maxOffsetX;
                animationStatus = AnimationStatus.IDLE;
            }
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(offsetX, 0);
        for (Button button : buttons) {
            button.draw(canvas, paint);
        }
        if (dash != null) {
            paint.setColor(Game.BUTTON_COLOR);
            dash.draw(canvas, paint);
        }
        canvas.restore();
    }
}