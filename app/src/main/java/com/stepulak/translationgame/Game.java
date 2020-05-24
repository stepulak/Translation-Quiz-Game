package com.stepulak.translationgame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import static com.stepulak.translationgame.MyUIConstants.*;

public class Game {
    // UIManager proportions and positions

    // UIManager elements
    private Paint paint;
    private MyBitmaps bitmaps;
    private Dictionary dictionary;

    private float screenWidth;
    private float screenHeight;


    private boolean inputFormFillHandled;
    private boolean nextWord;



    public Game(Resources resources, int dictionaryDescriptor, float scrWidth, float scrHeight) {
        paint = new Paint();
        bitmaps = new MyBitmaps(resources);
        dictionary = new Dictionary(resources.obtainTypedArray(dictionaryDescriptor));
        screenWidth = scrWidth;
        screenHeight = scrHeight;

        createUI();
    }

    public void click(float x, float y) {
        Character c = keyboard.click(x, y);
        if (c != null) {
            inputForm.insertCharacter(c);
            return;
        }
        if (!inputForm.isAnimating()) {
            inputForm.click(x, y);
        }
        quitButton.click(x, y);
        clearButton.click(x, y);
        skipButton.click(x, y);
    }

    public void update(float deltaTime) {
        keyboard.update(deltaTime);
        inputForm.update(deltaTime);
        quitButton.update(deltaTime);
        clearButton.update(deltaTime);
        skipButton.update(deltaTime);

        boolean inputFormFilled = inputForm.isFilled();

        if (inputFormFilled && !inputFormFillHandled) {
            inputFormFillHandled = true;

            if (inputForm.isFilledCorrectly()) {
                nextWord = true;
                dictionary.next();
                keyboard.destroyButtonLabels();
                inputForm.startExitAnimation(screenWidth);
            } else {
                inputForm.startShakeAnimation();
            }
        } else if (!inputFormFilled) {
            inputFormFillHandled = false;
        }

        if (nextWord) {
            if (keyboard.areLabelsDestroyed() && !inputForm.isAnimating()) {
                inputForm.startEnterAnimation(screenWidth);
                keyboard.generateButtonLabels(dictionary.getTranslation());
                //inputFormFillHandled = false;
                nextWord = false;
            }
        }
    }

    public void draw(Canvas canvas) {

    }
}
