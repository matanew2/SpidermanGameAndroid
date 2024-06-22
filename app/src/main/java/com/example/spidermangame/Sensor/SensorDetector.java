package com.example.spidermangame.Sensor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorDetector {
    private final SensorManager sensorManager;
    private final Sensor sensor;
    long timeStamp = 0;
    private final CallBackSensorView callBackSensorView;
    @SuppressLint("ServiceCast")
    public SensorDetector(Context context, CallBackSensorView callBackSensorView) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.callBackSensorView = callBackSensorView;
    }

    private final SensorEventListener sensorEventListenerX = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            calculateStepX(x);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void startX() {
        sensorManager.registerListener(sensorEventListenerX, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopX() {
        sensorManager.unregisterListener(sensorEventListenerX);
    }

    private void calculateStepX(float x) {

        int responseTime = 300;
        if (x > 3.0) {//left
            if (System.currentTimeMillis() - timeStamp > responseTime) {
                timeStamp = System.currentTimeMillis();
                callBackSensorView.moveHeroBySensor(-1);
            }
        }
        if (x < -3.0) {//right
            if (System.currentTimeMillis() - timeStamp > responseTime) {
                timeStamp = System.currentTimeMillis();
                callBackSensorView.moveHeroBySensor(1);
            }
        }
    }
}
