package com.example.nigel.christiangame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Nigel on 9/28/2017.
 */

public class GameplayScene implements Scene {

    private Rect m_SceneBounds = new Rect();

    private Player m_Player;
    private Point m_PlayerStartPosition;
    private final int STARTING_PLAYER_LIVES = 5;
    private ObstacleManager m_ObstacleManager;
    private LaserManager m_LaserManager;

    private ActionButton m_TeleportButton;

    private boolean m_PlayerIsMoving = false;
    private boolean m_IsGameOver = false;
    private long m_GameOverTime;

    private OrientationData m_OrientationData;
    private float m_ElapsedTime;
    private long m_FrameTime;

    private float m_Pitch;
    private float m_Roll;

    private Paint m_HighScorePaint;
    private Paint m_HighScoreTextPaint;
    private Paint m_ScorePaint;
    private Paint m_ScoreTextPaint;
    private Paint m_LivesPaint;
    private Paint m_LivesTextPaint;

    private SharedPreferences m_FileManager;

    public GameplayScene() {

        m_Player = new Player(new Rect(100, 100, 200, 200));
        m_PlayerStartPosition = new Point(Constants.ScreenWidth / 2, 3 * Constants.ScreenHeight / 4);
        m_Player.SetPosition(m_PlayerStartPosition);
        m_Player.Update(m_Player.GetPosition());
        m_ObstacleManager = new ObstacleManager();
        m_ObstacleManager.SetTarget(m_Player);

        m_LaserManager = new LaserManager();
        m_LaserManager.SetPlayer(m_Player);

        // These two are for checking when the lasers and obstacles collide
        m_ObstacleManager.SetLaserManager(m_LaserManager);
        m_LaserManager.SetObstacleManager(m_ObstacleManager);

        InitializeOrientationData();

        PlaceButton(EButtonType.TELEPORT, m_TeleportButton, Color.rgb(22, 22, 200), Color.WHITE, "Teleport", 2 * Constants.ScreenWidth / 4,
                Constants.ScreenHeight - Constants.SCREEN_HEIGHT_PADDING);

        m_FrameTime = System.currentTimeMillis();

        m_HighScorePaint = new Paint();
        m_HighScorePaint.setColor(Constants.HIGH_SCORE_TEXT_COLOR);
        m_HighScorePaint.setTextSize(Constants.HIGH_SCORE_TEXT_SIZE);
        m_HighScoreTextPaint = new Paint();
        m_HighScoreTextPaint.setColor(Constants.HIGH_SCORE_TEXT_COLOR);
        m_HighScoreTextPaint.setTextSize(Constants.HIGH_SCORE_LABEL_SIZE);

        m_ScorePaint = new Paint();
        m_ScorePaint.setColor(Constants.SCORE_TEXT_COLOR);
        m_ScorePaint.setTextSize(Constants.SCORE_TEXT_SIZE);
        m_ScoreTextPaint = new Paint();
        m_ScoreTextPaint.setColor(Constants.SCORE_TEXT_COLOR);
        m_ScoreTextPaint.setTextSize(Constants.SCORE_LABEL_SIZE);

        m_LivesPaint = new Paint();
        m_LivesPaint.setColor(Constants.PLAYER_LIVES_TEXT_COLOR);
        m_LivesPaint.setTextSize(Constants.PLAYER_LIVES_TEXT_SIZE);
        m_LivesTextPaint = new Paint();
        m_LivesTextPaint.setColor(Constants.PLAYER_LIVES_TEXT_COLOR);
        m_LivesTextPaint.setTextSize(Constants.PLAYER_LIVES_LABEL_SIZE);

        Constants.Score = 0;
        m_FileManager = Constants.CurrentContext.getSharedPreferences("HighScore", Context.MODE_PRIVATE);

        if (m_FileManager.contains(Constants.HighScoreID)) {
            Constants.HighScore = m_FileManager.getInt(Constants.HighScoreID, 0);
        }

        Constants.PlayerExplosionSound = MediaPlayer.create(Constants.CurrentContext, R.raw.player_explosion1);

    }

    private void InitializeOrientationData() {
        m_OrientationData = new OrientationData();
        m_OrientationData.Register();
    }


    private void PlaceButton(EButtonType type, ActionButton button, int backgroundColor, int textColor, String text, float x, float y) {
        button = new ActionButton(type, new Button(Constants.CurrentContext), backgroundColor, textColor, text, x, y);
        button.SetTarget(m_Player);

        Constants.RelativeLayout.addView(button.GetButton());
    }

    public void ResetGame() {

        if (Constants.HighScore < Constants.Score) {
            Constants.HighScore = Constants.Score;
        }

        m_Player.SetPosition(m_PlayerStartPosition);
        m_Player.ResetLives();
        m_Player.Update(m_Player.GetPosition());
        m_ObstacleManager = new ObstacleManager();
        m_ObstacleManager.SetTarget(m_Player);
        m_LaserManager = new LaserManager();
        m_LaserManager.SetPlayer(m_Player);

        // These two are for checking when the lasers and obstacles collide
        m_ObstacleManager.SetLaserManager(m_LaserManager);
        m_LaserManager.SetObstacleManager(m_ObstacleManager);

        Constants.BackgroundMusic.start();
        Constants.Score = 0;
        m_PlayerIsMoving = false;
    }

