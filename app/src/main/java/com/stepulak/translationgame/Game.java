package com.stepulak.translationgame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import java.security.Key;

public class Game {
    private static final float FETCH_NEXT_WORD_TIME = 2.f;

    private Dictionary dictionary;
    private UI ui;

    // Game logic variables
    private float gameLoadTime = 0.5f;
    private boolean inputFormFillHandled;
    private boolean fetchNextWord;
    private float fetchNextWordTimer;

    public Game(Resources resources, int dictionaryDescriptor, float scrWidth, float scrHeight) {
        dictionary = new Dictionary(resources.obtainTypedArray(dictionaryDescriptor));
        ui = new UI(resources, scrWidth, scrHeight);
        ui.createUIForTranslation(dictionary.getTranslation());
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

        if (inputForm.isFilled()) {
            if (!inputFormFillHandled) {
                if (inputForm.isFilledCorrectly()) {
                    fetchNextWordTimer = inputForm.isSkipped() ? FETCH_NEXT_WORD_TIME : .001f;
                } else {
                    inputForm.startShakeAnimation();
                }
                inputFormFillHandled = true;
            }
        } else {
            inputFormFillHandled = false;
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
            inputFormFillHandled = false;
            fetchNextWord = false;
        }
    }
}
