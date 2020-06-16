package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class VerticalMenu extends MotionElement {
    public static int VERTICAL_MENU_NUM_ELEMENTS_PER_SCREEN_DEFAULT = 5;

    private List<MenuElement> elements = new ArrayList<>();
    private MenuElement lastClickedElement;
    private RectF clipArea;
    private float elementHeight;
    private float verticalOffset;

    public VerticalMenu(RectF clipArea, float elementHeight) {
        this.clipArea = clipArea;
        this.elementHeight = elementHeight;
    }

    public void addElement(MenuElement element) {
        float top = clipArea.top + elements.size() * elementHeight;
        element.setup(new RectF(clipArea.left, top, clipArea.right, top + elementHeight));
        elements.add(element);
    }

    public boolean wasClicked() {
        return lastClickedElement != null;
    }

    public MenuElement getLastClickedElement() {
        MenuElement tmp = lastClickedElement;
        lastClickedElement = null;
        return tmp;
    }

    @Override
    public boolean motionTouch(float touchOrigX, float touchOrigY, float motionX, float motionY) {
        if (!clipArea.contains(touchOrigX, touchOrigY)) {
            return false;
        }
        float maxVerticalOffset = Math.max(0.f, elementHeight * elements.size() - clipArea.height());
        verticalOffset = Math.max(0.f, Math.min(maxVerticalOffset, verticalOffset - motionY));
        return true;
    }

    @Override
    public boolean click(float x, float y) {
        if (!clipArea.contains(x, y)) {
            return false;
        }
        x += verticalOffset;
        for (MenuElement element : elements) {
            if (element.click(x, y)) {
                lastClickedElement = element;
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        for (MenuElement element : elements) {
            element.update(deltaTime);
        }
    }

    @Override
    void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.clipRect(clipArea);
        canvas.translate(0, -verticalOffset);
        for (MenuElement element : elements) {
            element.draw(canvas, paint);
        }
        canvas.restore();
    }
}
