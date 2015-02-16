package edu.washington.chau93.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.washington.chau93.quizdroid.domains.Topic;
import edu.washington.chau93.quizdroid.repositories.TopicRepository;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        ArrayList<Topic> totspic = new ArrayList<Topic>();
        totspic.addAll(topicRepo.getTopics().values());
        CustomAdapter customAdapter = new CustomAdapter(
                this,
                totspic,
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
