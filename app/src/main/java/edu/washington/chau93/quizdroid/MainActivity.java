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

import java.util.ArrayList;

import edu.washington.chau93.quizdroid.managers.QuizManager;


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
                // Ideally I would do a pull request on the topic to get some information
                // such as the description and how many questions there are and put them
                // the intent as well.

                // Once you choose begin I would do another GET request for the questions, choices
                // and the answers. Sort them out and put them into the QuizManager.
                ArrayList<String> questions = new ArrayList<String>();
                ArrayList<String[]> choices = new ArrayList<String[]>();
                ArrayList<Integer> answers = new ArrayList<Integer>();

                // Some "hard coded" questions/answers
                for(int i = 0; i < 4; i++){
                    questions.add("This is a question? Here's an index: " + i);
                    choices.add(new String[4]);
                    String[] ans = choices.get(i);
                    for(int j = 0; j < 4; j++){
                        ans[j] = "Answer number: " + (j+1); // Some choices for the quiz
                    }

                    answers.add(i); // Each answer to the question is the index of the question.
                }

                String description =
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod\n" +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,\n" +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo\n" +
                "consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse\n" +
                "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non\n" +
                "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";




                QuizManager qm = new QuizManager(description, questions, choices, answers);

                overviewIntent.putExtra("topic", topic);
                overviewIntent.putExtra("quiz_manager", qm);

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
