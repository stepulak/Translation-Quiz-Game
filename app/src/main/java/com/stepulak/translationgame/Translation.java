package com.stepulak.translationgame;

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
