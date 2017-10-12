package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Nigel on 9/23/2017.
 */

public class ObstacleManager {

    private ArrayList<Obstacle> m_Obstacles;

    private float m_Speed;
    private float m_ElapsedTime;
    private int m_StartingXPosition;

    private int m_Gap;
    private int m_ObstacleGap;
    private int m_ObstacleHeight;
    private int m_Color;
    private long m_StartTime;
    private long m_InitialTime;
    private int m_Score = 0;

    public ObstacleManager(int gap, int obstacleGap, int obstacleHeight, int color) {
        m_Gap = gap;
        m_ObstacleGap = obstacleGap;
        m_ObstacleHeight = obstacleHeight;
        m_Color = color;
        m_StartTime = System.currentTimeMillis();
        m_InitialTime = m_StartTime;

        m_Obstacles = new ArrayList<>();

        PopulateObstacles();
    }

    public boolean IsCollidingWith(Player player) {
        for (Obstacle obstacle : m_Obstacles) {
            if (obstacle.IsCollidingWith(player)) {
                return true;
            }
        }
        return false;
    }

    private void PopulateObstacles() {
        int currentY = -5 * Constants.ScreenHeight / 4;
//        m_Obstacles.add(new Obstacle(m_ObstacleHeight, 0, 0, m_PlayerGap, m_Color));
        while (currentY < 0) {
            int xStart = (int)(Math.random() * (Constants.ScreenWidth - m_Gap));
            m_Obstacles.add(new Obstacle(m_ObstacleHeight, xStart, currentY, m_Gap, m_Color));
            currentY += m_ObstacleHeight + m_ObstacleGap;
        }
    }

    public void Update() {
        // Restarts m_StartTime when you reenter the app
        if (m_StartTime < Constants.InitialTime) {
            m_StartTime = Constants.InitialTime;
        }
        m_ElapsedTime = (int)(System.currentTimeMillis() - m_StartTime);
        m_StartTime = System.currentTimeMillis();
        m_Speed = Constants.ScreenHeight / 10000.0f;

        for (Obstacle obstacle : m_Obstacles) {
            // Moves each obstacle down the screen
            obstacle.Move(m_Speed * m_ElapsedTime);
        }

        // If the last obstacle is off screen, add a new obstacle at the top of the screen with a random gap size
        if (m_Obstacles.get(m_Obstacles.size() - 1).GetCollider1().top >= Constants.ScreenHeight) {
            m_StartingXPosition = (int)(Math.random() * (Constants.ScreenWidth - m_Gap));
            m_Obstacles.add(0, new Obstacle(m_ObstacleHeight, m_StartingXPosition, m_Obstacles.get(0).GetCollider1().top - m_ObstacleHeight - m_ObstacleGap, m_Gap, m_Color));
            m_Obstacles.remove(m_Obstacles.size() - 1);

            m_Score = m_Score + 1;
        }
    }

    public void Draw(Canvas canvas) {
        for (Obstacle obstacle : m_Obstacles) {
            obstacle.Draw(canvas);
        }
        Paint paint = new Paint();
        paint.setTextSize(Constants.GAME_OVER_TEXT_SIZE);
        paint.setColor(Constants.GAME_OVER_TEXT_COLOR);
        canvas.drawText("" + m_Score, 50, 50 + paint.descent() - paint.ascent(), paint);
    }
}
