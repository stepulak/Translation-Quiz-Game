package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.arch.core.util.Function;

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

    public void clickFirst(final float x, final float y) {
        applyLastRecursive(elements.values().iterator(), new Function<UIElement, Boolean>() {
            @Override
            public Boolean apply(UIElement element) {
                if (element instanceof ClickableElement) {
                    return ((ClickableElement)element).click(x, y);
                }
                return false;
            }
        });
    }

    public void motionTouchFirst(final float touchOrigX, final float touchOrigY, final float motionX, final float motionY) {
        applyLastRecursive(elements.values().iterator(), new Function<UIElement, Boolean>() {
            @Override
            public Boolean apply(UIElement element) {
                if (element instanceof MotionElement) {
                    return ((MotionElement)element).motionTouch(touchOrigX, touchOrigY, motionX, motionY);
                }
                return false;
            }
        });
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

    private static boolean applyLastRecursive(Iterator<UIElement> iterator, Function<UIElement, Boolean> applyFunc) {
        if (!iterator.hasNext()) {
            return false;
        }
        UIElement element = iterator.next();
        if (applyLastRecursive(iterator, applyFunc)) {
            return true;
        }
        return applyFunc.apply(element);
    }
}
