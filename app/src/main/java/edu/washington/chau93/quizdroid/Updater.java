package edu.washington.chau93.quizdroid;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Aaron on 2/22/2015.
 */
public class Updater extends BroadcastReceiver {
    private final String TAG = "Updater";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (QuizApp.isOnline(context) && !QuizApp.isUpdating()) {
            String url = intent.getStringExtra("quiz_url");

            Intent updaterServiceIntent = new Intent(context, UpdaterService.class);
            updaterServiceIntent.putExtra("quiz_url", url);

            context.startService(updaterServiceIntent);
        } else {
            Log.d(TAG, "User is not online.");
            Toast.makeText(context, "Quiz App is unable to check for updates because there is" +
                    " no WIFI or Mobile Data connection.", Toast.LENGTH_LONG).show();
        }

    }

}
