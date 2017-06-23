package com.example.tarun.test;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor mProximity;
    float distance;
    MediaPlayer mp;
    TextView textView;
    TextView textView2;
    public CountDownTimer aCounter;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.text);
        textView2=(TextView)findViewById(R.id.text2);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if(mProximity==null)
        {
            Toast.makeText(this,"Proximity sensor is not available",Toast.LENGTH_LONG).show();
            onStop();
        }

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mp = MediaPlayer.create(getApplicationContext(), alarmTone);

        aCounter = new CountDownTimer(10000 , 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText("" + millisUntilFinished / 1000 );
                textView2.setText(R.string.tes);
            }

            public void onFinish() {
                mp.start();
                mp.setLooping(true);
                textView.setText("");
                textView2.setText("");
            }
        };

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        distance = event.values[0];
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (distance==0){
                    aCounter.start();
                }
                else
                {
                    aCounter.cancel();
                    if(mp.isPlaying())
                    {
                        mp.pause();
                        mp.seekTo(0);
                    }
                }
            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mp.isPlaying())
        {
            mp.pause();
            mp.seekTo(0);
        }
        aCounter.cancel();
        mSensorManager.unregisterListener(this);

    }
}