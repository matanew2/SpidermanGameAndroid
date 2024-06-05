package com.example.spidermangame;

import android.os.Bundle;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spidermangame.Logic.GameManager;
import com.example.spidermangame.Logic.GameController;
import com.example.spidermangame.Logic.GameView;
import com.example.spidermanvsvenomgame.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView[] game_IMG_hearts;
    private GridLayout game_LAYOUT_matrix;
    private MaterialTextView game_TXT_score;

    private MaterialTextView game_TXT_toast;
    private ExtendedFloatingActionButton[] game_BTN_arrows; // init listeners
    private GameManager gameManager;
    private GameView gameView;
    private GameController gameController;

    private static final int INITIAL_HEALTH = 3;
    private static final int GRID_SIZE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeGameComponents();
        initViews();
        initListeners();
    }

    private void initializeGameComponents() {
        findViewObjects();
        gameManager = new GameManager(INITIAL_HEALTH, GRID_SIZE);
        gameView = new GameView(this, game_IMG_hearts, game_LAYOUT_matrix, game_TXT_score, gameManager);
        gameController = new GameController(gameManager, gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameController.startGame();
    }

    @Override
    protected void onStop(){
        super.onStop();
        gameController.stopGame();
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameController.stopGame();
    }

    private void initListeners() {
        game_BTN_arrows[0].setOnClickListener(v -> {
            gameManager.moveHero(-1);
            gameView.updateHeroVisibility();
        });

        game_BTN_arrows[1].setOnClickListener(v -> {
            gameManager.moveHero(1);
            gameView.updateHeroVisibility();
        });
    }

    private void initViews() {
        gameView.showHearts();
        gameView.initMatrix();
        gameView.updateHeroVisibility();
    }

    private void findViewObjects() {
        findAddScore();
        findHearts();
        findGrid();
        findArrowsBar();
    }

    private void findGrid() {
        this.game_LAYOUT_matrix = findViewById(R.id.gridLayout);
    }

    private void findAddScore() {
        this.game_TXT_score = findViewById(R.id.main_TXT_addScore);
    }

    private void findHearts() {
        this.game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };
    }

    private void findArrowsBar() {
        this.game_BTN_arrows = new ExtendedFloatingActionButton[]{
                findViewById(R.id.main_BTN_left),
                findViewById(R.id.main_BTN_right)
        };
    }
}
