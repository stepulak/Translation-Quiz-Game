package com.stepulak.translationgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.core.util.Consumer;

import static com.stepulak.translationgame.MyUIConstants.*;

// This class holds UI
public class UI {
    private Paint paint = new Paint();
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

    public Paint getPaint() {
        return paint;
    }

    public UIManager getUIManager() {
        return uiManager;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public void createUIForTranslation(Translation translation) {
        Label label = new Label(translation.getOriginalWord(), paint, getLabelPosition());

        final InputForm inputForm = new InputForm(translation,
            bitmaps.get(MyBitmaps.BitmapType.LABEL_BUTTON),
            bitmaps.get(MyBitmaps.BitmapType.DASH),
            getInputFormPosition()
        );
        inputForm.startEnterAnimation(screenWidth);

        uiManager.set(UIElementType.INPUT_FORM, inputForm);
        uiManager.set(UIElementType.WORD_LABEL, label);
        uiManager.<Keyboard>get(UIElementType.KEYBOARD).generateButtonLabels(translation);

        uiManager.<Button>get(UIElementType.CLEAR_BUTTON).setClickCallback(new Callback() {
            @Override
            public void apply() {
                inputForm.clear();
            }
        });

        uiManager.<Button>get(UIElementType.SKIP_BUTTON).setClickCallback(new Callback() {
            @Override
            public void apply() {
                inputForm.skipWordAndFillResult();
            }
        });
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
        float maxWidth = screenWidth * INPUT_FORM_WIDTH_RATIO;
        float maxHeight = screenHeight * INPUT_FORM_HEIGHT_RATIO;
        float x = (screenWidth - maxWidth) / 2f;
        float y = getClearButtonPosition().top - maxHeight - screenHeight * INPUT_FORM_HEIGHT_OFFSET_RATIO;

        return new RectF(x, y, x + maxWidth, y + maxHeight);
    }

    private RectF getLabelPosition() {
        float width = screenWidth * LABEL_WIDTH_RATIO;
        float height = screenHeight * LABEL_HEIGHT_RATIO;
        float x = (screenWidth - width) / 2.f;
        float y = getInputFormPosition().top - height - screenHeight * LABEL_HEIGHT_OFFSET_RATIO;

        return new RectF(x, y, x + width, y + height);
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

        uiManager.set(UIElementType.BACKGROUND, new Background(screenWidth, screenHeight));
        uiManager.set(UIElementType.KEYBOARD, new Keyboard(keyboardBitmap, keyboardPosition));
        uiManager.set(UIElementType.QUIT_BUTTON, new Button(quitButtonBitmap, quitButtonPosition));
        uiManager.set(UIElementType.SKIP_BUTTON, new Button(skipButtonBitmap, skipButtonPosition));
        uiManager.set(UIElementType.CLEAR_BUTTON, new Button(clearButtonBitmap, clearButtonPosition));


    }
}
