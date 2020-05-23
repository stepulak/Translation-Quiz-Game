package com.example.translationgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.EnumMap;
import java.util.Map;

public class MyBitmaps {
    public enum BitmapType {
        LABEL_BUTTON,
        SKIP_BUTTON,
        CLEAR_BUTTON,
        QUIT_BUTTON,
        DASH,
    }

    private Map<BitmapType, Bitmap> bitmaps;

    public MyBitmaps(Resources resources) {
        bitmaps = new EnumMap<>(BitmapType.class);
        addBitmap(resources, BitmapType.LABEL_BUTTON, R.drawable.label_button);
        addBitmap(resources, BitmapType.SKIP_BUTTON, R.drawable.skip_button);
        addBitmap(resources, BitmapType.CLEAR_BUTTON, R.drawable.clear_button);
        addBitmap(resources, BitmapType.QUIT_BUTTON, R.drawable.quit_button);
        addBitmap(resources, BitmapType.DASH, R.drawable.dash);
    }

    public Bitmap get(BitmapType bitmapType) {
        Bitmap bitmap = bitmaps.get(bitmapType);
        if (bitmap == null) {
            throw new NullPointerException("Bitmap " + bitmapType.toString() + " not found!");
        }
        return bitmap;
    }

    private void addBitmap(Resources resources, BitmapType bitmapType, int resourceDescriptor) {
        bitmaps.put(bitmapType, BitmapFactory.decodeResource(resources, resourceDescriptor));
    }
}
