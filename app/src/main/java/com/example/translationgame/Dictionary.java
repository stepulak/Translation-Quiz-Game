package com.example.translationgame;

import android.content.res.TypedArray;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

public class Dictionary {
    public class Word {
        private String original;
        private String translated;
        private String translatedWithoutFormatting;

        public Word(String original, String translated) {
            this.original = original;
            this.translated = translated;
            translatedWithoutFormatting = translated.replace("~", "");
        }

        public String getOriginal() {
            return original;
        }

        public String getTranslated() {
            return translated;
        }

        public String getTranslatedWithoutFormatting() {
            return translatedWithoutFormatting;
        }
    }

    private ArrayList<Word> dict;
    private int index = 0;

    public Dictionary(TypedArray resource) {
        assert(resource.length() > 0);

        dict = new ArrayList<>(resource.length());

        for (int i = 0; i < resource.length(); i++) {
            String str = resource.getString(i);
            String[] parts = str.split(";");

            assert(parts.length == 2);

            dict.add(new Word(parts[0], parts[1]));
        }
    }

    public Word getWord() {
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
