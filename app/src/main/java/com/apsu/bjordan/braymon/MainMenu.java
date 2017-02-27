package com.apsu.bjordan.braymon;

import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;


public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView score = (TextView)findViewById(R.id.textView_CurrentScore);
        score.setVisibility(View.INVISIBLE);
        TextView highscore = (TextView)findViewById(R.id.textView_HighScore);
        highscore.setVisibility(View.INVISIBLE);

        findViewById(R.id.button_game_i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GameOne.class);
                startActivity(i);
            }
        });

        findViewById(R.id.button_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "<html>" +
                        "<h2>About Braymon</h2>" +
                        "<p>Interface, Graphics, &amp; Sounds</p>" +
                        "<p><b>Source:</b> All Original Content<br>" +
                        "<b>Creator:</b> Bradley Jordan<br>" +
                        "<p>Gameplay Logic</p>" +
                        "<b>Creator:</b> Todd Bray<br>" +
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
    }


}
