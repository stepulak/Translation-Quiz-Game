package com.example.translationgame;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Game {
    // TODO MOVE COLORS TO SEPARATE CLASS
    // TODO CHECK FINAL STATIC
    public static final int BACKGROUNDC_COLOR = Color.rgb(86, 81, 128);
    public static final int BUTTON_COLOR = Color.WHITE;
    public static final int BUTTON_TEXT_COLOR = BACKGROUNDC_COLOR;

    private Paint paint;
    private Keyboard keyboard;
    private InputForm inputForm;
    private Label label;
    private Button quitButton;
    private Button skipButton;
    private Button doneButton;

    public Game(Resources resources, float scrWidth, float scrHeight) {
        paint = new Paint();
        createUI(resources, scrWidth, scrHeight);
    }

    private void createUI(Resources resources, float scrWidth, float scrHeight) {
        Bitmap buttonBitmap = BitmapFactory.decodeResource(resources, R.drawable.button);
        Bitmap dashBitmap = BitmapFactory.decodeResource(resources, R.drawable.dash);
        Bitmap doneBitmap = BitmapFactory.decodeResource(resources, R.drawable.done);
        Bitmap quitBitmap = BitmapFactory.decodeResource(resources, R.drawable.quit);
        Bitmap skipBitmap = BitmapFactory.decodeResource(resources, R.drawable.skip);

        Dictionary dictionary = new Dictionary(resources.obtainTypedArray(R.array.czech_german));

        float keyboardWidth = scrWidth * 0.9f;
        float keyboardHeight = scrHeight * 0.2892f;
        float keyboardX = (scrWidth - keyboardWidth) / 2f;
        float keyboardY = scrHeight - keyboardHeight - scrHeight * 0.025f;

        float buttonWidth = keyboardWidth / Keyboard.BUTTONS_PER_WIDTH;
        float buttonHeight = keyboardHeight / Keyboard.BUTTONS_PER_HEIGHT;

        keyboard = new Keyboard(buttonBitmap, keyboardX, keyboardY, buttonWidth, buttonHeight);
        //keyboard.generateButtonLabels(dictionary.getTranslation());

        float quitButtonWidth = scrWidth * 0.07f;
        float quitButtonHeight = scrHeight * 0.0392f;
        float quitButtonX = scrWidth - quitButtonWidth;

        quitButton = new Button(quitBitmap, quitButtonX, 0, quitButtonWidth, quitButtonHeight);

        float doneSkipButtonWidth = buttonWidth * 1.2f;
        float doneSkipButtonHeight = buttonHeight * 1.2f;
        float doneSkipButtonY = keyboardY - doneSkipButtonHeight * 1.2f;
        float skipButtonX = scrWidth * 0.35f - doneSkipButtonWidth / 2.f;
        float doneButtonX = scrWidth * 0.65f - doneSkipButtonWidth / 2.f;

        skipButton = new Button(skipBitmap, skipButtonX, doneSkipButtonY, doneSkipButtonWidth, doneSkipButtonHeight);
        doneButton = new Button(doneBitmap, doneButtonX, doneSkipButtonY, doneSkipButtonWidth, doneSkipButtonHeight);

        float inputFormWidth = InputForm.WIDTH_IN_BUTTONS * buttonWidth;
        float inputFormX = (scrWidth - inputFormWidth) / 2f;
        float inputFormY = doneSkipButtonY - InputForm.HEIGHT_IN_BUTTONS * buttonHeight - doneSkipButtonHeight * 0.2f;

        //dictionary.shuffle();
        inputForm = new InputForm(dictionary.getTranslation(), buttonBitmap, dashBitmap, inputFormX, inputFormY, buttonWidth, buttonHeight);

        float labelWidth = scrWidth * 0.9f;
        float labelHeight = buttonHeight * 1.5f;
        float labelX = (scrWidth - labelWidth) / 2.f;
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
    }

    public void draw(Canvas canvas) {
        paint.setColor(BACKGROUNDC_COLOR);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
        keyboard.draw(canvas, paint);
        inputForm.draw(canvas, paint);
        quitButton.draw(canvas, paint, 1.f);
        doneButton.draw(canvas, paint, 1.f);
        skipButton.draw(canvas, paint, 1.f);
        label.draw(canvas, paint);
    }
}
