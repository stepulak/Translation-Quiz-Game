package com.example.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class InputForm {
    public static final float WIDTH_IN_BUTTONS = 6f;
    public static final float HEIGHT_IN_BUTTONS = 4.6f;
    private static final float SPACE_HEIGHT_IN_BUTTONS = 0.2f;

    private Word word;
    private InputFormLine[] lines;

    public InputForm(Word word, Bitmap button, Bitmap dash, float x, float y, float buttonWidth, float buttonHeight) {
        this.word = word;

        List<Pair<String, Boolean>> components = word.getComponents();

        float maxWidth = WIDTH_IN_BUTTONS * buttonWidth;
        float maxHeight = HEIGHT_IN_BUTTONS * buttonHeight;
        float actualHeight = 0f;

        for(Pair<String, Boolean> c : components) {
            actualHeight += buttonHeight;
            if (!c.second) {
                actualHeight += SPACE_HEIGHT_IN_BUTTONS * buttonHeight;
            }
        }
        y += (maxHeight - actualHeight) / 2;

        lines = new InputFormLine[components.size()];

        for(int i = 0; i < components.size(); i++) {
            Pair<String, Boolean> c = components.get(i);
            float startX = x + (maxWidth - c.first.length() * buttonWidth) / 2;

            lines[i] = new InputFormLine(c.first, c.second, button, dash, startX, y, buttonWidth, buttonHeight);
            y += buttonHeight;

            if (!c.second) {
                y += SPACE_HEIGHT_IN_BUTTONS * buttonHeight;
            }
        }
    }

    public boolean isAnimating() {
        for (InputFormLine line : lines) {
            if (line.isAnimating()) {
                return true;
            }
        }
        return false;
    }

    public void startEnterAnimation(float scrWidth) {
        float acceleration = InputFormLine.ACCELERATION_BASE;

        for (InputFormLine line : lines) {
            line.startEnterAnimation(-scrWidth, acceleration);
            acceleration += InputFormLine.ACCELERATION_INCREASE;
        }
    }

    public void startExitAnimation(float scrWidth) {
        float acceleration = InputFormLine.ACCELERATION_BASE;

        for (InputFormLine line : lines) {
            line.startExitAnimation(scrWidth, acceleration);
            acceleration += InputFormLine.ACCELERATION_INCREASE;
        }
    }

    public void insertCharacter(char c) {
        for (InputFormLine line : lines) {
            if (line.insertCharacter(c)) {
                return;
            }
        }
    }

    public boolean click(float x, float y) {
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
    }

    public void draw(Canvas canvas, Paint paint) {
        for (InputFormLine line : lines) {
            line.draw(canvas, paint);
        }
    }
}
