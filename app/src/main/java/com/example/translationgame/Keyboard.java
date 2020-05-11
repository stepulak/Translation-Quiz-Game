package com.example.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

class Keyboard {
    private static int BUTTONS_PER_WIDTH = 6;
    private static int BUTTONS_PER_HEIGHT = 6;
    private float buttonWidth;
    private float buttonHeight;

    private Button[][] buttons;

    public Keyboard(Bitmap button, float x, float y, float width, float height) {
        buttonWidth = width / BUTTONS_PER_WIDTH;
        buttonHeight = height / BUTTONS_PER_HEIGHT;
        float buttonOffsetW = buttonWidth * 0.04f;
        float buttonOffsetH = buttonHeight * 0.04f;

        buttons = new Button[BUTTONS_PER_WIDTH][BUTTONS_PER_HEIGHT];

        for (int i = 0; i < BUTTONS_PER_WIDTH; i++) {
            for (int j = 0; j < BUTTONS_PER_HEIGHT; j++) {
                buttons[i][j] = new Button(
                        button,
                        x + i * buttonWidth + buttonOffsetW,
                        y + j * buttonHeight + buttonOffsetW,
                        buttonWidth - 2 * buttonOffsetW,
                        buttonHeight - 2 * buttonOffsetH);
            }
        }
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
