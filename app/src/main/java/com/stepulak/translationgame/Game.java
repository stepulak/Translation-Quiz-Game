package com.stepulak.translationgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.Log;

import static androidx.core.content.ContextCompat.getSystemService;

public class Game {
    private static final float FETCH_NEXT_WORD_WAIT_TIME = 2.f;
    private static final float SKIP_WORD_VIBRATION_TIME = 100f;
    private static final float GAME_END_VIBRATION_TIME = 500f;

    private Context context;
    private Dictionary dictionary;
    private UI ui;

    // Game logic variables
    private float gameLoadTime = 0.5f;
    private boolean wordSkipHandled;
    private boolean userFillHandled;
    private boolean showEndScreenHandled;
    private boolean getNextWord;
    private float getNextWordTimer;
    private int correctWordCounter;
    private int skippedWordCounter;

    public Game(Context context, int dictionaryDescriptor, float scrWidth, float scrHeight) {
        this.context = context;
        dictionary = new Dictionary(context.getResources().obtainTypedArray(dictionaryDescriptor));
        dictionary.shuffle();
        ui = new UI(context.getResources(), scrWidth, scrHeight);
        ui.createUIForTranslation(dictionary.getTranslation());
    }

    public boolean toQuit() {
        return ui.toQuit();
    }

    public boolean toRestart() {
        return ui.toRestart();
    }

    public void click(float x, float y) {
        ui.getUIManager().clickFirst(x, y);
        handleAfterClickLogic();
    }

    public void update(float deltaTime) {
        if (gameLoadTime > 0.f) {
            gameLoadTime -= deltaTime;
            return;
        }
        ui.getUIManager().updateAll(deltaTime);
        handleAfterUpdateLogic(deltaTime);
    }

    public void draw(Canvas canvas) {
        ui.getUIManager().drawAll(canvas, ui.getPaint());
    }

    private void handleAfterClickLogic() {
        UIManager manager = ui.getUIManager();
        Character character = manager.<Keyboard>get(UIElementType.KEYBOARD).getLastClickedCharacter();
        if (character != null) {
            manager.<InputForm>get(UIElementType.INPUT_FORM).insertCharacter(character);
        }
    }

    private void handleAfterUpdateLogic(float deltaTime) {
        UIManager manager = ui.getUIManager();
        Keyboard keyboard = manager.get(UIElementType.KEYBOARD);
        InputForm inputForm = manager.get(UIElementType.INPUT_FORM);
        Timer timer = manager.get(UIElementType.TIMER);

        inputFormUpdate(inputForm);
        getNextWordUpdate(deltaTime, keyboard, inputForm);

        if (!showEndScreenHandled && timer.hasExceeded()) {
            ui.showEndScreen(correctWordCounter, skippedWordCounter);
            showEndScreenHandled = true;
            vibrate(GAME_END_VIBRATION_TIME);
        }
    }

    private void inputFormUpdate(InputForm inputForm) {
        Timer timer = ui.getUIManager().get(UIElementType.TIMER);

        if (!wordSkipHandled && inputForm.isWordSkipped()) {
            getNextWordTimer = FETCH_NEXT_WORD_WAIT_TIME;
            wordSkipHandled = true;
            skippedWordCounter++;
            timer.addTime(Timer.SKIP_WORD_SUBTRACT_TIME);
            timer.freeze();
            //vibrate(SKIP_WORD_VIBRATION_TIME);
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
            Timer timer = ui.getUIManager().get(UIElementType.TIMER);
            timer.unfreeze();
        }
    }

    private void vibrate(float time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate((int)time);
    }
}
