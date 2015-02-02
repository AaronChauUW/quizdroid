package edu.washington.chau93.quizdroid.managers;

import java.io.Serializable;

/**
 * Created by Aaron Chau on 1/31/2015.
 */
public class TopicManager implements Serializable {
    private String topic, description, details;

    public TopicManager(){
        this.topic = "No topic";
        this.description = "No description";
        this.details = "No details";
    }
}
