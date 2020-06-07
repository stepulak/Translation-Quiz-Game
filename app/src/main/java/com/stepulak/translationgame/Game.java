package com.stepulak.translationgame;

import android.content.Context;
import android.graphics.Canvas;

public class Game extends GameRunnable {
    private static final float FETCH_NEXT_WORD_WAIT_TIME = 2.f; // in sec
    private static final float SKIP_WORD_VIBRATION_TIME = 100f; // in ms

    private Dictionary dictionary;
    private GameUI ui;

    // Game logic variables
    private boolean wordSkipHandled;
    private boolean userFillHandled;
    private boolean getNextWord;
    private float getNextWordTimer;

    // Counter
    private int correctWordCounter;
    private int skippedWordCounter;

    public Game(Context context, float screenWidth, float screenHeight, Dictionary dictionary) {
        super(context, screenWidth, screenHeight);
        this.dictionary = dictionary;
        ui = new GameUI(context.getResources(), screenWidth, screenHeight);
        ui.createUIForTranslation(dictionary.getTranslation());
    }

    @Override
    public boolean moveToPreviousGameRunnable() {
        return ui.isQuitButtonPressed();
    }

    @Override
    public boolean moveToNextGameRunnable() {
        return ui.getUIManager().<Timer>get(UIElementType.TIMER).hasExceeded();
    }

    @Override
    public GameRunnable createNextGameRunnable() {
        return new GameEndScreen(getContext(), getScreenWidth(), getScreenHeight(), dictionary, correctWordCounter, skippedWordCounter);
    }

    @Override
    public GameRunnable createPreviousGameRunnable() {
        return new GameMenu(getContext(), getScreenWidth(), getScreenHeight());
    }

    @Override
    public void click(float x, float y) {
        ui.getUIManager().clickFirst(x, y);
        handleAfterClick();
    }

    @Override
    public void update(float deltaTime) {
        ui.getUIManager().updateAll(deltaTime);
        handleAfterUpdate(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        ui.getUIManager().drawAll(canvas, ui.getPaint());
    }

    //
    // Game logic methods
    //
    private void handleAfterClick() {
        UIManager manager = ui.getUIManager();
        Character character = manager.<Keyboard>get(UIElementType.KEYBOARD).getLastClickedCharacter();
        if (character != null) {
            manager.<InputForm>get(UIElementType.INPUT_FORM).insertCharacter(character);
        }
    }

    private void handleAfterUpdate(float deltaTime) {
        Keyboard keyboard = ui.getUIManager().get(UIElementType.KEYBOARD);
        InputForm inputForm = ui.getUIManager().get(UIElementType.INPUT_FORM);
        inputFormUpdate(inputForm);
        getNextWordUpdate(deltaTime, keyboard, inputForm);
    }

    private void inputFormUpdate(InputForm inputForm) {
        Timer timer = ui.getUIManager().get(UIElementType.TIMER);

        if (!wordSkipHandled && inputForm.isWordSkipped()) {
            getNextWordTimer = FETCH_NEXT_WORD_WAIT_TIME;
            wordSkipHandled = true;
            skippedWordCounter++;
            timer.addTime(Timer.SKIP_WORD_SUBTRACT_TIME);
            timer.freeze();
            vibrate(SKIP_WORD_VIBRATION_TIME);
            return;
        }
        if (!wordSkipHandled && inputForm.isFilledWithCharacters()) {
            if (!userFillHandled) {
                userFillHandled = true;
                if (!inputForm.isFilledCorrectly()) {
                    inputForm.startShakeAnimation();
                    return;
                }
                getNextWordTimer = .001f;
                correctWordCounter++;
                timer.addTime(Timer.CORRECT_WORD_ADD_TIME);
                timer.freeze();
            }
            return;
        }
        userFillHandled = false;
    }

    private void getNextWordUpdate(float deltaTime, Keyboard keyboard, InputForm inputForm) {
        if (getNextWordTimer > 0.f) {
            getNextWordTimer -= deltaTime;
            if (getNextWordTimer <= 0.f) {
                keyboard.destroyButtonLabels();
                inputForm.startExitAnimation(ui.getScreenWidth());
                getNextWord = true;
            }
        }
        if (getNextWord && keyboard.areLabelsDestroyed() && !inputForm.isAnimating()) {
            dictionary.next();
            ui.createUIForTranslation(dictionary.getTranslation());
            userFillHandled = false;
            wordSkipHandled = false;
            getNextWord = false;
        }
    }
}
