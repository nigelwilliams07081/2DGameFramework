package com.example.nigel.christiangame;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Nigel on 10/14/2017.
 */

public class DefaultButton {

    private Button m_Button;
    private int m_DefaultColor;
    private boolean m_IsBoostButton;
    private Player m_TargetPlayer;

    public DefaultButton(Button button, int background, int textColor, String text, float xPosition, float yPosition) {
        m_Button = button;
        m_DefaultColor = background;
        m_Button.setBackgroundColor(background);
        m_Button.setTextColor(textColor);
        m_Button.setText(text);
        m_Button.setX(xPosition);
        m_Button.setY(yPosition);
        m_Button.setOnClickListener(new ButtonClickListener());
    }

    public Button GetButton() {
        return m_Button;
    }

    /**
     * If m_IsBoostButton is false, then it is a teleport button
     * */
    public boolean GetIsBoost() { return m_IsBoostButton; }

    public void SetTarget(Player player) {
        m_TargetPlayer = player;
    }

    public void SetIsBoost(boolean isBoost) {
        m_IsBoostButton = isBoost;
    }

    public void Enable() {
        m_Button.setEnabled(true);
//        m_Button.setBackgroundColor(m_DefaultColor);
    }

    public void Disable() {
        m_Button.setEnabled(false);
//        m_Button.setBackgroundColor(Color.GRAY);
    }

    private class ButtonClickListener implements View.OnClickListener {

        public ButtonClickListener() {
        }

        @Override
        public void onClick(View v) {
            // Will be changed to have a cool-down
            if (m_Button.isEnabled()) {
                m_TargetPlayer.SetIsBoosting(true);
                Disable();
            }
            else {
                Toast.makeText(Constants.CurrentContext, "Cooling down", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
