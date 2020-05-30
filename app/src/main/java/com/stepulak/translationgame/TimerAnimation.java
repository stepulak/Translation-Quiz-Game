package com.stepulak.translationgame;

public class TimerAnimation<AnimationType> extends AnimationWithType<AnimationType> {
    private float expireTime;
    private float timer = 0f;
    private ParameterCallback updateCallback;
    private ParameterCallback expireCallback;

    TimerAnimation(AnimationType animationType, float expireTime) {
        this(animationType, expireTime, null, null);
    }

    TimerAnimation(AnimationType animationType, float expireTime, ParameterCallback<TimerAnimation> expireCallback) {
        this(animationType, expireTime, null, expireCallback);
    }

    TimerAnimation(AnimationType animationType, float expireTime,
                   ParameterCallback<TimerAnimation> updateCallback,
                   ParameterCallback<TimerAnimation> expireCallback) {
        super(animationType);
        this.expireTime = expireTime;
        this.updateCallback = updateCallback;
        this.expireCallback = expireCallback;
    }

    public float getProcessRatio() {
        return timer / expireTime;
    }

    public boolean expired() {
        return timer >= expireTime;
    }

    public void update(float deltaTime) {
        if (expired()) {
            return;
        }
        timer += deltaTime;
        if (updateCallback != null) {
            updateCallback.apply(this);
        }
        if (expired()) {
            timer = expireTime;
            if (expireCallback != null) {
                expireCallback.apply(this);
            }
        }
    }
}
