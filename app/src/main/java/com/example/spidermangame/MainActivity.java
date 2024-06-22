package com.example.spidermangame;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.content.Intent;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spidermangame.Logic.GameManager;
import com.example.spidermangame.Logic.GameController;
import com.example.spidermangame.Logic.GameView;
import com.example.spidermangame.Sensor.SensorDetector;
import com.example.spidermanvsvenomgame.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView[] game_IMG_hearts;
    private RelativeLayout main_BTN_bar;
    private GridLayout game_LAYOUT_matrix;
    private MaterialTextView game_TXT_score;
    private MaterialTextView main_TXT_distance;
    private ExtendedFloatingActionButton[] game_BTN_arrows; // init listeners
    private GameManager gameManager;
    private GameView gameView;
    private GameController gameController;

    private SensorDetector sensorDetector;

    private boolean isFasterMode;
    private boolean isSensorOn;

    private static final int INITIAL_HEALTH = 3;
    private static final int GRID_SIZE = 5;
    public static final String KEY_SENSOR = "KEY_SENSOR", KEY_DELAY = "KEY_DELAY", KEY_NAME = "KEY_NAME", KEY_LON = "KEY_LON", KEY_LAT = "KEY_LAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAndInitializeGameComponents();
        initViews();
        extractIntentData();
        initListeners();
    }

    private void extractIntentData() {
        Intent previousIntent = getIntent();
        this.isFasterMode = previousIntent.getBooleanExtra(KEY_DELAY, false);
        this.isSensorOn = previousIntent.getBooleanExtra(KEY_SENSOR, false);
    }

    private void findAndInitializeGameComponents() {
        findViewObjects();
        gameManager = new GameManager(INITIAL_HEALTH, GRID_SIZE);
        gameView = new GameView(this, game_IMG_hearts, game_LAYOUT_matrix, game_TXT_score, gameManager, main_TXT_distance);
        gameController = new GameController(gameManager, gameView);
        sensorDetector = new SensorDetector(this, gameView.callBackSensorView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.gameView.getSoundManager().releaseResources();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameController.defineFastMode(isFasterMode);
        if (isSensorOn) sensorDetector.startX();
    }

    @Override
    protected void onStop(){
        super.onStop();
        gameController.stopGame();
        if (isSensorOn) sensorDetector.stopX();
        gameView.getSoundManager().releaseResources();
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameController.stopGame();
        if (isSensorOn) sensorDetector.stopX();
    }

    private void initListeners() {
        if (isSensorOn) {
            sensorDetector.startX();
            hideGameButtons();
        } else {
            showGameButtons();
        }

        game_BTN_arrows[0].setOnClickListener(v -> {
            gameManager.moveHero(-1);
            gameView.updateHeroVisibility();
        });

        game_BTN_arrows[1].setOnClickListener(v -> {
            gameManager.moveHero(1);
            gameView.updateHeroVisibility();
        });

    }

    private void hideGameButtons() {
        game_BTN_arrows[0].setVisibility(View.INVISIBLE);
        game_BTN_arrows[1].setVisibility(View.INVISIBLE);
        main_BTN_bar.setVisibility(View.INVISIBLE);
    }

    private void showGameButtons() {
        game_BTN_arrows[0].setVisibility(View.VISIBLE);
        game_BTN_arrows[1].setVisibility(View.VISIBLE);
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
        findDistance();
    }

    private void findDistance() {
        this.main_TXT_distance = findViewById(R.id.main_TXT_distance);
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
        this.main_BTN_bar =findViewById(R.id.main_BTN_bar);
    }
}
