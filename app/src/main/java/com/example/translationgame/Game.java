package com.example.translationgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Game {
    // TODO CHECK FINAL STATIC

    private Paint paint = new Paint();
    private Keyboard keyboard;
    private InputForm inputForm;
    private Label label;
    private Button quitButton;
    private Button skipButton;
    private Button doneButton;

    private float screenWidth;
    private float screenHeight;
    private boolean inputFormFillHandled;
    private boolean nextWord;

    private Dictionary dictionary;

    Bitmap buttonBitmap;
    Bitmap dashBitmap;
    Bitmap doneBitmap;
    Bitmap quitBitmap;
    Bitmap skipBitmap;

    public Game(Resources resources, float scrWidth, float scrHeight) {
        screenWidth = scrWidth;
        screenHeight = scrHeight;
        createUI(resources);
    }

    private void createUI(Resources resources) {
        buttonBitmap = BitmapFactory.decodeResource(resources, R.drawable.button);
        dashBitmap = BitmapFactory.decodeResource(resources, R.drawable.dash);
        doneBitmap = BitmapFactory.decodeResource(resources, R.drawable.done);
        quitBitmap = BitmapFactory.decodeResource(resources, R.drawable.quit);
        skipBitmap = BitmapFactory.decodeResource(resources, R.drawable.skip);

        dictionary = new Dictionary(resources.obtainTypedArray(R.array.czech_german));

        float keyboardWidth = screenWidth * 0.9f;
        float keyboardHeight = screenHeight * 0.2892f;
        float keyboardX = (screenWidth - keyboardWidth) / 2f;
        float keyboardY = screenHeight - keyboardHeight - screenHeight * 0.025f;

        float buttonWidth = keyboardWidth / Keyboard.BUTTONS_PER_WIDTH;
        float buttonHeight = keyboardHeight / Keyboard.BUTTONS_PER_HEIGHT;

        keyboard = new Keyboard(buttonBitmap, keyboardX, keyboardY, buttonWidth, buttonHeight);
        keyboard.generateButtonLabels(dictionary.getTranslation());

        float quitButtonWidth = screenWidth * 0.07f;
        float quitButtonHeight = screenHeight * 0.0392f;
        float quitButtonX = screenWidth - quitButtonWidth;

        quitButton = new Button(quitBitmap, quitButtonX, 0, quitButtonWidth, quitButtonHeight);

        float doneSkipButtonWidth = buttonWidth * 1.2f;
        float doneSkipButtonHeight = buttonHeight * 1.2f;
        float doneSkipButtonY = keyboardY - doneSkipButtonHeight * 1.2f;
        float skipButtonX = screenWidth * 0.35f - doneSkipButtonWidth / 2.f;
        float doneButtonX = screenWidth * 0.65f - doneSkipButtonWidth / 2.f;

        skipButton = new Button(skipBitmap, skipButtonX, doneSkipButtonY, doneSkipButtonWidth, doneSkipButtonHeight);
        doneButton = new Button(doneBitmap, doneButtonX, doneSkipButtonY, doneSkipButtonWidth, doneSkipButtonHeight);

        float inputFormWidth = InputForm.WIDTH_IN_BUTTONS * buttonWidth;
        float inputFormX = (screenWidth - inputFormWidth) / 2f;
        float inputFormY = doneSkipButtonY - InputForm.HEIGHT_IN_BUTTONS * buttonHeight - doneSkipButtonHeight * 0.2f;

        //dictionary.shuffle();
        inputForm = new InputForm(dictionary.getTranslation(), buttonBitmap, dashBitmap, inputFormX, inputFormY, buttonWidth, buttonHeight);

        float labelWidth = screenWidth * 0.9f;
        float labelHeight = buttonHeight * 1.5f;
        float labelX = (screenWidth - labelWidth) / 2.f;
        float labelY = inputFormY - labelHeight - buttonHeight * 0.2f;

        label = new Label(dictionary.getTranslation().getOriginalWord(), paint, labelX, labelY, labelWidth, labelHeight);
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
        doneButton.click(x, y);
        skipButton.click(x, y);
    }

    public void update(float deltaTime) {
        keyboard.update(deltaTime);
        inputForm.update(deltaTime);
        quitButton.update(deltaTime);
        doneButton.update(deltaTime);
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
        paint.setColor(MyColors.BACKGROUND_COLOR);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
        keyboard.draw(canvas, paint);
        inputForm.draw(canvas, paint);
        quitButton.draw(canvas, paint, 1.f);
        doneButton.draw(canvas, paint, 1.f);
        skipButton.draw(canvas, paint, 1.f);
        label.draw(canvas, paint);
    }
}
