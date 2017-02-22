package com.apsu.bjordan.braymon;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Random;



public class GameOne extends AppCompatActivity {

    ArrayList<Integer> cpu = new ArrayList<Integer>();
    ArrayList<Integer> player = new ArrayList<Integer>();

    MediaPlayer mp_blue;
    MediaPlayer mp_green;
    MediaPlayer mp_red;
    MediaPlayer mp_yellow;
    MediaPlayer mp_fail;

    private UpdateTask sg;
    int it = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_i);

        mp_blue = MediaPlayer.create(getApplicationContext(), R.raw.blue_note);
        mp_green = MediaPlayer.create(getApplicationContext(), R.raw.green_note);
        mp_red = MediaPlayer.create(getApplicationContext(), R.raw.red_note);
        mp_yellow = MediaPlayer.create(getApplicationContext(), R.raw.yellow_note);
        mp_fail = MediaPlayer.create(getApplicationContext(), R.raw.fail_note);

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


            try {
                for (int i = 0; i < cpu.size(); i++) {
                    Thread.sleep(1000);
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

        mp_blue.start();
        td.startTransition(1000);
        td.reverseTransition(1000);
    }

    public void lightRed () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Red);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_red_dark_rt),
                getResources().getDrawable(R.drawable.button_red_bright_rt)
        });

        b.setImageDrawable(td);

        mp_red.start();
        td.startTransition(1000);
        td.reverseTransition(1000);
    }

    public void lightGreen () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Green);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_green_dark_rt),
                getResources().getDrawable(R.drawable.button_green_bright_rt)
        });

        b.setImageDrawable(td);

        mp_green.start();
        td.startTransition(1000);
        td.reverseTransition(1000);
    }

    public void lightYellow () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Yellow);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_yellow_dark_rt),
                getResources().getDrawable(R.drawable.button_yellow_bright_rt)
        });

        b.setImageDrawable(td);

        mp_yellow.start();
        td.startTransition(1000);
        td.reverseTransition(1000);
    }



}
