package com.example.nigel.christiangame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Nigel on 9/19/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread m_Thread;

    private SceneManager m_SceneManager;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        Constants.CurrentContext = context;

        m_Thread = new MainThread(getHolder(), this);

        m_SceneManager = new SceneManager();

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_Thread = new MainThread(getHolder(), this);
        Constants.InitialTime = System.currentTimeMillis();
        m_Thread.SetIsRunning(true);
        m_Thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                m_Thread.SetIsRunning(false);
                m_Thread.join();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
//        return super.onTouchEvent(motionEvent);
        m_SceneManager.ReceiveTouch(motionEvent);
        return true;
    }

    public void Update() {
        m_SceneManager.Update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        m_SceneManager.Draw(canvas);
    }
}
