package edu.washington.chau93.quizdroid.domains;

import java.io.Serializable;
import java.util.Queue;

/**
 * Created by Aaron Chau on 2/14/2015.
 */
public class Topic implements Serializable {
    private String title, longDescription, shortDescription, image;
    private Queue<Question> questions;

    public Topic(String title, String longDescription, String shortDescription,
                 Queue<Question> questions){
        this(title, longDescription, shortDescription, questions, "default_icon");
    }

    public Topic(String title, String longDescription, String shortDescription,
                 Queue<Question> questions, String image){
        this.title = title;
        this.longDescription = longDescription;
        this.shortDescription = shortDescription;
        this.questions = questions;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Queue<Question> getQuestions() {
        return questions;
    }
}
