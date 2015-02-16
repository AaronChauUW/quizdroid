package edu.washington.chau93.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import edu.washington.chau93.quizdroid.managers.QuizManager;


public class QuizActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        if (savedInstanceState == null) {

            Intent fromMain = getIntent();

            Fragment toFragment = new TopicOverViewFragment();
            toFragment.setArguments(fromMain.getExtras());
            // Need to find a place to keep track of question number.
            // Maybe just keep passing it in and out of fragments?

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, toFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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

    /**
     * The Topic OverView Fragment
     */
    public static class TopicOverViewFragment extends Fragment {
        private final String TAG = "Topic Overview Fragment";

        public TopicOverViewFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_topic_overview, container, false);

            Bundle args = getArguments();

            String topic = args.getString("topic");
            final QuizManager qm = QuizApp.createNewQuizManager(topic);


            // Get views
            TextView ovTitle = (TextView) rootView.findViewById(R.id.ovTopic);
            TextView ovDescription = (TextView) rootView.findViewById(R.id.ovDescription);
            TextView ovDetails = (TextView) rootView.findViewById(R.id.ovDetails);
            Button begin = (Button) rootView.findViewById(R.id.ovQuizStart);

            // Edit views
            ovTitle.setText(topic);
            ovDescription.setText(qm.getLongDescription());
            ovDetails.setText("There is a total of " + qm.getTotalQuestions() + " " +
                    ((qm.getTotalQuestions() == 1)? "question" : "questions") + ".");


            begin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Begin button clicked.");

//                    Fragment qFragment = new QuestionFragment();
//
//                    Bundle b = new Bundle();
//                    b.putSerializable("quiz_manager", qm);
//                    qFragment.setArguments(b);

                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    android.R.anim.slide_in_left, android.R.anim.slide_out_right
                            )
                            .replace(R.id.container, new QuestionFragment())
                            .commit();
                }
            });

            return rootView;
        }
    }

    /**
     * The Question Fragment
     */
    public static class QuestionFragment extends Fragment {
        private final String TAG = "Question Fragment";
        private int choice;

        public QuestionFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_question, container, false);

            final QuizManager qm = QuizApp.getQuizManager();

            choice = (savedInstanceState == null)? -1 : savedInstanceState.getInt("user_choice");
            TextView questionTitle = (TextView) rootView.findViewById(R.id.qQuestion);
            RadioGroup answerChoices = (RadioGroup) rootView.findViewById(R.id.qAnswers);
            final Button submit = (Button) rootView.findViewById(R.id.qSubmit);
            submit.setEnabled(choice != -1);

            String question = qm.getQuestion();
            List<String> answers = qm.getChoices();

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
                RadioButton rb = new RadioButton(rootView.getContext());
                rb.setText(ans);
                rb.setId(answerChoices.getChildCount());
                rb.setOnClickListener(radioClicks);
                answerChoices.addView(rb);
            }

            final QuizManager finalQm = qm;
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Submit Answer!");
                    Fragment rFragment = new ResultsFragment();

                    Bundle b = new Bundle();
//                    b.putSerializable("quiz_manager", qm);
                    b.putInt("user_choice", choice);

                    rFragment.setArguments(b);

                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    android.R.anim.slide_in_left, android.R.anim.slide_out_right
                            )
                            .replace(R.id.container, rFragment)
                            .commit();
                }
            });

            return rootView;
        }


        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt("user_choice", choice);
        }
    }

    public static class ResultsFragment extends Fragment {
        private final String TAG = "Result Fragment";

        public ResultsFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_results, container, false);

            Bundle args = getArguments();

            final QuizManager qm = QuizApp.getQuizManager();
            int choice = args.getInt("user_choice", -1);
            int correctChoice = qm.getAnswer();

            // If he got it right, give that human a point!
            if (choice == correctChoice){
                qm.incrementScore();
            }

            // Get views
            TextView question, userAns, correctAns, score, totalQs, currQ, totalQs2;
            question = (TextView) rootView.findViewById(R.id.rQuestion);
            userAns = (TextView) rootView.findViewById(R.id.rChosenAns);
            correctAns = (TextView) rootView.findViewById(R.id.rCorrectAns);
            score = (TextView) rootView.findViewById(R.id.rCorrectCount);
            totalQs = (TextView) rootView.findViewById(R.id.rTotalCount);
            currQ = (TextView) rootView.findViewById(R.id.rQuestionsLeft);
            totalQs2 = (TextView) rootView.findViewById(R.id.rrTotalCount);

            // Edit views
            question.setText(qm.getQuestion());
            userAns.setText(qm.getChoices().get(choice));
            correctAns.setText(qm.getChoices().get(correctChoice));
            score.setText(qm.getScore() + "");
            totalQs.setText(qm.getTotalQuestions() + "");
            currQ.setText((qm.getQuestionNumber() + 1) + "");
            totalQs2.setText(qm.getTotalQuestions() + "");

            Button nextBtn = (Button) rootView.findViewById(R.id.rNextQuestion);

            // Jump to next question
            qm.nextQuestion();

            if(!qm.hasNextQuestion()){
                nextBtn.setText("Finish");
            }
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    Fragment nextFragment;


                    if(qm.hasNextQuestion()) {
                        Log.d(TAG, "Next question!");
                        // Show next question
                        nextFragment = new QuestionFragment();
                    } else {
                        Log.d(TAG, "User finished the quiz.");
                        // Show final results
                        nextFragment = new FinalResultFragment();
                    }


//                    b.putSerializable("quiz_manager", qm);
//                    nextFragment.setArguments(b);

                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    android.R.anim.slide_in_left, android.R.anim.slide_out_right
                            )
                            .replace(R.id.container, nextFragment)
                            .commit();
                }
            });

            return rootView;
        }

    }

    public static class FinalResultFragment extends Fragment {
        private final String TAG = "Final Result Fragment";

        public FinalResultFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_final_results, container, false);

            // Get intent
//            Bundle args = getArguments();
            QuizManager qm = QuizApp.getQuizManager();

            // Get views
            TextView frScore = (TextView) rootView.findViewById(R.id.frScore);
            Button frHome = (Button) rootView.findViewById(R.id.frGoHome);

            // Edit views
            frScore.setText(qm.getScore() + "");

            frHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            });

            return rootView;
        }
    }
}
