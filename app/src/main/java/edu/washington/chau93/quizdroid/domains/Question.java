package edu.washington.chau93.quizdroid.domains;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aaron Chau on 2/14/2015.
 */
public class Question implements Serializable {

    private String question;
    private List<String> choices;
    private int answer;

    public Question(String question, List<String> choices, int answer){
        this.question = question;
        this.choices = choices;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getChoices() {
        return choices;
    }

    public int getAnswer() {
        return answer;
    }
}
