package com.stepulak.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Pair;

import java.util.List;

public class InputForm extends UIElement {
    public static final float WIDTH_IN_BUTTONS = 6f;
    public static final float HEIGHT_IN_BUTTONS = 4.6f;

    private static final float SPACE_HEIGHT_IN_BUTTONS = 0.2f;
    private static final float SHAKE_ROTATION_VELOCITY = 360.f;
    private static final float SHAKE_ROTATION_TURN_TIME = 0.1f;
    private static final int SHAKE_ROTATIONS = 5;

    private String translatedWord;
    private InputFormLine[] lines;
    private RectF body;

    private boolean shakeRotationEnabled;
    private int shakeRotations;
    private float shakeRotationTimer;
    private float shakeRotationDirection;
    private float shakeRotationAngle;


    public InputForm(Translation translation, Bitmap button, Bitmap dash, RectF body) {
        translatedWord = translation.getTranslatedWordWithoutFormatting();
        this.body = body;

        Word translatedWord = new Word(translation.getTranslatedWord());
        List<Pair<String, Boolean>> components = translatedWord.getComponents();

        float maxWidth = body.width();
        float maxHeight = body.height();
        float buttonWidth = maxWidth / WIDTH_IN_BUTTONS;
        float buttonHeight = maxHeight / HEIGHT_IN_BUTTONS;

        float y = body.top + (maxHeight - getHeight(translatedWord, buttonHeight)) / 2;

        lines = new InputFormLine[components.size()];

        for(int i = 0; i < components.size(); i++) {
            Pair<String, Boolean> c = components.get(i);
            float x = body.left + (maxWidth - c.first.length() * buttonWidth) / 2;

            lines[i] = new InputFormLine(c.first, c.second, button, dash, x, y, buttonWidth, buttonHeight);
            y += buttonHeight;

            if (!c.second) {
                y += SPACE_HEIGHT_IN_BUTTONS * buttonHeight;
            }
        }
    }

    public boolean isAnimating() {
        if (shakeRotationEnabled) {
            return true;
        }
        for (InputFormLine line : lines) {
            if (line.isAnimating()) {
                return true;
            }
        }
        return false;
    }

    public void startEnterAnimation(float scrWidth) {
        if (isAnimating()) {
            return;
        }
        float acceleration = InputFormLine.ACCELERATION_BASE;
        for (InputFormLine line : lines) {
            line.startEnterAnimation(-scrWidth, acceleration);
            acceleration += InputFormLine.ACCELERATION_INCREASE;
        }
    }

    public void startExitAnimation(float scrWidth) {
        if (isAnimating()) {
            return;
        }
        float acceleration = InputFormLine.ACCELERATION_BASE;
        for (InputFormLine line : lines) {
            line.startExitAnimation(scrWidth, acceleration);
            acceleration += InputFormLine.ACCELERATION_INCREASE;
        }
    }

    public void startShakeAnimation() {
        if (isAnimating()) {
            return;
        }
        shakeRotationEnabled = true;
        shakeRotationTimer = 0.f;
        shakeRotationAngle = 0.f;
        shakeRotationDirection = 1.f;
        shakeRotations = SHAKE_ROTATIONS;
    }

    public boolean isFilled() {
        for (InputFormLine line : lines) {
            if (!line.isFilled()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFilledCorrectly() {
        StringBuilder builder = new StringBuilder();
        for (InputFormLine line : lines) {
            builder.append(line.getStringContent());
        }
        return builder.toString().equals(translatedWord);
    }

    public void insertCharacter(char c) {
        for (InputFormLine line : lines) {
            if (line.insertCharacter(c)) {
                return;
            }
        }
    }

    public void clear() {
        for (InputFormLine line : lines) {
            line.clear();
        }
    }
    public boolean click(float x, float y) {
        if (isAnimating()) {
            return false;
        }
        for (InputFormLine line : lines) {
            if (line.removeCharacter(x, y)) {
                return true;
            }
        }
        return false;
    }

    public void update(float deltaTime) {
        for (InputFormLine line : lines) {
            line.update(deltaTime);
        }
        if (shakeRotationEnabled) {
            updateShakeRotation(deltaTime);
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        if (shakeRotationEnabled) {
            canvas.rotate(shakeRotationAngle, body.centerX(), body.centerY());
        }
        for (InputFormLine line : lines) {
            line.draw(canvas, paint);
        }
        canvas.restore();
    }

    private void updateShakeRotation(float deltaTime) {
        shakeRotationAngle += shakeRotationDirection * SHAKE_ROTATION_VELOCITY * deltaTime;
        shakeRotationTimer += deltaTime;
        float turnTime = (shakeRotations == 1 || shakeRotations == SHAKE_ROTATIONS) ? SHAKE_ROTATION_TURN_TIME / 2 : SHAKE_ROTATION_TURN_TIME;

        if (shakeRotationTimer > turnTime) {
            shakeRotationTimer = 0.f;
            shakeRotationDirection *= -1.f;
            shakeRotations--;
            if (shakeRotations <= 0) {
                shakeRotationEnabled = false;
            }
        }
    }

    private static float getHeight(Word word, float buttonHeight) {
        float actualHeight = 0.f;

        for(Pair<String, Boolean> c :  word.getComponents()) {
            actualHeight += buttonHeight;
            if (!c.second) {
                actualHeight += SPACE_HEIGHT_IN_BUTTONS * buttonHeight;
            }
        }

        return actualHeight;
    }
}
