package edu.washington.chau93.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Aaron on 2/22/2015.
 */
public class Updater extends BroadcastReceiver {
    private final String TAG = "Updater";
    private boolean updating = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!updating) {
            Log.d(TAG, "Checking for updates");
            String url = intent.getStringExtra("quiz_url");
            updateQuizTopics(context, url);
        }
    }

    private void updateQuizTopics(Context context, String url) {
        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }
}
