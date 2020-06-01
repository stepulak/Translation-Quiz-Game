package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

public class UIManager {

    private Map<UIElementType, UIElement> elements = new EnumMap<>(UIElementType.class);

    @SuppressWarnings("unchecked")
    public <T extends UIElement> T get(UIElementType elementType) {
        UIElement element = elements.get(elementType);
        if (element == null) {
            throw new NullPointerException("Element " + elementType.toString() + " not found!");
        }
        return (T)element;
    }

    public void set(UIElementType elementType, UIElement element) {
        elements.put(elementType, element);
    }

    // Click first element but in reverse order, so actually last :-)
    public void clickFirst(float x, float y) {
        clickRecursive(elements.values().iterator(), x, y);
    }

    public void updateAll(float deltaTime) {
        for (UIElement element : elements.values()) {
            element.update(deltaTime);
        }
    }

    public void drawAll(Canvas canvas, Paint paint) {
        for (UIElement element : elements.values()) {
            element.draw(canvas, paint);
        }
    }

    private static boolean clickRecursive(Iterator<UIElement> iterator, float x, float y) {
        if (!iterator.hasNext()) {
            return false;
        }
        UIElement element = iterator.next();
        if (clickRecursive(iterator, x, y)) {
            return true;
        }
        if (element instanceof ClickableElement) {
            return ((ClickableElement)element).click(x, y);
        }
        return false;
    }
}
