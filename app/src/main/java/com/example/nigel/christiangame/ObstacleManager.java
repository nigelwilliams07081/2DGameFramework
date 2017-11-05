package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nigel on 9/23/2017.
 */

public class ObstacleManager {

    private ArrayList<Obstacle> m_Obstacles;

    private int m_Score = 0;

    private int m_XStart;
    private int m_YStart;
    private boolean m_IsCollidingWithPlayer;
    private Player m_Player;
    private Paint m_Paint;
    private Random m_Random;
    private boolean m_IsAtTop;
    private boolean m_IsAtBottom;
    private boolean m_IsAtLeft;
    private boolean m_IsAtRight;

    private LaserManager m_LaserManager;

    /**
     * Holds a list of obstacles of size 50, 50, and color Color.WHITE
     */
    public ObstacleManager() {
        m_Obstacles = new ArrayList<>();

        m_Paint = new Paint();
        m_Paint.setTextSize(Constants.GAME_OVER_TEXT_SIZE);
        m_Paint.setColor(Constants.GAME_OVER_TEXT_COLOR);

        m_Random = new Random();
        PopulateObstacles();
    }

    /**
     * Returns the list of Obstacles
     * @return Returns an ArrayList of Obstacles
     */
    public ArrayList<Obstacle> GetObstacles() {
        return m_Obstacles;
    }

    public int GetScore() {
        return m_Score;
    }

    public void SetScore(int score) {
        m_Score = score;
    }

    public void SetTarget(Player player) {
        m_Player = player;
    }

    public void SetLaserManager(LaserManager manager) {
        m_LaserManager = manager;
    }

    /**
     * Returns true if any Obstacle has collided with the Player
     * @return boolean
     */
    public boolean GetIsCollidingWithPlayer() { return m_IsCollidingWithPlayer; }

    public boolean IsCollidingWith(Rect collider) {
        for (Obstacle obstacle : m_Obstacles) {
            if (obstacle.IsCollidingWith(collider)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes an obstacle form the ObstacleManager if it hits any of the bounds, and creates a new obstacle at a random position
     * @param leftX @description The left bound value
     * @param rightX @description The right bound value
     * @param upY @description The top bound value
     * @param downY @description The bottom bound value
     */
    private void CheckIfOutOfBounds(int leftX, int rightX, int upY, int downY) {
        for (Obstacle obstacle : m_Obstacles) {
            if (obstacle.IsOutOfBounds(leftX, rightX, upY, downY)) {
                int xStart = (int)(Math.random() * Constants.ScreenWidth);
                int yStart = 50;
//                int yStart = 100;

                m_Obstacles.add(0, new Obstacle(xStart, yStart, xStart + 50, yStart + 50, 50, 50, Color.WHITE));
                m_Obstacles.remove(obstacle);

                m_Score = m_Score + 1;
            }
        }
    }

    /**
     * Populates the initial list of obstacles
     */
    private void PopulateObstacles() {
        for (int i = 0; i < 7; i++) {
            ResetObstaclePosition();
            m_Obstacles.add(new Obstacle(m_XStart, m_YStart, m_XStart + 50, m_YStart + 50, 50, 50, Color.WHITE));
        }
    }

    /**
     * Resets the position of the Obstacle to be at a very random position
     */
    private void ResetObstaclePosition() {
        if (m_Random.nextBoolean()) {
            m_IsAtTop = true;
            m_IsAtLeft = true;
            m_IsAtBottom = false;
            m_IsAtRight = false;
        }
        else {
            m_IsAtTop = false;
            m_IsAtLeft = false;
            m_IsAtBottom = true;
            m_IsAtRight = true;
        }

        if (m_IsAtTop) {
            m_XStart = (int)(Math.random() * Constants.ScreenWidth);
            m_YStart = 0;
        }
        else if (m_IsAtBottom) {
            m_XStart = (int)(Math.random() * Constants.ScreenWidth);
            m_YStart = Constants.ScreenHeight;
        }
        if (m_IsAtLeft) {
            m_XStart = 0;
            m_YStart = (int)(Math.random() * Constants.ScreenHeight);
        }
        else if (m_IsAtRight) {
            m_XStart = Constants.ScreenWidth;
            m_YStart = (int)(Math.random() * Constants.ScreenHeight);
        }
    }

    public void Update() {

        // Instead of removing and adding the obstacles, we will replace the same obstacle (to avoid much lag)
        for (Obstacle obstacle : m_Obstacles) {
            // Moves each obstacle down the screen
            obstacle.Move(obstacle.GetHorizontalSpeed(), obstacle.GetVerticalSpeed());
            if (obstacle.IsOutOfBounds(-100, Constants.ScreenWidth + 100, -100, Constants.ScreenHeight + 100)) {
                ResetObstaclePosition();
                obstacle.GetCollider().set(m_XStart, m_YStart, m_XStart + obstacle.GetWidth(), m_YStart + obstacle.GetHeight());
            }
            else if (obstacle.IsCollidingWith(m_Player.GetCollider())) {
                m_IsCollidingWithPlayer = true;
            }

            for (Laser laser : m_LaserManager.GetLasers()) {
                if (obstacle.IsCollidingWith(laser.GetCollider())) {
                    ResetObstaclePosition();
                    obstacle.GetCollider().set(m_XStart, m_YStart, m_XStart + obstacle.GetWidth(), m_YStart + obstacle.GetHeight());
                }
            }
        }
    }

    public void Draw(Canvas canvas) {
        for (Obstacle obstacle : m_Obstacles) {
            obstacle.Draw(canvas);
        }
    }
}
