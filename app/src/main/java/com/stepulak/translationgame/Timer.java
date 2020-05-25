package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Timer extends UIElement {
    public static final int DEFAULT_TIME_SECONDS = 60;
    public static final int CORRECT_WORD_ADD_TIME = 7;
    public static final int SKIP_WORD_SUBTRACT_TIME = 3;

    private float remainingTime;

    Timer(int seconds) {
        remainingTime = seconds * 1000.f;
    }

    public void addTime(int seconds) {

    }

    public boolean hasExceeded() {
        return remainingTime <= 0.f;
    }

    @Override
    void update(float deltaTime) {

    }

    @Override
    void draw(Canvas canvas, Paint paint) {

    }
}
