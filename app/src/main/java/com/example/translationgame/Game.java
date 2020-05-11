package com.example.translationgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class Game {
    private Paint paint;
    private int backgroundColor;
    private Keyboard keyboard;

    Game(Resources resources, float scrWidth, float scrHeight) {
        paint = new Paint();
        backgroundColor = Color.rgb(86, 81, 128);

        float keyboardWidth = scrWidth * 0.8f;
        float keyboardHeight = scrHeight * 0.45f;
        float keyboardX = (scrWidth - keyboardWidth) / 2f;
        float keyboardY = scrHeight - keyboardHeight - scrHeight * 0.1125f;

        Bitmap button = BitmapFactory.decodeResource(resources, R.drawable.button);

        keyboard = new Keyboard(button, keyboardX, keyboardY, keyboardWidth, keyboardHeight);
    }

    public void update(float deltaTime) {

    }

    public void draw(Canvas canvas) {
        paint.setColor(backgroundColor);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
        paint.setColor(Color.WHITE);
        keyboard.draw(canvas, paint);
    }
}
