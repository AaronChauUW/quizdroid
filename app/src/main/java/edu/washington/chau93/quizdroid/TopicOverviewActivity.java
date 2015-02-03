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

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.washington.chau93.quizdroid.managers.QuizManager;


public class TopicOverviewActivity extends ActionBarActivity {
    private final String TAG = "Topic Overview Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_overview);

        // Get intent and grab content
        Intent fromMain = this.getIntent();
        String topic = fromMain.getStringExtra("topic");
        final QuizManager qm = (QuizManager) fromMain.getSerializableExtra("quiz_manager");

        // Get views
        TextView ovTitle = (TextView) findViewById(R.id.ovTopic);
        TextView ovDescription = (TextView) findViewById(R.id.ovDescription);
        TextView ovDetails = (TextView) findViewById(R.id.ovDetails);
        Button begin = (Button) findViewById(R.id.ovQuizStart);

        // Edit views
        ovTitle.setText(topic);
        ovDescription.setText(qm.getDescription());
        ovDetails.setText("There is a total of " + qm.getTotalQuestions() + " " +
                ((qm.getTotalQuestions() == 1)? "question" : "questions") + ".");


        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
