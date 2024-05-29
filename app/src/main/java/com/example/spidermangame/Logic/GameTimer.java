package com.example.spidermangame.Logic;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    private final GameManager gameManager;
    private final GameViewManager gameViewManager;
    private final Timer timer;
    private static final int DELAY = 1000;
    private int time = 0;

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
                    if (time % 2 != 0) {
                        createRandomObj();
                    }
                    time++;
                });
            }
        }, 0, DELAY);
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
}
