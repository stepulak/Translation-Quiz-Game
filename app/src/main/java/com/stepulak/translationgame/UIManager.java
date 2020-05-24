package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.EnumMap;
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

    public void add(UIElementType elementType, UIElement element) {
        elements.put(elementType, element);
    }

    public void clickFirst(float x, float y) {
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
}
