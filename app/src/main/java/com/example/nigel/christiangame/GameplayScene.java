package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Nigel on 9/28/2017.
 */

public class GameplayScene implements Scene {

    private Rect m_SceneBounds = new Rect();

    private Player m_Player;
    private ObstacleManager m_ObstacleManager;

    private ActionButton m_BoostButton;
    private ActionButton m_TeleportButton;
    private ActionButton m_FireButton;

    private boolean m_PlayerIsMoving = false;
    private boolean m_IsGameOver = false;
    private long m_GameOverTime;

    private OrientationData m_OrientationData;
    private float m_ElapsedTime;
    private long m_FrameTime;

    private float m_Pitch;
    private float m_Roll;


    public GameplayScene() {

        m_Player = new Player(new Rect(100, 100, 200, 200));
        Point position = new Point(Constants.ScreenWidth / 2, 3 * Constants.ScreenHeight / 4);
        m_Player.SetPosition(position);
        m_Player.Update(m_Player.GetPosition());
        m_ObstacleManager = new ObstacleManager(50, 50, Color.WHITE);
        m_ObstacleManager.SetTarget(m_Player);
        InitializeOrientationData();

        PlaceButton(m_BoostButton, Color.rgb(22, 200, 22), Color.WHITE, "Boost", Constants.ScreenWidth / 4,
                Constants.ScreenHeight - Constants.SCREEN_HEIGHT_PADDING, true);
        PlaceButton(m_TeleportButton, Color.rgb(22, 22, 200), Color.WHITE, "Teleport", 2 * Constants.ScreenWidth / 4,
                Constants.ScreenHeight - Constants.SCREEN_HEIGHT_PADDING, false);
        PlaceButton(m_FireButton, Color.rgb(200, 22, 22), Color.WHITE, "Fire", 3 * Constants.ScreenWidth / 4,
                Constants.ScreenHeight - Constants.SCREEN_HEIGHT_PADDING, false);

        m_FrameTime = System.currentTimeMillis();

    }

    private void InitializeOrientationData() {
        m_OrientationData = new OrientationData();
        m_OrientationData.Register();
    }


    private void PlaceButton(ActionButton button, int backgroundColor, int textColor, String text, float x, float y, boolean isBoost) {
        button = new ActionButton(new Button(Constants.CurrentContext), backgroundColor, textColor, text, x, y);
        button.SetTarget(m_Player);
        button.SetIsBoost(isBoost);

        Constants.RelativeLayout.addView(button.GetButton());
    }

    public void ResetGame() {

        m_Player.SetPosition(new Point(Constants.ScreenWidth / 2, 3 * Constants.ScreenHeight / 4));
        m_Player.Update(m_Player.GetPosition());
        m_ObstacleManager = new ObstacleManager(50, 50, Color.WHITE);
        m_ObstacleManager.SetTarget(m_Player);
        m_PlayerIsMoving = false;
    }

    @Override
    public void ReceiveTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {

            // If the user is holding finger on screen
            case MotionEvent.ACTION_DOWN:
                if (!m_IsGameOver && m_Player.GetCollider().contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
                    m_PlayerIsMoving = true;
                }
                // If the game is over and it has been 2 seconds or more, make the game restart
                if (m_IsGameOver && System.currentTimeMillis() - m_GameOverTime >= 2000) {
                    ResetGame();
                    m_IsGameOver = false;
                    // Reset the orientation data for a new game
                    m_OrientationData.NewGame();
                }
                break;

            // if user is moving finger on screen
            case MotionEvent.ACTION_MOVE:
                if (!m_IsGameOver && m_PlayerIsMoving) {
                    m_Player.SetPosition((int)motionEvent.getX(), (int)motionEvent.getY());
                }
                break;
            // if the user's finger is lifted from the screen
            case MotionEvent.ACTION_UP:
                m_PlayerIsMoving = false;
                break;
        }
    }

    // Don't worry about this for now
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

            MovePlayerByTilting();

            m_Player.Update(m_Player.GetPosition());
            m_ObstacleManager.Update();

            // Checks to see if any obstacle in the ObstacleManager is touching the player
            if (m_ObstacleManager.GetIsCollidingWithPlayer()) {
                m_IsGameOver = true;
                m_GameOverTime = Constants.CurrentGameTime;

                // TODO: Reduce the number of player lives and replace the player at a random position
            }
        }
    }

    @Override
    public void Draw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);

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

    private void MovePlayerByTilting() {

        m_ElapsedTime = (int)(System.currentTimeMillis() - m_FrameTime);
        m_FrameTime = System.currentTimeMillis();

        if (m_OrientationData.GetOrientation() != null && m_OrientationData.GetStartOrientation() != null) {
            MovePlayer();
        }

        if (m_Player.GetPosition().x < 0) {
            m_Player.SetXPosition(0);
        }
        else if (m_Player.GetPosition().x > Constants.ScreenWidth) {
            m_Player.SetXPosition(Constants.ScreenWidth);
        }

        if (m_Player.GetPosition().y < 0) {
            m_Player.SetYPosition(0);
        }
        else if (m_Player.GetPosition().y > Constants.ScreenHeight) {
            m_Player.SetYPosition(Constants.ScreenHeight);
        }
    }

    private void MovePlayer() {
        m_Pitch = m_OrientationData.GetOrientation()[1] - m_OrientationData.GetStartOrientation()[1];
        m_Roll = m_OrientationData.GetOrientation()[2] - m_OrientationData.GetStartOrientation()[2];

        if (m_Player.GetIsBoosting()) {
            m_Player.SetXSpeed(m_Player.BOOST_AMOUNT * 2 * m_Roll * Constants.ScreenWidth / 1000.0f);
            m_Player.SetYSpeed(m_Player.BOOST_AMOUNT * -m_Pitch * Constants.ScreenHeight / 1000.0f);

        }
        else {
            m_Player.SetXSpeed(2 * m_Roll * Constants.ScreenWidth / 1000.0f);
            m_Player.SetYSpeed(-m_Pitch * Constants.ScreenHeight / 1000.0f);
        }

        if (Math.abs(m_Player.GetXSpeed() * m_ElapsedTime) > 5.0) {
            m_Player.SetXPosition(m_Player.GetPosition().x + (int)(m_Player.GetXSpeed() * m_ElapsedTime));
        }
        else {
            m_Player.SetXPosition(m_Player.GetPosition().x);
        }

        if (Math.abs(m_Player.GetYSpeed() * m_ElapsedTime) > 5.0) {
            m_Player.SetYPosition(m_Player.GetPosition().y + (int)(m_Player.GetYSpeed() * m_ElapsedTime));
        }
        else {
            m_Player.SetYPosition(m_Player.GetPosition().y);
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
