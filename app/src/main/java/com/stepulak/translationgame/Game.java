package com.stepulak.translationgame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import static com.stepulak.translationgame.MyUIConstants.*;

public class Game {
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

    private RectF getKeyboardPosition() {
        float width = screenWidth * KEYBOARD_WIDTH_RATIO;
        float height = screenHeight * KEYBOARD_HEIGHT_RATIO;
        float x = (screenWidth - width) / 2f;
        float y = screenHeight - height - screenHeight * KEYBOARD_BOTTOM_OFFSET_RATIO;

        return new RectF(x, y, x + width, y + height);
    }

    private RectF getQuitButtonPosition() {
        float width = screenWidth * QUIT_BUTTON_WIDTH_RATIO;
        float height = screenHeight * QUIT_BUTTON_HEIGHT_RATIO;
        float x = screenWidth - width;

        return new RectF(x, 0.f, x + width, height);
    }

    private RectF getSkipButtonPosition(float keyboardY) {
        float width = screenWidth * ACTION_BUTTON_WIDTH_RATIO;
        float height = screenHeight * ACTION_BUTTON_HEIGHT_RATIO;
        float x = screenWidth * SKIP_BUTTON_X_POSITION_RATIO - width / 2.f;
        float y = keyboardY - height * ACTION_BUTTON_BOTTOM_OFFSET_RATIO;

        return new RectF(x, y, x + width, y + height);
    }

    private RectF getClearButtonPosition(float keyboardY) {
        float width = screenWidth * ACTION_BUTTON_WIDTH_RATIO;
        float height = screenHeight * ACTION_BUTTON_HEIGHT_RATIO;
        float x = screenWidth * CLEAR_BUTTON_X_POSITION_RATIO - width / 2.f;
        float y = keyboardY - height * ACTION_BUTTON_BOTTOM_OFFSET_RATIO;

        return new RectF(x, y, x + width, y + height);
    }

    private RectF getInputFormPosition() {

    }


    private void createUI() {
        RectF keyboardPosition = getKeyboardPosition();
        RectF quitButtonPosition = getQuitButtonPosition();
        RectF clearButtonPosition = getClearButtonPosition(keyboardPosition.top);
        RectF skipButtonPosition = getSkipButtonPosition(keyboardPosition.top);

        background = new Background(screenWidth, screenHeight);

        keyboard = new Keyboard(bitmaps.get(MyBitmaps.BitmapType.LABEL_BUTTON), keyboardPosition);
        keyboard.generateButtonLabels(dictionary.getTranslation());

        quitButton = new Button(bitmaps.get(MyBitmaps.BitmapType.QUIT_BUTTON), quitButtonPosition);
        skipButton = new Button(bitmaps.get(MyBitmaps.BitmapType.SKIP_BUTTON), skipButtonPosition);
        clearButton = new Button(bitmaps.get(MyBitmaps.BitmapType.CLEAR_BUTTON), clearButtonPosition);

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
