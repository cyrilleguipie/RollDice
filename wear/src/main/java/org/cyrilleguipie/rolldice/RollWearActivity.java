package org.cyrilleguipie.rolldice;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RollWearActivity extends Activity implements SensorEventListener{
    private AnimationDrawable dice_anim = null;
    private Handler handler = new Handler();
    private ImageView die1 = null;
    private LinearLayout diceContainer;
    private SensorManager sensorMgr;

    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;

    private static final int UPDATE_DELAY = 75;
    private static final int SHAKE_THRESHOLD = 1250;



    class ArgRunnable implements Runnable
    {
        ImageView v = null;

        public ArgRunnable(ImageView v)
        {
            this.v = v;
        }

        public void run()
        {
            v.setImageResource(getResources().getIdentifier("d"+((int)(Math.random()*6) + 1), "drawable", "org.cyrilleguipie.rolldice"));
        }
    };

    ArgRunnable fixDice = null;

    private void setDice()
    {
        die1.setImageResource(R.drawable.dice_anim);
        dice_anim = (AnimationDrawable) die1.getDrawable();
        dice_anim.start();
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        die1.startAnimation(rotation);

        handler.removeCallbacks(fixDice);
        handler.postDelayed(fixDice, 650);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                diceContainer = (LinearLayout) findViewById(R.id.diceContainer);
                diceContainer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            setDice();
                        } catch (Exception e) {};
                    }
                });
                die1 = (ImageView) findViewById(R.id.die1);
                fixDice = new ArgRunnable(die1);

                sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
                boolean accelSupported = sensorMgr.registerListener(RollWearActivity.this,
                        sensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER),	SensorManager.SENSOR_DELAY_GAME);
                if (!accelSupported) sensorMgr.unregisterListener(RollWearActivity.this); //no accelerometer on the device
                setDice();
            }
        });
    }


    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();
        finish();
    }

    public void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > UPDATE_DELAY) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) { //the screen was shaked
                    setDice();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        return; //this method isn't used
    }

}
