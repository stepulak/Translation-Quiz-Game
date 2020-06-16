package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.CallSuper;

public abstract class MenuElement extends ClickableElement {
    private RectF area;

    @CallSuper
    public void setup(RectF area) {
        this.area = area;
    }

    @Override
    public boolean click(float x, float y) {
        if (this.area == null) {
            throw new RuntimeException("setup() not called");
        }
        return area.contains(x, y);
    }

    protected RectF getArea() {
        return area;
    }
}
