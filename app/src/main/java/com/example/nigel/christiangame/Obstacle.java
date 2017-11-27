package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.webkit.ConsoleMessage;

/**
 * Created by Nigel on 9/22/2017.
 */

public class Obstacle implements GameObject {

    private final float OBSTACLE_SPEED = 5.0f;
    private final float LARGE_OBSTACLE_SPEED = 3.0f;

    private Rect m_Collider;
    private int m_Color;
    private float m_HorizontalSpeed;
    private float m_VerticalSpeed;
    private int m_Width;
    private int m_Height;

    private boolean m_IsAtTop;
    private boolean m_IsAtBottom;
    private boolean m_IsAtLeft;
    private boolean m_IsAtRight;

    private boolean m_IsActive;
    private Paint m_Paint;

    private boolean m_IsLarge; //for reference go to ObstacleManager

    public Obstacle(int width, int height, int color, boolean isLarge) {
        m_IsLarge = isLarge;
        if (m_IsLarge) {
            m_Width = width * 2;
            m_Height = height * 2;
            m_Collider = new Rect(0, 0, m_Width, m_Height);
        }
        else {
            m_Width = width;
            m_Height = height;
            m_Collider = new Rect(0, 0, m_Width, m_Height);
        }
        ResetObstaclePosition();
        m_Color = color;
        m_IsActive = true;
        m_Paint = new Paint();
        m_Paint.setColor(m_Color);
        SetSpeed(m_Collider.left, m_Collider.top, m_IsLarge);

    }

    public Obstacle(int left, int top, int right, int bottom, int color, boolean isLarge) {
        m_IsLarge = isLarge;
        if (m_IsLarge) {
            m_Width = (right - left) * 2;
            m_Height = (bottom - top) * 2;
            m_Collider = new Rect(left, top, left + m_Width, bottom + m_Height);
        }
        else {
            m_Collider = new Rect(left, top, right, bottom);
            m_Width = right - left;
            m_Height = bottom - top;
        }
        m_Color = color;
        m_IsActive = true;
        m_Paint = new Paint();
        m_Paint.setColor(m_Color);
        SetSpeed(left, top, m_IsLarge);
    }

    private void SetSpeed(int left, int top, boolean isLarge) {
        float speed = isLarge ? LARGE_OBSTACLE_SPEED : OBSTACLE_SPEED;

        if ((float)left < Constants.ScreenWidth / 2.0f) {
            m_HorizontalSpeed = (float)Math.random() * speed + 1.0f;
        }
        else {
            m_HorizontalSpeed = (float)Math.random() * -speed - 1.0f;
        }

        if ((float)top < Constants.ScreenHeight / 2.0f) {
            m_VerticalSpeed = (float)Math.random() * speed + 1.0f;
        }
        else {
            m_VerticalSpeed = (float)Math.random() * -speed - 1.0f;
        }
    }

    /**
     * Returns true if the asteroid is large
     * @return boolean
     */
    public boolean GetIsLarge() { return m_IsLarge; }

    /**
     * Returns whether or not the Obstacle has been destroyed
     * @return boolean
     */
    public boolean GetIsActive() { return m_IsActive; }

    public void SetIsActive(boolean isActive) { m_IsActive = isActive; }

    /**
     * Returns the Collider of the Obstacle
     * @return Rect
     */
    public Rect GetCollider() {
        return m_Collider;
    }

    /**
     * Sets the Obstacle's Collider to be null
     */
    public void DestroyCollider() {
        m_Collider = null;
    }

    /**
     * Returns the Color of the Collider
     * @return int
     */
    public int GetColor() { return m_Color; }

    /**
     * Returns the Obstacle's Horizontal Speed
     * @return float
     */
    public float GetHorizontalSpeed() { return m_HorizontalSpeed; }

    /**123hyb n46+/
     * Sets the Obstacle's Horizontal Speed
     * @param speed The horizontal speed the obstacle will be set to
     */
    public void SetHorizontalSpeed(float speed) { m_HorizontalSpeed = speed; }

    /**
     * Returns the Obstacle's Vertical Speed
     * @return float
     */
    public float GetVerticalSpeed() { return m_VerticalSpeed; }

    /**
     * Sets the Obstacle's Vertical Speed
     * @param speed The vertical speed the obstacle will be set to
     */
    public void SetVerticalSpeed(float speed) { m_VerticalSpeed = speed; }

    /**
     * Returns the Width of the Collider
     * @return int
     */
    public int GetWidth() { return m_Width; }

    /**
     * Returns the Height of the Collider
     * @return int
     */
    public int GetHeight() { return m_Height; }

    /**
     * Returns true if this is touching the other collider
     * @param collider
     * @return
     */
    public boolean IsCollidingWith(Rect collider) {
        return Rect.intersects(m_Collider, collider);
    }

    /**
     * Returns true if the Obstacle is outside of the specified bounds
     * @param leftX The left X-Boundary
     * @param rightX The right X-Boundary
     * @param upY The top Y-Boundary
     * @param downY The bottom Y-Boundary
     * @return boolean
     */
    public boolean IsOutOfBounds(int leftX, int rightX, int upY, int downY) {
        return m_Collider.left < leftX || m_Collider.right > rightX || m_Collider.top < upY || m_Collider.bottom > downY;
    }

    /**
     * Moves the Collider's left and right values by the xAmount, and the Collider's top and bottom values by the yAmount
     * @param xAmount The amount in the X-Direction the Collider should move
     * @param yAmount The amount in teh Y-Direction the Collider should move
     */
    public void Move(float xAmount, float yAmount) {
        m_Collider.left += xAmount;
        m_Collider.right += xAmount;
        m_Collider.top += yAmount;
        m_Collider.bottom += yAmount;
    }

    private void ResetObstaclePosition() {

        m_IsAtTop = Math.random() > 0.5;
        m_IsAtBottom = !m_IsAtTop;
        m_IsAtLeft = Math.random() > 0.5;
        m_IsAtRight = !m_IsAtLeft;

        if (m_IsAtTop) {
            m_Collider.right = (int)(Math.random() * Constants.ScreenWidth);
            m_Collider.left = m_Collider.right - m_Width;
            m_Collider.bottom = 0;
            m_Collider.top = m_Collider.bottom - m_Height;
        }
        else if (m_IsAtBottom) {
            m_Collider.right = (int)(Math.random() * Constants.ScreenWidth);
            m_Collider.left = m_Collider.right - m_Width;
            m_Collider.top = Constants.ScreenHeight;
            m_Collider.bottom = m_Collider.top + m_Height;
        }
        if (m_IsAtLeft) {
            m_Collider.right = 0;
            m_Collider.left = m_Collider.right - m_Width;
            m_Collider.top = (int)(Math.random() * Constants.ScreenHeight);
            m_Collider.bottom = m_Collider.top + m_Height;
        }
        else if (m_IsAtRight) {
            m_Collider.left = Constants.ScreenWidth;
            m_Collider.right = m_Collider.left + m_Width;
            m_Collider.top = (int)(Math.random() * Constants.ScreenHeight);
            m_Collider.bottom = m_Collider.top + m_Height;
        }
    }

    @Override
    public void Update() {
        Move(m_HorizontalSpeed, m_VerticalSpeed);
        if (IsOutOfBounds(-100, Constants.ScreenWidth + 100, -100, Constants.ScreenHeight + 100)) {
            m_IsActive = false;
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        if (m_IsActive) {
            canvas.drawRect(m_Collider, m_Paint);
        }
    }
}
