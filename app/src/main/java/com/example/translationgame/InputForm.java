package com.example.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class InputForm {
    public static final int BUTTONS_PER_WIDTH = 6;
    public static final int BUTTONS_PER_HEIGHT = 3;

    private Word word;
    private List<InputFormLine> lines;

    public InputForm(Word word, Bitmap button, Bitmap dash, float x, float y, float buttonWidth, float buttonHeight) {
        this.word = word;

        float maxWidth = BUTTONS_PER_WIDTH * buttonWidth;
        List<Pair<String, Boolean>> components = word.getComponents();

        lines = new ArrayList<>(components.size());

        for(Pair<String, Boolean> component : components) {
            String str = component.first;
            boolean endingWithDash = component.second;
            float startX = x + (maxWidth - str.length() * buttonWidth) / 2;

            lines.add(new InputFormLine(str, endingWithDash, button, dash, startX, y, buttonWidth, buttonHeight));

            y += buttonHeight;
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
