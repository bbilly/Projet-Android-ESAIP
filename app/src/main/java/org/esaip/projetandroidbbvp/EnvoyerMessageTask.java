package org.esaip.projetandroidbbvp;

import android.os.AsyncTask;

/**
 * Created by Baptiste on 28/11/2014.
 */
public class EnvoyerMessageTask extends AsyncTask<String, Void, Boolean> {

    interface OnTaskEvent {
        public void onPreExecute();
        public boolean onDoIn(String...strings);
        public void onFinish(Boolean result);
    }

    OnTaskEvent onTaskEvent;

    EnvoyerMessageTask(OnTaskEvent onTaskEvent) {
        this.onTaskEvent = onTaskEvent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onTaskEvent.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        return onTaskEvent.onDoIn(strings);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        onTaskEvent.onFinish(aBoolean);
    }
}
