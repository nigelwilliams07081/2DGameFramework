package com.example.nigel.christiangame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Nigel on 10/7/2017.
 */

public class OrientationData implements SensorEventListener {

    private SensorManager m_SensorManager;
    private Sensor m_Accelerometer;
    private Sensor m_Magnometer;

    private float[] m_AccelerometerOutput;
    private float[] m_MagnometerOutput;

    private float[] m_Orientation = new float[3];
    private float[] m_StartOrientation;

    public OrientationData() {
        m_SensorManager = (SensorManager)Constants.CurrentContext.getSystemService(Context.SENSOR_SERVICE);
        m_Accelerometer = m_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m_Magnometer = m_SensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    public float[] GetOrientation() {
        return m_Orientation;
    }

    public float[] GetStartOrientation() {
        return m_StartOrientation;
    }

    public void NewGame() {
        m_StartOrientation = null;
    }

    public void Register() {
        m_SensorManager.registerListener(this, m_Accelerometer, SensorManager.SENSOR_DELAY_GAME);
        m_SensorManager.registerListener(this, m_Magnometer, SensorManager.SENSOR_DELAY_GAME);

    }

    public void Pause() {
        m_SensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            m_AccelerometerOutput = sensorEvent.values;
        }
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            m_MagnometerOutput = sensorEvent.values;
        }

        if (m_AccelerometerOutput != null && m_MagnometerOutput != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, m_AccelerometerOutput, m_MagnometerOutput);
            if (success) {
                SensorManager.getOrientation(R, m_Orientation);
                if (m_StartOrientation == null) {
                    m_StartOrientation = new float[m_Orientation.length];
                    System.arraycopy(m_Orientation, 0, m_StartOrientation, 0, m_Orientation.length);
                }
            }
        }
    }
}
