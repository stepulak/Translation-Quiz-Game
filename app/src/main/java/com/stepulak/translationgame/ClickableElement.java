package com.stepulak.translationgame;

public abstract class ClickableElement extends UIElement {
    private Callback clickCallback;

    public void setClickCallback(Callback callback) {
        clickCallback = callback;
    }

    protected void invokeClickCallback() {
        if (clickCallback != null) {
            clickCallback.apply();
        }
    }

    public abstract boolean click(float x, float y);
}
