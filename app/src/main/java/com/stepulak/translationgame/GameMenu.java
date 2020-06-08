package com.stepulak.translationgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.HashMap;
import java.util.Map;

public class GameMenu extends GameRunnable {
    private Map<Integer, String> dictionaries = new HashMap<>();

    // UI
    private Background background;
    private VerticalMenu verticalMenu;

    public GameMenu(Context context, float screenWidth, float screenHeight) {
        super(context, screenWidth, screenHeight);
        setupDictionaries();
        setupUI();
    }

    @Override
    public GameRunnable createNextGameRunnable() {
        Dictionary dictionary = new Dictionary(getContext().getResources().obtainTypedArray(R.array.czech_german), "X");
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
        background.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = getPaint();
        background.draw(canvas, paint);
    }

    private void setupDictionaries() {
        dictionaries.put(R.array.czech_german, "Czech - German");
    }

    private void setupUI() {
        background = new Background(getScreenWidth(), getScreenHeight());

        float screenWidth = getScreenWidth();
        float screenHeight = getScreenHeight();
        float verticalMenuWidth = screenWidth * ;
        float horizontalMenuHeight = screenHeight * ;
        float
        RectF verticalMenuArea = new RectF(0.f, 0.f, getScreenWidth(), getScreenHeight());

        verticalMenu = new VerticalMenu(, )
    }
}
