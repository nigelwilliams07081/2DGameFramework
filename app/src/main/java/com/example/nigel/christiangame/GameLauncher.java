package com.example.nigel.christiangame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Nigel on 10/14/2017.
 */

public class GameLauncher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Constants.BackgroundMusic = MediaPlayer.create(this, R.raw.rendezvous);
        Constants.BackgroundMusic.setLooping(true);
        Constants.BackgroundMusic.setVolume(0.4f, 0.4f);
        Constants.BackgroundMusic.start();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.ScreenHeight = metrics.heightPixels;
        Constants.ScreenWidth = metrics.widthPixels;

        Constants.RelativeLayout = new RelativeLayout(this);
        Constants.RelativeLayout.setLayoutParams(Constants.LAYOUT_PARAMS_FP);
        Constants.RelativeLayout.addView(new GamePanel(this));

        setContentView(Constants.RelativeLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Constants.BackgroundMusic.stop();
        Constants.BackgroundMusic.release();
        if (Constants.LaserSound != null) {
            Constants.LaserSound.stop();
            Constants.LaserSound.release();
        }
        if (Constants.AsteroidExplosionSound != null) {
            Constants.AsteroidExplosionSound.stop();
            Constants.AsteroidExplosionSound.release();
        }
        if (Constants.PlayerExplosionSound != null) {
            Constants.PlayerExplosionSound.stop();
            Constants.PlayerExplosionSound.release();
        }
    }
}
