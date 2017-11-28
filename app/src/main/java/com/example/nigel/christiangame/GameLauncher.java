package com.example.nigel.christiangame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
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
}
