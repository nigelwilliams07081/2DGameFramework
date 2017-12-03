package com.example.nigel.christiangame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Nigel on 9/19/2017.
 */

public class Player implements GameObject {

    private Rect m_Collider;
    private Point m_Position;
    private int m_Color;
    private Paint m_Paint;
    private int m_OldLeftValue;

    private float m_XSpeed;
    private float m_YSpeed;
    private float m_RotationSpeed = 1.0f;

    public final float BOOST_AMOUNT = 2.0f;
    public final float INITIAL_BOOST_TIME = 5.0f;
    private float m_BoostTimeLimit;

    private boolean m_IsShooting;
    private boolean m_IsBoosting;

    private Animation m_IdleAnimation;
    private Bitmap m_Image;
    private Matrix m_Matrix;
    private int m_AnimationState;

    private AnimationManager m_AnimationManager;

    private LaserManager m_LaserManager;

    private int m_Lives;

    private final float INITIAL_HIT_TIMER = 1.0f;
    private float m_HitTimer;

    public Player(Rect collider) {
        m_Collider = collider;
        ResetLives();
        m_BoostTimeLimit = INITIAL_BOOST_TIME;
        BitmapFactory bitmapFactory = new BitmapFactory();
        m_Image = bitmapFactory.decodeResource(Constants.CurrentContext.getResources(), R.drawable.ship);
        m_Matrix = new Matrix();

        m_IdleAnimation = new Animation(new Bitmap[]{ m_Image }, 2);

        m_AnimationManager = new AnimationManager(new Animation[]{ m_IdleAnimation });

        m_Paint = new Paint();
    }

    /**
     * Returns the Player's Collider
     * @return Rect
     */
    public Rect GetCollider() {
        return m_Collider;
    }
    public Bitmap GetImage() { return m_Image; }

    public float GetBoostTimeLimit() { return m_BoostTimeLimit; }
    public void SetBoostTimeLimit(float time) {
        m_BoostTimeLimit = time;
    }

    /**
     * Returns the Player's position
     * @return Point
     */
    public Point GetPosition() { return m_Position; }

    /**
     * Sets the Position of the Player on the screen
     * @param position The Point the Player will be set to in pixels
     */
    public void SetPosition(Point position) { m_Position = position; }

    /**
     * Sets the X,Y Position of the Player on the screen
     * @param xPosition The X-Position the Player will be set to
     * @param yPosition The Y-Position the Player will be set to
     */
    public void SetPosition(int xPosition, int yPosition) {
        m_Position.x = xPosition;
        m_Position.y = yPosition;
    }

    /**
     * Sets the X-Position of the Player on the screen
     * @param x The X-Position in pixels
     */
    public void SetXPosition(int x) { m_Position.x = x; }

    /**
     * Sets the Y-Position of the Player on the screen
     * @param y The Y-Position in pixels
     */
    public void SetYPosition(int y) { m_Position.y = y; }

    public void SetRotation(float rotation) { m_RotationSpeed = rotation; }

    /**
     * Returns the X-Speed of the Player
     * @return float
     */
    public float GetXSpeed() { return m_XSpeed; }

    /**
     * Sets the X-Speed of the Player
     * @param speed The speed in the X-direction the player should move
     */
    public void SetXSpeed(float speed) { m_XSpeed = speed; }

    public float GetRotation() { return m_RotationSpeed; }

    /**
     * Returns the Y-Speed of the Player
     * @return float
     */
    public float GetYSpeed() { return m_YSpeed; }

    /**
     * Sets the Y-Speed of the Player
     * @param speed The speed in the Y-direction the player should move
     */
    public void SetYSpeed(float speed) { m_YSpeed = speed; }

    /**
     * Returns whether or not the Player is boosting
     * @return boolean
     */
    public boolean GetIsBoosting() { return m_IsBoosting; }

    /**
     * Sets whether or not the Player is boosting
     * @param isBoosting
     */
    public void SetIsBoosting(boolean isBoosting) {
        m_IsBoosting = isBoosting;
    }

    public boolean GetIsShooting() { return m_IsShooting; }

    public void SetIsShooting(boolean isShooting) { m_IsShooting = isShooting; }

    public LaserManager GetLaserManager() { return m_LaserManager; }

    public int GetLives() { return m_Lives; }
    public void DecreaseLives() { m_Lives -= 1; }
    public void ResetLives() { m_Lives = 5; }

    public float GetHitTimer() { return m_HitTimer; }
    public void ReduceHitTimer(float amount) {
        if (m_HitTimer >= 0.0f) {
            m_HitTimer -= amount;
        }
    }
    public void ResetHitTimer() { m_HitTimer = INITIAL_HIT_TIMER; }

    @Override
    public void Update() {
        m_AnimationManager.Update();
    }

    public void Update(Point point) {
        m_OldLeftValue = m_Collider.left;

        m_Collider.set(point.x - m_Collider.width() / 2, point.y - m_Collider.height() / 2, point.x + m_Collider.width() / 2, point.y + m_Collider.height() / 2);

        m_AnimationState = 0;

        if (m_IsBoosting) {
            if (m_BoostTimeLimit > 0.0f) {
                m_BoostTimeLimit -= 0.01f;
            }
            else {
                m_IsBoosting = false;
                m_BoostTimeLimit = INITIAL_BOOST_TIME;
            }
        }

        m_AnimationManager.PlayAnimation(m_AnimationState);
        m_AnimationManager.Update();

//        m_LaserManager.SetSpawnPosition(m_Position.x, m_Position.y - 100);
//        m_LaserManager.Update();
    }

    @Override
    public void Draw(Canvas canvas) {
        m_AnimationManager.Draw(canvas, m_Collider, m_Paint);
    }
}
