package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Nigel on 9/28/2017.
 */

public interface Scene {

    public void Update();
    public void Draw(Canvas canvas);
    public void SwitchScene();
    public void ReceiveTouch(MotionEvent motionEvent);
}
