package com.stepulak.translationgame;

public abstract class Animation<AnimationType> {
    private AnimationType animationType;

    Animation(AnimationType animationType) {
        this.animationType = animationType;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public abstract boolean expired();
    public abstract void update(float deltaTime);
}
