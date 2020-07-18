package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Timer extends UIElement {
    public static final int DEFAULT_TIME_SECONDS = 40;
    public static final int CORRECT_WORD_ADD_TIME = 15;
    public static final int SKIP_WORD_SUBTRACT_TIME = -7;

    private String counter;
    private String label;
    private float x;
    private float y;
    private float counterTextSize;
    private float labelTextSize;
    private float remainingTime;
    private float nextLabelUpdateTime;
    private boolean frozen;

    private String counterDiff;
    private int remainingTimeDiff;

    Timer(int remainingTime, float x, float y, float counterTextSize, float labelTextSize) {
        this.x = x;
        this.y = y;
        this.counterTextSize = counterTextSize;
        this.labelTextSize = labelTextSize;
        this.remainingTime = remainingTime - 0.0001f;
        nextLabelUpdateTime = remainingTime - 1.f;
        updateLabels();
    }

    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
    }

    public void addTime(int seconds) {
        remainingTimeDiff = seconds;
        counterDiff = (seconds >= 0 ? " +" : " ") + seconds;
    }

    public boolean hasExceeded() {
        return remainingTime <= 0.f;
    }

    @Override
    void update(float deltaTime) {
        if (frozen) {
            return;
        }
        if (remainingTimeDiff != 0 && counterDiff != null) {
            remainingTime += remainingTimeDiff;
            nextLabelUpdateTime = remainingTime - 1.f;
            remainingTimeDiff = 0;
            counterDiff = null;
            updateLabels();
        }
        remainingTime -= deltaTime;
        if (remainingTime < 0.f) {
            remainingTime = 0.f;
        }
        if (remainingTime <= nextLabelUpdateTime) {
            nextLabelUpdateTime -= 1.f;
            updateLabels();
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(counterTextSize);
        canvas.drawText(counter, x, y + counterTextSize, paint);
        if (counterDiff != null) {
            canvas.drawText(counterDiff, x, y + counterTextSize * 2f, paint);
        }
        float counterTextWidth = paint.measureText(counter);
        paint.setTextSize(labelTextSize);
        canvas.drawText(label, x + counterTextWidth, y + counterTextSize, paint);
    }

    private void updateLabels() {
        int remainingTimeInSeconds = (int)remainingTime;
        counter = Integer.toString(remainingTimeInSeconds);
        if (remainingTimeInSeconds == 1) {
            label = " second remaining";
        } else {
            label = " seconds remaining";
        }
    }
}
