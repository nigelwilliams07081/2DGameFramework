package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by Nigel on 11/4/2017.
 */

public class LaserManager {

    private ArrayList<Laser> m_Lasers;

    private int m_LaserHeight;
    private int m_LaserWidth;

    private int m_XStart;
    private int m_YStart;

    private boolean m_IsCollidingWithObstacle;
    private Player m_Player;
    private ObstacleManager m_ObstacleManager;

    public LaserManager() {
        m_Lasers = new ArrayList<>();
        m_LaserWidth = 5;
        m_LaserHeight = 20;

        Constants.AsteroidExplosionSound = MediaPlayer.create(Constants.CurrentContext, R.raw.explosion);
        Constants.LaserSound = MediaPlayer.create(Constants.CurrentContext, R.raw.laser2);
    }

    public ArrayList<Laser> GetLasers() { return m_Lasers; }

    public void SetPlayer(Player player) {
        m_Player = player;
    }

    public void SetSpawnPosition(int xStart, int yStart) {
        m_XStart = xStart;
        m_YStart = yStart;
    }

    public void SpawnLaser() {
        m_Lasers.add(0, new Laser(m_XStart, m_YStart, m_XStart + 5, m_YStart + 20, Color.GREEN));
        Constants.LaserSound.seekTo(0);
        Constants.LaserSound.start();
    }

    public boolean GetIsCollidingWithObstacles() { return m_IsCollidingWithObstacle; }

    public boolean IsCollidingWith(Rect collider) {
        for (Laser laser : m_Lasers) {
            if (laser.IsCollidingWith(collider)) {
                return true;
            }
        }
        return false;
    }

    public void SetObstacleManager(ObstacleManager manager) {
        m_ObstacleManager = manager;
    }

    public void Update() {
        for (Laser laser : m_Lasers) {
            if (!laser.GetIsActive()) {
                m_Lasers.remove(laser);
            }
            else {
                laser.Update();
                for (Obstacle obstacle : m_ObstacleManager.GetObstacles()) {
                    if (laser.IsCollidingWith(obstacle.GetCollider())) {
                        Constants.Score += 1;
                        obstacle.SetIsActive(false);
                        m_Lasers.remove(laser);
                        Constants.AsteroidExplosionSound.seekTo(0);
                        Constants.AsteroidExplosionSound.start();
                        break;
                    }
                }
            }
        }
    }

    public void Draw(Canvas canvas) {
        for (Laser laser : m_Lasers) {
            laser.Draw(canvas);
        }
    }
}
