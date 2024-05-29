package com.example.spidermangame.Logic;

import java.util.Random;

public class GameManager {
    private int score;
    private int health;
    public static final int VILLAIN_ROWS = 5;
    public static final int VILLAIN_COLS = 5;
    private static final int TOP_RIGHT = VILLAIN_COLS - 1;
    private final Random rand = new Random();
    private int currentIndexHero = VILLAIN_COLS / 2;
    private int[][] mainTypeMatrix;

    public GameManager(int health) {
        this.health = health;
        this.score = 0;
        initMatrixType();
    }

    public int getHealth() {
        return health;
    }


    public void decreaseHealth() {
        health--;
    }

    public void increaseHealth() {
        health++;
    }

    private void initMatrixType() {
        mainTypeMatrix = new int[VILLAIN_ROWS][VILLAIN_COLS];
        for (int[] row : mainTypeMatrix) {
            java.util.Arrays.fill(row, -1);
        }
    }

    public int getMain_type_matrix(int row, int col) {
        return mainTypeMatrix[row][col];
    }

    public int[][] getMatrix() {
        return mainTypeMatrix;
    }

    public void addScore() {
        score += rand.nextInt(50) + 10;
    }

    public void setMainTypeMatrix(int row, int col, int type) {
        mainTypeMatrix[row][col] = type;
    }

    public int getMainTypeMatrix(int row, int col) {
        return mainTypeMatrix[row][col];
    }

    public int getCurrentIndexHero() {
        return currentIndexHero;
    }

    public void moveHero(int direction) {
        if (direction == 1 && currentIndexHero < TOP_RIGHT) {
            currentIndexHero++;
        } else if (direction == -1 && currentIndexHero > 0) {
            currentIndexHero--;
        }
    }

    public int randomViewImage() {
        int num =  rand.nextInt(VILLAIN_COLS-2); // 0-4
        return num;
    }

    public int randTypeImage() {
        // 0 - villain, 1 - heart, 2 - web score
        int num = rand.nextInt(10);
        if (num < 6) {
            return 0;
        } else if (num < 8) {
            return 2;
        } else {
            return 1;
        }
    }

    public void gameOver() {
        System.out.println("Game Over");
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
