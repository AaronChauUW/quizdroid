package edu.washington.chau93.quizdroid;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

/**
 * Created by Aaron Chau on 2/26/2015.
 */
public class DownloadReceiver extends BroadcastReceiver {
    private final String TAG = "Update Receiver";
    private long downloadId = -1;
    private boolean updatingOld = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        String action = intent.getAction();
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action) && (downloadId != -1)){
            Log.d(TAG, "Download is done");
//            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = dm.query(query);
            if(cursor.moveToFirst()){
                checkStatus(context, cursor);
            }
        }
    }

    private void checkStatus(Context context, Cursor cursor){

        //column for status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
        String filename = cursor.getString(filenameIndex);

        String statusText = "";
        String reasonText = "";

        switch(status){
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch(reason){
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch(reason){
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "Update Successful.\n " +
                        "You can refresh the quiz content by restarting the app or " +
                        "clicking on the top right menu.";

                String uriString = cursor
                        .getString(cursor
                                .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                if(updatingOld){
                    replaceOld(context, uriString);
                }
                QuizApp.createTopicRepo(context.getString(R.string.uri_path), context.getAssets());
                break;
        }


        Toast toast = Toast.makeText(context,
                statusText + "\n" +
                        reasonText,
                Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();

        QuizApp.setUpdating(false);
        context.unregisterReceiver(this);

    }

    private void replaceOld(Context context, String uriString) {
        String uri_path = context.getString(R.string.uri_path);
        File reallyOldFile = new File(URI.create(uri_path + "old_questions.json"));
        File oldFile = new File(URI.create(uri_path + "questions.json"));
        File newFile = new File(URI.create(uri_path + "updating_questions.json"));

        if(reallyOldFile.exists()){
            reallyOldFile.delete();
        }
        oldFile.renameTo(new File(URI.create(uri_path + "old_questions.json")));
        newFile.renameTo(new File(URI.create(uri_path + "questions.json")));
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public void setUpdatingOld(boolean updatingOld) { this.updatingOld = updatingOld; }
}
