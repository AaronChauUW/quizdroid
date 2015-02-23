package edu.washington.chau93.quizdroid.managers;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

import edu.washington.chau93.quizdroid.QuizApp;
import edu.washington.chau93.quizdroid.domains.Question;
import edu.washington.chau93.quizdroid.domains.Topic;

/**
 * Created by Aaron Chau on 1/31/2015.
 */
public class QuizManager implements Serializable {
    private final String TAG = "Quiz Manager";

    private int score;
    private List<Question> questions;
    private int index;
    private Topic topicObj;

    public QuizManager(String topic){
        this.index = 0;
        this.topicObj = QuizApp.getTopicRepo().getTopic(topic);
        this.questions = topicObj.getQuestions();
        this.score = 0;
    }

    // Get the question at the current question number
    public String getQuestion(){
        return questions.get(index).getQuestion();
    }

    // Get the choices at the current question number
    public List<String> getChoices(){
        return questions.get(index).getChoices();
    }

    // Get the answer to the current question.
    public int getAnswer(){
        return questions.get(index).getAnswer();
    }

    // Increment question number count
    public void nextQuestion(){
        if(hasNextQuestion()) {
            index++;
        } else {
            Log.d(TAG, "There are no more questions. Finish the quiz already!");
        }
    }

    // Increment the user's correctly answered total
    public void incrementScore(){
        score++;
    }

    // Getter for total score
    public int getScore(){
        return score;
    }

    // Check if there are still questions to ask
    public boolean hasNextQuestion(){
        return index < questions.size();
    }

    // Getter for question index
    public int getQuestionNumber() {
        return index;
    }

    // Get question count
    public int getTotalQuestions() {
        return questions.size();
    }

    // Get Long Description
    public String getLongDescription() { return topicObj.getLongDescription(); }

    // Get Short Description
    public String getShortDescription() { return topicObj.getShortDescription(); }

}
