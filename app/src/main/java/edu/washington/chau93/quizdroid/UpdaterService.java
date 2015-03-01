package edu.washington.chau93.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.net.URI;
import java.net.URL;

import static android.app.DownloadManager.Request;

/**
 * Created by Aaron Chau on 2/26/2015.
 */
public class UpdaterService extends Service {
    private final String TAG = "UpdaterService";
    private DownloadReceiver dlReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("quiz_url");
        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        try {
            Request request = new Request(Uri.parse(url));
            File jsonFile = new File(URI.create(getString(R.string.uri_path) + "questions.json"));
            String fileName = "questions.json";
            boolean updatingOld = false;
            if(jsonFile.exists()){
                Log.d(TAG, "A data file already exist.");
                fileName = "updating_questions.json";
                updatingOld = true;
            }
            if (QuizApp.isExternalStorageWritable()){
                request.setDestinationInExternalFilesDir(
                        getApplicationContext(),
                        getApplicationInfo().dataDir,
                        fileName
                );
            }
            long downloadId = dm.enqueue(request);
            QuizApp.setUpdating(true);

            dlReceiver = new DownloadReceiver();
            dlReceiver.setDownloadId(downloadId);
            if(updatingOld){
                dlReceiver.setUpdatingOld(true);
            }

            registerReceiver(dlReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            Log.d(TAG, "Downloading questions.json from " + url);
        } catch (Exception e){
            Log.e(TAG, "Update failed.\n" + e.toString());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "UpdateService is destroyed.");
        unregisterReceiver(dlReceiver);
    }
}
