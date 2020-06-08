package com.stepulak.translationgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static float NEXT_RUNNABLE_ANIMATION_VELOCITY_RATIO = 2.f;

    private float screenWidth;
    private float screenHeight;
    private float touchX = -1f;
    private float touchY = -1f;
    private float motionX = 0f;
    private float motionY = 0f;
    private boolean touchDown;
    private long lastUpdateTime = -1;

    private MainThread thread;
    private GameRunnable currentGameRunnable;
    private GameRunnable nextGameRunnable;
    private TranslationAnimation nextGameRunnableAnimation;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        setupClickListeners();
        setupScreenSize();
        currentGameRunnable = new GameMenu(context, screenWidth, screenHeight);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread != null) {
            surfaceDestroyed(holder);
        }
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                if (thread != null) {
                    thread.setRunning(false);
                    thread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (currentGameRunnable != null) {
            currentGameRunnable.draw(canvas);
            if (nextGameRunnableAnimation != null && nextGameRunnable != null) {
                canvas.save();
                canvas.translate(0.f, nextGameRunnableAnimation.getCurrentPosition());
                nextGameRunnable.draw(canvas);
                canvas.restore();
            }
        }
    }

    public boolean toQuit() {
        return currentGameRunnable == null;
    }

    public void update() {
        long time = System.currentTimeMillis();
        if (currentGameRunnable != null && lastUpdateTime > 0) {
            float deltaTime = (time - lastUpdateTime) / 1000.0f;
            currentGameRunnable.update(deltaTime);
            currentGameRunnable.touch(motionX, motionY);
            motionX = motionY = 0.f;
            updateGameRunnableSwitching(deltaTime);
        }
        lastUpdateTime = time;
    }

    public void pause() {
        if (thread != null) {
            thread.setRunning(false);
        }
    }

    private void switchGameRunnable(GameRunnable nextRunnable) {
        float animationVelocity = NEXT_RUNNABLE_ANIMATION_VELOCITY_RATIO * screenHeight;
        nextGameRunnableAnimation = new TranslationAnimation(null, -screenHeight, 0.f, animationVelocity, 0.f);
        nextGameRunnable = nextRunnable;
    }

    private void updateGameRunnableSwitching(float deltaTime) {
        if (nextGameRunnable == null) {
            if (currentGameRunnable.moveToNextGameRunnable()) {
                switchGameRunnable(currentGameRunnable.createNextGameRunnable());
            } else if (currentGameRunnable.moveToPreviousGameRunnable()) {
                switchGameRunnable(currentGameRunnable.createPreviousGameRunnable());
            }
        }
        if (nextGameRunnableAnimation != null) {
            nextGameRunnableAnimation.update(deltaTime);
            if (nextGameRunnableAnimation.expired()) {
                currentGameRunnable = nextGameRunnable;
                nextGameRunnable = null;
                nextGameRunnableAnimation = null;
            }
        }
    }

    private void setupClickListeners() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentGameRunnable != null && touchX >= 0f && touchY >= 0f) {
                    currentGameRunnable.click(touchX, touchY);
                }
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float currentTouchX = motionEvent.getX();
                float currentTouchY = motionEvent.getY();
                if (touchDown && touchX >= 0f && touchX >= 0f) {
                    motionX += currentTouchX - touchX;
                    motionY += currentTouchY - touchY;
                }
                touchX = currentTouchX;
                touchY = currentTouchY;
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    touchDown = true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    touchDown = false;
                }
                return false;
            }
        });
    }

    private void setupScreenSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }
}
