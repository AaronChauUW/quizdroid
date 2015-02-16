package edu.washington.chau93.quizdroid.managers;

import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.washington.chau93.quizdroid.QuizApp;
import edu.washington.chau93.quizdroid.domains.Question;
import edu.washington.chau93.quizdroid.domains.Topic;

/**
 * Created by Aaron Chau on 1/31/2015.
 */
public class QuizManager implements Serializable {
    private final String TAG = "Quiz Manager";

    private int totalQuestions;
    private int score;
    private String topic;
    private Queue<Question> questions;
    private Topic topicObj;

    public QuizManager(String topic){
        this.topic = topic;
        this.topicObj = QuizApp.getTopicRepo().getTopic(topic);
        this.questions = new LinkedList<Question>();
        questions.addAll(topicObj.getQuestions());
        this.totalQuestions = questions.size();
        this.score = 0;
    }

    // Get the question at the current question number
    public String getQuestion(){
        return questions.peek().getQuestion();
    }

    // Get the choices at the current question number
    public List<String> getChoices(){
        return questions.peek().getChoices();
    }

    // Get the answer to the current question.
    public int getAnswer(){
        return questions.peek().getAnswer();
    }

    // Increment question number count
    public void nextQuestion(){
        if(hasNextQuestion()) {
            questions.remove();
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
        return questions.size() > 0;
    }

    // Getter for question index
    public int getQuestionNumber() {
        return totalQuestions - questions.size();
    }

    // Get question count
    public int getTotalQuestions() {
        return totalQuestions;
    }

    // Get Long Description
    public String getLongDescription() { return topicObj.getLongDescription(); }

    // Get Short Description
    public String getShortDescription() { return topicObj.getShortDescription(); }

}
