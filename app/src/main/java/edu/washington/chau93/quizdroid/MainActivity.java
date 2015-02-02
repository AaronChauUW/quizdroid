package edu.washington.chau93.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] quizChoices = {"Math", "Physics", "Marvel Super Heroes"};

        ArrayAdapter<String> quizChoiceAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quizChoices);

        final ListView lv = (ListView) findViewById(R.id.quizChoice);
        lv.setAdapter(quizChoiceAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent overviewIntent = new Intent(MainActivity.this, TopicOverviewActivity.class);
                String topic = (String) lv.getItemAtPosition(position);
                overviewIntent.putExtra("topic", topic);
                // Ideally I would do a pull request on the topic to get some information
                // such as the description and how many questions there are and put them
                // the intent as well.
                if (overviewIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(overviewIntent);


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
