package com.stepulak.translationgame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import java.security.Key;

public class Game {
    private Dictionary dictionary;
    private UI ui;

    // Game logic variables
    private float gameLoadTime = 0.5f;
    private boolean inputFormFillHandled;
    private boolean fetchNextWord;

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
        handleAfterUpdateLogic();
    }

    public void draw(Canvas canvas) {
        ui.getUIManager().drawAll(canvas, ui.getPaint());
    }

    private void handleAfterClickLogic() {
        UIManager manager = ui.getUIManager();
        Character character = manager.<Keyboard>get(UIElementType.KEYBOARD).fetchClickedCharacter();
        if (character != null) {
            manager.<InputForm>get(UIElementType.INPUT_FORM).insertCharacter(character);
        }
    }

    private void handleAfterUpdateLogic() {
        UIManager manager = ui.getUIManager();
        Keyboard keyboard = manager.get(UIElementType.KEYBOARD);
        InputForm inputForm = manager.get(UIElementType.INPUT_FORM);

        if (inputForm.isFilled()) {
            if (!inputFormFillHandled) {
                if (inputForm.isFilledCorrectly()) {
                    keyboard.destroyButtonLabels();
                    inputForm.startExitAnimation(ui.getScreenWidth());
                    fetchNextWord = true;
                } else {
                    inputForm.startShakeAnimation();
                }
                inputFormFillHandled = true;
            }
        } else {
            inputFormFillHandled = false;
        }

        if (fetchNextWord && keyboard.areLabelsDestroyed() && !inputForm.isAnimating()) {
            dictionary.next();
            ui.createUIForTranslation(dictionary.getTranslation());
            inputFormFillHandled = false;
            fetchNextWord = false;
        }
    }
}
