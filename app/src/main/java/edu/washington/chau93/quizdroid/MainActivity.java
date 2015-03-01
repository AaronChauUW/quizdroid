package edu.washington.chau93.quizdroid;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.washington.chau93.quizdroid.domains.Topic;
import edu.washington.chau93.quizdroid.repositories.TopicRepository;


public class MainActivity extends ActionBarActivity {
    private final String TAG = "Quiz App";
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "on create");
        setContentView(R.layout.activity_main);

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if(QuizApp.getQuizApp() == null) startScheduledUpdate();

        initQuizApp();

        updateView();

        checkAirplaneMode();


    }

    protected void initQuizApp(){
        QuizApp.initQuizApp();
        QuizApp.createTopicRepo(getString(R.string.uri_path), getAssets());
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

    private void checkAirplaneMode() {
        if(QuizApp.isAirplaneModeOn(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Update cannot proceed unless you turn airplane mode off." +
                    "Would you like to turn airplane mode off?")
                    .setTitle("Quiz App Updater")
                    .setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private PendingIntent dataChecker;
    private void startScheduledUpdate(){
        Log.d(TAG, "Scheduling updates.");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String url = sharedPref.getString("quiz_url", getString(R.string.default_quiz_url));
        int interval = Integer.parseInt(sharedPref.getString("download_interval", "30"));
        long time = interval * 60 * 1000;

        Intent updateIntent = new Intent(this, Updater.class);
        updateIntent.putExtra("quiz_url", url);

        pendingIntent = PendingIntent.getBroadcast(this, 0, updateIntent, 0);

        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME, 0, time, pendingIntent);
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
        switch (id){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.action_refresh:
                updateView();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cnacel the update checks if user closes app.
        if(alarmMgr != null) {
            alarmMgr.cancel(pendingIntent);
            alarmMgr.cancel(dataChecker);
        }
        if(pendingIntent != null) pendingIntent.cancel();
        if(dataChecker != null) dataChecker.cancel();


        Log.d(TAG, "Good bye!");
    }

}
