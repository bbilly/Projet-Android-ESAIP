package org.esaip.projetandroidbbvp;

import android.content.Intent;
import android.os.AsyncTask;


public class ListerMessagesTask extends AsyncTask<String, Void, Boolean> {

    interface OnTaskEvent {
        public void onPreExecute();
        public void onFinish(Boolean result);
    }

    OnTaskEvent onTaskEvent;

    ListerMessagesTask(OnTaskEvent onTaskEvent) {
        this.onTaskEvent = onTaskEvent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onTaskEvent.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        onTaskEvent.onFinish(aBoolean);
    }
}
