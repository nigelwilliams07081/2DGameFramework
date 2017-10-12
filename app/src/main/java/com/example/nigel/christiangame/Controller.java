package com.example.nigel.christiangame;

/**
 * Created by Nigel on 9/18/2017.
 */

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.InputEvent;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import java.lang.Object;

enum ControllerDirection {
    NONE,
    LEFT,
    RIGHT,
    UP,
    DOWN
}

public class Controller extends AppCompatActivity {

    public static ControllerDirection Direction;
    private float m_LastXPosition;
    private float m_LastYPosition;
    private float m_XPosition;
    private float m_YPosition;
    private float m_DX;
    private float m_DY;
    private final float MAX_MOVEMENT_AMOUNT = 2.0f;
    private boolean m_IsRightController;

    private int m_ActivePointerId;

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Direction = ControllerDirection.NONE;

        final int action = MotionEventCompat.getActionMasked(motionEvent);

        if (action == MotionEvent.ACTION_DOWN) {
            final int pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
            SetLastXPosition(MotionEventCompat.getAxisValue(motionEvent, MotionEvent.AXIS_X, pointerIndex));
            SetLastYPosition(MotionEventCompat.getAxisValue(motionEvent, MotionEvent.AXIS_Y, pointerIndex));
        }
        else if (action == MotionEvent.ACTION_MOVE) {
            final int pointerIndex = motionEvent.findPointerIndex(m_ActivePointerId);
            m_DX = MotionEventCompat.getAxisValue(motionEvent, MotionEvent.AXIS_X, pointerIndex) - m_LastXPosition;
            m_DY = MotionEventCompat.getAxisValue(motionEvent, MotionEvent.AXIS_Y, pointerIndex) - m_LastYPosition;

            m_XPosition += m_DX;
            m_YPosition += m_DY;

            invalidateOptionsMenu();

            SetLastXPosition(MotionEventCompat.getAxisValue(motionEvent, MotionEvent.AXIS_X, pointerIndex));
            SetLastYPosition(MotionEventCompat.getAxisValue(motionEvent, MotionEvent.AXIS_Y, pointerIndex));
        }
        else if (action == MotionEvent.ACTION_UP) {
            m_ActivePointerId = -1;
        }
        else if (action == MotionEvent.ACTION_CANCEL) {
            m_ActivePointerId = -1;
        }
        else if (action == MotionEvent.ACTION_POINTER_UP) {
            final int pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
            final int pointerId = motionEvent.getPointerId(m_ActivePointerId);

            if (pointerId == m_ActivePointerId) {
                final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                SetLastXPosition(MotionEventCompat.getAxisValue(motionEvent, MotionEvent.AXIS_X, newPointerIndex));
                SetLastYPosition(MotionEventCompat.getAxisValue(motionEvent, MotionEvent.AXIS_Y, newPointerIndex));
            }
        }
        return true;
    }

    public float GetLastXPosition() {
        return m_LastXPosition;
    }

    public float GetLastYPosition() {
        return m_LastYPosition;
    }

    public boolean GetIsRightController() {
        return m_IsRightController;
    }

    private void SetLastXPosition(float value) {
        m_LastXPosition = value;
    }

    private void SetLastYPosition(float value) {
        m_LastYPosition = value;
    }
}
