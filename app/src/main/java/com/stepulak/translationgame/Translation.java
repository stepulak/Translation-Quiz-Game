package com.stepulak.translationgame;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Pair<String, Boolean>> decomposeTranslatedString(String str) {
        List<Pair<String, Boolean>> result = new ArrayList<>();
        int start = 0;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c == ' ' || c == '~') {
                result.add(new Pair<>(str.substring(start, i), c == '~'));
                start = i + 1;
            }
        }
        // Rest
        if (start < str.length()) {
            result.add(new Pair<>(str.substring(start), false));
        }

        return result;
    }
}
