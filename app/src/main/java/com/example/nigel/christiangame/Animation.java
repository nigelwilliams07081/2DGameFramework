package com.example.nigel.christiangame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.Settings;

/**
 * Created by Nigel on 9/28/2017.
 */

public class Animation {

    private Bitmap[] m_Frames;
    private int m_FrameIndex;
    private boolean m_IsPlaying = false;
    private float m_FrameTime;
    private long m_LastFrame;

    public boolean IsPlaying() {
        return m_IsPlaying;
    }

    public void Play() {
        m_IsPlaying = true;
        m_FrameIndex = 0;
        SetLastFrame(System.currentTimeMillis());
    }

    private void SetLastFrame(long frame) {
        m_LastFrame = frame;
    }

    public void Stop() {
        m_IsPlaying = false;
    }

    public Animation(Bitmap[] frames, float animationTime) {
        m_Frames = frames;
        m_FrameIndex = 0;
        m_FrameTime = animationTime / frames.length;
        SetLastFrame(System.currentTimeMillis());
    }

    public void Update() {

        if (!m_IsPlaying)
            return;

        if (System.currentTimeMillis() - m_LastFrame > m_FrameTime * 1000) {
            m_FrameIndex++;
            m_FrameIndex = m_FrameIndex >= m_Frames.length ? 0 : m_FrameIndex;
            SetLastFrame(System.currentTimeMillis());
        }
    }

    public void Draw(Canvas canvas, Rect destination) {
        if (!m_IsPlaying)
            return;

//        ScaleRectangle(destination);
        canvas.drawBitmap(m_Frames[m_FrameIndex], null, destination, new Paint());
    }

    private void ScaleRectangle(Rect rectangle) {
        float widthHeightRatio = (float)(m_Frames[m_FrameIndex].getWidth()) / m_Frames[m_FrameIndex].getHeight();
        if (rectangle.width() > rectangle.height()) {
            rectangle.left = rectangle.right - (int)(rectangle.height() * widthHeightRatio);
        }
        else {
            rectangle.top = rectangle.bottom - (int)(rectangle.width() * (1 / widthHeightRatio));
        }
    }
}
