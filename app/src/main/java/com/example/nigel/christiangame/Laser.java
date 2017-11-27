package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Nigel on 11/4/2017.
 */

public class Laser implements GameObject {

    private final float LASER_SPEED = 1.5f;

    private Rect m_Collider;
    private int m_Color;
    private float m_Speed;
    private int m_Width;
    private int m_Height;

    private boolean m_IsCollidingWithObstacle;
    private boolean m_IsActive;
    private Paint m_Paint;

    public Laser(int left, int top, int right, int bottom, int color) {
        m_Collider = new Rect(left, top, right, bottom);
        m_Color = color;
        m_Paint = new Paint();
        m_Paint.setColor(m_Color);
        m_Width = Math.abs(m_Collider.right - m_Collider.left);
        m_Height = Math.abs(m_Collider.bottom - m_Collider.top);
        SetSpeed();
        m_IsActive = true;
    }

    public Rect GetCollider() { return m_Collider; }
    public int GetColor() { return m_Color; }

    public float GetSpeed() { return m_Speed; }

    private void SetSpeed() {
        m_Speed = LASER_SPEED * Constants.ElapsedTime;
    }

    public int GetWidth() { return m_Width; }
    public int GetHeight() { return m_Height; }

    public boolean GetIsActive() { return m_IsActive; }

    public boolean IsCollidingWith(Rect collider) {
        return Rect.intersects(m_Collider, collider);
    }

    public boolean IsOutOfBounds(int leftX, int rightX, int upY, int downY) {
        return m_Collider.left < leftX || m_Collider.right > rightX || m_Collider.top < upY || m_Collider.bottom > downY;
    }

    public void Move(float xAmount, float yAmount) {
        m_Collider.left += xAmount;
        m_Collider.right += xAmount;
        m_Collider.top += yAmount;
        m_Collider.bottom += yAmount;
    }

    @Override
    public void Update() {
        Move(0.0f, -m_Speed);
        if (IsOutOfBounds(-100, Constants.ScreenWidth + 100, -100, Constants.ScreenHeight + 100)) {
            m_IsActive = false;
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        canvas.drawRect(m_Collider, m_Paint);
    }
}
