package com.stepulak.translationgame;

import android.content.res.TypedArray;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dictionary {
    private List<Translation> translations;
    private String name;
    private String alphabet;
    private int index = 0;

    public Dictionary(TypedArray resource, String name, String alphabet) {
        if (resource.length() <= 0) {
            throw new InvalidParameterException("Given resource is empty!");
        }
        this.translations = parseTranslations(resource);
        this.name = name;
        this.alphabet = alphabet;
    }

    public String getName() {
        return name;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public Translation getTranslation() {
        return translations.get(index);
    }

    public void shuffle() {
        Collections.shuffle(translations);
    }

    public void next() {
        index++;
        if (index >= translations.size()) {
            index = 0;
        }
    }

    private static List<Translation> parseTranslations(TypedArray resource) {
        List<Translation> trans = new ArrayList<>();
        for (int i = 0; i < resource.length(); i++) {
            String str = resource.getString(i);
            String[] parts = str.split(";");

            if (parts.length != 2) {
                throw new InvalidParameterException("Given translation: " + str + " is in invalid format");
            }
            trans.add(new Translation(parts[0], parts[1]));
        }
        return trans;
    }
}
