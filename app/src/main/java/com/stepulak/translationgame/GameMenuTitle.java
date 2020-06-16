package com.stepulak.translationgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class GameMenuTitle extends UIElement {
    private List<CenteredLabel> labels;

    public GameMenuTitle(RectF area, Paint paint, List<String> labels) {
        this.labels = new ArrayList<>(labels.size());
        float labelHeight = area.height() / labels.size();
        float labelY = area.top;

        for (String label : labels) {
            RectF labelArea = new RectF(area.left, labelY, area.right, labelY + labelHeight);
            this.labels.add(new CenteredLabel(label, paint, labelArea));
            labelY += labelHeight;
        }
    }

    @Override
    void draw(Canvas canvas, Paint paint) {
        for (CenteredLabel label : labels) {
            label.draw(canvas, paint);
        }
    }
}
