package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Nigel on 9/22/2017.
 */

public class Obstacle implements GameObject {

    private Rect m_Collider1;
    private Rect m_Collider2;
    private int m_Color;

    public Obstacle(int colliderHeight, int startX, int startY, int gap, int color) {
        m_Collider1 = new Rect(0, startY, startX, startY + colliderHeight);
        m_Collider2 = new Rect(startX + gap, startY, Constants.ScreenWidth, startY + colliderHeight);
        m_Color = color;
    }

    public Rect GetCollider1() {
        return m_Collider1;
    }
    public Rect GetCollider2() { return m_Collider2; }

    public void DestroyColliders() {
        m_Collider1 = null;
        m_Collider2 = null;
    }

    public boolean IsCollidingWith(Player player) {
        return Rect.intersects(m_Collider1, player.GetCollider()) ||
                Rect.intersects(m_Collider2, player.GetCollider());
    }

    public void Move(float amount) {
        m_Collider1.top += amount;
        m_Collider1.bottom += amount;
        m_Collider2.top += amount;
        m_Collider2.bottom += amount;
    }

    @Override
    public void Update() {

    }

    @Override
    public void Draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(m_Color);
        canvas.drawRect(m_Collider1, paint);
        canvas.drawRect(m_Collider2, paint);
    }
}
