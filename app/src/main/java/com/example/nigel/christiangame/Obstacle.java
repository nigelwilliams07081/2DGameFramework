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

    private Rect m_Collider;
    private int m_Color;
    private float m_Speed;

    private long m_StartTime;
    private float m_ElapsedTime;
    private int m_Width;
    private int m_Height;

    private boolean m_IsActive;

    private Point m_Position;

    public Obstacle(int left, int top, int right, int bottom, int width, int height, int color) {
        m_Collider = new Rect(left, top, right, bottom);
        m_Width = width;
        m_Height = height;
        m_Color = color;
        m_Speed = (float)Math.random() * 7.0f + 1.0f;
        m_IsActive = true;
    }

    /**
     * Returns whether or not the Obstacle has been destroyed
     * @return boolean
     */
    public boolean GetIsActive() { return m_IsActive; }

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
     * Returns the Obstacle's Speed
     * @return float
     */
    public float GetSpeed() { return m_Speed; }

    /**
     * Sets the Obstacle's Speed
     * @param speed The speed the obstacle will be set to
     */
    public void SetSpeed(float speed) { m_Speed = speed; }

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
     * Returns true if this is touching the Player
     * @param player The Spaceship
     * @return boolean
     */
    public boolean IsCollidingWith(Player player) {
        return Rect.intersects(m_Collider, player.GetCollider());
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
        return m_Collider.bottom > downY;
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

    /**
     * Sets the X,Y Position of the Obstacle
     * @param xPosition
     * @param yPosition
     */
    public void SetPosition(int xPosition, int yPosition) {
        m_Position.x = xPosition;
        m_Position.y = yPosition;

    }

    @Override
    public void Update() {

    }

    @Override
    public void Draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(m_Color);
        canvas.drawRect(m_Collider, paint);
    }
}
