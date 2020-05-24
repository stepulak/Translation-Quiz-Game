package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class UIElement {
    abstract void update(float deltaTime);
    abstract void draw(Canvas canvas, Paint paint);
}
