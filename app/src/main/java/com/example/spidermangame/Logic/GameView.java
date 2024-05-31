package com.example.spidermangame.Logic;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;

import androidx.core.content.ContextCompat;

import com.example.spidermanvsvenomgame.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;


public class GameView {

    private final ShapeableImageView[] game_IMG_hearts;
    private final MaterialTextView game_TXT_score;
    private final GridLayout gridLayout;
    private final GameManager gameManager;
    private final Context context;

    public GameView(Context context, ShapeableImageView[] game_IMG_hearts, GridLayout game_LAYOUT_matrix, MaterialTextView game_TXT_score, GameManager gameManager) {
        this.gameManager = gameManager;
        this.game_IMG_hearts = game_IMG_hearts;
        this.game_TXT_score = game_TXT_score;
        this.gridLayout = game_LAYOUT_matrix;
        this.context = context;
    }

    public void showHearts() {
        for (ShapeableImageView heart : game_IMG_hearts) {
            heart.setVisibility(View.VISIBLE);
        }
    }

    public void updateHeroVisibility() {
        for (int i = 0; i < gameManager.sizeMatrix; i++) {
            gridLayout.getChildAt((gameManager.sizeMatrix - 1) * gameManager.sizeMatrix + i).setVisibility(View.INVISIBLE);
        }
        setImage(gameManager.sizeMatrix - 1, gameManager.getCurrentIndexHero(), 3);
        gridLayout.getChildAt((gameManager.sizeMatrix - 1) * gameManager.sizeMatrix + gameManager.getCurrentIndexHero()).setVisibility(View.VISIBLE);
    }

    public GridLayout getGridLayout() {
        return gridLayout;
    }
    public void updateViewObjects() {
        for (int i = gameManager.getSizeMatrix() - 2; i >= 0; i--) {
            for (int j = gameManager.getSizeMatrix() - 1; j >= 0; j--) {
                if (gridLayout.getChildAt(i * gameManager.getSizeMatrix() + j).getVisibility() == View.VISIBLE) {
                    gridLayout.getChildAt(i * gameManager.getSizeMatrix() + j).setVisibility(View.INVISIBLE);

                    if ( i == gameManager.getSizeMatrix() - 2) {
                        continue;
                    }

                    int type = gameManager.getTypeCellInMatrix(i, j);
                    gameManager.setTypeCellInMatrix(i, j, -1);

                    setImage(i + 1, j, type);
                    gameManager.setTypeCellInMatrix(i + 1, j, type);
                    gridLayout.getChildAt((i + 1) * gameManager.getSizeMatrix() + j).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setImage(int row, int col, int type) {
        if (type == 0) {
            gridLayout.getChildAt(row * gameManager.getSizeMatrix() + col).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_villian));
            gameManager.setTypeCellInMatrix(row, col, 0);
        } else if (type == 1) {
            gridLayout.getChildAt(row * gameManager.getSizeMatrix() + col).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_heart));
            gameManager.setTypeCellInMatrix(row, col, 1);
        } else if (type == 2) {
            gridLayout.getChildAt(row * gameManager.getSizeMatrix() + col).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_web_score));
            gameManager.setTypeCellInMatrix(row, col, 2);
        } else if (type == 3) {
            gridLayout.getChildAt(row * gameManager.getSizeMatrix() + col).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_hero));
            gameManager.setTypeCellInMatrix(row, col, 3);
        }
    }

    public void detectAndHandleCollision() {
        for (int row = gameManager.getSizeMatrix() - 1; row >= 0; row--) {
            for (int col = 0; col < gameManager.getSizeMatrix(); col++) {
                ShapeableImageView imageView = (ShapeableImageView) gridLayout.getChildAt(row * gameManager.getSizeMatrix() + col);

                if (row == gameManager.getSizeMatrix() - 2 && imageView.getVisibility() == View.VISIBLE && col == gameManager.getCurrentIndexHero()) {
                    // Collision with hero detected
                    System.out.println("Collision with hero at row " + row + " and column " + col);
                    handleCollisionWithVillain(row, col);
                }
            }
        }
    }


    private void handleCollisionWithVillain(int row, int col) {
        int mainType = gameManager.getTypeCellInMatrix(row, col);
        if (mainType == 0) { // Main type 0 indicates villain
            gameManager.decreaseHealth();
            game_IMG_hearts[gameManager.getHealth()].setVisibility(View.INVISIBLE);
            gameManager.setTypeCellInMatrix(row, col, -1);
        }
        if (mainType == 1) { // Main type 1 indicates heart
            if (gameManager.getHealth() == 3) {
                return;
            }
            gameManager.increaseHealth();
            System.out.println(gameManager.getHealth());
            game_IMG_hearts[gameManager.getHealth() - 1].setVisibility(View.VISIBLE);
            gameManager.setTypeCellInMatrix(row, col, -1);
        }
        if (mainType == 2) { // Main type 2 indicates bullet
            gameManager.addScore();
            game_TXT_score.setText(String.valueOf(gameManager.getScore()));
            gameManager.setTypeCellInMatrix(row, col, -1);
        }
    }

    public void gameOver() {
        this.gridLayout.setVisibility(View.INVISIBLE);
        gameManager.gameOver();
    }

    public void initMatrix() {
        int sizeMatrix = gameManager.getSizeMatrix();

        // Set the number of columns and rows
        gridLayout.setColumnCount(sizeMatrix);
        gridLayout.setRowCount(sizeMatrix);

        // Create the grid
        for (int i = 0; i < sizeMatrix; i++) {
            for (int j = 0; j < sizeMatrix; j++) {
                ShapeableImageView imageView = new ShapeableImageView(context);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.rowSpec = GridLayout.spec(i, 1f);
                params.columnSpec = GridLayout.spec(j, 1f);
                params.setMargins(5, 5, 5, 5); // Optional: Set margins if needed
                imageView.setLayoutParams(params);

                gridLayout.addView(imageView);
            }
        }
    }


}
