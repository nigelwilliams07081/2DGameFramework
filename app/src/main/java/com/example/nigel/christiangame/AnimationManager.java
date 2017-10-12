package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Nigel on 9/28/2017.
 */

public class AnimationManager {

    private Animation[] m_Animations;
    private int m_AnimationIndex = 0;

    public AnimationManager(Animation[] animations) {
        m_Animations = animations;
    }

    public void PlayAnimation(int index) {
        for (int i = 0; i < m_Animations.length; i++) {
            if (i == index) {
                if (!m_Animations[index].IsPlaying()) {
                    m_Animations[i].Play();
                }
            }
            else {
                if (m_Animations[index].IsPlaying()) {
                    m_Animations[i].Stop();
                }
            }
            m_AnimationIndex = index;
        }
    }

    public void Update() {
        if (m_Animations[m_AnimationIndex].IsPlaying()) {
            m_Animations[m_AnimationIndex].Update();
        }
    }

    public void Draw(Canvas canvas, Rect rect) {
        if (m_Animations[m_AnimationIndex].IsPlaying()) {
            m_Animations[m_AnimationIndex].Draw(canvas, rect);
        }
    }

}
