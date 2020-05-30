package com.stepulak.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Pair;

import java.util.List;

public class InputForm extends UIElement {
    private static final float WIDTH_IN_BUTTONS = 7f;
    private static final float HEIGHT_IN_BUTTONS = 4.6f;
    private static final float SPACE_HEIGHT_IN_BUTTONS = 0.2f;
    private static final float SHAKE_ROTATION_VELOCITY = 360.f;
    private static final float SHAKE_ROTATION_TURN_TIME = 0.1f;
    private static final int SHAKE_ROTATIONS = 5;

    private RectF body;
    private String translatedWord;
    private InputFormLine[] lines;
    private boolean skipped;

    public InputForm(Translation translation, Bitmap button, Bitmap dash, RectF body) {
        this.body = body;
        this.translatedWord = translation.getTranslatedWordWithoutFormatting();

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

    public void startEnterAnimation(float screenWidth) {
        if (isAnimating()) {
            return;
        }
        for (int i = 0; i < lines.length; i++) {
            lines[i].startEnterAnimation(screenWidth, i);
        }
    }

    public void startExitAnimation(float screenWidth) {
        if (isAnimating()) {
            return;
        }
        for (int i = 0; i < lines.length; i++) {
            lines[i].startExitAnimation(screenWidth, i);
        }
    }

    public void startShakeAnimation() {
        if (isAnimating()) {
            return;
        }
        shakeAnimation = new ShakeAnimation();
    }

    public boolean isFilled() {
        for (InputFormLine line : lines) {
            if (!line.isFilledWithCharacters()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFilledCorrectly() {
        StringBuilder builder = new StringBuilder();
        for (InputFormLine line : lines) {
            builder.append(line.toString());
        }
        return builder.toString().equals(translatedWord);
    }

    public void fillWithCorrectWord() {
        clear();
        skipped = true;
        for (char c : translatedWord.toCharArray()) {
            insertCharacter(c);
        }
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void insertCharacter(char c) {
        for (InputFormLine line : lines) {
            if (line.insertCharacter(c)) {
                return;
            }
        }
    }

    public void clear() {
        if (isAnimating() || isSkipped()) {
            return;
        }
        for (InputFormLine line : lines) {
            line.clearCharacters();
        }
    }

    @Override
    public boolean click(float x, float y) {
        if (isAnimating()) {
            return false;
        }
        for (InputFormLine line : lines) {
            if (line.click(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        for (InputFormLine line : lines) {
            line.update(deltaTime);
        }
        if (shakeRotationEnabled) {
            updateShakeRotation(deltaTime);
        }
    }

    @Override
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
