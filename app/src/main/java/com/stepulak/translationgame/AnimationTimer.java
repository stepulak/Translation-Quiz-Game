package com.stepulak.translationgame;

public class AnimationTimer<AnimationType> {
    private AnimationType animationType;
    private float expireTime;
    private float timer = 0f;
    private ParameterCallback updateCallback;
    private ParameterCallback expireCallback;

    AnimationTimer(AnimationType animationType, float expireTime,
                   ParameterCallback<AnimationTimer> updateCallback,
                   ParameterCallback<AnimationTimer> expireCallback) {
        this.animationType = animationType;
        this.expireTime = expireTime;
        this.updateCallback = updateCallback;
        this.expireCallback = expireCallback;
    }

    public AnimationType getAnimationType() {
        return animationType;
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
        if (expireCallback != null && expired()) {
            expireCallback.apply(this);
        }
    }
}
