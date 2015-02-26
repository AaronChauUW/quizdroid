package edu.washington.chau93.quizdroid;

import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;
import static android.app.DownloadManager.EXTRA_DOWNLOAD_ID;
import static android.content.Context.*;

/**
 * Created by Aaron Chau on 2/25/2015.
 */
public class DownloadReceiver extends BroadcastReceiver {
    private final String TAG = "Download Receiver";
    private Long dlReference;

    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        String action = intent.getAction();
        if(ACTION_DOWNLOAD_COMPLETE.equals(action)){
            Log.d(TAG, "Download is done");
            long downloadId = intent.getLongExtra(EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(dlReference);
            Cursor c = dm.query(query);
            if(c.moveToFirst()){
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if(DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)){
                    Log.d(TAG, "Status successful... whatever that means.");
                    String uriString = c
                            .getString(c
                                    .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    Log.d(TAG, "uriString: " + uriString);
                }
            }
        }
    }

    public void setDlReference(Long dlReference) {
        this.dlReference = dlReference;
    }
}
