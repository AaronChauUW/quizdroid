package edu.washington.chau93.quizdroid.managers;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aaron Chau on 1/31/2015.
 */
public class QuizManager implements Serializable {
    private final String TAG = "QuizManager";

    private int questionNumber;
    private int totalQuestions;
    private int score;
    private String description;
    private ArrayList<String> questions;
    private ArrayList<String[]> choices;
    private ArrayList<Integer> answers;

    public QuizManager(String description, ArrayList<String> questions, ArrayList<String[]> answers,
                       ArrayList<Integer> correctAnswer){
        this.description = description;
        this.questions = questions;
        this.choices = answers;
        this.answers = correctAnswer;
        this.totalQuestions = questions.size();
        this.questionNumber = 0;
        score = 0;
    }

    // Get the question at the current question number
    public String getQuestion(){
        return questions.get(questionNumber);
    }

    // Get the choices at the current question number
    public String[] getChoices(){
        return choices.get(questionNumber);
    }

    // Get the answer to the current question.
    public int getAnswer(){
        return answers.get(questionNumber);
    }

    // Increment question number count
    public void nextQuestion(){
        if(hasNextQuestion()) {
            questionNumber++;
        } else {
            Log.d(TAG, "There are no more questions.");
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
        return questionNumber < (totalQuestions - 1);
    }

    // Getter for question index
    public int getQuestionNumber() {
        return questionNumber;
    }

    // Get question count
    public int getTotalQuestions() {
        return totalQuestions;
    }

    // Get Description
    public String getDescription() { return description; }

    // Set Description
    public void setDescription(String description) { this.description = description; }
}
