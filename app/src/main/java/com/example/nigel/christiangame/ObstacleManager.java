package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nigel on 9/23/2017.
 */

public class ObstacleManager {

    private ArrayList<Obstacle> m_Obstacles;

    private int m_ObstacleHeight;
    private int m_ObstacleWidth;
    private int m_Color;
    private long m_StartTime;
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

    /**
     * Holds a list of obstacles of size @obstacleWidth @obstacleHeight and color @color
     * @param obstacleWidth The width of all obstacles
     * @param obstacleHeight The height of all obstacles
     * @param color The color of  all obstacles
     */
    public ObstacleManager(int obstacleWidth, int obstacleHeight, int color) {
        m_ObstacleWidth = obstacleWidth;
        m_ObstacleHeight = obstacleHeight;
        m_Color = color;
        m_StartTime = Constants.CurrentGameTime;

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

    /**
     * Returns true if any Obstacle has collided with the Player
     * @return boolean
     */
    public boolean GetIsCollidingWithPlayer() { return m_IsCollidingWithPlayer; }

    public boolean IsCollidingWith(Player player) {
        for (Obstacle obstacle : m_Obstacles) {
            if (obstacle.IsCollidingWith(player)) {
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

                m_Obstacles.add(0, new Obstacle(xStart, yStart, xStart + m_ObstacleWidth, yStart + m_ObstacleHeight, 50, 50,  m_Color));
                m_Obstacles.remove(obstacle);

                m_Score = m_Score + 1;
            }
        }
    }

    /**
     * Populates the initial list of obstacles
     */
    private void PopulateObstacles() {
        System.out.println("Trying to populate obstacles");
        for (int i = 0; i < 7; i++) {
            ResetObstaclePosition();
            m_Obstacles.add(new Obstacle(m_XStart, m_YStart, m_XStart + 50, m_YStart + 50, 50, 50, m_Color));
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
        // Restarts m_StartTime when you reenter the app

//        for (int i = 0; i < m_Obstacles.size(); i++) {
//            m_Obstacles.get(i).Move(m_Obstacles.get(i).GetHorizontalSpeed(), m_Obstacles.get(i).GetVerticalSpeed());
//            if (m_Obstacles.get(i).IsOutOfBounds(-100, Constants.ScreenWidth + 100, -100, Constants.ScreenHeight + 100)) {
//                ResetObstaclePosition();
////                m_Obstacles.remove(m_Obstacles.get(i));
//                m_Obstacles.remove(i);
//                m_Obstacles.add(0, new Obstacle(m_XStart, m_YStart, m_XStart + m_Obstacles.get(i).GetWidth(), m_YStart + m_Obstacles.get(i).GetHeight(), 50, 50, m_Obstacles.get(i).GetColor()));
//            }
//            else if (m_Obstacles.get(i).IsCollidingWith(m_Player)) {
//                m_IsCollidingWithPlayer = true;
//            }
//        }

        for (Obstacle obstacle : m_Obstacles) {
            // Moves each obstacle down the screen
            obstacle.Move(obstacle.GetHorizontalSpeed(), obstacle.GetVerticalSpeed());
            if (obstacle.IsOutOfBounds(-100, Constants.ScreenWidth + 100, -100, Constants.ScreenHeight + 100)) {
                ResetObstaclePosition();
                m_Obstacles.remove(obstacle);
                m_Obstacles.add(0, new Obstacle(m_XStart, m_YStart, m_XStart + obstacle.GetWidth(), m_YStart + obstacle.GetHeight(), 50, 50, obstacle.GetColor()));
            }
            else if (obstacle.IsCollidingWith(m_Player)) {
                m_IsCollidingWithPlayer = true;
            }
        }
    }

    public void Draw(Canvas canvas) {
        for (Obstacle obstacle : m_Obstacles) {
            obstacle.Draw(canvas);
        }
        canvas.drawText("" + m_Score, 50, 50 + m_Paint.descent() - m_Paint.ascent(), m_Paint);
    }
}
