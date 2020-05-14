package com.example.translationgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Game {
    private Paint paint;
    private int backgroundColor;
    private Keyboard keyboard;
    private InputForm inputForm;

    public Game(Resources resources, float scrWidth, float scrHeight) {
        paint = new Paint();
        backgroundColor = Color.rgb(86, 81, 128);
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

        float inputFormX = scrWidth * 0.8f;
        float inputFormY = scrHeight * 0.15f;

        Word testWord = new Word("Das Auto");

        inputForm = new InputForm(testWord, button, dash, inputFormX, inputFormY, buttonWidth, buttonHeight);
    }

    public void update(float deltaTime) {
        keyboard.update(deltaTime);

    }

    public void draw(Canvas canvas) {
        paint.setColor(backgroundColor);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
        paint.setColor(Color.WHITE);
        keyboard.draw(canvas, paint);
        inputForm.draw(canvas, paint);
    }
}
