package com.example.nigel.christiangame;

import android.graphics.Color;
import android.telecom.Connection;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Nigel on 10/14/2017.
 */

public class ActionButton {

    private Button m_Button;
    private int m_DefaultColor;
    private boolean m_IsBoostButton;
    private Player m_TargetPlayer;
    private EButtonType m_ButtonType;

    /**
     * Creates an ActionButton used for different events such as shooting, boosting, or teleporting
     * @param button The Button attribute
     * @param background The background color of the Button
     * @param textColor The text color of the Button
     * @param text The text of the button
     * @param xPosition The X-Position of the Button
     * @param yPosition The Y-Position of the Button
     */
    public ActionButton(EButtonType type, Button button, int background, int textColor, String text, float xPosition, float yPosition) {
        m_ButtonType = type;
        m_Button = button;
        m_DefaultColor = background;
        m_Button.setBackgroundColor(background);
        m_Button.setTextColor(textColor);
        m_Button.setText(text);
        m_Button.setX(xPosition);
        m_Button.setY(yPosition);
        m_Button.setOnClickListener(new ButtonClickListener());
    }

    /**
     * Returns the ActionButton's Button
     * @return Button
     */
    public Button GetButton() {
        return m_Button;
    }

    /**
     * Sets the target of this button
     * @param player The Spaceship
     */
    public void SetTarget(Player player) {
        m_TargetPlayer = player;
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
            if (m_ButtonType == EButtonType.BOOST) {
                // Deciding on a way to implement BOOST
            }
            else if (m_ButtonType == EButtonType.TELEPORT) {
                m_TargetPlayer.SetPosition((int)(Math.random() * Constants.ScreenWidth), (int)(Math.random() * Constants.ScreenHeight));
            }
            else if (m_ButtonType == EButtonType.FIRE) {
                // Deciding if we need a button to shoot lasers
            }
        }
    }
}
