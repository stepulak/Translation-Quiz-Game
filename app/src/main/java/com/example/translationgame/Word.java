package com.example.translationgame;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Word {
    private String word;
    private List<Pair<String, Boolean>> components;

    public Word(String str) {
        word = str;
        components = decompose(str);
    }

    public List<Pair<String, Boolean>> getComponents() {
        return Collections.unmodifiableList(components);
    }

    public boolean match(String str) {
        return word.equals(str);
    }

    private static List<Pair<String, Boolean>> decompose(String str) {
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
