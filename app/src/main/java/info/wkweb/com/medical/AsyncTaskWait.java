package info.wkweb.com.medical;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by muhammadchehab on 11/5/17.
 */

public class AsyncTaskWait extends AsyncTask<Void, Void, Void> {

    private WeakReference<Context> context;

    public AsyncTaskWait(WeakReference<Context> context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void...params){
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void nothing){
        Intent intent = new Intent("result");
        LocalBroadcastManager.getInstance(context.get().getApplicationContext()).sendBroadcast
                (intent);
    }
}
