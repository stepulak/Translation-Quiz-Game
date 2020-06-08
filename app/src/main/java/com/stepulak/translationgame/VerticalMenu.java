package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class VerticalMenu extends ClickableElement {
    private List<ClickableElement> elements = new ArrayList<>();
    private ClickableElement lastClickedElement;
    private RectF clipArea;
    private float elementHeight;
    private float verticalOffset;

    public VerticalMenu(RectF clipArea, float elementHeight) {
        this.clipArea = clipArea;
        this.elementHeight = elementHeight;
    }

    public ClickableElement getLastClickedElement() {
        ClickableElement tmp = lastClickedElement;
        lastClickedElement = null;
        return tmp;
    }

    @Override
    public boolean click(float x, float y) {
        x += verticalOffset;
        if (!clipArea.contains(x, y)) {
            return false;
        }
        for (ClickableElement element : elements) {
            if (element.click(x, y)) {
                lastClickedElement = element;
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        for (ClickableElement element : elements) {
            element.update(deltaTime);
        }
    }

    @Override
    void draw(Canvas canvas, Paint paint) {

    }
}
