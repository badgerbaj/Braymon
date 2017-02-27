package com.apsu.bjordan.braymon;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class GameTwo extends AppCompatActivity {

    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;

    private static final String IT_KEY = "IT";
    private static final String CPU_KEY = "CPU";
    private static final String PLAYER_KEY = "PLAYER";
    private static final String SCORE_KEY = "0";

    int blue_sound, red_sound, green_sound, yellow_sound, end_sound, it;
    private static UpdateTask sg;

    ArrayList<Integer> cpu;
    ArrayList<Integer> player;
    Boolean turn = false;

    int score = 0;
    int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // opens game I high score file
        readHighScore();

        // displays game I high score file
        TextView highScoreText = (TextView)findViewById(R.id.textView_HighScore);
        highScoreText.setText(Integer.toString(highScore));

        // blue button on game screen
        ImageButton blue = (ImageButton) findViewById(R.id.imageButton_Blue);
        blue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // checks status of game so that user cannot press button until their turn
                cpuStatus();
                if (turn == true) {
                    selectButton(1); // lights and sound effects for button 1
                    player.add(1); // adds selected button to player array
                    checkStatus(); // checks if correct button was pressed
                }
            }
        });

        // red button on game screen
        ImageButton red = (ImageButton) findViewById(R.id.imageButton_Red);
        red.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // checks status of game so that user cannot press button until their turn
                cpuStatus();
                if (turn == true) {
                    selectButton(2); // lights and sound effects for button 2
                    player.add(2); // adds selected button to player array
                    checkStatus(); // checks if correct button was pressed
                }
            }
        });

        // green button on game screen
        ImageButton green = (ImageButton) findViewById(R.id.imageButton_Green);
        green.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // checks status of game so that user cannot press button until their turn
                cpuStatus();
                if (turn == true) {
                    selectButton(3); // lights and sound effects for button 3
                    player.add(3); // adds selected button to player array
                    checkStatus(); // checks if correct button was pressed
                }
            }
        });

        // yellow button on game screen
        ImageButton yellow = (ImageButton) findViewById(R.id.imageButton_Yellow);
        yellow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // checks status of game so that user cannot press button until their turn
                cpuStatus();
                if (turn == true) {
                    selectButton(4); // lights and sound effects for button 4
                    player.add(4); // adds selected button to player array
                    checkStatus(); // checks if correct button was pressed
                }
            }
        });

        // creates sound effects set
        soundsLoaded = new HashSet<Integer>();

        // no screen rotation - total new start
        if (savedInstanceState == null) {
            turn = false; // turn set to false to lock buttons
            it = 0; // iterator to go through arrays
            cpu = new ArrayList<Integer>(); // creates array list for cpu
            player = new ArrayList<Integer>(); // creates array list for player
            startTurn();
        }
        else { // possibly a rotation - may have data
            turn = false; // turn set to false to lock buttons

            // if the screen is rotated during the computer's turn, cancels the cpu's turn and restarts
            if (sg.getStatus() == AsyncTask.Status.RUNNING) {
                sg.cancel(true);
            }
            sg = new UpdateTask();
            sg.execute();

            // sets correct variables after screen rotation
            it = savedInstanceState.getInt(IT_KEY, 0);
            cpu = savedInstanceState.getIntegerArrayList(CPU_KEY);
            player = savedInstanceState.getIntegerArrayList(PLAYER_KEY);
            score = savedInstanceState.getInt(SCORE_KEY);
            TextView scoreText = (TextView)findViewById(R.id.textView_CurrentScore);
            scoreText.setText(Integer.toString(score));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // outputs values on screen rotation
        outState.putInt(IT_KEY, it);
        outState.putIntegerArrayList(CPU_KEY, cpu);
        outState.putIntegerArrayList(PLAYER_KEY, player);
        outState.putInt(SCORE_KEY, score);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // soundpool for SDK greater than or equal to 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        }
        // soundpool for SDK less than 21
        else {
            soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC, 1);
        }

        // loads sound files
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
        end_sound = soundPool.load(this, R.raw.fail_note, 1);
    }

    // checks if cpu has completed turn
    public void cpuStatus () {
        if (sg != null && sg.getStatus() == AsyncTask.Status.FINISHED) {
            sg = null;
            turn = true;
        }
    }

    // checks if button pressed is correct
    public void checkStatus () {
        if (player.get(it) == cpu.get(it)) {
            it++;
        }
        else {
            endGame();
        }

        // if the player is correct on full turn, increases score and starts a new turn
        if (it == cpu.size()) {
            it = 0;
            player.clear();
            score++;
            TextView scoreText = (TextView)findViewById(R.id.textView_CurrentScore);
            scoreText.setText(Integer.toString(score));
            if (score > highScore) {
                TextView highScoreText = (TextView)findViewById(R.id.textView_HighScore);
                highScoreText.setText(Integer.toString(score));
            }
            startTurn();
        }
    }

    // starts new cpu turn
    // assigns new color to cpu array and starts task
    private void startTurn() {
        turn = false;
        cpu.add(pickButton());
        sg = new UpdateTask();
        sg.execute();
    }

    // if incorrect button is pressed
    // plays end game sound and returns to main menu
    public void endGame () {
        if (soundsLoaded.contains(end_sound)) {
            soundPool.play(end_sound, 1.0f, 1.0f, 0, 0, 1.0f);
        }
        // if player has the high score, writes new high score to file
        if (score > highScore) {
            writeHighScore();
        }
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
    }

    // writes high score to file
    private void writeHighScore() {
        try {
            FileOutputStream fos = openFileOutput("gameI.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(score);
            pw.close();
        } catch (FileNotFoundException e) {
            // Logs an error message, prints the StackTrace and shows the user an error message if the data file cannot be found
            Log.e("WRITE_ERR", "Cannot dave data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // reads high score from file
    private void readHighScore() {
        try {
            FileInputStream fis = openFileInput("gameI.txt");
            Scanner scanner = new Scanner(fis);
            String scoreIn = scanner.nextLine();
            highScore = Integer.parseInt(scoreIn);
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // AsyncTask used for cpu turn
    class UpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // sets time between cpu buttons
            // as rounds increase, the speed increases
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
            // sleeps for one second then cycles through cpu array by calling each button
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

    // picks a random number between 1 and 4 to add to the cpu array
    public int pickButton () {
        Random random = new Random();

        int num = random.nextInt(4) + 1;

        return num;
    }

    // switch used to light button and play sound for each button
    // value passed from cpu array and player button press
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

    // lights blue button and plays sound
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

    // lights red button and plays sound
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

    // lights green button and plays sound
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

    // lights yellow button and plays sound
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

    // plays sound depending on button selected
    private void playSound(int soundId) {
        if (soundsLoaded.contains(soundId)) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 2.0f);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (sg.getStatus() == AsyncTask.Status.RUNNING) {
                sg.cancel(true);
            }
            Intent iActivity_Main = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(iActivity_Main);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
