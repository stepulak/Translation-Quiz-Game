package com.stepulak.translationgame;

import android.content.res.TypedArray;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

public class Dictionary {
    public class Translation {
        private String originalWord;
        private String translatedWord;
        private String translatedWordWithoutFormatting;

        public Translation(String original, String translated) {
            originalWord = original;
            translatedWord = translated;
            translatedWordWithoutFormatting = translated.replace("~", "").replace(" ", "");
        }

        public String getOriginalWord() {
            return originalWord;
        }

        public String getTranslatedWord() {
            return translatedWord;
        }

        public String getTranslatedWordWithoutFormatting() {
            return translatedWordWithoutFormatting;
        }
    }

    private ArrayList<Translation> dict;
    private int index = 0;

    public Dictionary(TypedArray resource) {
        assert(resource.length() > 0);

        dict = new ArrayList<>(resource.length());

        for (int i = 0; i < resource.length(); i++) {
            String str = resource.getString(i);
            String[] parts = str.split(";");

            assert(parts.length == 2);

            dict.add(new Translation(parts[0], parts[1]));
        }
    }

    public Translation getTranslation() {
        return dict.get(index);
    }

    public void shuffle() {
        Collections.shuffle(dict);
    }

    public void next() {
        index++;
        if (index >= dict.size()) {
            index = 0;
        }
    }

    public void reset() {
        index = 0;
    }
}
