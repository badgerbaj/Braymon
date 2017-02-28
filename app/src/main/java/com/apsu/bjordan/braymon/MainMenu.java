package com.apsu.bjordan.braymon;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    int gameMode = 0;
    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;

    private static final String IT_KEY = "IT";
    private static final String CPU_KEY = "CPU";
    private static final String PLAYER_KEY = "PLAYER";
    private static final String SCORE_KEY = "SCORE";
    private static final String HIGH_KEY = "HIGH";
    private static final String GAME_KEY = "GAME";

    private Color blue, red, green, yellow;
    private HashMap<Integer, Color> colorMap;

    int blue_sound, red_sound, green_sound, yellow_sound, end_sound, it;
    private static UpdateTask sg;

    ArrayList<Integer> cpu;
    ArrayList<Integer> player;
    Boolean turn = false;

    int playerScore = 0;
    int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        blue = new Color();
        blue.setDark(R.drawable.button_blue_dark_rt);
        blue.setBright(R.drawable.button_blue_bright_rt);
        blue.setIb(R.id.imageButton_Blue);
        blue.setColorId(1);

        red = new Color();
        red.setDark(R.drawable.button_red_dark_rt);
        red.setBright(R.drawable.button_red_bright_rt);
        red.setIb(R.id.imageButton_Red);
        red.setColorId(2);

        green = new Color();
        green.setDark(R.drawable.button_green_dark_rt);
        green.setBright(R.drawable.button_green_bright_rt);
        green.setIb(R.id.imageButton_Green);
        green.setColorId(3);

        yellow = new Color();
        yellow.setDark(R.drawable.button_yellow_dark_rt);
        yellow.setBright(R.drawable.button_yellow_bright_rt);
        yellow.setIb(R.id.imageButton_Yellow);
        yellow.setColorId(4);

        colorMap = new HashMap<>();
        colorMap.put(R.id.imageButton_Blue, blue);
        colorMap.put(R.id.imageButton_Red, red);
        colorMap.put(R.id.imageButton_Green, green);
        colorMap.put(R.id.imageButton_Yellow, yellow);

        TextView scoreTV = (TextView) findViewById(R.id.textView_CurrentScore);
        scoreTV.setVisibility(View.INVISIBLE);
        TextView highScoreTV = (TextView) findViewById(R.id.textView_HighScore);
        highScoreTV.setVisibility(View.INVISIBLE);

        // no screen rotation - total new start
        if (savedInstanceState == null) {
            turn = false; // turn set to false to lock buttons
            it = 0; // iterator to go through arrays
            cpu = new ArrayList<Integer>(); // creates array list for cpu
            player = new ArrayList<Integer>(); // creates array list for player
         }
        else { // possibly a rotation - may have data
            turn = false; // turn set to false to lock buttons
            // if the screen is rotated during the computer's turn, cancels the cpu's turn and restarts
            // sets correct variables after screen rotation

            it = savedInstanceState.getInt(IT_KEY, 0);
            cpu = savedInstanceState.getIntegerArrayList(CPU_KEY);
            player = savedInstanceState.getIntegerArrayList(PLAYER_KEY);
            playerScore = savedInstanceState.getInt(SCORE_KEY);
            highScore = savedInstanceState.getInt(HIGH_KEY);
            gameMode = savedInstanceState.getInt(GAME_KEY);
            scoreTV.setText(Integer.toString(playerScore));
            highScoreTV.setText(Integer.toString(highScore));
            scoreTV.setVisibility(View.VISIBLE);
            highScoreTV.setVisibility(View.VISIBLE);

            if (sg.getStatus() == AsyncTask.Status.RUNNING) {
                sg.cancel(true);
            }
            sg = new MainMenu.UpdateTask();
            sg.execute();
        }

        // selects game mode 1
        findViewById(R.id.button_game_i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameMode == 0) {
                    gameMode = 1;
                    GameOne();
                }
            }
        });

        // selects game mode 2
        findViewById(R.id.button_game_ii).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameMode == 0) {
                    gameMode = 2;
                    GameTwo();
                }
            }
        });

        // selects game mode 3
        findViewById(R.id.button_game_iii).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameMode == 0) {
                    gameMode = 3;
                    GameThree();
                }
            }
        });

        // selects about button
        findViewById(R.id.button_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "<html>" +
                        "<h2>About Braymon</h2>" +
                        "<p>Interface, Graphics, &amp; Sounds</p>" +
                        "<p><b>Source:</b> All Original Content<br>" +
                        "<b>Creator:</b> Bradley Jordan<br>" +
                        "<p>Gameplay Logic</p>" +
                        "<b>Creator:</b> Todd Bray<br><br>" +
                        "<b>Game Mode I:</b> Braymon Says<br>" +
                        "Braymon will create a pattern that the player must match<br><br>" +
                        "<b>Game Mode II:</b> Player Says<br>" +
                        "Braymon will create the first button in the sequence. The player will repeat the button and then add the next button and repeat the sequence.<br><br>" +
                        "<b>Game Mode III:</b> Braymon and Player Says<br>" +
                        "Braymon will create the first button in the sequence. The player will repeat the button and then add the next button. Braymon will then add a button to the one the player made and the player must complete the entire sequence.<br><br>" +
                        "<b>Link: </b> <a href='https://github.com/badgerbaj/Braymon'>GitHub Source Code</a><br>" +
                        "</p></html>";

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(Html.fromHtml(message));
                builder.setPositiveButton("Ok", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                // must be done after the call to show();
                // allows anchor tags to work
                TextView tv = (TextView) dialog.findViewById(android.R.id.message);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });

        int[] buttonIds = {R.id.imageButton_Blue, R.id.imageButton_Red, R.id.imageButton_Green, R.id.imageButton_Yellow };
        for( int id : buttonIds)
        {
            ImageButton ib = (ImageButton) findViewById(id);
            ib.setOnClickListener(this);
        }

        // creates sound effects set
        soundsLoaded = new HashSet<Integer>();

    }

    @Override
    public void onClick(View v) {
        Color color = colorMap.get(v.getId());

        switch (gameMode) {
            case 0:
                // lights and sound effects
                lightItUp(color);
                break;
            case 1:
                cpuStatus();
                if (turn == true) {
                    // lights and sound effects
                    lightItUp(color); // lights and sound effects
                    player.add(color.getColorId()); // adds selected button to player array
                    checkStatus(); // checks if correct button was pressed
                }
                break;
            case 2:
                if (gameMode == 2 && player.size() != cpu.size()) {
                    cpuStatus();
                    // lights and sound effects
                    lightItUp(color);
                    player.add(color.getColorId()); // adds selected button to player array
                    checkStatus(); // checks if correct button was pressed
                }
                else {
                    player.clear(); // clears player array
                    cpu.add(color.getColorId()); // adds selected button to player array
                    // lights and sound effects
                    lightItUp(color);
                }
                break;
            case 3:
                cpuStatus();
                if (gameMode == 3 && player.size() != cpu.size()) {
                    // lights and sound effects
                    lightItUp(color);
                    player.add(color.getColorId()); // adds selected button to player array
                    checkStatus(); // checks if correct button was pressed
                }
                else {
                    player.clear(); // clears player array
                    cpu.add(color.getColorId()); // adds selected button to player array
                    // lights and sound effects
                    lightItUp(color);
                    startTurn(); // starts cpu turn
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // outputs values on screen rotation
        outState.putInt(IT_KEY, it);
        outState.putIntegerArrayList(CPU_KEY, cpu);
        outState.putIntegerArrayList(PLAYER_KEY, player);
        outState.putInt(SCORE_KEY, playerScore);
        outState.putInt(HIGH_KEY, highScore);
        outState.putInt(GAME_KEY, gameMode);

    }

    // lights blue button and plays sound
    public void lightItUp (Color color) {

        if (color.getColorId() > 0) {
            ImageButton ib = (ImageButton) findViewById(color.getIb());

            TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                    getResources().getDrawable(color.getDark()),
                    getResources().getDrawable(color.getBright())
            });

            ib.setImageDrawable(td);

            playSound(color.getSound());
            td.startTransition(500);
            td.reverseTransition(500);
        }
        else {
            // Fail silently
        }
    }

    // Game I function
    // gets game high score and starts cpu turn
    public void GameOne() {
        readHighScore(gameMode);
        TextView scoreTV = (TextView) findViewById(R.id.textView_CurrentScore);
        scoreTV.setText(Integer.toString(playerScore));
        scoreTV.setVisibility(View.VISIBLE);
        TextView highScoreTV = (TextView) findViewById(R.id.textView_HighScore);
        highScoreTV.setText(Integer.toString(highScore));
        highScoreTV.setVisibility(View.VISIBLE);
        startTurn();
    }

    // Game II function
    // gets game high score and starts cpu turn
    public void GameTwo() {
        readHighScore(gameMode);
        TextView scoreTV = (TextView) findViewById(R.id.textView_CurrentScore);
        scoreTV.setText(Integer.toString(playerScore));
        scoreTV.setVisibility(View.VISIBLE);
        TextView highScoreTV = (TextView) findViewById(R.id.textView_HighScore);
        highScoreTV.setText(Integer.toString(highScore));
        highScoreTV.setVisibility(View.VISIBLE);
        startTurn();
    }

    // Game III function
    // gets game high score and starts cpu turn
    public void GameThree() {
        readHighScore(gameMode);
        TextView scoreTV = (TextView) findViewById(R.id.textView_CurrentScore);
        scoreTV.setText(Integer.toString(playerScore));
        scoreTV.setVisibility(View.VISIBLE);
        TextView highScoreTV = (TextView) findViewById(R.id.textView_HighScore);
        highScoreTV.setText(Integer.toString(highScore));
        highScoreTV.setVisibility(View.VISIBLE);
        startTurn();
    }

    public void checkStatus() {
        // checks if button pressed is correct in game mode I
        if (gameMode == 1) {
            if (player.get(it) == cpu.get(it)) {
                it++;
            } else {
                endGame();
            }
            // if the player is correct on full turn, increases score and starts a new turn
            if (it == cpu.size() && cpu.size() != 0) {
                it = 0;
                player.clear();
                playerScore++;
                TextView scoreText = (TextView) findViewById(R.id.textView_CurrentScore);
                scoreText.setText(Integer.toString(playerScore));
                if (playerScore > highScore) {
                    TextView highScoreText = (TextView) findViewById(R.id.textView_HighScore);
                    highScoreText.setText(Integer.toString(playerScore));
                }
                startTurn();
            }
        }
        if (gameMode == 2) {
            // checks if button pressed is correct in game mode II
            if (player.get(it) == cpu.get(it)) {
                it++;
            } else {
                endGame();
            }
            // if the player is correct on full turn, increases score and starts a new turn
            if (it == cpu.size() && cpu.size() != 0) {
                it = 0;
                playerScore++;
                TextView scoreText = (TextView) findViewById(R.id.textView_CurrentScore);
                scoreText.setText(Integer.toString(playerScore));
                if (playerScore > highScore) {
                    TextView highScoreText = (TextView) findViewById(R.id.textView_HighScore);
                    highScoreText.setText(Integer.toString(playerScore));
                }

            }
        }
        if (gameMode == 3) {
            // checks if button pressed is correct in game mode III
            if (player.get(it) == cpu.get(it)) {
                it++;
            } else {
                endGame();
            }
            // if the player is correct on full turn, increases score and starts a new turn
            if (it == cpu.size() && cpu.size() != 0) {
                it = 0;
                playerScore++;
                TextView scoreText = (TextView) findViewById(R.id.textView_CurrentScore);
                scoreText.setText(Integer.toString(playerScore));
                if (playerScore > highScore) {
                    TextView highScoreText = (TextView) findViewById(R.id.textView_HighScore);
                    highScoreText.setText(Integer.toString(playerScore));
                }

            }
        }
    }

    // starts new cpu turn
    // assigns new color to cpu array and starts task
    public void startTurn() {
            turn = false;
            cpu.add(pickButton());
            sg = new MainMenu.UpdateTask();
            sg.execute();
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
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
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
        blue.setSound(soundPool.load(this, R.raw.blue_note, 1));
        red.setSound(soundPool.load(this, R.raw.red_note, 1));
        green.setSound(soundPool.load(this, R.raw.green_note, 1));
        yellow.setSound(soundPool.load(this, R.raw.yellow_note, 1));
        end_sound = soundPool.load(this, R.raw.fail_note, 1);
    }

    // checks if cpu has completed turn
    public void cpuStatus () {
        if (sg != null && sg.getStatus() == AsyncTask.Status.FINISHED) {
            sg = null;
            turn = true;
        }
    }

    // if incorrect button is pressed
    // plays end game sound and returns to main menu
    public void endGame () {
        // if player has the high score, writes new high score to file, and resets all arrays, textviews and scores
        if (playerScore > highScore) {
            writeHighScore(gameMode);
        }
        if (soundsLoaded.contains(end_sound)) {
            soundPool.play(end_sound, 1.0f, 1.0f, 0, 0, 1.0f);
            it = 0;
            player.clear();
            cpu.clear();
            TextView scoreTV = (TextView)findViewById(R.id.textView_CurrentScore);
            scoreTV.setVisibility(View.INVISIBLE);
            TextView highScoreTV = (TextView)findViewById(R.id.textView_HighScore);
            highScoreTV.setVisibility(View.INVISIBLE);
            playerScore = 0;
            highScore = 0;
            gameMode = 0;

        }
    }

    // writes high score to file
    private void writeHighScore(int gm) {
        if (gm == 1) {
            try {
                FileOutputStream fos = openFileOutput("gameI.txt", Context.MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(playerScore);
                pw.close();
            } catch (FileNotFoundException e) {
                // Logs an error message, prints the StackTrace and shows the user an error message if the data file cannot be found
                Log.e("WRITE_ERR", "Cannot dave data: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (gm == 2) {
            try {
                FileOutputStream fos = openFileOutput("gameII.txt", Context.MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(playerScore);
                pw.close();
            } catch (FileNotFoundException e) {
                // Logs an error message, prints the StackTrace and shows the user an error message if the data file cannot be found
                Log.e("WRITE_ERR", "Cannot dave data: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (gm == 3) {
            try {
                FileOutputStream fos = openFileOutput("gameIII.txt", Context.MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(playerScore);
                pw.close();
            } catch (FileNotFoundException e) {
                // Logs an error message, prints the StackTrace and shows the user an error message if the data file cannot be found
                Log.e("WRITE_ERR", "Cannot dave data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // reads high score from file
    private void readHighScore(int gm) {
        if (gm == 1) {
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
        if (gm == 2) {
            try {
                FileInputStream fis = openFileInput("gameII.txt");
                Scanner scanner = new Scanner(fis);
                String scoreIn = scanner.nextLine();
                highScore = Integer.parseInt(scoreIn);
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (gm == 3) {
            try {
                FileInputStream fis = openFileInput("gameIII.txt");
                Scanner scanner = new Scanner(fis);
                String scoreIn = scanner.nextLine();
                highScore = Integer.parseInt(scoreIn);
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // AsyncTask used for cpu turn
    class UpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // sets time between cpu buttons
            // as rounds increase, the speed increases
            int timer = 1000;
            int div = 4;
            if (gameMode == 3){
                div = 6;
            }
            int round = cpu.size()/div;
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
                            selectColor(num);
                        }
                    });

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void selectColor(int color){

        Color aColor;
        switch (color) {
            case 1: aColor = blue;
                break;
            case 2: aColor = red;
                break;
            case 3: aColor = green;
                break;
            case 4: aColor = yellow;
                break;
            default: aColor = new Color();
                break;
        }

        lightItUp(aColor);
    }

    // picks a random number between 1 and 4 to add to the cpu array
    public int pickButton () {
        Random random = new Random();

        int num = random.nextInt(4) + 1;

        return num;
    }

    // plays sound depending on button selected
    private void playSound(int soundId) {
        if (soundsLoaded.contains(soundId)) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 2.0f);
        }
    }
}
