package com.example.spidermangame.Audio;
import android.content.Context;
import android.media.MediaPlayer;

import com.example.spidermanvsvenomgame.R;

public class SoundManager {
    private final MediaPlayer clickSound;
    private final MediaPlayer crashSound;
    private final MediaPlayer collectCoinSound;

    public SoundManager(Context context) {
        clickSound = MediaPlayer.create(context, R.raw.click_sound);
        crashSound = MediaPlayer.create(context, R.raw.crash_sound);
        collectCoinSound = MediaPlayer.create(context, R.raw.collect_coin_sound);
    }

    public void playClickSound() {
        if (clickSound != null) {
            clickSound.start();
        }
    }

    public void playCrashSound() {
        if (crashSound != null) {
            crashSound.start();
        }
    }

    public void playCollectCoinSound() {
        if (collectCoinSound != null) {
            collectCoinSound.start();
        }
    }

    // Call this method when your activity is getting destroyed
    public void releaseResources() {
        if (clickSound != null) {
            clickSound.release();
        }
        if (crashSound != null) {
            crashSound.release();
        }
        if (collectCoinSound != null) {
            collectCoinSound.release();
        }
    }
}