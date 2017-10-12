package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.MotionEvent;

/**
 * Created by Nigel on 9/28/2017.
 */

public class GameplayScene implements Scene {

    private Rect m_SceneBounds = new Rect();

    private Player m_Player;
    private Point m_PlayerPoint;
    private ObstacleManager m_ObstacleManager;

    private boolean m_PlayerIsMoving = false;
    private boolean m_IsGameOver = false;
    private long m_GameOverTime;

    private OrientationData m_OrientationData;
    private float m_ElapsedTime;
    private long m_FrameTime;

    private float m_Pitch;
    private float m_Roll;
    private float m_Yaw;
    private float m_XSpeed;
    private float m_YSpeed;

    public GameplayScene() {

        m_Player = new Player(new Rect(100, 100, 200, 200));
        m_PlayerPoint = new Point(Constants.ScreenWidth / 2, 3 * Constants.ScreenHeight / 4);
        m_Player.Update(m_PlayerPoint);
        m_ObstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        m_OrientationData = new OrientationData();
        m_OrientationData.Register();
        m_FrameTime = System.currentTimeMillis();
    }

    public void ResetGame() {

        m_PlayerPoint = new Point(Constants.ScreenWidth / 2, 3 * Constants.ScreenHeight / 4);
        m_Player.Update(m_PlayerPoint);
        m_ObstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        m_PlayerIsMoving = false;
    }

    @Override
    public void ReceiveTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            // if the user is holding finger on screen
            case MotionEvent.ACTION_DOWN:
                if (!m_IsGameOver && m_Player.GetCollider().contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
                    m_PlayerIsMoving = true;
                }
                if (m_IsGameOver && System.currentTimeMillis() - m_GameOverTime >= 2000) {
                    ResetGame();
                    m_IsGameOver = false;
                    m_OrientationData.NewGame();
                }
                break;
            // if user is moving finger on screen
            case MotionEvent.ACTION_MOVE:
                if (!m_IsGameOver && m_PlayerIsMoving) {
                    m_PlayerPoint.set((int)motionEvent.getX(), (int)motionEvent.getY());
                }
                break;
            // if the user's finger is lifted from the screen
            case MotionEvent.ACTION_UP:
                m_PlayerIsMoving = false;
                break;
        }
    }

    @Override
    public void SwitchScene() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void Update() {
        if (!m_IsGameOver) {
            if (m_FrameTime < Constants.InitialTime) {
                m_FrameTime = Constants.InitialTime;
            }

            m_ElapsedTime = (int)(System.currentTimeMillis() - m_FrameTime);
            m_FrameTime = System.currentTimeMillis();
            if (m_OrientationData.GetOrientation() != null && m_OrientationData.GetStartOrientation() != null) {
                m_Pitch = m_OrientationData.GetOrientation()[1] - m_OrientationData.GetStartOrientation()[1];
                m_Roll = m_OrientationData.GetOrientation()[2] - m_OrientationData.GetStartOrientation()[2];
                m_XSpeed = 2 * m_Roll * Constants.ScreenWidth / 1000.0f;
                m_YSpeed = -m_Pitch * Constants.ScreenHeight / 1000.0f;

                if (Math.abs(m_XSpeed * m_ElapsedTime) > 5.0) {
                    m_PlayerPoint.x += (int)(m_XSpeed * m_ElapsedTime);
                }
                else {
                    m_PlayerPoint.x += 0;
                }

                if (Math.abs(m_YSpeed * m_ElapsedTime) > 5.0) {
                    m_PlayerPoint.y += (int)(m_YSpeed * m_ElapsedTime);
                }
                else {
                    m_PlayerPoint.y += 0;
                }

            }

            if (m_PlayerPoint.x < 0) {
                m_PlayerPoint.x = 0;
            }
            else if (m_PlayerPoint.x > Constants.ScreenWidth) {
                m_PlayerPoint.x = Constants.ScreenWidth;
            }

            if (m_PlayerPoint.y < 0) {
                m_PlayerPoint.y = 0;
            }
            else if (m_PlayerPoint.y > Constants.ScreenHeight) {
                m_PlayerPoint.y = Constants.ScreenHeight;
            }

            m_Player.Update(m_PlayerPoint);
            m_ObstacleManager.Update();

            if (m_ObstacleManager.IsCollidingWith(m_Player)) {
                m_IsGameOver = true;
                m_GameOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        m_Player.Draw(canvas);
        m_ObstacleManager.Draw(canvas);

        if (m_IsGameOver) {
            // Will fix later
            Paint paint = new Paint();
            paint.setTextSize(Constants.GAME_OVER_TEXT_SIZE);
            paint.setColor(Constants.GAME_OVER_TEXT_COLOR);
            DrawCenteredText(canvas, paint, "Game Over");
        }
    }

    private void DrawCenteredText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(m_SceneBounds);
        int canvasHeight = m_SceneBounds.height();
        int canvasWidth = m_SceneBounds.width();
        paint.getTextBounds(text, 0, text.length(), m_SceneBounds);
        float x = canvasWidth / 2.0f - m_SceneBounds.width() / 2.0f - m_SceneBounds.left;
        float y = canvasHeight / 2.0f + m_SceneBounds.height() / 2.0f - m_SceneBounds.bottom;
        canvas.drawText(text, x, y, paint);

    }
}
