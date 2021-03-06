package com.stepulak.translationgame;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.stepulak.translationgame.MyUIConstants.*;

public class GameMenu extends GameRunnable {
    private class DictionaryEntity {
        String alphabet;
        int resourceDescriptor;

        public DictionaryEntity(String alphabet, int resourceDescriptor) {
            this.alphabet = alphabet;
            this.resourceDescriptor = resourceDescriptor;
        }
    }

    private static final List<String> GAME_MENU_TITLES = Arrays.asList("Translation", "Quiz Game");
    private static final String GERMAN_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜß";
    private static final String ENGLISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CZECH_ALPHABET = "AÁBCČDĎEÉĚFGHIÍJKLMNŇOÓPQRŘSŠTŤUÚŮVWXYÝZŽ";

    private Map<String, DictionaryEntity> dictionaryEntities = new TreeMap<>();
    private UIManager uiManager;
    private boolean toQuit;

    public GameMenu(Context context, float screenWidth, float screenHeight) {
        super(context, screenWidth, screenHeight);
        setupDictionaries();
        setupUI();
    }

    @Override
    public GameRunnable createNextGameRunnable() {
        GameMenuElement element = (GameMenuElement)uiManager
                        .<VerticalMenu>get(UIElementType.VERTICAL_MENU)
                        .getLastClickedElement();

        if (element == null) {
            return null;
        }
        String dictionaryName = element.getDictionaryName();
        DictionaryEntity entity = dictionaryEntities.get(dictionaryName);
        TypedArray array = getContext().getResources().obtainTypedArray(entity.resourceDescriptor);
        Dictionary dictionary = new Dictionary(array, dictionaryName, entity.alphabet);

        return new Game(getContext(), getScreenWidth(), getScreenHeight(), dictionary);
    }

    @Override
    public GameRunnable createPreviousGameRunnable() {
        return null;
    }

    @Override
    public boolean moveToPreviousGameRunnable() {
        return toQuit;
    }

    @Override
    public boolean moveToNextGameRunnable() {
        return uiManager.<VerticalMenu>get(UIElementType.VERTICAL_MENU).wasClicked();
    }

    @Override
    public void motionTouch(float touchOrigX, float touchOrigY, float motionX, float motionY) {
        uiManager.motionTouchFirst(touchOrigX, touchOrigY, motionX, motionY);
    }

    @Override
    public void click(float x, float y) {
        uiManager.clickFirst(x, y);
    }

