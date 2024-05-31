package com.example.spidermangame.Logic;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class GameController {

    private final GameManager gameManager;
    private final GameView gameView;
    private final Timer timer;
    private int time = 0;

    public GameController(GameManager gameManager, GameView gameView) {
        this.gameManager = gameManager;
        this.gameView = gameView;
        this.timer = new Timer();
    }

    public void startGame() {
        // Initial delay
        int initialDelay = 1000;
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
        }, 0, initialDelay);
    }

    private void createRandomObj() {
        int row = 0;
        int col = gameManager.randomViewImage();
        int type = gameManager.randTypeImage();
        gameView.setImage(row, col, type);
        gameView.getGridLayout().getChildAt(col).setVisibility(View.VISIBLE);
    }

    private void play() {
        gameView.detectAndHandleCollision();
        checkHealthHero();
        gameView.updateViewObjects();
    }

    private void checkHealthHero() {
        if (gameManager.getHealth() == 0) {
            stopGame();
            gameOver();
        }
    }

    private void gameOver() {
        runOnUiThread(gameView::gameOver);
    }

    private void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void stopGame() {
        timer.cancel();
    }

}
