package com.stepulak.translationgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import static com.stepulak.translationgame.MyUIConstants.*;

public class GameEndScreen extends GameRunnable {
    private static final float GAME_END_VIBRATION_TIME = 500f; // in ms

    private Dictionary dictionary;
    private boolean restartGame;

    // UI
    private Background background;
    private Label theEndLabel;
    private Label correctWordsLabel;
    private Label skippedWordsLabel;
    private Label tapToPlayLabel;

    public GameEndScreen(Context context, float screenWidth, float screenHeight, Dictionary dictionary, int correctWords, int skippedWords) {
        super(context, screenWidth, screenHeight);
        this.dictionary = dictionary;
        setup(correctWords, skippedWords);
        vibrate(GAME_END_VIBRATION_TIME);
    }

    @Override
    public GameRunnable createNextGameRunnable() {
        return null;
    }

    @Override
    public GameRunnable createPreviousGameRunnable() {
        return new Game(getContext(), getScreenWidth(), getScreenHeight(), dictionary);
    }

    @Override
    public boolean moveToPreviousGameRunnable() {
        return restartGame;
    }

    @Override
    public boolean moveToNextGameRunnable() {
        return false;
    }

    @Override
    public void click(float x, float y) {
        restartGame = true;
    }

    @Override
    public void update(float deltaTime) {
        background.update(deltaTime);
        theEndLabel.update(deltaTime);
        correctWordsLabel.update(deltaTime);
        skippedWordsLabel.update(deltaTime);
        tapToPlayLabel.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = getPaint();
        background.draw(canvas, paint);
        theEndLabel.draw(canvas, paint);
        correctWordsLabel.draw(canvas, paint);
        skippedWordsLabel.draw(canvas, paint);
        tapToPlayLabel.draw(canvas, paint);
    }

    private void setup(int correctWords, int skippedWords) {
        String theEndString = "THE END!";
        String correctWordsString = "Correct words: " + correctWords;
        String skippedWordsString = "Skipped words: " + skippedWords;
        String tapToPlayString = "Press TAP to play again!";

        float screenWidth = getScreenWidth();
        float screenHeight = getScreenHeight();
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
        Paint paint = getPaint();

        theEndLabel = new Label(theEndString, paint, theEndBody);
        correctWordsLabel = new Label(correctWordsString, paint, correctWordsBody);
        skippedWordsLabel = new Label(skippedWordsString, paint, skippedWordsBody);
        tapToPlayLabel = new Label(tapToPlayString, paint, tapToPlayBody);
        background = new Background(screenWidth, screenHeight);
    }
}
