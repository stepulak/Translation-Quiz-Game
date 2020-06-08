package com.stepulak.translationgame;

import android.content.Context;
import android.content.SharedPreferences;

public class PhoneMemory {
    private static SharedPreferences sharedPreferences;

    private PhoneMemory() {
    }

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static boolean putBestScore(String dictionaryName, int score) {
        if (score <= getBestScore(dictionaryName)) {
            return false;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(dictionaryName, score);
        editor.commit();
        return true;
    }

    public static int getBestScore(String dictionaryName) {
        return sharedPreferences.getInt(dictionaryName, -1);
    }
}
