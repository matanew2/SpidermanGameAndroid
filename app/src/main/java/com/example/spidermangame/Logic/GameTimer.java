package com.example.spidermangame.Logic;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    private final GameManager gameManager;
    private final GameViewManager gameViewManager;
    private Timer timer;
    private int initialDelay = 1000; // Initial delay
    private int time = 0;
    private static final int DELAY_INCREMENT_INTERVAL = 20; // 10 seconds

    private static final int REDUCE_DELAY = 50; // 50 milliseconds

    public GameTimer(GameManager gameManager, GameViewManager gameViewManager) {
        this.gameManager = gameManager;
        this.gameViewManager = gameViewManager;
        this.timer = new Timer();
    }

    public void startGame() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    play();
                    if (time % DELAY_INCREMENT_INTERVAL == 0) {
                        if (initialDelay > 300)
                            updateDelay(initialDelay - REDUCE_DELAY);
                    }
                    System.out.println("Time: " + time+ " Delay: " + initialDelay);
                    if (time % 2 != 0) {
                        createRandomObj();
                    }
                    time++;
                });
            }
        }, 0, initialDelay);
    }

    private void createRandomObj() {
        int row = 0;
        int col = gameManager.randomViewImage();
        int type = gameManager.randTypeImage();
        gameViewManager.setImage(row, col, type);
        gameViewManager.game_IMG_villains[0][col].setVisibility(View.VISIBLE);
    }

    private void play() {
        gameViewManager.detectAndHandleCollision();
        checkHealthHero();
        gameViewManager.updateVillains();
    }

    private void checkHealthHero() {
        if (gameManager.getHealth() == 0) {
            stopGame();
            gameOver();
        }
    }

    private void gameOver() {
        runOnUiThread(gameViewManager::gameOver);
    }

    private void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void stopGame() {
        timer.cancel();
    }

    private void updateDelay(int newDelay) {
        initialDelay = newDelay;
        restartTimer();
    }

    private void restartTimer() {
        timer.cancel();
        timer = new Timer();
        startGame();
    }
}
