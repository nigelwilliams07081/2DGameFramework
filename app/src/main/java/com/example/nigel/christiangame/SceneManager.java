package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by Nigel on 9/28/2017.
 */

public class SceneManager {

    private ArrayList<Scene> m_Scenes;
    public static int ACTIVE_SCENE;

    public SceneManager() {
        ACTIVE_SCENE = 0;
        m_Scenes = new ArrayList<>();
        m_Scenes.add(new GameplayScene());
    }

    public void ReceiveTouch(MotionEvent motionEvent) {
        m_Scenes.get(ACTIVE_SCENE).ReceiveTouch(motionEvent);
    }

    public void Update() {
        m_Scenes.get(ACTIVE_SCENE).Update();
    }

    public void Draw(Canvas canvas) {
        m_Scenes.get(ACTIVE_SCENE).Draw(canvas);
    }
}
