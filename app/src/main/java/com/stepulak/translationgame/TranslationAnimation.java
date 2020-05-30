package com.stepulak.translationgame;

public class TranslationAnimation<AnimationType> extends Animation<AnimationType> {
    private float currentPosition;
    private float maximumPosition;
    private float velocity;
    private float acceleration;

    TranslationAnimation(AnimationType animationType, float currentPosition, float maximumPosition, float velocity, float acceleration) {
        super(animationType);
        this.currentPosition = currentPosition;
        this.maximumPosition = maximumPosition;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public float getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public boolean expired() {
        return currentPosition >= maximumPosition;
    }

    @Override
    public void update(float deltaTime) {
        if (expired()) {
            return;
        }
        currentPosition += velocity * deltaTime;
        velocity += acceleration * deltaTime;
        if (expired()) {
            currentPosition = maximumPosition;
        }
    }
}
