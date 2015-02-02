package edu.washington.chau93.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.washington.chau93.quizdroid.managers.QuizManager;


public class QuestionActivity extends ActionBarActivity {
    private final String TAG = "Question Activity";
    private int choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        final Intent fromOV = this.getIntent();
        QuizManager qm = null;
        if (fromOV.getSerializableExtra("quiz_manager") instanceof QuizManager) {
            qm = (QuizManager) fromOV.getSerializableExtra("quiz_manager");
        }

        if (qm == null){
            Log.d(TAG, "Well.... questions didn't go through. This is bad.");
            finish();
        } else {
            choice = (savedInstanceState == null)? -1 : savedInstanceState.getInt("choice");
            TextView questionTitle = (TextView) findViewById(R.id.qQuestion);
            RadioGroup answerChoices = (RadioGroup) findViewById(R.id.qAnswers);
            final Button submit = (Button) findViewById(R.id.qSubmit);
            submit.setEnabled(choice != -1);

            String question = qm.getQuestion();
            String[] answers = qm.getChoices();

            questionTitle.setText(question);

            View.OnClickListener radioClicks = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit.setEnabled(true);
                    choice = v.getId();
                    Log.d(TAG, "You have chosen answer with id " + choice);
                }
            };

            for(String ans : answers){
                RadioButton rb = new RadioButton(this);
                rb.setText(ans);
                rb.setId(answerChoices.getChildCount());
                rb.setOnClickListener(radioClicks);
                answerChoices.addView(rb);
            }

            final QuizManager finalQm = qm;
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resultsIntent = new Intent(QuestionActivity.this, ResultsActivity.class);
                    resultsIntent.putExtra("user_choice", choice);
                    resultsIntent.putExtra("quiz_manager", finalQm);
                    startActivity(resultsIntent);
                }
            });

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("choice", choice);
    }
}
