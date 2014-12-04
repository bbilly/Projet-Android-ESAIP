package org.esaip.projetandroidbbvp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;

public class bubbleActivity extends Activity {
    InputStream in;
    BufferedReader reader;
    String line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        Bundle extras = getIntent().getExtras();

        Button buttonAscii = (Button) findViewById(R.id.AsciiButton);
        buttonAscii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    in = getAssets().open("test.txt");
//                    reader = new BufferedReader(new InputStreamReader(in));
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(getApplicationContext(), "ASCII ! ", Toast.LENGTH_SHORT).show();
//
//                activityTaskASCII connect = new activityTaskASCII();
//                connect.execute(reader);
                Circle c = (Circle) findViewById(R.id.circleLayout);
                c.getCircleStack().clear();
                c.invalidate();
            }
        });
    }
}
