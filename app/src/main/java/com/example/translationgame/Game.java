package com.example.translationgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Game {
    // UI proportions ratios
    private static final float KEYBOARD_WIDTH_RATIO = 0.9f;
    private static final float KEYBOARD_HEIGHT_RATIO = 0.2892f;
    private static final float KEYBOARD_BOTTOM_OFFSET_RATIO = 0.025f;

    // UI proportions and positions

    // UI elements
    private Background background;
    private Keyboard keyboard;
    private InputForm inputForm;
    private Label wordLabel;
    private Button quitButton;
    private Button skipButton;
    private Button clearButton;

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
        background.draw(canvas, paint);
        keyboard.draw(canvas, paint);
        inputForm.draw(canvas, paint);
        quitButton.draw(canvas, paint, 1.f);
        clearButton.draw(canvas, paint, 1.f);
        skipButton.draw(canvas, paint, 1.f);
        wordLabel.draw(canvas, paint);
    }

    private void createKeyboard() {
    }

    private void setupUIPositionsAndProportions() {
        keyboardWidth = screenWidth * KEYBOARD_WIDTH_RATIO;
        keyboardHeight = screenHeight * KEYBOARD_HEIGHT_RATIO;
        keyboardX = (screenWidth - keyboardWidth) / 2f;
        keyboardY = screenHeight - keyboardHeight - screenHeight * KEYBOARD_BOTTOM_OFFSET_RATIO;


    }

    private void createUI() {
        background = new Background(screenWidth, screenHeight);

        float keyboardWidth = screenWidth * 0.9f;
        float keyboardHeight = screenHeight * 0.2892f;
        float keyboardX = (screenWidth - keyboardWidth) / 2f;
        float keyboardY = screenHeight - keyboardHeight - screenHeight * 0.025f;

        float buttonWidth = keyboardWidth / Keyboard.BUTTONS_PER_WIDTH;
        float buttonHeight = keyboardHeight / Keyboard.BUTTONS_PER_HEIGHT;

        keyboard = new Keyboard(bitmaps.get(MyBitmaps.BitmapType.LABEL_BUTTON), keyboardX, keyboardY, buttonWidth, buttonHeight);
        keyboard.generateButtonLabels(dictionary.getTranslation());

        float quitButtonWidth = screenWidth * 0.07f;
        float quitButtonHeight = screenHeight * 0.0392f;
        float quitButtonX = screenWidth - quitButtonWidth;

        quitButton = new Button(bitmaps.get(MyBitmaps.BitmapType.QUIT_BUTTON), quitButtonX, 0, quitButtonWidth, quitButtonHeight);

        float doneSkipButtonWidth = buttonWidth * 1.2f;
        float doneSkipButtonHeight = buttonHeight * 1.2f;
        float doneSkipButtonY = keyboardY - doneSkipButtonHeight * 1.2f;
        float skipButtonX = screenWidth * 0.35f - doneSkipButtonWidth / 2.f;
        float doneButtonX = screenWidth * 0.65f - doneSkipButtonWidth / 2.f;

        skipButton = new Button(bitmaps.get(MyBitmaps.BitmapType.SKIP_BUTTON), skipButtonX, doneSkipButtonY, doneSkipButtonWidth, doneSkipButtonHeight);
        clearButton = new Button(bitmaps.get(MyBitmaps.BitmapType.CLEAR_BUTTON), doneButtonX, doneSkipButtonY, doneSkipButtonWidth, doneSkipButtonHeight);

        float inputFormWidth = InputForm.WIDTH_IN_BUTTONS * buttonWidth;
        float inputFormX = (screenWidth - inputFormWidth) / 2f;
        float inputFormY = doneSkipButtonY - InputForm.HEIGHT_IN_BUTTONS * buttonHeight - doneSkipButtonHeight * 0.2f;

        //dictionary.shuffle();
        inputForm = new InputForm(dictionary.getTranslation(), bitmaps.get(MyBitmaps.BitmapType.LABEL_BUTTON), bitmaps.get(MyBitmaps.BitmapType.DASH), inputFormX, inputFormY, buttonWidth, buttonHeight);

        float labelWidth = screenWidth * 0.9f;
        float labelHeight = buttonHeight * 1.5f;
        float labelX = (screenWidth - labelWidth) / 2.f;
        float labelY = inputFormY - labelHeight - buttonHeight * 0.2f;

        wordLabel = new Label(dictionary.getTranslation().getOriginalWord(), paint, labelX, labelY, labelWidth, labelHeight);
    }
}
