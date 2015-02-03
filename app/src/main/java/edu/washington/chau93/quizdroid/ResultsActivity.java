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

import edu.washington.chau93.quizdroid.managers.QuizManager;


public class ResultsActivity extends ActionBarActivity {
    private final String TAG = "Result Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Get intent
        Intent fromQuestion = this.getIntent();
        final QuizManager qm = (QuizManager) fromQuestion.getSerializableExtra("quiz_manager");
        int choice = fromQuestion.getIntExtra("user_choice", -1);
        int correctChoice = qm.getAnswer();

        // If he got it right, give that human a point!
        if (choice == correctChoice){
            qm.incrementScore();
        }

        // Get views
        TextView question, userAns, correctAns, score, totalQs, currQ, totalQs2;
        question = (TextView) findViewById(R.id.rQuestion);
        userAns = (TextView) findViewById(R.id.rChosenAns);
        correctAns = (TextView) findViewById(R.id.rCorrectAns);
        score = (TextView) findViewById(R.id.rCorrectCount);
        totalQs = (TextView) findViewById(R.id.rTotalCount);
        currQ = (TextView) findViewById(R.id.rQuestionsLeft);
        totalQs2 = (TextView) findViewById(R.id.rrTotalCount);

        // Edit views
        question.setText(qm.getQuestion());
        userAns.setText(qm.getChoices()[choice]);
        correctAns.setText(qm.getChoices()[correctChoice]);
        score.setText(qm.getScore() + "");
        totalQs.setText(qm.getTotalQuestions() + "");
        currQ.setText((qm.getQuestionNumber() + 1) + "");
        totalQs2.setText(qm.getTotalQuestions() + "");

        Button nextBtn = (Button) findViewById(R.id.rNextQuestion);

        if(!qm.hasNextQuestion()){
            nextBtn.setText("Finish");
        }
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qm.hasNextQuestion()) {
                    Log.d(TAG, "Next question!");
                    qm.nextQuestion();
                    Intent questionIntent =
                            new Intent(ResultsActivity.this, QuestionActivity.class);
                    questionIntent.putExtra("quiz_manager", qm);
                    startActivity(questionIntent);
                } else {
                    Log.d(TAG, "User finished the quiz.");
                    Intent finalResultIntent =
                            new Intent(ResultsActivity.this, FinalResultsActivity.class);
                    finalResultIntent.putExtra("quiz_manager", qm);
                    startActivity(finalResultIntent);
                }
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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
