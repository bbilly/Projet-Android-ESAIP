package org.esaip.projetandroidbbvp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class EnvoyerMessageActivity extends Activity implements EnvoyerMessageTask.OnTaskEvent {
    private Button envoie;
    private EditText message;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envoyer_message);
        message = (EditText) findViewById(R.id.texte_message);
        envoie = (Button) findViewById(R.id.btn_envoyer_msg);
        envoie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EnvoyerMessageTask(EnvoyerMessageActivity.this).execute(message.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_envoyer_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreExecute() {
        progress = new ProgressDialog(EnvoyerMessageActivity.this);
        progress.setMessage("Envoie du message...");
        progress.show();
    }

    @Override
    public boolean onDoIn(String... strings) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet("http://formation-android-esaip.herokuapp.com/message/baptiste/test/"+strings[0]);
            Log.i("", strings[0]);
            HttpResponse response = client.execute(request);
            String res  = InputStreamToString.convert(response.getEntity().getContent());
            if(res.equals(""))
                return true;
            else
                return false;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onFinish(Boolean result) {
        if(result == true) {
            Intent intent = new Intent(getApplicationContext(), ListerMessagesActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(),"Erreur ! Réessayer plus tard",Toast.LENGTH_SHORT);

        if (progress.isShowing()) {
            progress.dismiss();
        }
    }
}
