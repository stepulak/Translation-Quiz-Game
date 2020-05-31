package com.stepulak.translationgame;

public class WordCounter extends StatsElement {
    private int correctWordCounter;

    public WordCounter(float x, float y, float counterTextSize, float labelTextSize) {
        super(x, y, counterTextSize, labelTextSize);
        updateLabels();
    }

    public void increaseCounter() {
        correctWordCounter++;
        updateLabels();
    }

    private void updateLabels() {
        setCounter(Integer.toString(correctWordCounter));
        if (correctWordCounter == 1) {
            setLabel(" correct word");
        } else {
            setLabel(" correct words");
        }
    }
}
