package com.stepulak.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Keyboard {
    private enum AnimationType {
        NONE,
        BUTTON_LABELS_FADE_IN,
        BUTTON_LABELS_FADE_OUT
    }

    public static final int BUTTONS_PER_WIDTH = 7;
    public static final int BUTTONS_PER_HEIGHT = 4;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜß";
    private static final float ANIMATION_TIME = 0.3f;

    private Button[][] buttons;
    private AnimationType animationType = AnimationType.NONE;
    private float animationTimer;
    private boolean labelsDestroyed;

    public Keyboard(Bitmap button, RectF position) {
        buttons = new Button[BUTTONS_PER_WIDTH][BUTTONS_PER_HEIGHT];

        float buttonWidth = position.width() / BUTTONS_PER_WIDTH;
        float buttonHeight = position.height() / BUTTONS_PER_HEIGHT;

        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j] = new Button(button,
                        position.left + i * buttonWidth,
                        position.right + j * buttonHeight,
                        buttonWidth,
                        buttonHeight
                );
            }
        }
    }

    public void generateButtonLabels(Dictionary.Translation translation) {
        animationType = AnimationType.BUTTON_LABELS_FADE_IN;
        animationTimer = 0.f;

        // Generate labels for buttons
        ArrayList<Character> chars = new ArrayList<>();
        for (Character c : translation.getTranslatedWordWithoutFormatting().toCharArray()) {
            chars.add(c);
        }

        // Fill the rest with random stuff
        int totalChars = BUTTONS_PER_WIDTH * BUTTONS_PER_HEIGHT;
        Random random = new Random();

        while (chars.size() < totalChars) {
            chars.add(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        // Shuffle it and fill buttons labels
        Collections.shuffle(chars);

        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j].setCharacter(chars.get(i * BUTTONS_PER_HEIGHT + j));
            }
        }
    }

    public void destroyButtonLabels() {
        animationType = AnimationType.BUTTON_LABELS_FADE_OUT;
        animationTimer = 0.f;
    }

    public boolean areLabelsDestroyed() {
        return labelsDestroyed;
    }

    public Character click(float x, float y) {
        if (animationType != AnimationType.NONE) {
            return null;
        }
        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                if (buttons[i][j].click(x, y)) {
                    return buttons[i][j].getCharacter();
                }
            }
        }
        return null;
    }

    public void update(float deltaTime) {
        if (animationType != AnimationType.NONE) {
            animationTimer += deltaTime;
            if (animationTimer >= ANIMATION_TIME) {
                if (animationType == AnimationType.BUTTON_LABELS_FADE_OUT) {
                    labelsDestroyed = true;
                }
                animationType = AnimationType.NONE;
            }
        }

        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j].update(deltaTime);
            }
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        float alpha = getAlpha();

        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j].draw(canvas, paint, alpha);
            }
        }
    }

    private float getAlpha() {
        if (animationType == AnimationType.NONE) {
            return 1.f;
        }
        if (animationType == AnimationType.BUTTON_LABELS_FADE_IN) {
            return animationTimer / ANIMATION_TIME;
        }
        // AnimationType.BUTTON_LABELS_FADE_OUT
        return 1.f - animationTimer / ANIMATION_TIME;
    }
}