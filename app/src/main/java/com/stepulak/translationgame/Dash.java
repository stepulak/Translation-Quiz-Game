package com.stepulak.translationgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Dash extends UIElement {
    private static final float SIZE_RATIO = 0.96f;

    private Bitmap bitmap;
    private RectF rect;

    public Dash(Bitmap b, float x, float y, float width, float height) {
        bitmap = b;

        float newWidth = width * SIZE_RATIO;
        float newHeight = height * SIZE_RATIO;
        float offsetX = (width - newWidth) / 2;
        float offsetY = (height - newHeight) / 2;

        rect = new RectF(x + offsetX, y + offsetY, x + offsetX + newWidth, y + offsetY + newHeight);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(MyColors.BUTTON_COLOR);
        canvas.drawBitmap(bitmap, null, rect, paint);
    }
}
