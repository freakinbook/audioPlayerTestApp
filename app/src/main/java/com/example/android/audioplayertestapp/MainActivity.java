package com.example.android.audioplayertestapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;
    int positionA = 0;
    int positionB;
    boolean loopAToggledOn = false;
    boolean loopBToggledOn = false;

    @Override
    protected void onStop() {
        super.onStop();
        mp.release();
        mp = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this, R.raw.me_and_michael);
        positionB = mp.getDuration();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(MainActivity.this,"Michael belongs to everyone now",Toast.LENGTH_SHORT).show();
            }
        });
        Button playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
            }
        });
        Button pauseButton = (Button) findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.pause();
            }
        });
        final Button loopAButton = (Button) findViewById(R.id.loop_a_button);
        loopAButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (!loopAToggledOn){
                    positionA = mp.getCurrentPosition();
                    loopAToggledOn = true;
                    loopAButton.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    return;
                } else{
                    positionA = 0;
                    loopAToggledOn = false;
                    loopAButton.setBackground(getResources().getDrawable(android.R.drawable.btn_default));
                    return;
                }
            }
        });
        final Button loopBButton = (Button) findViewById(R.id.loop_b_button);
        loopBButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if(!loopBToggledOn){
                    positionB = mp.getCurrentPosition();
                    mp.seekTo(positionA);
                    loopBToggledOn = true;
                    loopBButton.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    return;
                } else {
                    positionB = mp.getDuration();
                    loopBToggledOn = false;
                    loopBButton.setBackground(getResources().getDrawable(android.R.drawable.btn_default));
                    return;
                }
            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mp != null){
                    while (true){
                        int mCurrentPosition = mp.getCurrentPosition();
                        if (mCurrentPosition > positionB){
                            mp.seekTo(positionA);
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t.start();
    }
}