    private void SaveHighScore() {
        m_FileManager.edit().putInt(Constants.HighScoreID, Constants.HighScore);
        m_FileManager.edit().commit();
    }

    @Override
    public void ReceiveTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {

            // If the user is holding finger on screen
            case MotionEvent.ACTION_DOWN:
                // If the game is over and it has been 2 seconds or more, make the game restart
                if (m_IsGameOver && System.currentTimeMillis() - m_GameOverTime >= 2000) {
                    ResetGame();
                    m_IsGameOver = false;
                    // Reset the orientation data for a new game
                    m_OrientationData.NewGame();
                }
                else {
                    m_LaserManager.SpawnLaser();
                }
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
            m_LaserManager.SetSpawnPosition(m_Player.GetPosition().x, m_Player.GetPosition().y);
            m_LaserManager.Update();

            // Checks to see if any obstacle in the ObstacleManager is touching the player
            if (m_ObstacleManager.GetIsCollidingWithPlayer()) {
                m_Player.ReduceHitTimer(0.2f);
            }

            if (m_Player.GetHitTimer() <= 0.0f) {

                m_Player.DecreaseLives();
                m_Player.SetPosition(m_PlayerStartPosition);
                m_Player.ResetHitTimer();
                m_ObstacleManager.Reset();
                if (m_Player.GetLives() <= 0) {
                    if (Constants.BackgroundMusic.isPlaying()) {
                        Constants.ResetBackgroundMusic();
                    }
                    Constants.PlayerExplosionSound.start();
                    SaveHighScore();
                    m_IsGameOver = true;
                    m_GameOverTime = Constants.CurrentGameTime;
                }
            }

        }
    }

    @Override
    public void Draw(Canvas canvas) {
        if (m_IsGameOver) {
            // Will fix later
            Paint paint = new Paint();
            paint.setTextSize(Constants.GAME_OVER_TEXT_SIZE);
            paint.setColor(Constants.GAME_OVER_TEXT_COLOR);
            DrawCenteredText(canvas, paint, "Game Over");
        }
        m_Player.Draw(canvas);
        m_ObstacleManager.Draw(canvas);
        m_LaserManager.Draw(canvas);

        canvas.drawText(Constants.SCORE_LABEL, Constants.ScreenWidth - Constants.ScreenWidth / 2,
                Constants.ScreenHeight / 48 + m_ScoreTextPaint.descent() - m_ScoreTextPaint.ascent(), m_ScoreTextPaint);
        canvas.drawText(String.valueOf(Constants.Score), Constants.ScreenWidth - Constants.ScreenWidth / 2,
                Constants.ScreenHeight / 24 + m_ScorePaint.descent() - m_ScorePaint.ascent(), m_ScorePaint);

        canvas.drawText(Constants.HIGH_SCORE_LABEL, Constants.ScreenWidth - Constants.ScreenWidth / 3,
                Constants.ScreenHeight / 48 + m_HighScoreTextPaint.descent() - m_HighScoreTextPaint.ascent(), m_HighScoreTextPaint);
        canvas.drawText(String.valueOf(Constants.HighScore),Constants.ScreenWidth - Constants.ScreenWidth / 3,
                Constants.ScreenHeight / 24 + m_HighScorePaint.descent() - m_HighScorePaint.ascent(), m_HighScorePaint);

        canvas.drawText(Constants.LIVES_LABEL, Constants.ScreenWidth / 6,
                Constants.ScreenHeight / 48 + m_LivesTextPaint.descent() - m_LivesTextPaint.ascent(), m_LivesTextPaint);
        canvas.drawText(String.valueOf(m_Player.GetLives()), Constants.ScreenWidth / 6,
                Constants.ScreenHeight / 24 + m_LivesPaint.descent() - m_LivesPaint.ascent(), m_LivesPaint);

    }

    private void MovePlayerByTilting() {

        Constants.ElapsedTime = (int)(System.currentTimeMillis() - m_FrameTime);
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

        if (Math.abs(m_Player.GetXSpeed() * Constants.ElapsedTime) > 5.0) {
            m_Player.SetXPosition(m_Player.GetPosition().x + (int)(m_Player.GetXSpeed() * Constants.ElapsedTime));
        }
        else {
            m_Player.SetXPosition(m_Player.GetPosition().x);
        }

        if (Math.abs(m_Player.GetYSpeed() * Constants.ElapsedTime) > 5.0) {
            m_Player.SetYPosition(m_Player.GetPosition().y + (int)(m_Player.GetYSpeed() * Constants.ElapsedTime));
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
