package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class GameMenuElement extends MenuElement {
    private static float ELEMENT_WIDTH_RATIO = 0.6f;
    private static float DICTIONARY_NAME_LABEL_HEIGHT_RATIO_ORIG = 0.5f;
    private static float DICTIONARY_NAME_LABEL_HEIGHT_RATIO = 0.64f;
    private static float BEST_SCORE_LABEL_HEIGHT_RATIO_ORIG = 0.3f;
    private static float BEST_SCORE_LABEL_HEIGHT_RATIO = 0.36f;

    private String dictionaryName;
    private CenteredLabel dictionaryNameLabel;
    private CenteredLabel bestScoreLabel;
    private Paint paint;
    private boolean clicked;

    public GameMenuElement(String dictionaryName, Paint paint) {
        this.dictionaryName = dictionaryName;
        this.paint = paint;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    @Override
    public boolean click(float x, float y) {
        if (super.click(x, y)) {
            clicked = true;
            return true;
        }
        return false;
    }

    @Override
    public void setup(RectF area) {
        super.setup(cutArea(area));
        setupDictionaryNameLabel();
        setupBestScoreLabel();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (clicked) {
            paint.setColor(MyColors.GAME_MENU_ELEMENT_CLICK_COLOR);
            canvas.drawRect(getArea(), paint);
        }
        if (dictionaryName != null) {
            dictionaryNameLabel.draw(canvas, paint);
        }
        if (bestScoreLabel != null) {
            bestScoreLabel.draw(canvas, paint);
        }
    }

    private void setupDictionaryNameLabel() {
        RectF area = getArea();
        float dictionaryNameLabelHeight = area.height() * DICTIONARY_NAME_LABEL_HEIGHT_RATIO;
        RectF dictionaryNameLabelRect = new RectF(area.left, area.top, area.right, area.top + dictionaryNameLabelHeight);

        dictionaryNameLabel = new CenteredLabel(dictionaryName, paint, dictionaryNameLabelRect);
    }

    private void setupBestScoreLabel() {
        int bestScore = PhoneMemory.getBestScore(dictionaryName);
        String labelString = bestScore >= 0 ? "Correct words best streak: " + bestScore : "Not played yet";
        RectF area = getArea();
        float bestScoreLabelHeight = area.height() * BEST_SCORE_LABEL_HEIGHT_RATIO;
        RectF bestScoreLabelRect = new RectF(area.left, area.bottom - bestScoreLabelHeight, area.right, area.bottom);

        bestScoreLabel = new CenteredLabel(labelString, paint, bestScoreLabelRect);
    }

    private static RectF cutArea(RectF area) {
        float areaWidthOrig = area.width();
        float areaHeightOrig = area.height();
        float areaWidth = areaWidthOrig * ELEMENT_WIDTH_RATIO;
        float areaHeight = areaHeightOrig * (DICTIONARY_NAME_LABEL_HEIGHT_RATIO_ORIG + BEST_SCORE_LABEL_HEIGHT_RATIO_ORIG);
        float horizontalOffset = (areaWidthOrig - areaWidth) / 2.f;
        float verticalOffset = (areaHeightOrig - areaHeight) / 2.f;

        area.left += horizontalOffset;
        area.right -= horizontalOffset;
        area.top += verticalOffset;
        area.bottom -= verticalOffset;

        return area;
    }
}
