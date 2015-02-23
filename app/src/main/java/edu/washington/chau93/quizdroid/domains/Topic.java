package edu.washington.chau93.quizdroid.domains;

import java.io.Serializable;
import java.util.List;
import java.util.Queue;

/**
 * Created by Aaron Chau on 2/14/2015.
 */
public class Topic implements Serializable {
    private String title, longDescription, shortDescription, image;
    private List<Question> questions2;

    public Topic(String title, String longDescription, String shortDescription,
                 List<Question> questions){
        this(title, longDescription, shortDescription, questions, "default_icon");
    }

    public Topic(String title, String longDescription, String shortDescription,
                 List<Question> questions, String image){
        this.title = title;
        this.longDescription = longDescription;
        this.shortDescription = shortDescription;
        this.questions2 = questions;
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

    public List<Question> getQuestions() {
        return questions2;
    }
}
