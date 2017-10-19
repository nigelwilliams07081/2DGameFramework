package com.example.nigel.christiangame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Nigel on 9/19/2017.
 */

public class Player implements GameObject {

    private Rect m_Collider;
    private Point m_Position;
    private int m_Color;
    private int m_OldLeftValue;

    private float m_XSpeed;
    private float m_YSpeed;
    public final float BOOST_AMOUNT = 2.0f;
    public final float INITIAL_BOOST_TIME = 5.0f;
    private float m_BoostTimeLimit;

    private boolean m_IsBoosting;

    private Animation m_IdleAnimation;
    private Animation m_WalkRightAnimation;
    private Animation m_WalkLeftAnimation;
    private int m_AnimationState;

    private AnimationManager m_AnimationManager;

    public Player(Rect collider) {
        m_Collider = collider;
        m_BoostTimeLimit = INITIAL_BOOST_TIME;
        BitmapFactory bitmapFactory = new BitmapFactory();
        Bitmap idle = bitmapFactory.decodeResource(Constants.CurrentContext.getResources(), R.drawable.alien_blue);
        Bitmap walk1 = bitmapFactory.decodeResource(Constants.CurrentContext.getResources(), R.drawable.alien_blue_walk1);
        Bitmap walk2 = bitmapFactory.decodeResource(Constants.CurrentContext.getResources(), R.drawable.alien_blue_walk2);

        m_IdleAnimation = new Animation(new Bitmap[]{ idle }, 2);
        m_WalkRightAnimation = new Animation(new Bitmap[]{ walk1, walk2 }, 0.5f);

        // flip walkright to be walkleft
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), matrix, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), matrix, false);

        m_WalkLeftAnimation = new Animation(new Bitmap[]{ walk1, walk2 }, 0.5f);

        m_AnimationManager = new AnimationManager(new Animation[]{ m_IdleAnimation, m_WalkLeftAnimation, m_WalkRightAnimation });
    }

    public Player(Rect collider, int color) {
        m_Collider = collider;
        m_Color = color;

        BitmapFactory bitmapFactory = new BitmapFactory();
        Bitmap idle = bitmapFactory.decodeResource(Constants.CurrentContext.getResources(), R.drawable.alien_blue);
        Bitmap walk1 = bitmapFactory.decodeResource(Constants.CurrentContext.getResources(), R.drawable.alien_blue_walk1);
        Bitmap walk2 = bitmapFactory.decodeResource(Constants.CurrentContext.getResources(), R.drawable.alien_blue_walk2);

        m_IdleAnimation = new Animation(new Bitmap[]{ idle }, 2);
        m_WalkRightAnimation = new Animation(new Bitmap[]{ walk1, walk2 }, 0.5f);

        // flip walkright to be walkleft
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), matrix, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), matrix, false);

        m_WalkLeftAnimation = new Animation(new Bitmap[]{ walk1, walk2 }, 0.5f);

        m_AnimationManager = new AnimationManager(new Animation[]{ m_IdleAnimation, m_WalkLeftAnimation, m_WalkRightAnimation });

    }

    public Rect GetCollider() {
        return m_Collider;
    }

    public float GetBoostTimeLimit() { return m_BoostTimeLimit; }
    public void SetBoostTimeLimit(float time) {
        m_BoostTimeLimit = time;
    }

    public Point GetPosition() { return m_Position; }
    public void SetPosition(Point position) { m_Position = position; }
    public void SetPosition(int xPosition, int yPosition) {
        m_Position.x = xPosition;
        m_Position.y = yPosition;
    }
    public void SetXPosition(int x) { m_Position.x = x; }
    public void SetYPosition(int y) { m_Position.y = y; }

    public float GetXSpeed() { return m_XSpeed; }
    public void SetXSpeed(float speed) { m_XSpeed = speed; }

    public float GetYSpeed() { return m_YSpeed; }
    public void SetYSpeed(float speed) { m_YSpeed = speed; }

    public boolean GetIsBoosting() { return m_IsBoosting; }
    public void SetIsBoosting(boolean isBoosting) {
        m_IsBoosting = isBoosting;
    }

    @Override
    public void Update() {
        m_AnimationManager.Update();
    }

    public void Update(Point point) {
        m_OldLeftValue = m_Collider.left;

        m_Collider.set(point.x - m_Collider.width() / 2, point.y - m_Collider.height() / 2, point.x + m_Collider.width() / 2, point.y + m_Collider.height() / 2);

        m_AnimationState = 0;
        if (m_Collider.left - m_OldLeftValue < -5) {
            m_AnimationState = 1;
        }
        else if (m_Collider.left - m_OldLeftValue > 5) {
            m_AnimationState = 2;
        }

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
    }

    @Override
    public void Draw(Canvas canvas) {
//         Paint paint = new Paint();
//         paint.setColor(m_Color);
//         canvas.drawRect(m_Rectangle, paint);
        m_AnimationManager.Draw(canvas, m_Collider);
    }
}
