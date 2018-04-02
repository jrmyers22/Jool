package com.example.jool;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import pl.droidsonroids.gif.GifTextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Juul hand me the Joel");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mp = MediaPlayer.create(getApplicationContext(), R.raw.vape_trim);

        toast("Shake to toggle Party Mode!");

        final GifTextView colorGif = findViewById(R.id.colorGif);
        colorGif.setAlpha(0);

        // Activate party mode button. Used in development.
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (colorGif.getAlpha() == 1) {
//                    colorGif.setAlpha(0);
//                } else {
//                    colorGif.setAlpha(1);
//                }
//            }
//        });

//        Shake works correctly
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                if (colorGif.getAlpha() == 1) {
                    colorGif.setAlpha(0);
                } else {
                    colorGif.setAlpha(1);
                }
            }
        });

        final ImageView jool = findViewById(R.id.jool);
        jool.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //toast("Long click activated");
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                jool.startAnimation(shake);
                mp.start();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
