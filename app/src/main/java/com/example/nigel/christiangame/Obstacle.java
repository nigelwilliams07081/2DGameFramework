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

    private final float OBSTACLE_SPEED = 10.0f;

    private Rect m_Collider;
    private int m_Color;
    private float m_HorizontalSpeed;
    private float m_VerticalSpeed;
    private int m_Width;
    private int m_Height;

    private boolean m_IsActive;
    private Paint m_Paint;

    private boolean m_IsLarge; //for reference go to ObstacleManager

    public Obstacle(int left, int top, int right, int bottom, int width, int height, int color, boolean isLarge) {
        if (isLarge) {
            // double size
            // width = right - left
            // height = bottom - top
        }
        else {
            m_Collider = new Rect(left, top, right, bottom);
            m_Width = width;
            m_Height = height;
        }
        m_IsLarge = isLarge;
        m_Color = color;
        m_IsActive = true;
        m_Paint = new Paint();
        m_Paint.setColor(m_Color);
        SetSpeed(left, top);
    }

    private void SetSpeed(int left, int top) {
        if ((float)left < Constants.ScreenWidth / 2.0f) {
            m_HorizontalSpeed = (float)Math.random() * OBSTACLE_SPEED + 1.0f;
        }
        else {
            m_HorizontalSpeed = (float)Math.random() * -OBSTACLE_SPEED - 1.0f;
        }

        if ((float)top < Constants.ScreenHeight / 2.0f) {
            m_VerticalSpeed = (float)Math.random() * OBSTACLE_SPEED + 1.0f;
        }
        else {
            m_VerticalSpeed = (float)Math.random() * -OBSTACLE_SPEED - 1.0f;
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
    private void DestroyCollider() {
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

    @Override
    public void Update() {

    }

    @Override
    public void Draw(Canvas canvas) {
        canvas.drawRect(m_Collider, m_Paint);
    }
}
