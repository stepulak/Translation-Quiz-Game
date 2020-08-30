package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class VerticalMenu extends MotionElement {
    public static final int VERTICAL_MENU_NUM_ELEMENTS_PER_SCREEN_DEFAULT = 5;
    public static final float SCROLL_BAR_MIN_HEIGHT_RATIO = 0.2f;
    public static final float SCROLL_BAR_WIDTH_RATIO = 0.02f;
    public static final float SCROLL_BAR_HEIGHT_CUT_RATIO = 0.05f;
    public static final float SCROLL_BAR_VISIBILITY_TIMER = 1.f;

    private List<MenuElement> elements = new ArrayList<>();
    private MenuElement lastClickedElement;
    private RectF clipArea;
    private float elementHeight;
    private float verticalOffset;
    private TimerAnimation scrollBarVisibilityTimer = new TimerAnimation(null, SCROLL_BAR_VISIBILITY_TIMER);

    public VerticalMenu(RectF clipArea, float elementHeight) {
        this.clipArea = clipArea;
        this.elementHeight = elementHeight;
    }

    public void addElement(MenuElement element) {
        float top = clipArea.top + elements.size() * elementHeight;
        element.setup(new RectF(clipArea.left, top, clipArea.right, top + elementHeight));
        elements.add(element);
        // Not visible by default
        scrollBarVisibilityTimer.setExpired();
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
        scrollBarVisibilityTimer.reset();
        return true;
    }

    @Override
    public boolean click(float x, float y) {
        if (!clipArea.contains(x, y)) {
            return false;
        }
        y += verticalOffset;
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
        scrollBarVisibilityTimer.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.clipRect(clipArea);
        canvas.translate(0, -verticalOffset);
        for (MenuElement element : elements) {
            element.draw(canvas, paint);
        }
        canvas.restore();
        drawScrollBar(canvas, paint);
    }

    public void drawScrollBar(Canvas canvas, Paint paint) {
        if (scrollBarVisibilityTimer.expired()) {
            return;
        }
        float clipAreaHeight = clipArea.height();
        float elementsOffset = elementHeight * elements.size() - clipAreaHeight;
        // Height of elements does not overflow clipArea's height
        if (elementsOffset < 0.f) {
            return;
        }
        // Setup proportions
        float scrollBarHeight = Math.max(
                clipAreaHeight - elementsOffset,
                SCROLL_BAR_MIN_HEIGHT_RATIO * clipAreaHeight);
        float scrollBarHeightCut = SCROLL_BAR_HEIGHT_CUT_RATIO * clipAreaHeight / 2.f;
        float scrollBarRange = clipAreaHeight - scrollBarHeight;
        float scrollBarPosition = (verticalOffset / elementsOffset) * scrollBarRange + clipArea.top;
        float scrollBarWidth = SCROLL_BAR_WIDTH_RATIO * clipArea.width();
        // Draw it
        paint.setColor(MyColors.SCROLL_BAR_COLOR);
        paint.setAlpha(255 - (int)(scrollBarVisibilityTimer.getProcessRatio() * 255));
        canvas.drawRect(0.f,
                scrollBarPosition + scrollBarHeightCut,
                scrollBarWidth,
                scrollBarPosition + scrollBarHeight - scrollBarHeightCut,
                paint);
        paint.setAlpha(255);
    }
}
