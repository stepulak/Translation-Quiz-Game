package com.example.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InputForm {
    private enum AnimationStatus {
        IDLE,
        ENTERING,
        EXITING
    };

    private class FormLine {
        private boolean drawDash;
        private float offsetX;
        private Button[] buttons;

        FormLine(String word, boolean endingWithDash, Bitmap button, float x, float y, float buttonWidth, float buttonHeight) {
            drawDash = endingWithDash;
            offsetX = 0;
            buttons = new Button[word.length()];

            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new Button(button, x + i * buttonWidth, y, buttonWidth, buttonHeight);
            }
        }

        public boolean click(float x, float y) {
            return false;
        }

        public void update(float deltaTime) {

        }

        public void draw(Canvas canvas, Paint paint, Bitmap dash) {
            //canvas.save();
            //canvas.translate(offsetX, 0);
            for (Button button : buttons) {
                button.draw(canvas, paint);
            }
           // canvas.restore();
        }
    };

    public static int BUTTONS_PER_WIDTH = 6;
    public static int BUTTONS_PER_HEIGHT = 3;

    private AnimationStatus animationStatus;
    private Word word;
    private Bitmap dash;
    private List<FormLine> formLines;

    public InputForm(Word word, Bitmap button, Bitmap dash, float x, float y, float buttonWidth, float buttonHeight) {
        this.word = word;
        this.dash = dash;

        float maxWidth = BUTTONS_PER_WIDTH * buttonWidth;
        List<Pair<String, Boolean>> components = word.getComponents();

        formLines = new ArrayList<>(components.size());

        for(Pair<String, Boolean> component : components) {
            String str = component.first;
            boolean endingWithDash = component.second;
            float startX = x + (maxWidth - str.length() * buttonWidth) / 2;

            formLines.add(new FormLine(str, endingWithDash, button, startX, y, buttonWidth, buttonHeight));

            y += buttonHeight;
        }
    }

    public void startEnterAnimation() {

    }

    public void startExitAnimation() {

    }

    public void update(float deltaTime) {

    }

    public void draw(Canvas canvas, Paint paint) {
        for (FormLine formLine : formLines) {
            formLine.draw(canvas, paint, dash);
        }
    }
}
