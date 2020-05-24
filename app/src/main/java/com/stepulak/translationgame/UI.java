package com.stepulak.translationgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.RectF;

import static com.stepulak.translationgame.MyUIConstants.*;

public class UI {
    private UIManager uiManager = new UIManager();
    private MyBitmaps bitmaps;
    private float screenWidth;
    private float screenHeight;

    public UI(Resources resources, float scrWidth, float scrHeight) {
        bitmaps = new MyBitmaps(resources);
        screenWidth = scrWidth;
        screenHeight = scrHeight;

        createBaseUI();
    }

    public UIManager getUIManager() {
        return uiManager;
    }

    public void createUIForTranslation(Translation translation) {
        RectF inputFormPosition = getInputFormPosition();
        inputForm = new InputForm(dictionary.getTranslation(), bitmaps.get(MyBitmaps.BitmapType.LABEL_BUTTON), bitmaps.get(MyBitmaps.BitmapType.DASH), inputFormX, inputFormY, buttonWidth, buttonHeight);
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

    private RectF getSkipButtonPosition() {
        float width = screenWidth * ACTION_BUTTON_WIDTH_RATIO;
        float height = screenHeight * ACTION_BUTTON_HEIGHT_RATIO;
        float x = screenWidth * SKIP_BUTTON_X_POSITION_RATIO - width / 2.f;
        float y = getKeyboardPosition().top - height * ACTION_BUTTON_BOTTOM_OFFSET_RATIO;

        return new RectF(x, y, x + width, y + height);
    }

    private RectF getClearButtonPosition() {
        float width = screenWidth * ACTION_BUTTON_WIDTH_RATIO;
        float height = screenHeight * ACTION_BUTTON_HEIGHT_RATIO;
        float x = screenWidth * CLEAR_BUTTON_X_POSITION_RATIO - width / 2.f;
        float y = getKeyboardPosition().top - height * ACTION_BUTTON_BOTTOM_OFFSET_RATIO;

        return new RectF(x, y, x + width, y + height);
    }

    private RectF getInputFormPosition() {
        float maxWidth = screenWidth * INPUT_SCREEN_WIDTH_RATIO;
        float maxHeight = screenHeight * INPUT_SCREEN_HEIGHT_RATIO;
        float x = (screenWidth - maxWidth) / 2f;
        float y = getClearButtonPosition().top - maxHeight - screenHeight * INPUT_SCREEN_HEIGHT_OFFSET_RATIO;

        return new RectF(x, y, x + maxWidth, y + maxHeight);
    }

    private RectF getLabelPosition() {
        float width = ;
        float height = ;
        float x = ;
        float y = ;
    }

    private void createBaseUI() {
        RectF keyboardPosition = getKeyboardPosition();
        RectF quitButtonPosition = getQuitButtonPosition();
        RectF skipButtonPosition = getSkipButtonPosition();
        RectF clearButtonPosition = getClearButtonPosition();

        Bitmap keyboardBitmap = bitmaps.get(MyBitmaps.BitmapType.LABEL_BUTTON);
        Bitmap quitButtonBitmap = bitmaps.get(MyBitmaps.BitmapType.QUIT_BUTTON);
        Bitmap skipButtonBitmap = bitmaps.get(MyBitmaps.BitmapType.SKIP_BUTTON);
        Bitmap clearButtonBitmap = bitmaps.get(MyBitmaps.BitmapType.CLEAR_BUTTON);

        uiManager.add(UIElementType.BACKGROUND, new Background(screenWidth, screenHeight));
        uiManager.add(UIElementType.KEYBOARD, new Keyboard(keyboardBitmap, keyboardPosition));
        uiManager.add(UIElementType.QUIT_BUTTON, new Button(quitButtonBitmap, quitButtonPosition));
        uiManager.add(UIElementType.SKIP_BUTTON, new Button(skipButtonBitmap, skipButtonPosition));
        uiManager.add(UIElementType.CLEAR_BUTTON, new Button(clearButtonBitmap, clearButtonPosition));

        float labelWidth = screenWidth * 0.9f;
        float labelHeight = buttonHeight * 1.5f;
        float labelX = (screenWidth - labelWidth) / 2.f;
        float labelY = inputFormY - labelHeight - buttonHeight * 0.2f;

        wordLabel = new Label(dictionary.getTranslation().getOriginalWord(), paint, labelX, labelY, labelWidth, labelHeight);
    }
}
