package com.stepulak.translationgame;

public class RotationAnimation implements Animation {
    private float rotationAngle;
    private float rotationTimer;
    private float rotationTurnTime;
    private float rotationDirection = 1.f;
    private float rotationVelocity;
    private int maxRotations;
    private int rotationNumber;

    public RotationAnimation(float rotationVelocity, float rotationTurnTime, int maxRotations) {
        this.rotationVelocity = rotationVelocity;
        this.maxRotations = maxRotations;
        this.rotationNumber = maxRotations;
        this.rotationTurnTime = rotationTurnTime;
    }

    public float getRotationAngle() {
        return rotationAngle;
    }

    @Override
    public boolean expired() {
        return rotationNumber <= 0;
    }

    @Override
    public void update(float deltaTime) {
        rotationAngle += rotationDirection * rotationVelocity * deltaTime;
        rotationTimer += deltaTime;
        if (rotationTimer > getRotationTurnTime()) {
            rotationTimer = 0.f;
            rotationDirection *= -1.f;
            rotationNumber--;
        }
    }

    private float getRotationTurnTime() {
        return (rotationNumber == 1 || rotationNumber == maxRotations) ? rotationTurnTime / 2 : rotationTurnTime;
    }
}
