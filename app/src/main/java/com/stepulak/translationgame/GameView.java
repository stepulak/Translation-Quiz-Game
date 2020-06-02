package com.stepulak.translationgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Game game;
    private long lastUpdateTime = -1;
    private float clickX = -1;
    private float clickY = -1;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickX >= 0f && clickY >= 0f) {
                    game.click(clickX, clickY);
                }
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    clickX = motionEvent.getX();
                    clickY = motionEvent.getY();
                }
                return false;
            }
        });

        newGame();
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
        game.draw(canvas);
    }

    public void update() {
        long time = System.currentTimeMillis();
        if (lastUpdateTime > 0) {
            float deltaTime = (time - lastUpdateTime) / 1000.0f;
            game.update(deltaTime);
        }
        lastUpdateTime = time;

        if (game.toRestart()) {
            newGame();
        }
    }

    public void pause() {
        if (thread != null) {
            thread.setRunning(false);
        }
    }

    public boolean toQuit() {
        return game.toQuit();
    }

    private Point getScreenSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private void newGame() {
        Point screenSize = getScreenSize();
        game = new Game(getContext(), R.array.czech_german, screenSize.x, screenSize.y);
    }
}