    @Override
    public void update(float deltaTime) {
        uiManager.updateAll(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        uiManager.drawAll(canvas, getPaint());
    }

    private void setupDictionaries() {
        dictionaryEntities.put("Czech-German: School",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.czech_german_school));
        dictionaryEntities.put("German-Czech: School",
                new DictionaryEntity(CZECH_ALPHABET, R.array.german_czech_school));
        dictionaryEntities.put("Czech-German: Home",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.czech_german_home));
        dictionaryEntities.put("German-Czech: Home",
                new DictionaryEntity(CZECH_ALPHABET, R.array.german_czech_home));
        dictionaryEntities.put("Czech-German: Irregular verbs",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.czech_german_irregular_verbs));
        dictionaryEntities.put("German-Czech: Irregular verbs",
                new DictionaryEntity(CZECH_ALPHABET, R.array.german_czech_irregular_verbs));
        dictionaryEntities.put("Czech-German: Mathematics",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.czech_german_mathematics));
        dictionaryEntities.put("German-Czech: Mathematics",
                new DictionaryEntity(CZECH_ALPHABET, R.array.german_czech_mathematics));
        dictionaryEntities.put("Czech-German: Food",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.czech_german_food));
        dictionaryEntities.put("German-Czech: Food",
                new DictionaryEntity(CZECH_ALPHABET, R.array.german_czech_food));
        dictionaryEntities.put("Czech-German: Mixed",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.czech_german));
        dictionaryEntities.put("German-Czech: Mixed",
                new DictionaryEntity(CZECH_ALPHABET, R.array.german_czech));
        dictionaryEntities.put("Czech-English: Computers",
                new DictionaryEntity(ENGLISH_ALPHABET, R.array.czech_english_computers));
        dictionaryEntities.put("English-Czech: Computers",
                new DictionaryEntity(CZECH_ALPHABET, R.array.english_czech_computers));
        dictionaryEntities.put("German-English: Time and days",
                new DictionaryEntity(ENGLISH_ALPHABET, R.array.german_english_dates));
        dictionaryEntities.put("German-English: Business",
                new DictionaryEntity(ENGLISH_ALPHABET, R.array.german_english_business));
        dictionaryEntities.put("German-English: Body",
                new DictionaryEntity(ENGLISH_ALPHABET, R.array.german_english_body));
        dictionaryEntities.put("English-German: Time and days",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.english_german_dates));
        dictionaryEntities.put("English-German: Business",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.english_german_business));
        dictionaryEntities.put("English-German: Body",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.english_german_body));
        dictionaryEntities.put("English-German: Animals",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.english_german_animals));
        dictionaryEntities.put("English-German: Vehicles",
                new DictionaryEntity(GERMAN_ALPHABET, R.array.english_german_vehicles));
        dictionaryEntities.put("German-English: Animals",
                new DictionaryEntity(ENGLISH_ALPHABET, R.array.german_english_animals));
        dictionaryEntities.put("German-English: Vehicles",
                new DictionaryEntity(ENGLISH_ALPHABET, R.array.german_english_vehicles));
    }

    private void setupUI() {
        uiManager = new UIManager();
        uiManager.set(UIElementType.BACKGROUND, new Background(getScreenWidth(), getScreenHeight()));
        float quitButtonBottom = setupQuitButton();
        float versionLabelBottom = setupVersionLabel();
        float gameMenuTitleBottom = setupGameMenuTitle(Math.max(quitButtonBottom, versionLabelBottom));
        float delimiterBottom = setupDelimiter(gameMenuTitleBottom);
        float chooseLabelBottom = setupChooseLabel(delimiterBottom);
        setupVerticalMenu(chooseLabelBottom);
    }

    private void setupVerticalMenu(float startY) {
        float screenWidth = getScreenWidth();
        float screenHeight = getScreenHeight();
        float width = screenWidth * VERTICAL_MENU_WIDTH_RATIO;
        float height = screenHeight - startY;
        float x = (screenWidth - width) / 2f;
        float y = startY + screenHeight * VERTICAL_MENU_HEIGHT_OFFSET_RATIO;
        RectF verticalMenuArea = new RectF(x, y, x + width, startY + height);
        VerticalMenu menu = new VerticalMenu(verticalMenuArea, height / VerticalMenu.VERTICAL_MENU_NUM_ELEMENTS_PER_SCREEN_DEFAULT);

        for (String dictionaryName : dictionaryEntities.keySet()) {
            GameMenuElement element = new GameMenuElement(dictionaryName, getPaint());
            menu.addElement(element);
        }

        uiManager.set(UIElementType.VERTICAL_MENU, menu);
    }

    private float setupQuitButton() {
        float screenWidth = getScreenWidth();
        float screenHeight = getScreenHeight();
        float width = screenWidth * QUIT_BUTTON_WIDTH_RATIO;
        float height = screenHeight * QUIT_BUTTON_HEIGHT_RATIO;
        RectF quitButtonRect = new RectF(screenWidth - width, 0.f, screenWidth, height);

        Button quitButton = new Button(MyBitmaps.get(MyBitmaps.BitmapType.QUIT_BUTTON), quitButtonRect);

        quitButton.setClickCallback(new Callback(){
            @Override
            public void apply() {
                toQuit = true;
            }
        });
        uiManager.set(UIElementType.QUIT_BUTTON, quitButton);

        return height;
    }

    private float setupVersionLabel() {
        float fontSize = getScreenHeight() * VERSION_LABEL_HEIGHT_RATIO;

        Label versionLabel = new Label("version: " + BuildConfig.VERSION_NAME, 0.f, 0.f, fontSize);
        uiManager.set(UIElementType.VERSION_LABEL, versionLabel);

        return fontSize;
    }

    private float setupGameMenuTitle(float startY) {
        float screenWidth = getScreenWidth();
        float screenHeight = getScreenHeight();
        float height = screenHeight * GAME_MENU_TITLE_LABELS_HEIGHT_RATIO;
        float y = startY + GAME_MENU_TITLE_LABELS_HEIGHT_OFFSET_RATIO * screenHeight;
        RectF area = new RectF(0.f, y, screenWidth, y + height);

        GameMenuTitle title = new GameMenuTitle(area, getPaint(), GAME_MENU_TITLES);
        uiManager.set(UIElementType.GAME_MENU_TITLE, title);

        return area.bottom;
    }

    private float setupChooseLabel(float startY) {
        float screenWidth = getScreenWidth();
        float screenHeight = getScreenHeight();
        float width = screenWidth * LANGUAGE_SET_LABEL_WIDTH_RATIO;
        float height = screenHeight * LANGUAGE_SET_LABEL_HEIGHT_RATIO;
        float x = (screenWidth - width) / 2.f;
        float y = startY + screenHeight * LANGUAGE_SET_LABEL_HEIGHT_OFFSET_RATIO;
        RectF area = new RectF(x, y, x + width, y  + height);

        CenteredLabel label = new CenteredLabel("Choose language set:", getPaint(), area);
        uiManager.set(UIElementType.LANGUAGE_SET_LABEL, label);

        return area.bottom;
    }

    private float setupDelimiter(float startY) {
        float screenWidth = getScreenWidth();
        float screenHeight = getScreenHeight();
        float width = DELIMITER_WIDTH_RATIO * screenWidth;
        float height = DELIMITER_HEIGHT_RATIO * screenHeight;
        float x = (screenWidth - width) / 2.f;
        float y = startY + DELIMITER_HEIGHT_OFFSET_RATIO * screenHeight;
        RectF area = new RectF(x, y, x + width, y + height);

        Delimiter delimiter = new Delimiter(area);
        uiManager.set(UIElementType.DELIMITER, delimiter);

        return y + height;
    }
}
