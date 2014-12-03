package com.iic.tapemeasure;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.commons.logging.Log;

import java.util.Date;


public class MainActivity extends Activity {

    private KolGenerator kolGenerator;
    Handler handler = new Handler();
    Button playButton;
    Button listenButton;
    WaveForm waveForm;
    long soundPlayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kolGenerator = new KolGenerator();
        playButton = (Button)findViewById(R.id.button);
        listenButton = (Button)findViewById(R.id.button2);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSoundLikeABoss();
            }
        });
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenToSoundLikeABoss();
            }
        });
        waveForm = (WaveForm)findViewById(R.id.view);
    }

    void listenToSoundLikeABoss() {
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, audioFormat) * 10;
        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, audioFormat, bufferSize);
        short bytes[] = new short[bufferSize / 2];
        record.startRecording();
        record.read(bytes, 0, bufferSize / 2);

        for (short s : bytes) {
            waveForm.AddSample(s);
        }
    }

    void playSoundLikeABoss() {
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                kolGenerator.genTone();
                handler.post(new Runnable() {

                    public void run() {
                        kolGenerator.playSound();
                    }
                });
            }
        });
        thread.start();
        soundPlayDate = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
