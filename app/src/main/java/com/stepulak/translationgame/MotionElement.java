package com.stepulak.translationgame;

public abstract class MotionElement extends ClickableElement {
    private Callback motionCallback;

    public void setMotionCallback(Callback callback) {
        motionCallback = callback;
    }

    protected void invokeMotionCallback() {
        if (motionCallback != null) {
            motionCallback.apply();
        }
    }

    public abstract boolean motionTouch(float touchOrigX, float touchOrigY, float motionX, float motionY);
}
