package edu.washington.chau93.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.washington.chau93.quizdroid.domains.Topic;
import edu.washington.chau93.quizdroid.repositories.TopicRepository;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;
import static android.app.DownloadManager.EXTRA_DOWNLOAD_ID;


public class MainActivity extends ActionBarActivity {
    private final String TAG = "Quiz App";
    private String fileDir;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startScheduledUpdate();

        initQuizApp();

        updateView();
    }

    protected void initQuizApp(){
        QuizApp.initQuizApp();
        String json = null;

        try {
//            File dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File jsonFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "questions.json");
            Log.d(TAG, jsonFile.toString());
            FileInputStream stream = new FileInputStream(jsonFile);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                /* Instead of using default, pass in a decoder. */
                json = Charset.defaultCharset().decode(bb).toString();
            }
            finally {
                stream.close();
            }
        } catch (Exception e) {e.printStackTrace();}
        if(json != null) {
            try {
                QuizApp.createTopicRepo(new JSONObject(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateView(){
        TopicRepository topicRepo = QuizApp.getTopicRepo();

        // Create list of topics with the title and short description.
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for(Topic t : topicRepo.getTopics().values()){
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("topic", t.getTitle());
            datum.put("shortD", t.getShortDescription());
            data.add(datum);
        }

        // Custom adapter
        ArrayList<Topic> topics = new ArrayList<Topic>();
        topics.addAll(topicRepo.getTopics().values());
        CustomAdapter customAdapter = new CustomAdapter(
                this,
                topics,
                getResources()
        );

        final ListView lv = (ListView) findViewById(R.id.quizChoice);
        lv.setAdapter(customAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String topic = (String) ((Topic) lv.getItemAtPosition(position)).getTitle();

                Intent quizIntent = new Intent(MainActivity.this, QuizActivity.class);
                quizIntent.putExtra("topic", topic);

                startActivity(quizIntent);

            }
        });
    }

    private BroadcastReceiver receiver;
    private long dlReference;
    private DownloadManager dm;

    private void startScheduledUpdate(){
        Log.d(TAG, "Scheduling updates.");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String url = sharedPref.getString("quiz_url", String.valueOf(R.string.default_quiz_url));
        int interval = Integer.parseInt(sharedPref.getString("download_interval", "30"));
        long time = interval * 60 * 1000;

        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String dirType = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d(TAG, "Download directory: " + dirType);
        request.setDestinationInExternalFilesDir(
                this,
                dirType,
                "questions.json"
        );
        dlReference = dm.enqueue(request);

        receiver = new BroadcastReceiver() {
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
                            fileDir = uriString;
                        }
                    }
                }
            }
        };
        registerReceiver(
                receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        );
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cancel the update checks if user closes app.
        unregisterReceiver(receiver);

        Log.d(TAG, "Good bye!");
    }
}
