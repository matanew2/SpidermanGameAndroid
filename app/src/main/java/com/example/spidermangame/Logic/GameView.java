package com.example.spidermangame.Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.spidermangame.Audio.SoundManager;
import com.example.spidermangame.Sensor.CallBackSensorView;
import com.example.spidermanvsvenomgame.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class GameView {
    private final ShapeableImageView[] game_IMG_hearts;
    private final MaterialTextView game_TXT_score;
    private final MaterialTextView main_TXT_distance;
    private final SoundManager soundManager;
    private final GridLayout gridLayout;
    private final GameManager gameManager;
    private final Context context;

    public GameView(Context context, ShapeableImageView[] game_IMG_hearts, GridLayout game_LAYOUT_matrix, MaterialTextView game_TXT_score,
                    GameManager gameManager, MaterialTextView main_TXT_distance){
        this.gameManager = gameManager;
        this.game_IMG_hearts = game_IMG_hearts;
        this.game_TXT_score = game_TXT_score;
        this.gridLayout = game_LAYOUT_matrix;
        this.context = context;
        this.soundManager = new SoundManager(context);
        this.main_TXT_distance = main_TXT_distance;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    @SuppressLint("SetTextI18n")
    public void countDistance() {
        this.gameManager.countDistance();
        this.main_TXT_distance.setText(gameManager.getDistacne() + "m");
    }

    public void showHearts() {
        for (ShapeableImageView heart : game_IMG_hearts) {
            heart.setVisibility(View.VISIBLE);
        }
    }

    public final CallBackSensorView callBackSensorView = new CallBackSensorView() {
        @Override
        public void moveHeroBySensor(int index) {
            gridLayout.getChildAt(gameManager.getCurrentIndexHero()).setVisibility(View.INVISIBLE);
            gameManager.moveHero(index);
            gridLayout.getChildAt(gameManager.getCurrentIndexHero()).setVisibility(View.VISIBLE);
        }
    };
    public void updateHeroVisibility() {
        int lastIndex = gameManager.getRowSize() - 1;
        for (int i = 0; i < gameManager.getColSize(); i++) {
            gridLayout.getChildAt(lastIndex * gameManager.getColSize() + i).setVisibility(View.INVISIBLE);
        }
        setImage(lastIndex, gameManager.getCurrentIndexHero(), 3);
        gridLayout.getChildAt(lastIndex * gameManager.getColSize() + gameManager.getCurrentIndexHero()).setVisibility(View.VISIBLE);
    }

    public void updateViewObjects() {
        int lastIndex = gameManager.getRowSize() - 2; //
        for (int i = lastIndex; i >= 0; i--) {
            for (int j = gameManager.getColSize() - 1; j >= 0; j--) {
                View childView = gridLayout.getChildAt(i * gameManager.getColSize() + j);
                if (childView.getVisibility() == View.VISIBLE) {
                    childView.setVisibility(View.INVISIBLE);
                    if (i == lastIndex) {
                        continue;
                    }
                    int type = gameManager.getTypeCellInMatrix(i, j);
                    gameManager.setTypeCellInMatrix(i, j, -1);
                    setImage(i + 1, j, type);
                    gameManager.setTypeCellInMatrix(i + 1, j, type);
                    gridLayout.getChildAt((i + 1) * gameManager.getColSize() + j).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setImage(int row, int col, int type) {
        ShapeableImageView imageView = (ShapeableImageView) gridLayout.getChildAt(row * gameManager.getColSize() + col);
        if (imageView != null) {
            int drawableResId;
            switch (type) {
                case 0:
                    drawableResId = R.drawable.ic_villian;
                    break;
                case 1:
                    drawableResId = R.drawable.ic_heart;
                    break;
                case 2:
                    drawableResId = R.drawable.ic_web_score;
                    break;
                case 3:
                    drawableResId = R.drawable.ic_hero;
                    break;
                default:
                    drawableResId = 0; // Handle invalid type gracefully
            }
            if (drawableResId != 0) {
                imageView.setBackground(ContextCompat.getDrawable(context, drawableResId));
                gameManager.setTypeCellInMatrix(row, col, type);
            }
        }
    }

    public void detectAndHandleCollision() {
        int lastIndex = gameManager.getRowSize() - 2;
        for (int row = lastIndex; row >= 0; row--) {
            for (int col = 0; col < gameManager.getColSize(); col++) {
                ShapeableImageView imageView = (ShapeableImageView) gridLayout.getChildAt(row * gameManager.getColSize() + col);
                if (row == lastIndex && imageView != null && imageView.getVisibility() == View.VISIBLE && col == gameManager.getCurrentIndexHero()) {
                    handleCollisionWithVillain(row, col);
                }
            }
        }
    }

    private void handleCollisionWithVillain(int row, int col) {
        int mainType = gameManager.getTypeCellInMatrix(row, col);
        switch (mainType) {
            case 0: // Villain
                soundManager.playCrashSound();
                gameManager.decreaseHealth();
                game_IMG_hearts[gameManager.getHealth()].setVisibility(View.INVISIBLE);
                // Show the toast message and vibrate the device
                vibrateAndToast();

                break;
            case 1: // Heart
                soundManager.playCollectCoinSound();
                if (gameManager.getHealth() < game_IMG_hearts.length) {
                    gameManager.increaseHealth();
                    game_IMG_hearts[gameManager.getHealth() - 1].setVisibility(View.VISIBLE);
                }
                break;
            case 2: // Bullet (web score)
                soundManager.playCollectCoinSound();
                gameManager.addScore();
                game_TXT_score.setText(String.valueOf(gameManager.getScore()));
                break;
            default:
                break; // Handle other types gracefully
        }
        gameManager.setTypeCellInMatrix(row, col, -1);
    }

    private void vibrateAndToast() {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(100);
                toastMessageCrash();
            }

    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void toastMessageCrash() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.toast_layout, null);
        // Set the text for the custom Toast
        MaterialTextView text = layout.findViewById(R.id.toast_text);
        text.setText("Ouch! You've been hit! Health: " + gameManager.getHealth() + " lives left");

        // Create the Toast with the custom layout
        Toast toast = getToast(layout);
        toast.show();
    }

    @NonNull
    private Toast getToast(View layout) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);  // Adjust the gravity and offset as needed
        return toast;
    }

    public void initMatrix() {
        gridLayout.setColumnCount( gameManager.getColSize());
        gridLayout.setRowCount(gameManager.getRowSize());
        for (int i = 0; i < gameManager.getRowSize(); i++) {
            for (int j = 0; j < gameManager.getColSize(); j++) {
                ShapeableImageView imageView = new ShapeableImageView(context);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.rowSpec = GridLayout.spec(i, 1f);
                params.columnSpec = GridLayout.spec(j, 1f);
                imageView.setLayoutParams(params);
                gridLayout.addView(imageView);
            }
        }
    }

    public ViewGroup getGridLayout() {
        return gridLayout;
    }

    public void resetGame() {
        // Reset all hearts to be visible
        for (ShapeableImageView heart : game_IMG_hearts) {
            heart.setVisibility(View.VISIBLE);
        }

        // Reset the score display
        game_TXT_score.setText("0");

        // Hide all the views in the grid layout
        for (int i = 0; i < gameManager.getRowSize(); i++) {
            for (int j = 0; j < gameManager.getColSize(); j++) {
                gridLayout.getChildAt(i * gameManager.getColSize() + j).setVisibility(View.INVISIBLE);
                gameManager.setTypeCellInMatrix(i, j, -1);  // Clear the game matrix types
            }
        }

        // Reinitialize game manager states
        gameManager.resetHealth();
        gameManager.resetScore();
        gameManager.initMatrixType();
        gameManager.initGame();

        // Update the hero's visibility in the last row
        updateHeroVisibility();

        // Ensure the grid layout is visible
        gridLayout.setVisibility(View.VISIBLE);
    }

}
