package com.example.spidermangame.Logic;

import static com.example.spidermangame.Logic.GameManager.VILLAIN_COLS;
import static com.example.spidermangame.Logic.GameManager.VILLAIN_ROWS;

import android.view.View;

import com.example.spidermanvsvenomgame.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Arrays;

public class GameViewManager {

    private final ShapeableImageView[] game_IMG_hearts;
    public final ShapeableImageView[][] game_IMG_villains;
    private final ShapeableImageView[] game_IMG_hero;
    private final GameManager gameManager;

    public GameViewManager(ShapeableImageView[] game_IMG_hearts, ShapeableImageView[][] game_IMG_villains, ShapeableImageView[] game_IMG_hero, GameManager gameManager) {
        this.game_IMG_hearts = game_IMG_hearts;
        this.game_IMG_villains = game_IMG_villains;
        this.game_IMG_hero = game_IMG_hero;
        this.gameManager = gameManager;
    }

    public void showHearts() {
        for (ShapeableImageView heart : game_IMG_hearts) {
            heart.setVisibility(View.VISIBLE);
        }
    }

    public void hideVillains() {
        for (ShapeableImageView[] row : game_IMG_villains) {
            for (ShapeableImageView villain : row) {
                villain.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void updateHeroVisibility() {
        for (int i = 0; i < game_IMG_hero.length; i++) {
            if (i != gameManager.getCurrentIndexHero())
                game_IMG_hero[i].setVisibility(View.INVISIBLE);
        }
        game_IMG_hero[gameManager.getCurrentIndexHero()].setVisibility(View.VISIBLE);
    }

    public void updateVillains() {
        for (int i = VILLAIN_ROWS - 1; i >= 0; i--) {
            for (int j = GameManager.VILLAIN_COLS - 1; j >= 0; j--) {
                if (game_IMG_villains[i][j].getVisibility() == View.VISIBLE) {
                    game_IMG_villains[i][j].setVisibility(View.INVISIBLE);

                    if (i == VILLAIN_ROWS - 1) {
                        continue;
                    }

                    int type = gameManager.getMain_type_matrix(i, j);
                    gameManager.setMainTypeMatrix(i, j, -1);

                    setImage(i + 1, j, type);
                    gameManager.setMainTypeMatrix(i + 1, j, type);
                    game_IMG_villains[i + 1][j].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setImage(int row, int col, int type) {
        if (type == 0) {
            game_IMG_villains[row][col].setImageResource(R.drawable.ic_villian);
            gameManager.setMainTypeMatrix(row, col, 0);
        } else if (type == 1) {
            game_IMG_villains[row][col].setImageResource(R.drawable.ic_heart);
            gameManager.setMainTypeMatrix(row, col, 1);
        }
    }

    public void detectAndHandleCollision() {
        for (int row = VILLAIN_ROWS - 1; row >= 0; row--) {
            for (int col = VILLAIN_COLS - 1; col >= 0; col--) {
                if (row == VILLAIN_ROWS - 1 && game_IMG_villains[row][col].getVisibility() == View.VISIBLE && col == gameManager.getCurrentIndexHero()) {
                    // Checking the last row for collision with hero
                    System.out.println("Collision with hero");
                    System.out.println(Arrays.deepToString(gameManager.getMatrix()));
                    handleCollisionWithVillain(row, col);
                }
            }
        }
    }

    private void handleCollisionWithVillain(int row, int col) {
        int mainType = gameManager.getMainTypeMatrix(row, col);
        if (mainType == 0) { // Main type 0 indicates villain
            gameManager.decreaseHealth();
            game_IMG_hearts[gameManager.getHealth()].setVisibility(View.INVISIBLE);
            gameManager.setMainTypeMatrix(row, col, -1);
        }
        if (mainType == 1) { // Main type 1 indicates heart
            if (gameManager.getHealth() == 3) {
                return;
            }
            gameManager.increaseHealth();
            System.out.println(gameManager.getHealth());
            game_IMG_hearts[gameManager.getHealth() - 1].setVisibility(View.VISIBLE);
            gameManager.setMainTypeMatrix(row, col, -1);
        }
    }

    public void gameOver() {
        for (ShapeableImageView[] row : game_IMG_villains) {
            for (ShapeableImageView villain : row) {
                villain.setVisibility(View.INVISIBLE);
            }
        }
        gameManager.gameOver();
    }
}
