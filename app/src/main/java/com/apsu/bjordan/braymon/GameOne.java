package com.apsu.bjordan.braymon;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Random;



public class GameOne extends AppCompatActivity {

    ArrayList<Integer> cpu = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_i);

        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startTurn();
            }
        });
    }

    private UpdateTask sg;

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
                    final int num = cpu.get(i);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            selectButton(num);
                        }
                    });

                Thread.sleep(1000);
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

        td.startTransition(1000);
        td.reverseTransition(1000);
    }



}
