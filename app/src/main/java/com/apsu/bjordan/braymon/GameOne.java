package com.apsu.bjordan.braymon;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class GameOne extends AppCompatActivity {

    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;

    int blue_sound;
    int red_sound;
    int green_sound;
    int yellow_sound;

    ArrayList<Integer> cpu = new ArrayList<Integer>();
    ArrayList<Integer> player = new ArrayList<Integer>();

    private UpdateTask sg;
    int it = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_i);

        Button start = (Button) findViewById(R.id.start_button);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startTurn();
            }
        });

        ImageButton blue = (ImageButton) findViewById(R.id.imageButton_Blue);
        blue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectButton(1);
                player.add(1);
                checkStatus();
            }
        });

        ImageButton red = (ImageButton) findViewById(R.id.imageButton_Red);
        red.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectButton(2);
                player.add(2);
                checkStatus();
            }
        });

        ImageButton green = (ImageButton) findViewById(R.id.imageButton_Green);
        green.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectButton(3);
                player.add(3);
                checkStatus();
            }
        });

        ImageButton yellow = (ImageButton) findViewById(R.id.imageButton_Yellow);
        yellow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectButton(4);
                player.add(4);
                checkStatus();
            }
        });

        soundsLoaded = new HashSet<Integer>();

    }

        @Override
        protected void onResume() {
            super.onResume();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                soundPool = new SoundPool.Builder().setMaxStreams(10).build();
            }
            else {
                soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC, 1);
            }

            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (status == 0 ) {
                        soundsLoaded.add(sampleId);
                    } else {
                        Log.i("SOUND", "Sound Error");
                    }
                }
            });

            blue_sound = soundPool.load(this, R.raw.blue_note, 1);
            red_sound = soundPool.load(this, R.raw.red_note, 1);
            green_sound = soundPool.load(this, R.raw.green_note, 1);
            yellow_sound = soundPool.load(this, R.raw.yellow_note, 1);
        }






    public void checkStatus () {
        if (player.get(it) == cpu.get(it)) {
            it++;
        }
        else {
            Intent i = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(i);
        }
        if (it == cpu.size()) {
            it = 0;
            player.clear();
            startTurn();
        }
    }

    private void startTurn() {
        cpu.add(pickButton());
        sg = new UpdateTask();
        sg.execute();
    }

    class UpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            int timer = 1000;
            int round = cpu.size()/4;
            switch (round) {
                case 0: timer = 1000;
                    break;
                case 1: timer = 500;
                    break;
                case 2: timer = 400;
                    break;
                case 3: timer = 300;
                    break;
                case 4: timer = 200;
                    break;
                default: timer = 150;
                    break;
            }
            try {
                Thread.sleep(1000);
                for (int i = 0; i < cpu.size(); i++) {
                    Thread.sleep(timer);
                    final int num = cpu.get(i);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            selectButton(num);
                        }
                    });

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
              return null;
        }
    }

    public int pickButton () {
        Random random = new Random();

        int num = random.nextInt(4) + 1;

        return num;
    }

    public void selectButton (int i) {
        switch (i) {
            case 0:
                break;
            case 1: lightBlue();
                break;
            case 2: lightRed();
                break;
            case 3: lightGreen();
                break;
            case 4: lightYellow();
                break;
            default:
                break;
        }
    }

    public void lightBlue () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Blue);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_blue_dark_rt),
                getResources().getDrawable(R.drawable.button_blue_bright_rt)
        });

        b.setImageDrawable(td);

        playSound(blue_sound);
        td.startTransition(500);
        td.reverseTransition(500);

    }

    public void lightRed () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Red);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_red_dark_rt),
                getResources().getDrawable(R.drawable.button_red_bright_rt)
        });

        b.setImageDrawable(td);


        playSound(red_sound);
        td.startTransition(500);
        td.reverseTransition(500);
     }

    public void lightGreen () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Green);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_green_dark_rt),
                getResources().getDrawable(R.drawable.button_green_bright_rt)
        });

        b.setImageDrawable(td);

        playSound(green_sound);
        td.startTransition(500);
        td.reverseTransition(500);
    }

    public void lightYellow () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Yellow);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_yellow_dark_rt),
                getResources().getDrawable(R.drawable.button_yellow_bright_rt)
        });

        b.setImageDrawable(td);


        playSound(yellow_sound);
        td.startTransition(500);
        td.reverseTransition(500);
    }

    private void playSound(int soundId) {
        if (soundsLoaded.contains(soundId)) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 2.0f);
        }
    }



}
