package com.example.nigel.christiangame;

import android.graphics.Canvas;
import android.view.View;
import android.widget.Button;

/**
 * Created by Nigel on 10/22/2017.
 */

public class InputButton {

    private Button m_Button;
    private int m_DefaultColor;
    private boolean m_IsForward, m_IsRight;
    private boolean m_IsDown, m_IsUp;
    private EButtonType m_Type;

    public InputButton(Button button, int background, float xPosition, float yPosition, EButtonType buttonType) {
        m_Button = button;
        m_DefaultColor = background;
        m_Button.setBackgroundColor(background);
        m_Button.setX(xPosition);
        m_Button.setY(yPosition);
        m_Type = buttonType;
        m_Button.setOnClickListener(new InputButton.ButtonClickListener());
    }

    public Button GetButton() {
        return m_Button;
    }

    public boolean GetIsForward() { return m_IsForward; }
    public boolean GetIsBackward() { return !m_IsForward; }
    public boolean GetIsRight() { return m_IsRight; }
    public boolean GetIsLeft() { return !m_IsRight; }

    public boolean GetIsDown() { return m_IsDown; }

    public EButtonType Type() { return m_Type; }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (m_Button.isSelected()) {
                m_IsDown = true;
            }
            else {
                m_IsDown = false;
            }
        }
    }

}
