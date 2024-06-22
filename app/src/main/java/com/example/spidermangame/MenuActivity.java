package com.example.spidermangame;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.spidermangame.Audio.SoundManager;
import com.example.spidermangame.Utils.Signal;
import com.example.spidermanvsvenomgame.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    private MaterialButton startGame_BTN;
    private TextInputEditText menu_LBL_inputName;
    private SwitchMaterial menu_SW_faster;
    private SwitchMaterial menu_SW_sensor;
    private SoundManager soundManager;

    private boolean isSensorOn = false;
    private boolean isFasterOn = false;

    private double lat = 0.0;
    private double lon = 0.0;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        soundManager = new SoundManager(this);

        // Initialize the button and EditText
        findViews();

        startGame_BTN.setOnClickListener(view -> {
            soundManager.playClickSound();
            if(Objects.requireNonNull(menu_LBL_inputName.getText()).length() == 0) {
                Signal.init(this);
                Signal.getInstance().toast("Please Enter Your Name");
            } else {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.KEY_SENSOR, isSensorOn);
                intent.putExtra(MainActivity.KEY_NAME, menu_LBL_inputName.getText().toString());
                intent.putExtra(MainActivity.KEY_DELAY, isFasterOn);
                intent.putExtra(MainActivity.KEY_LON,lon);
                intent.putExtra(MainActivity.KEY_LAT,lat);
                startActivity(intent);
                finish();
            }
        });

        menu_SW_sensor.setOnCheckedChangeListener((compoundButton, sensorOn) -> {
            soundManager.playClickSound();
            isSensorOn = sensorOn;
        });
        menu_SW_faster.setOnCheckedChangeListener((compoundButton, fasterModeOn) -> {
            soundManager.playClickSound();
            isFasterOn = fasterModeOn;
        });
    }

    private void findViews() {
        startGame_BTN = findViewById(R.id.startGame_BTN);
        menu_LBL_inputName = findViewById(R.id.menu_LBL_inputName);
        menu_SW_faster = findViewById(R.id.menu_SW_faster);
        menu_SW_sensor = findViewById(R.id.menu_SW_sensor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetPlayerNameInput();
    }

    private void resetPlayerNameInput() {
        TextInputEditText playerNameInput = findViewById(R.id.menu_LBL_inputName);
        playerNameInput.setText("");
    }

}