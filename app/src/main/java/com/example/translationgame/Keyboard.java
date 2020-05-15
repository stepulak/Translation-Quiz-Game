package com.example.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

class Keyboard {
    public static final int BUTTONS_PER_WIDTH = 6;
    public static final int BUTTONS_PER_HEIGHT = 6;

    private Button[][] buttons;

    public Keyboard(Bitmap button, float x, float y, float buttonWidth, float buttonHeight) {
        buttons = new Button[BUTTONS_PER_WIDTH][BUTTONS_PER_HEIGHT];

        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j] = new Button(button, x + i * buttonWidth, y + j * buttonHeight, buttonWidth, buttonHeight);
            }
        }
    }

    public Character click(float x, float y) {
        return null;
    }

    public void update(float deltaTime) {
        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j].update(deltaTime);
            }
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j].draw(canvas, paint);
            }
        }
    }
}
