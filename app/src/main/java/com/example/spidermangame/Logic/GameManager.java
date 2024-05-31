package com.example.spidermangame.Logic;

import java.util.Arrays;
import java.util.Random;

public class GameManager {
    private int score;
    private int health;
    private final int sizeMatrix;
    private final Random random;
    private final int[][] mainTypeMatrix;
    private int currentIndexHero;
    private static final int SCORE_INCREMENT_MIN = 10;
    private static final int SCORE_INCREMENT_MAX = 59; // Adjusted for better range
    private static final int VILLAIN_THRESHOLD = 6;
    private static final int HEART_THRESHOLD = 8;

    public GameManager(int health, int sizeMatrix) {
        this.sizeMatrix = sizeMatrix;
        this.health = health;
        this.currentIndexHero = sizeMatrix / 2;
        this.score = 0;
        this.random = new Random();
        this.mainTypeMatrix = new int[sizeMatrix][sizeMatrix];
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
        for (int[] row : mainTypeMatrix) {
            java.util.Arrays.fill(row, -1);
        }
    }

    public void addScore() {
        score += random.nextInt(SCORE_INCREMENT_MAX - SCORE_INCREMENT_MIN + 1) + SCORE_INCREMENT_MIN;
    }

    public void setTypeCellInMatrix(int row, int col, int type) {
        mainTypeMatrix[row][col] = type;
    }

    public void moveHero(int direction) {
        if (direction == 1 && currentIndexHero < sizeMatrix - 1) {
            currentIndexHero++;
        } else if (direction == -1 && currentIndexHero > 0) {
            currentIndexHero--;
        }
    }

    public int randomViewImage() {
        return random.nextInt(sizeMatrix);
    }

    public int randTypeImage() {
        int num = random.nextInt(10);
        if (num < VILLAIN_THRESHOLD) {
            return 0; // Villain
        } else if (num < HEART_THRESHOLD) {
            return 2; // Web score
        } else {
            return 1; // Heart
        }
    }

    public void gameOver() {
        System.out.println("Game Over");
    }

    public int getCurrentIndexHero() {
        return currentIndexHero;
    }

    public int getSizeMatrix() {
        return sizeMatrix;
    }

    public int getTypeCellInMatrix(int i, int j) {
        return mainTypeMatrix[i][j];
    }

    public int getScore() {
        return score;
    }
}
