package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class UIElement {
    void update(float deltaTime) {
    }

    abstract void draw(Canvas canvas, Paint paint);
}
