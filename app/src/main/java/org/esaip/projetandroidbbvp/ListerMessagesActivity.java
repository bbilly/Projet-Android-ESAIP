package org.esaip.projetandroidbbvp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;


public class ListerMessagesActivity extends Activity implements ListerMessagesTask.OnTaskEvent {
    ProgressDialog progress;
    ListView maliste;
    //parametres
    String user;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister_messages);

        //on recupere les parametres
        user = getIntent().getExtras().getString("user");
        password = getIntent().getExtras().getString("password");

        // Get ListView object from xml
        maliste = (ListView) findViewById(R.id.list);
        //on appelle la tache pour lister les messages
        new ListerMessagesTask(this).execute();

        //evenement lors du clic sur le bouton, rafraichissement
        Button rafraichir= (Button) findViewById(R.id.btn_rafraichir);

        rafraichir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new ListerMessagesTask(ListerMessagesActivity.this).execute();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accueil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.deconnexion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Etes vous sûr de vouloir vous deconnecter?")
                    .setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            ListerMessagesActivity.this.finish();
                            //on vide les shared preferences
                            SharedPreferences.Editor editor = getSharedPreferences("myAppPrefs", MODE_PRIVATE).edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(ListerMessagesActivity.this, ConnexionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        if (id == R.id.quitter) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Etes vous sûr de vouloir quitter?")
                    .setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ListerMessagesActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPreExecute() {
        progress = new ProgressDialog(ListerMessagesActivity.this);
        progress.setMessage("Chargement en cours");
        progress.show();
    }

    @Override
    public ArrayList<String> onDoIn(String... strings) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet("http://formation-android-esaip.herokuapp.com/messages/"+user+"/"+password);
            HttpResponse response = client.execute(request);
            String res  = InputStreamToString.convert(response.getEntity().getContent());
            ArrayList<String> les_messages = new ArrayList<String>();
            for(String m : res.split(";") ) {
                String auteur = m.substring(0, m.indexOf(":"));
                String message = m.substring(m.indexOf(":")+1);
                les_messages.add(message);
            }
            return les_messages;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }

    @Override
    public void onFinish(ArrayList<String> result) {
        if(result.size() !=0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,result);
            maliste = (ListView) findViewById(R.id.list);
            // Assign adapter to ListView
            maliste.setAdapter(adapter);
        }
        else
            Toast.makeText(getApplicationContext(),"Aucun message !",Toast.LENGTH_LONG);
        //on supprime le loader de l'écran
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }
}
