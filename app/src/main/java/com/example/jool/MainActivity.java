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
import android.widget.ImageView;
import android.widget.Toast;

import pl.droidsonroids.gif.GifTextView;

/**
 * Populates the sole view with the Jool image,
 * multicolored gif (transparent), and title text.
 * On a shake, the gif becomes visible.
 * When the Jool is long-pressed, it "wiggles" and
 * plays the inhale sound effect.
 */
public class MainActivity extends AppCompatActivity {

    /** Sensor manager used for the shake event. */
    private SensorManager mSensorManager;

    /** Listens for the shake event. */
    private ShakeEventListener mSensorListener;

    /** Media player for the inhale/exhale sound effect. */
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Juul hand me the Joel");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mp = MediaPlayer.create(getApplicationContext(), R.raw.vape_trim_short);

        toast("Shake to toggle Party Mode!", true);

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

        // Toggle the "Party Mode" visibility on shake
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                if (colorGif.getAlpha() == 1) {
                    colorGif.setAlpha(0);
                } else {
                    colorGif.setAlpha(1);
                }
            }
        });

        // Activate the shake and sound when image is long pressed
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

    /**
     * Registers the shake event listener
     * when shake is initially toggled.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Unregisters the shake event listener
     * when shake is toggled.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    /**
     * Wrapper to make toast message.
     * @param msg Message to display
     * @param isShort To display a short vs long message
     */
    public void toast(String msg, boolean isShort) {
        int length = 0;
        if (isShort) {
            length = Toast.LENGTH_SHORT;
        } else {
            length = Toast.LENGTH_LONG;
        }
        Toast.makeText(this, msg, length).show();
    }

}
