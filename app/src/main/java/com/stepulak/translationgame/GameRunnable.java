package com.stepulak.translationgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Vibrator;

public abstract class GameRunnable {
    private Paint paint = new Paint();
    private Context context;
    private float screenWidth;
    private float screenHeight;

    public GameRunnable(Context context, float screenWidth, float screenHeight) {
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public abstract GameRunnable createNextGameRunnable();
    public abstract GameRunnable createPreviousGameRunnable();
    public abstract boolean moveToPreviousGameRunnable();
    public abstract boolean moveToNextGameRunnable();
    public abstract void click(float x, float y);
    public abstract void update(float deltaTime);
    public abstract void draw(Canvas canvas);

    public void motionTouch(float touchOrigX, float touchOrigY, float motionX, float motionY) {
    }

    protected float getScreenWidth() {
        return screenWidth;
    }

    protected float getScreenHeight() {
        return screenHeight;
    }

    protected Context getContext() {
        return context;
    }

    protected Paint getPaint() {
        return paint;
    }

    protected void vibrate(float time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate((int)time);
    }
}
