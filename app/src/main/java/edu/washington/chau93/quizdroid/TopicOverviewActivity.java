package edu.washington.chau93.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.washington.chau93.quizdroid.managers.QuizManager;


public class TopicOverviewActivity extends ActionBarActivity {
    private final String TAG = "Topic Overview Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_overview);

        Intent fromMain = this.getIntent();
        String topic = fromMain.getStringExtra("topic");

        TextView ovTitle = (TextView) findViewById(R.id.ovTopic);
        ovTitle.setText(topic);

        Button begin = (Button) findViewById(R.id.ovQuizStart);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



                QuizManager qm = new QuizManager(questions, choices, answers);

                Intent quizIntent = new Intent(TopicOverviewActivity.this, QuestionActivity.class);
                quizIntent.putExtra("quiz_manager", qm);
                Log.d(TAG, "Begin button clicked.");
                startActivity(quizIntent);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic_overview, menu);
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
