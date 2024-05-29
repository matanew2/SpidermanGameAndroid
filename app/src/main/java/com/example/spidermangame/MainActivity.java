package com.example.spidermangame;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spidermangame.Logic.GameManager;
import com.example.spidermangame.Logic.GameTimer;
import com.example.spidermangame.Logic.GameViewManager;
import com.example.spidermanvsvenomgame.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView[] game_IMG_hearts;
    private ShapeableImageView[][] game_IMG_villains;
    private ShapeableImageView[] game_IMG_hero;
    private ExtendedFloatingActionButton[] game_BTN_arrows;
    private GameManager gameManager;
    private GameViewManager gameViewManager;
    private GameTimer gameTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewObjects();
        gameManager = new GameManager(game_IMG_hearts.length);
        gameViewManager = new GameViewManager(game_IMG_hearts, game_IMG_villains, game_IMG_hero, gameManager);
        gameTimer = new GameTimer(gameManager, gameViewManager);

        initViews();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameTimer.startGame();
    }

    @Override
    protected void onStop(){
        super.onStop();
        gameTimer.stopGame();
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameTimer.stopGame();
    }

    private void initListeners() {
        game_BTN_arrows[0].setOnClickListener(v -> {
            gameManager.moveHero(-1);
            gameViewManager.updateHeroVisibility();
        });

        game_BTN_arrows[1].setOnClickListener(v -> {
            gameManager.moveHero(1);
            gameViewManager.updateHeroVisibility();
        });
    }

    private void initViews() {
        gameViewManager.showHearts();
        gameViewManager.hideVillains();
        gameViewManager.updateHeroVisibility();
    }

    private void findViewObjects() {
        findHearts();
        findVillains();
        findHero();
        findArrowsBar();
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

    private void findHero() {
        this.game_IMG_hero = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_leftHero),
                findViewById(R.id.main_IMG_leftMidHero),
                findViewById(R.id.main_IMG_middleHero),
                findViewById(R.id.main_IMG_rightMidHero),
                findViewById(R.id.main_IMG_rightHero)};
    }

    private void findVillains() {
        this.game_IMG_villains = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_villian1), findViewById(R.id.main_IMG_villian2), findViewById(R.id.main_IMG_villian3), findViewById(R.id.main_IMG_villian4), findViewById(R.id.main_IMG_villian5)},
                {findViewById(R.id.main_IMG_villian6), findViewById(R.id.main_IMG_villian7), findViewById(R.id.main_IMG_villian8), findViewById(R.id.main_IMG_villian9), findViewById(R.id.main_IMG_villian10)},
                {findViewById(R.id.main_IMG_villian11), findViewById(R.id.main_IMG_villian12), findViewById(R.id.main_IMG_villian13), findViewById(R.id.main_IMG_villian14), findViewById(R.id.main_IMG_villian15)},
                {findViewById(R.id.main_IMG_villian16), findViewById(R.id.main_IMG_villian17), findViewById(R.id.main_IMG_villian18), findViewById(R.id.main_IMG_villian19), findViewById(R.id.main_IMG_villian20)},
                {findViewById(R.id.main_IMG_villian21), findViewById(R.id.main_IMG_villian22), findViewById(R.id.main_IMG_villian23), findViewById(R.id.main_IMG_villian24), findViewById(R.id.main_IMG_villian25)}};
    }
}
