package edu.washington.chau93.quizdroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.washington.chau93.quizdroid.domains.Topic;
import edu.washington.chau93.quizdroid.repositories.TopicRepository;


public class MainActivity extends ActionBarActivity {
    private final String TAG = "Quiz App";
    private AlarmManager alarmMrg;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "on create");
        setContentView(R.layout.activity_main);

        alarmMrg = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        startScheduledUpdate();

        initQuizApp();

        updateView();
    }

    protected void initQuizApp(){
        QuizApp.initQuizApp();
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


    private void startScheduledUpdate(){
        Log.d(TAG, "Scheduling updates.");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String url = sharedPref.getString("quiz_url", String.valueOf(R.string.default_quiz_url));
        int interval = Integer.parseInt(sharedPref.getString("download_interval", "30"));
        long time = interval * 60 * 1000;

        Intent intent = new Intent(this, Updater.class);
        intent.putExtra("quiz_url", url);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmMrg.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                0, time,
                pendingIntent
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

        // Cnacel the update checks if user closes app.
        if(alarmMrg != null) alarmMrg.cancel(pendingIntent);
        if(pendingIntent != null) pendingIntent.cancel();
    }
}
