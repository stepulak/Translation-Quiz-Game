package com.stepulak.translationgame;

public class Timer extends StatsElement {
    public static final int DEFAULT_TIME_SECONDS = 20;
    public static final int CORRECT_WORD_ADD_TIME = 8;
    public static final int SKIP_WORD_SUBTRACT_TIME = -3;

    private float remainingTime;
    private float nextLabelUpdateTime;
    private boolean frozen;

    Timer(int remainingTime, float x, float y, float counterTextSize, float labelTextSize) {
        super(x, y, counterTextSize, labelTextSize);
        this.remainingTime = remainingTime;
        nextLabelUpdateTime = remainingTime - 1.f;
        updateLabels();
    }

    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
    }

    public void addTime(int seconds) {
        remainingTime += seconds;
    }

    public boolean hasExceeded() {
        return remainingTime <= 0.f;
    }

    @Override
    void update(float deltaTime) {
        if (frozen) {
            return;
        }
        remainingTime = Math.max(remainingTime - deltaTime, 0.f);
        if (remainingTime <= nextLabelUpdateTime) {
            updateLabels();
            nextLabelUpdateTime -= 1.f;
        }
    }

    private void updateLabels() {
        int remainingTimeInSeconds = (int)remainingTime;
        setCounter(Integer.toString(remainingTimeInSeconds));
        if (remainingTimeInSeconds == 1) {
            setLabel(" second remaining");
        } else {
            setLabel(" seconds remaining");
        }
    }
}
