package com.stepulak.translationgame;

public class ShakeAnimation extends Animation<> {

    public ShakeAnimation() {
        shakeRotationEnabled = true;
        shakeRotationTimer = 0.f;
        shakeRotationAngle = 0.f;
        shakeRotationDirection = 1.f;
        shakeRotations = SHAKE_ROTATIONS;
    }

    @Override
    public void update(float deltaTime) {
        shakeRotationAngle += shakeRotationDirection * SHAKE_ROTATION_VELOCITY * deltaTime;
        shakeRotationTimer += deltaTime;
        float turnTime = (shakeRotations == 1 || shakeRotations == SHAKE_ROTATIONS) ? SHAKE_ROTATION_TURN_TIME / 2 : SHAKE_ROTATION_TURN_TIME;

        if (shakeRotationTimer > turnTime) {
            shakeRotationTimer = 0.f;
            shakeRotationDirection *= -1.f;
            shakeRotations--;
            if (shakeRotations <= 0) {
                shakeRotationEnabled = false;
            }
        }
    }
}
