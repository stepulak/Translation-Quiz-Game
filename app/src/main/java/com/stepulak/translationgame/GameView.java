package com.stepulak.translationgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final float NEXT_RUNNABLE_ANIMATION_VELOCITY_RATIO = 2.f;
    private static final float MOTION_OR_CLICK_TIMER = 0.5f;
    private static final float MOTION_OR_CLICK_MAX_DISTANCE = 50.f;

    private float screenWidth;
    private float screenHeight;
    private PointF currentTouchPosition;
    private PointF originalTouchPosition;
    private PointF motion;
    private PointF overallMotionDistance;
    private float motionTimer;
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
        MyBitmaps.setup(context.getResources());
        currentGameRunnable = new GameMenu(context, screenWidth, screenHeight);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        restart();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
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
            updateLogic(deltaTime);
        }
        lastUpdateTime = time;
    }

    public void pause() {
        if (thread != null) {
            thread.setRunning(false);
        }
    }

    public void restart() {
        if (thread != null) {
            stop();
        }
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    private void stop() {
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

    private void updateLogic(float deltaTime) {
        currentGameRunnable.update(deltaTime);
        if (motion != null) {
            currentGameRunnable.motionTouch(originalTouchPosition.x, originalTouchPosition.y, motion.x, motion.y);
            motion.set(0.f, 0.f);
            motionTimer += deltaTime;
        }
        updateGameRunnableSwitching(deltaTime);
    }

    private void switchGameRunnable(GameRunnable nextRunnable) {
        float animationVelocity = NEXT_RUNNABLE_ANIMATION_VELOCITY_RATIO * screenHeight;
        nextGameRunnableAnimation = new TranslationAnimation(null, -screenHeight, 0.f, animationVelocity, 0.f);
        nextGameRunnable = nextRunnable;
    }

    private void updateGameRunnableSwitching(float deltaTime) {
        if (nextGameRunnable == null && nextGameRunnableAnimation == null) {
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
                onClickListener();
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onTouchListener(motionEvent);
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

    private void onClickListener() {
        if (currentGameRunnable == null || originalTouchPosition == null ||
                overallMotionDistance == null || motionTimer > MOTION_OR_CLICK_TIMER) {
            return;
        }
        float squareSum = overallMotionDistance.x * overallMotionDistance.x +
                overallMotionDistance.y * overallMotionDistance.y;
        if ((float)Math.sqrt(squareSum) > MOTION_OR_CLICK_MAX_DISTANCE) {
            return;
        }
        currentGameRunnable.click(originalTouchPosition.x, originalTouchPosition.y);
        originalTouchPosition = null;
        overallMotionDistance = null;
    }

    private void onTouchListener(MotionEvent motionEvent) {
        float currentTouchX = motionEvent.getX();
        float currentTouchY = motionEvent.getY();
        if (currentTouchPosition != null && motion != null) {
            float distanceX = currentTouchX - currentTouchPosition.x;
            float distanceY = currentTouchY - currentTouchPosition.y;
            motion.x += distanceX;
            motion.y += distanceY;
            overallMotionDistance.x += distanceX;
            overallMotionDistance.y += distanceY;
            currentTouchPosition.x = currentTouchX;
            currentTouchPosition.y = currentTouchY;
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            originalTouchPosition = new PointF(currentTouchX, currentTouchY);
            currentTouchPosition = new PointF(currentTouchX, currentTouchY);
            motion = new PointF(0.f, 0.f);
            overallMotionDistance = new PointF(0.f, 0.f);
            motionTimer = 0.f;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            currentTouchPosition = null;
            motion = null;
        }
    }
}
