package org.esaip.projetandroidbbvp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;


public class bubbleActivity extends Activity {
    InputStream in;
    BufferedReader reader;
    String line;

    private TextView timerValue, header;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Handler customHandler = new Handler();
    int numberOfCircle = 0;
    Button buttonAscii;
    String user;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        //parametres
        this.user = getIntent().getExtras().getString("user");
        this.password = getIntent().getExtras().getString("password");

        Bundle extras = getIntent().getExtras();

        Button buttonAscii = (Button) findViewById(R.id.AsciiButton);

        header = (TextView)findViewById(R.id.textViewBubble);

        timerValue = (TextView) findViewById(R.id.chronometerBubble);
        buttonAscii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Circle c = (Circle) findViewById(R.id.circleLayout);
                c.getCircleStack().clear();
                c.invalidate();

                //on cache le texte du haut

                header.setVisibility(View.GONE);

                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);


            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    //Chrono
    private Runnable updateTimerThread = new Runnable() {
        public void run() {

                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMilliseconds;

                int secs = (int) (updatedTime / 1000);
                int milliseconds = (int) (updatedTime % 1000);

                timerValue.setText(""+String.format("%02d", secs) + ":"
                        + String.format("%02d", milliseconds));
                customHandler.postDelayed(this, 0);
            if(timeInMilliseconds>10000){
                timerValue.setText("10:00");
                Circle count = (Circle) findViewById(R.id.circleLayout);
                numberOfCircle= count.getCircleStack().size();

                AlertDialog.Builder builder = new AlertDialog.Builder(bubbleActivity.this);
                builder.setMessage("Bravo vous avez fait "+numberOfCircle+" cercles! ")
                        .setCancelable(false)
                        .setPositiveButton("Partager", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(getApplication(), EnvoyerMessageActivity.class);
                                intent.putExtra("user", user);
                                intent.putExtra("password",password);
                                intent.putExtra("preText","J'ai fait "+numberOfCircle+" cercles sur Bubble! Viens on est bien, bien, bieeeeeeeennnnnnnnnnnnn!");
                                //Permet de fermer les fenetres precedentes
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                bubbleActivity.this.finish();
                            }
                        })
                        .setNeutralButton("Recommencer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                Circle c = (Circle) findViewById(R.id.circleLayout);
                                c.getCircleStack().clear();
                                c.invalidate();

                                //on cache le texte du haut
                                TextView header = (TextView)findViewById(R.id.textViewBubble);
                                header.setVisibility(View.VISIBLE);


                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                customHandler.removeCallbacks(updateTimerThread);

            }
        }
    };

}
