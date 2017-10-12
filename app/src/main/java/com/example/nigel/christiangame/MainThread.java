package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Nigel on 9/19/2017.
 */

public class MainThread extends Thread {
    public static final int MAX_FPS = 30;
    public static Canvas s_Canvas;

    private double m_AverageFPS;
    private SurfaceHolder m_SurfaceHolder;
    private GamePanel m_GamePanel;
    private boolean m_IsRunning;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        m_SurfaceHolder = surfaceHolder;
        m_GamePanel = gamePanel;
    }

    public void SetIsRunning(boolean isRunning) {
        m_IsRunning = isRunning;
    }

    @Override
    public void run() {
        long startTime;
        long timeMilliseconds = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(m_IsRunning) {
            startTime = System.nanoTime();
            s_Canvas = null;

            try {
                s_Canvas = m_SurfaceHolder.lockCanvas();
                synchronized (m_SurfaceHolder) {
                    m_GamePanel.Update();
                    m_GamePanel.draw(s_Canvas);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (s_Canvas != null) {
                    try {
                        m_SurfaceHolder.unlockCanvasAndPost(s_Canvas);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMilliseconds = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMilliseconds;

            try {
                if (waitTime > 0) {
                    sleep(waitTime);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            totalTime = System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == MAX_FPS) {
                m_AverageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }
}
