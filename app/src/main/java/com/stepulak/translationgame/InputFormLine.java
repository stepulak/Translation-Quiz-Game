package com.stepulak.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class InputFormLine extends UIElement {
    private enum AnimationType {
        ENTER_ANIMATION,
        EXIT_ANIMATION
    }

    private static final float ANIMATION_VELOCITY_BASE = 100.f;
    private static final float ANIMATION_ACCELERATION_BASE = 7000.f;
    private static final float ANIMATION_ACCELERATION_INCREASE = 5000.f;

    private Button[] buttons;
    private Dash dash;
    private TranslationAnimation<AnimationType> animation;
    private boolean hideButtons;

    InputFormLine(String word, boolean endingWithDash, Bitmap buttonBitmap, Bitmap dashBitmap,
                  float x, float y, float buttonWidth, float buttonHeight) {
        buttons = new Button[word.length()];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(buttonBitmap, new RectF(x, y, x + buttonWidth, y + buttonHeight));
            x += buttonWidth;
        }
        if (endingWithDash) {
            dash = new Dash(dashBitmap, x, y, buttonWidth / 2.5f, buttonHeight);
        }
    }

    public boolean insertCharacter(char character) {
        // Insert new character at first free space
        for (Button button : buttons) {
            if (button.getCharacter() == null) {
                button.setCharacter(character);
                return true;
            }
        }
        return false;
    }

    public void clearCharacters() {
        for (Button button : buttons) {
            button.setCharacter(null);
        }
    }

    public boolean isFilledWithCharacters() {
        for (Button button : buttons) {
            if (button.getCharacter() == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isAnimating() {
        return animation != null;
    }

    public void startEnterAnimation(float screenWidth, int rowIndex) {
        float acceleration = ANIMATION_ACCELERATION_BASE + rowIndex * ANIMATION_ACCELERATION_INCREASE;
        animation = new TranslationAnimation<>(
                AnimationType.ENTER_ANIMATION, -screenWidth, 0.f, ANIMATION_VELOCITY_BASE, acceleration);
    }

    public void startExitAnimation(float screenWidth, int rowIndex) {
        float acceleration = ANIMATION_ACCELERATION_BASE + rowIndex * ANIMATION_ACCELERATION_INCREASE;
        animation = new TranslationAnimation(
                AnimationType.EXIT_ANIMATION, 0.f, screenWidth, ANIMATION_VELOCITY_BASE, acceleration);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Button button : buttons) {
            if (button.getCharacter() == null) {
                break;
            }
            builder.append(button.getCharacter());
        }
        return builder.toString();
    }

    @Override
    public boolean click(float x, float y) {
        for (Button button : buttons) {
            if (button.click(x, y)) {
                button.setCharacter(null);
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        if (animation != null) {
            animation.update(deltaTime);
            if (animation.expired()) {
                if (animation.getAnimationType() == AnimationType.EXIT_ANIMATION) {
                    hideButtons = true;
                }
                animation = null;
            }
        }
        for (Button button : buttons) {
            button.update(deltaTime);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        if (animation != null) {
            canvas.translate(animation.getCurrentPosition(), 0.f);
        }
        if (!hideButtons) {
            for (Button button : buttons) {
                button.draw(canvas, paint);
            }
            if (dash != null) {
                dash.draw(canvas, paint);
            }
        }
        canvas.restore();
    }
}