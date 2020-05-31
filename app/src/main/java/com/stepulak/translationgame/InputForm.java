package com.stepulak.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Pair;

import java.util.List;

public class InputForm extends ClickableElement {
    private static final float WIDTH_IN_BUTTONS = 7f;
    private static final float HEIGHT_IN_BUTTONS = 4.6f;
    private static final float SPACE_HEIGHT_IN_BUTTONS = 0.2f;
    private static final float SHAKE_ROTATION_VELOCITY = 360.f;
    private static final float SHAKE_ROTATION_TURN_TIME = 0.1f;
    private static final int SHAKE_NUM_ROTATIONS = 5;

    private RectF body;
    private String translatedWord;
    private InputFormLine[] lines;
    private RotationAnimation shakeRotationAnimation;
    private boolean wordSkipped;

    public InputForm(Translation translation, Bitmap button, Bitmap dash, RectF body) {
        this.body = body;
        this.translatedWord = translation.getTranslatedWordWithoutFormatting();
        setup(translation, button, dash);
    }

    public boolean isAnimating() {
        if (shakeRotationAnimation != null) {
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
        shakeRotationAnimation = new RotationAnimation(SHAKE_ROTATION_VELOCITY, SHAKE_ROTATION_TURN_TIME, SHAKE_NUM_ROTATIONS);
    }

    public boolean isWordSkipped() {
        return wordSkipped;
    }

    public boolean isFilledWithCharacters() {
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

    public void insertCharacter(char c) {
        for (InputFormLine line : lines) {
            if (line.insertCharacter(c)) {
                return;
            }
        }
    }

    public void skipWordAndFillResult() {
        clear();
        for (char c : translatedWord.toCharArray()) {
            insertCharacter(c);
        }
        wordSkipped = true;
    }

    public void clear() {
        if (isAnimating() || isWordSkipped()) {
            return;
        }
        for (InputFormLine line : lines) {
            line.clearCharacters();
        }
    }

    @Override
    public boolean click(float x, float y) {
        if (isAnimating() || isWordSkipped()) {
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
        if (shakeRotationAnimation != null) {
            shakeRotationAnimation.update(deltaTime);
            if (shakeRotationAnimation.expired()) {
                shakeRotationAnimation = null;
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        if (shakeRotationAnimation != null) {
            canvas.rotate(shakeRotationAnimation.getRotationAngle(), body.centerX(), body.centerY());
        }
        for (InputFormLine line : lines) {
            line.draw(canvas, paint);
        }
        canvas.restore();
    }

    private static float getHeight(List<Pair<String, Boolean>> wordComponents, float buttonHeight) {
        float actualHeight = 0.f;
        for(Pair<String, Boolean> c : wordComponents) {
            actualHeight += buttonHeight;
            if (!c.second) {
                actualHeight += SPACE_HEIGHT_IN_BUTTONS * buttonHeight;
            }
        }
        return actualHeight;
    }

    private void setup(Translation translation, Bitmap button, Bitmap dash) {
        List<Pair<String, Boolean>> components = Translation.decomposeTranslatedString(translation.getTranslatedWord());
        lines = new InputFormLine[components.size()];

        float maxWidth = body.width();
        float maxHeight = body.height();
        float buttonWidth = maxWidth / WIDTH_IN_BUTTONS;
        float buttonHeight = maxHeight / HEIGHT_IN_BUTTONS;
        float y = body.top + (maxHeight - getHeight(components, buttonHeight)) / 2;

        for(int i = 0; i < components.size(); i++) {
            Pair<String, Boolean> component = components.get(i);
            String str = component.first;
            boolean endWithDash = component.second;
            float x = body.left + (maxWidth - str.length() * buttonWidth) / 2;

            lines[i] = new InputFormLine(str, endWithDash, button, dash, x, y, buttonWidth, buttonHeight);

            if (!endWithDash) {
                y += SPACE_HEIGHT_IN_BUTTONS * buttonHeight;
            }
            y += buttonHeight;
        }
    }
}
