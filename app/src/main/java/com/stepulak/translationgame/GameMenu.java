package com.stepulak.translationgame;

import android.content.Context;
import android.graphics.Canvas;

public class GameMenu extends GameRunnable {
    public GameMenu(Context context, float screenWidth, float screenHeight) {
        super(context, screenWidth, screenHeight);
    }

    @Override
    public GameRunnable createNextGameRunnable() {
        Dictionary dictionary = new Dictionary(getContext().getResources().obtainTypedArray(R.array.czech_german));
        return new Game(getContext(), getScreenWidth(), getScreenHeight(), dictionary);
    }

    @Override
    public GameRunnable createPreviousGameRunnable() {
        return null;
    }

    @Override
    public boolean moveToPreviousGameRunnable() {
        return false;
    }

    @Override
    public boolean moveToNextGameRunnable() {
        return true;
    }

    @Override
    public void click(float x, float y) {

    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
