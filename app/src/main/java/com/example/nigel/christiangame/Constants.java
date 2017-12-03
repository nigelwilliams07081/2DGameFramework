package com.example.nigel.christiangame;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Nigel on 9/22/2017.
 */

public class Constants {
    public static int ScreenWidth;
    /**
     * 50
     */
    public static final int SCREEN_WIDTH_PADDING = 50;
    public static int ScreenHeight;
    /**
     * 120
     */
    public static final int SCREEN_HEIGHT_PADDING = 120;
    public static final int HIGHSCORE_SCREEN_WIDTH_PADDING = 150;
    public static final int HIGHSCORE_SCREEN_HEIGHT_PADDING = 50;
    /**
     * BLUE
     */
    public static final int GAME_OVER_TEXT_COLOR = Color.BLUE;
    /**
     * 100
     */
    public static final int GAME_OVER_TEXT_SIZE = 100;
    public static final int HIGH_SCORE_TEXT_SIZE = 40;
    public static final int HIGH_SCORE_LABEL_SIZE = 20;
    public static final int SCORE_TEXT_SIZE = 40;
    public static final int SCORE_LABEL_SIZE = 20;
    public static final int PLAYER_LIVES_TEXT_SIZE = 40;
    public static final int PLAYER_LIVES_LABEL_SIZE = 20;

    public static final int HIGH_SCORE_TEXT_COLOR = Color.YELLOW;
    public static final int SCORE_TEXT_COLOR = Color.WHITE;
    public static final int PLAYER_LIVES_TEXT_COLOR = Color.CYAN;

    public static Context CurrentContext;
    public static long InitialTime;
    public static long CurrentGameTime;

    public static float ElapsedTime;

    public static int Score;
    public static int HighScore;

    public static final String HIGH_SCORE_LABEL = "High Score";
    public static final String SCORE_LABEL = "Score";
    public static final String LIVES_LABEL = "Lives";

    public static final String HighScoreID = "HighScore_ID";

    public static final RelativeLayout.LayoutParams LAYOUT_PARAMS_WC = new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    public static final RelativeLayout.LayoutParams LAYOUT_PARAMS_MP = new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    public static final FrameLayout.LayoutParams LAYOUT_PARAMS_FP = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
    public static RelativeLayout RelativeLayout;

    public static Intent Intent;

    public static final int ASTEROID_WIDTH = 50;
    public static final int ASTEROID_HEIGHT = 50;

    public static ArrayList<Bitmap> AsteroidImages = new ArrayList<>();
    public static Bitmap BackgroundImage;

    public static MediaPlayer BackgroundMusic;
    public static void ResetBackgroundMusic() {
        if (Constants.BackgroundMusic != null) {
            Constants.BackgroundMusic.stop();
            Constants.BackgroundMusic = MediaPlayer.create(Constants.CurrentContext, R.raw.rendezvous);
            Constants.BackgroundMusic.setVolume(0.4f, 0.4f);
        }
    }
    public static MediaPlayer LaserSound;
    public static MediaPlayer AsteroidExplosionSound;
    public static MediaPlayer PlayerExplosionSound;
}
