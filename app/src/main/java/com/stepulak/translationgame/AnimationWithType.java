package com.stepulak.translationgame;

public abstract class AnimationWithType<AnimationType> implements Animation {
    private AnimationType animationType;

    AnimationWithType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }
}
