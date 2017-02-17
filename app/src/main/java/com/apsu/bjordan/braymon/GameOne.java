package com.apsu.bjordan.braymon;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;


public class GameOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_i);

        }


    public void selectButton (int i) {
        switch (i) {
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
                getResources().getDrawable(R.drawable.button_blue_dark_sq),
                getResources().getDrawable(R.drawable.button_blue_bright_sq)
        });

        b.setImageDrawable(td);

        td.startTransition(1000);
        td.reverseTransition(1000);
    }

    public void lightRed () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Red);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_red_dark_sq),
                getResources().getDrawable(R.drawable.button_red_bright_sq)
        });

        b.setImageDrawable(td);

        td.startTransition(1000);
        td.reverseTransition(1000);
    }

    public void lightGreen () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Green);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_green_dark_sq),
                getResources().getDrawable(R.drawable.button_green_bright_sq)
        });

        b.setImageDrawable(td);

        td.startTransition(1000);
        td.reverseTransition(1000);
    }

    public void lightYellow () {
        ImageButton b = (ImageButton) findViewById(R.id.imageButton_Yellow);

        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                getResources().getDrawable(R.drawable.button_yellow_dark_sq),
                getResources().getDrawable(R.drawable.button_yellow_bright_sq)
        });

        b.setImageDrawable(td);

        td.startTransition(1000);
        td.reverseTransition(1000);
    }



}
