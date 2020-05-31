package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class EndScreen extends ClickableElement {
    private float SWIPE_ANIMATION_VELOCITY_RATIO = 2.f;

    private Background background;
    private Label theEndLabel;
    private Label correctWordsLabel;
    private Label skippedWordsLabel;
    private TranslationAnimation animation;

    public EndScreen(Paint paint, float screenWidth, float screenHeight, int correctWords, int skippedWords) {
        String theEndString = "THE END!";
        String correctWordsString = "Correct words: " + correctWords;
        String skippedWordsString = "Skipped words: " + skippedWords;

        RectF theEndBody = new RectF();
        RectF correctWordsBody = new RectF();
        RectF skippedWordsBody = new RectF();

        theEndLabel = new Label(theEndString, paint, theEndBody);
        correctWordsLabel = new Label(correctWordsString, paint, correctWordsBody);
        skippedWordsLabel = new Label(skippedWordsString, paint, skippedWordsBody);
        background = new Background(screenWidth, screenHeight);

        float animationVelocity = SWIPE_ANIMATION_VELOCITY_RATIO * screenHeight;
        animation = new TranslationAnimation(null, -screenHeight, 0.f, animationVelocity, 0.f);
    }

    @Override
    public boolean click(float x, float y) {
        if (!animation.expired()) {
            return false;
        }
        invokeClickCallback();
        return true;
    }

    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(0.f, animation.getCurrentPosition());
        background.draw(canvas, paint);
        theEndLabel.draw(canvas, paint);
        correctWordsLabel.draw(canvas, paint);
        skippedWordsLabel.draw(canvas, paint);
        canvas.restore();
    }
}
