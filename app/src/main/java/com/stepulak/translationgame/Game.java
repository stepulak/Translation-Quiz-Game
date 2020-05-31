package com.stepulak.translationgame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import java.security.Key;

public class Game {
    private static final float FETCH_NEXT_WORD_WAIT_TIME = 2.f;

    private Dictionary dictionary;
    private UI ui;

    // Game logic variables
    private float gameLoadTime = 0.5f;
    private boolean wordSkipHandled;
    private boolean userFillHandled;
    private boolean fetchNextWord;
    private float fetchNextWordTimer;

    public Game(Resources resources, int dictionaryDescriptor, float scrWidth, float scrHeight) {
        dictionary = new Dictionary(resources.obtainTypedArray(dictionaryDescriptor));
        ui = new UI(resources, scrWidth, scrHeight);
        ui.createUIForTranslation(dictionary.getTranslation());
    }

    public boolean toQuit() {
        return ui.toQuit();
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

        if (!wordSkipHandled && inputForm.isWordSkipped()) {
            fetchNextWordTimer = FETCH_NEXT_WORD_WAIT_TIME;
            wordSkipHandled = true;
        } else if (!wordSkipHandled && inputForm.isFilledWithCharacters()) {
            if (!userFillHandled) {
                if (inputForm.isFilledCorrectly()) {
                    fetchNextWordTimer = .001f;
                } else {
                    inputForm.startShakeAnimation();
                }
                userFillHandled = true;
            }
        } else {
            userFillHandled = false;
        }

        if (fetchNextWordTimer > 0.f) {
            fetchNextWordTimer -= deltaTime;
            if (fetchNextWordTimer <= 0.f) {
                keyboard.destroyButtonLabels();
                inputForm.startExitAnimation(ui.getScreenWidth());
                fetchNextWord = true;
            }
        }

        if (fetchNextWord && keyboard.areLabelsDestroyed() && !inputForm.isAnimating()) {
            dictionary.next();
            ui.createUIForTranslation(dictionary.getTranslation());
            userFillHandled = false;
            wordSkipHandled = false;
            fetchNextWord = false;
        }
    }
}
