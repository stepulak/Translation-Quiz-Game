package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class EndScreen extends ClickableElement {
    private float SWIPE_ANIMATION_VELOCITY_RATIO = 2.f;
    private float END_LABEL_WIDTH_RATIO = 0.8f;
    private float END_LABEL_HEIGHT_RATIO = 0.1f;
    private float END_LABEL_VERTICAL_POSITION_RATIO = 0.25f;
    private float STAT_LABEL_WIDTH_RATIO = 0.8f;
    private float STAT_LABEL_HEIGHT_RATIO = 0.1f;
    private float CORRECT_WORDS_LABEL_VERTICAL_POSITION_RATIO = 0.5f;
    private float SKIPPED_WORDS_LABEL_VERTICAL_POSITION_RATIO = 0.6f;
    private float TAP_TO_PLAY_LABEL_WIDTH_RATIO = 0.5f;
    private float TAP_TO_PLAY_LABEL_HEIGHT_RATIO = 0.05f;
    private float TAP_TO_PLAY_LABEL_VERTICAL_POSITION_RATIO = 0.85f;

    private Background background;
    private Label theEndLabel;
    private Label correctWordsLabel;
    private Label skippedWordsLabel;
    private Label tapToPlayLabel;
    private TranslationAnimation animation;

    public EndScreen(Paint paint, float screenWidth, float screenHeight, int correctWords, int skippedWords) {
        float animationVelocity = SWIPE_ANIMATION_VELOCITY_RATIO * screenHeight;
        animation = new TranslationAnimation(null, -screenHeight, 0.f, animationVelocity, 0.f);
        setup(paint, screenWidth, screenHeight, correctWords, skippedWords);
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
        tapToPlayLabel.draw(canvas, paint);
        canvas.restore();
    }

    private void setup(Paint paint, float screenWidth, float screenHeight, int correctWords, int skippedWords) {
        String theEndString = "THE END!";
        String correctWordsString = "Correct words: " + correctWords;
        String skippedWordsString = "Skipped words: " + skippedWords;
        String tapToPlayString = "Press TAP to play again!";

        float centerX = screenWidth / 2.f;
        float endLabelY = screenHeight * END_LABEL_VERTICAL_POSITION_RATIO;
        float endLabelWidth = screenWidth * END_LABEL_WIDTH_RATIO;
        float endLabelHeight = screenHeight * END_LABEL_HEIGHT_RATIO;
        float statLabelWidth = screenWidth * STAT_LABEL_WIDTH_RATIO;
        float statLabelHeight = screenHeight * STAT_LABEL_HEIGHT_RATIO;
        float correctWordsLabelY = screenHeight * CORRECT_WORDS_LABEL_VERTICAL_POSITION_RATIO;
        float skippedWordsLabelY = screenHeight * SKIPPED_WORDS_LABEL_VERTICAL_POSITION_RATIO;
        float tapToPlayLabelWidth = screenWidth * TAP_TO_PLAY_LABEL_WIDTH_RATIO;
        float tapToPlayLabelHeight = screenHeight * TAP_TO_PLAY_LABEL_HEIGHT_RATIO;
        float tapToPlayLabelY = screenHeight * TAP_TO_PLAY_LABEL_VERTICAL_POSITION_RATIO;

        RectF theEndBody = new RectF(
                centerX - endLabelWidth / 2.f,
                endLabelY,
                centerX + endLabelWidth / 2.f,
                endLabelY + endLabelHeight
        );
        RectF correctWordsBody = new RectF(
                centerX - statLabelWidth / 2.f,
                correctWordsLabelY,
                centerX + statLabelWidth / 2.f,
                correctWordsLabelY + statLabelHeight
        );
        RectF skippedWordsBody = new RectF(
                centerX - statLabelWidth / 2.f,
                skippedWordsLabelY,
                centerX + statLabelWidth / 2.f,
                skippedWordsLabelY + statLabelHeight
        );
        RectF tapToPlayBody = new RectF(
                centerX - tapToPlayLabelWidth / 2.f,
                tapToPlayLabelY,
                centerX + tapToPlayLabelWidth / 2.f,
                tapToPlayLabelY + tapToPlayLabelHeight
        );

        theEndLabel = new Label(theEndString, paint, theEndBody);
        correctWordsLabel = new Label(correctWordsString, paint, correctWordsBody);
        skippedWordsLabel = new Label(skippedWordsString, paint, skippedWordsBody);
        tapToPlayLabel = new Label(tapToPlayString, paint, tapToPlayBody);
        background = new Background(screenWidth, screenHeight);
    }
}
