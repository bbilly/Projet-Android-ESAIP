package com.example.kevin.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class MainActivity extends Activity {

    EditText editTextUsername;
    EditText editTextPassword;
    TextView errorTextView;
    Button buttonClear;
    Button buttonValidate;
    boolean connectValue;
    ProgressBar progressBar;
    String connectValide="false";


    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, "onSaveInstanceState!");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        errorTextView = (TextView)findViewById(R.id.errorTextView);
        if (savedInstanceState.getInt("Error") == View.VISIBLE){
            errorTextView.setVisibility(View.VISIBLE);
        }
        Log.i(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences mPrefs = this.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);

        if(mPrefs.getBoolean("connected",false)){
            activityTask connection = new activityTask();
            editTextUsername = (EditText)findViewById(R.id.userNameEditText);
            editTextPassword = (EditText)findViewById(R.id.passwordEditText);

            editTextUsername.setText(mPrefs.getString("user","none").toString());
            editTextPassword.setText(mPrefs.getString("password","none").toString());
            connection.execute(editTextUsername.getText().toString(),editTextPassword.getText().toString());
        }else{
            buttonClear = (Button)findViewById(R.id.clearButton);
            buttonValidate = (Button)findViewById(R.id.validateButton);

            buttonClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTextUsername = (EditText)findViewById(R.id.userNameEditText);
                    editTextPassword = (EditText)findViewById(R.id.passwordEditText);

                    editTextUsername.setText("");
                    editTextPassword.setText("");
                }
            });

            buttonValidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTextUsername = (EditText)findViewById(R.id.userNameEditText);
                    editTextPassword = (EditText)findViewById(R.id.passwordEditText);
                    errorTextView = (TextView)findViewById(R.id.errorTextView);

                    if (editTextPassword.length() ==0 || editTextUsername.length() == 0){
                        errorTextView.setVisibility(View.VISIBLE);
                    }else{
                        CheckBox checkBoxConnection = (CheckBox)findViewById(R.id.checkBoxConnected);
                        if (checkBoxConnection.isChecked()){
                            SharedPreferences.Editor editor = getSharedPreferences("myAppPrefs", MODE_PRIVATE).edit();
                            editor.putBoolean("connected",true);
                            editor.putString("user",editTextUsername.getText().toString());
                            editor.putString("password", editTextPassword.getText().toString());
                            editor.commit();
                        }

                        activityTask connection = new activityTask();
                        connection.execute(editTextUsername.getText().toString(),editTextPassword.getText().toString());
                        errorTextView.setVisibility(View.INVISIBLE);

                    }
                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
    class activityTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar)findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean retour = false;
            try {

                String param = "/connect/"+params[0]+"/"+params[1];

                URI uri = new URI("http","formation-android-esaip.herokuapp.com",param,"");
                Log.i(TAG,uri.toString());
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(uri.toASCIIString());
                HttpResponse response = client.execute(request);

                InputStream content = response.getEntity().getContent();
                connectValide = InputStreamToString.convert(content);
                if (connectValide.contains("true")){
                    retour = true;
                }else{
                    retour = false;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return retour;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressBar.setVisibility(View.INVISIBLE);
            if(result){
                Intent intent = new Intent(MainActivity.this,ConnectedActivity.class);
                intent.putExtra("user",editTextUsername.getText().toString());
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Loose ! ", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
