package com.stepulak.translationgame;

import android.content.res.TypedArray;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dictionary {
    private String name;
    private List<Translation> translations;
    private int index = 0;

    public Dictionary(TypedArray resource, String name) {
        if (resource.length() <= 0) {
            throw new InvalidParameterException("Given resource is empty!");
        }
        this.name = name;
        translations = parseTranslations(resource);
    }

    public String getName() {
        return name;
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
