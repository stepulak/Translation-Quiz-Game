package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class UIElement {
    boolean click(float x, float y) {
        return false;
    }

    void update(float deltaTime) {
    }

    abstract void draw(Canvas canvas, Paint paint);
}
