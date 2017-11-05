package com.example.nigel.christiangame;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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
    public static final int HIGHSCORE_SCREEN_WIDTH_PADDING = 100;
    public static final int HIGHSCORE_SCREEN_HEIGHT_PADDING = 50;
    /**
     * BLUE
     */
    public static final int GAME_OVER_TEXT_COLOR = Color.BLUE;
    /**
     * 100
     */
    public static final int GAME_OVER_TEXT_SIZE = 100;
    public static Context CurrentContext;
    public static long InitialTime;
    public static long CurrentGameTime;

    public static float ElapsedTime;

    public static int Score;
    public static int HighScore;
    public static final String HighScoreID = "HighScore_ID";

    public static final RelativeLayout.LayoutParams LAYOUT_PARAMS_WC = new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    public static final RelativeLayout.LayoutParams LAYOUT_PARAMS_MP = new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    public static final FrameLayout.LayoutParams LAYOUT_PARAMS_FP = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
    public static RelativeLayout RelativeLayout;

}
