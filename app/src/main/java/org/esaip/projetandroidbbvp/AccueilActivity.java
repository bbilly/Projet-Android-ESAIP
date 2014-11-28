package org.esaip.projetandroidbbvp;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class AccueilActivity extends Activity implements ListerMessagesTask.OnTaskEvent {
    Button btn_lister = null;
    Button btn_envoyer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
//on recupere les boutons dans la vue grace à leurs ids
        btn_lister = (Button) findViewById(R.id.btn_lister);
        btn_envoyer = (Button) findViewById(R.id.btn_envoyer);
//evenement lors du clic sur le bouton lister messages
        btn_lister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//on appelle la tache pour lister les messages
                new ListerMessagesTask(new ListerMessagesTask.OnTaskEvent() {
                    @Override
                    public void onPreExecute() {
//afficher la roue
                    }
                    @Override
                    public void onFinish(Boolean result) {
//lancer ou pas l'activité
                        Intent intent = new Intent(getApplicationContext(), ListerMessagesActivity.class);
                        startActivity(intent);
                    }
                }).execute();
            }
        });
//evenemet lors du clic sur le bouton envoyer message
        btn_envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EnvoyerMessageTask(new EnvoyerMessageTask.OnTaskEvent() {
                    @Override
                    public void onPreExecute() {
                    }
                    @Override
                    public void onFinish(Boolean result) {
                        Intent intent = new Intent(getApplicationContext(), EnvoyerMessageActivity.class);
                        startActivity(intent);
                    }
                }).execute();
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
            builder.setMessage(R.string.deconnexion_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            AccueilActivity.this.finish();
                            //on vide les shared preferences
                            SharedPreferences.Editor editor = getSharedPreferences("myAppPrefs", MODE_PRIVATE).edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(AccueilActivity.this, ConnexionActivity.class);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
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
            builder.setMessage(R.string.quitter_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AccueilActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
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

    /*
    Surcharge de la méthode onbackpressed pour permettre de quitter l'application en
    cliquant sur le bouton retour
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quitter_message)
                .setCancelable(false)
                .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AccueilActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    public void onPreExecute() {
    }
    @Override
    public void onFinish(Boolean result) {
    }
}