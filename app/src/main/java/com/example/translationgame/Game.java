package com.example.translationgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Game {
    // TODO MOVE COLORS TO SEPARATE CLASS
    // TODO CHECK FINAL STATIC
    public static final int BACKGROUNDC_COLOR = Color.rgb(86, 81, 128);
    public static final int BUTTON_COLOR = Color.WHITE;
    public static final int BUTTON_TEXT_COLOR = BACKGROUNDC_COLOR;

    private Paint paint;
    private Keyboard keyboard;
    private InputForm inputForm;

    public Game(Resources resources, float scrWidth, float scrHeight) {
        paint = new Paint();
        createUI(resources, scrWidth, scrHeight);
    }

    private void createUI(Resources resources, float scrWidth, float scrHeight) {
        Bitmap button = BitmapFactory.decodeResource(resources, R.drawable.button);
        Bitmap dash = BitmapFactory.decodeResource(resources, R.drawable.dash);

        float keyboardWidth = scrWidth * 0.8f;
        float keyboardHeight = scrHeight * 0.45f;
        float keyboardX = (scrWidth - keyboardWidth) / 2f;
        float keyboardY = scrHeight - keyboardHeight - scrHeight * 0.0562f;

        float buttonWidth = keyboardWidth / Keyboard.BUTTONS_PER_WIDTH;
        float buttonHeight = keyboardHeight / Keyboard.BUTTONS_PER_HEIGHT;

        keyboard = new Keyboard(button, keyboardX, keyboardY, buttonWidth, buttonHeight);

        float inputFormWidth = scrWidth * 0.8f;
        float inputFormX = (scrWidth - inputFormWidth) / 2f;
        float inputFormY = scrHeight * 0.15f;

        Word testWord = new Word("Das Volks~wagen");

        inputForm = new InputForm(testWord, button, dash, inputFormX, inputFormY, buttonWidth, buttonHeight);
        inputForm.startEnterAnimation(scrWidth);
    }

    public void update(float deltaTime) {
        keyboard.update(deltaTime);
        inputForm.update(deltaTime);
        if (!inputForm.isAnimating()) {
            inputForm.startExitAnimation(1000);
        }
    }

    public void draw(Canvas canvas) {
        paint.setColor(BACKGROUNDC_COLOR);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
        keyboard.draw(canvas, paint);
        inputForm.draw(canvas, paint);
    }
}
