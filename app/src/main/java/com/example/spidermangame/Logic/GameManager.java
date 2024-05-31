package com.example.spidermangame.Logic;

import java.util.Random;

public class GameManager {
    private int score;
    private int health;
    public int sizeMatrix;
    private final Random rand = new Random();
    private int[][] mainTypeMatrix;
    private int currentIndexHero;


    public GameManager(int health, int sizeMatrix) {
        this.health = health;
        this.sizeMatrix = sizeMatrix;
        this.currentIndexHero = sizeMatrix / 2;
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
        mainTypeMatrix = new int[sizeMatrix][sizeMatrix];
        for (int[] row : mainTypeMatrix) {
            java.util.Arrays.fill(row, -1);
        }
    }

    public void addScore() {
        score += rand.nextInt(50) + 10;
    }

    public void setTypeCellInMatrix(int row, int col, int type) {
        mainTypeMatrix[row][col] = type;
    }
    public void moveHero(int direction) {
        if (direction == 1 && currentIndexHero < sizeMatrix - 1){
            currentIndexHero++;
        } else if (direction == -1 && currentIndexHero > 0) {
            currentIndexHero--;
        }
    }

    public int randomViewImage() {
        return rand.nextInt(sizeMatrix);
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
