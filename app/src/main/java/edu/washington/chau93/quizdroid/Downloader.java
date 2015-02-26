package edu.washington.chau93.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static android.app.DownloadManager.*;

/**
 * Created by Aaron on 2/22/2015.
 */
public class Downloader extends BroadcastReceiver {
    private final String TAG = "Downloader";
    private long dlReference;
    private DownloadManager dm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Checking for updates");
        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        String url = intent.getStringExtra("quiz_url");
        updateQuizTopics(context, url);
    }

    private void updateQuizTopics(Context context, String url) {
        try {
            Request request = new Request(Uri.parse(url));
            request.setDestinationInExternalFilesDir(
                    context,
                    context.getApplicationInfo().dataDir + "/QuizApp/data/",
                    "questions.json"
            );
            dlReference = dm.enqueue(request);



            Toast.makeText(context, "Downloading quiz data at: " + url, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Log.e(TAG, "Download failed.\nError: " + e.toString());
        }
    }

    public long getDlReference() {
        return dlReference;
    }

    PendingIntent pendingIntent;
    AlarmManager alarmMgr;
    public void setAlarm(Context context, String url, long time){
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Downloader.class);
        intent.putExtra("quiz_url", url);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMgr.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                0, time,
                pendingIntent
        );
    }

    public void cancelAlarm(){
        if(alarmMgr != null) alarmMgr.cancel(pendingIntent);
        if(pendingIntent != null) pendingIntent.cancel();
    }
}
