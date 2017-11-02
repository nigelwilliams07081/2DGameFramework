package com.example.nigel.christiangame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Nigel on 9/28/2017.
 */

public class Animation {

    private Bitmap[] m_Images;
    private int m_ImageIndex;
    private boolean m_IsPlaying = false;
    private float m_FrameTime;
    private long m_LastImage;
    private Matrix m_Matrix;

    public boolean IsPlaying() {
        return m_IsPlaying;
    }

    public void Play() {
        m_IsPlaying = true;
        m_ImageIndex = 0;
        SetLastImage(System.currentTimeMillis());
    }

    private void SetLastImage(long frame) {
        m_LastImage = frame;
    }

    public void Stop() {
        m_IsPlaying = false;
    }

    public Animation(Bitmap[] frames, float animationTime) {
        m_Images = frames;
        m_ImageIndex = 0;
        m_FrameTime = animationTime / frames.length;
        SetLastImage(System.currentTimeMillis());
    }

    /***
     * Sets Matrix for rotating the image
     * @param matrix
     */
    public void SetMatrix(Matrix matrix) {
        m_Matrix = matrix;
    }

    public void Update() {
        if (!m_IsPlaying)
            return;

        if (System.currentTimeMillis() - m_LastImage > m_FrameTime * 1000) {
            m_ImageIndex++;
            m_ImageIndex = m_ImageIndex >= m_Images.length ? 0 : m_ImageIndex;
            SetLastImage(System.currentTimeMillis());
        }
    }

    public void Draw(Canvas canvas, Rect destination) {
        if (!m_IsPlaying)
            return;

//        ScaleRectangle(destination);
        canvas.drawBitmap(m_Images[m_ImageIndex], null, destination, new Paint());
//        canvas.drawBitmap(m_Images[m_ImageIndex], m_Matrix, new Paint());
    }

    private void ScaleRectangle(Rect rectangle) {
        float widthHeightRatio = (float)(m_Images[m_ImageIndex].getWidth()) / m_Images[m_ImageIndex].getHeight();
        if (rectangle.width() > rectangle.height()) {
            rectangle.left = rectangle.right - (int)(rectangle.height() * widthHeightRatio);
        }
        else {
            rectangle.top = rectangle.bottom - (int)(rectangle.width() * (1 / widthHeightRatio));
        }
    }
}
