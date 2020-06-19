package com.stepulak.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Keyboard extends ClickableElement {
    private enum AnimationType {
        BUTTON_LABELS_FADE_IN,
        BUTTON_LABELS_FADE_OUT
    }

    private static final int BUTTONS_PER_WIDTH = 6;
    private static final int BUTTONS_PER_HEIGHT = 4;
    private static final float ANIMATION_EXPIRE_TIME = 0.5f;

    private Button[][] buttons;
    private boolean labelsDestroyed;
    private Character lastClickedCharacter;
    private TimerAnimation<AnimationType> animation;

    public Keyboard(Bitmap button, RectF position) {
        buttons = new Button[BUTTONS_PER_WIDTH][BUTTONS_PER_HEIGHT];

        float buttonWidth = position.width() / BUTTONS_PER_WIDTH;
        float buttonHeight = position.height() / BUTTONS_PER_HEIGHT;

        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                float x = position.left + i * buttonWidth;
                float y = position.top + j * buttonHeight;
                RectF buttonBody = new RectF(x, y, x + buttonWidth, y + buttonHeight);
                buttons[i][j] = new Button(button, buttonBody);
            }
        }
    }

    public void generateButtonLabels(Translation translation, String alphabet) {
        // Generate labels for buttons
        ArrayList<Character> chars = new ArrayList<>();
        for (Character c : translation.getTranslatedWordWithoutFormatting().toCharArray()) {
            chars.add(c);
        }

        // Fill the rest with random stuff
        int totalChars = BUTTONS_PER_WIDTH * BUTTONS_PER_HEIGHT;
        Random random = new Random();
        while (chars.size() < totalChars) {
            chars.add(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        // Shuffle it and fill buttons labels
        Collections.shuffle(chars);
        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j].setCharacter(chars.get(i * BUTTONS_PER_HEIGHT + j));
            }
        }

        // Prepare animation
        animation = new TimerAnimation<>(AnimationType.BUTTON_LABELS_FADE_IN, ANIMATION_EXPIRE_TIME);
    }

    public void destroyButtonLabels() {
        animation = new TimerAnimation<>(AnimationType.BUTTON_LABELS_FADE_OUT, ANIMATION_EXPIRE_TIME,
                new ParameterCallback<TimerAnimation>() {
                    @Override
                    void apply(TimerAnimation animationTimer) {
                        labelsDestroyed = true;
                    }
                });
    }

    public boolean areLabelsDestroyed() {
        return labelsDestroyed;
    }

    public Character getLastClickedCharacter() {
        Character c = lastClickedCharacter;
        lastClickedCharacter = null;
        return c;
    }

    @Override
    public boolean click(float x, float y) {
        // Disable click during animation
        if (animation != null) {
            return false;
        }
        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                if (buttons[i][j].click(x, y)) {
                    lastClickedCharacter = buttons[i][j].getCharacter();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        if (animation != null) {
            animation.update(deltaTime);
            if (animation.expired()) {
                animation = null;
            }
        }
        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j].update(deltaTime);
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float alpha = getAlpha();
        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j].draw(canvas, paint, alpha);
            }
        }
    }

    private float getAlpha() {
        if (animation == null) {
            return 1.f;
        }
        if (animation.getAnimationType() == AnimationType.BUTTON_LABELS_FADE_IN) {
            return animation.getProcessRatio();
        }
        // AnimationType.BUTTON_LABELS_FADE_OUT
        return 1.f - animation.getProcessRatio();
    }
}
