package edu.washington.chau93.quizdroid.repositories;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import edu.washington.chau93.quizdroid.domains.Question;
import edu.washington.chau93.quizdroid.domains.Topic;

/**
 * Created by Aaron Chau on 2/14/2015.
 */
public class TopicRepository {
    private Map<String, Topic> topics;

    public TopicRepository(){
        topics = new TreeMap<String, Topic>();

        // Temp fake questions and choices
        makeFakeQuestions();

        // Make topics
        createTopics();
    }

    // Create the topics for the quiz
    private void createTopics(){
        topics.put("Math",
                new Topic("Math", longDescription, shortDescription, questions, "math_icon"));
        topics.put("Physics",
                new Topic("Physics", longDescription, shortDescription, questions, "physic_icon"));
        topics.put("Marvel Super Heroes",
                new Topic("Marvel Super Heroes", longDescription, shortDescription, questions));
    }

    // Get the list of the topics
    public Map<String, Topic> getTopics(){
        return topics;
    }

    // Get the list of topics titles
    public Set<String> getTopicTitles(){
        return topics.keySet();
    }

    // Get the topic
    public Topic getTopic(String topic) throws IllegalArgumentException {
        if(!topics.containsKey(topic)) {
            throw new IllegalArgumentException("Topic does not exist!");
        }
        return topics.get(topic);
    }







    // Temporary variables & methods used to create topics
    private void makeFakeQuestions() {
        makeLongD();
        makeShortD();
        makeQuestions();
    }

    private String longDescription;
    private String shortDescription;
    private List<Question> questions;

    private void makeLongD(){
        longDescription =
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod\n" +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,\n" +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo\n" +
                "consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse\n" +
                "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non\n" +
                "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    }

    private void makeShortD(){
        shortDescription = "Lorem ipsum dolor sit amet, consectetur adipisicing elit";
    }

    private void makeQuestions(){
        questions = new LinkedList<Question>();
        List<String> choices = new ArrayList<>();
        choices.add("Index 0");
        choices.add("Index 1");
        choices.add("Index 2");
        choices.add("Index 4");
        for(int i = 0; i < 4; i++){
            questions.add(new Question("What index am I?", choices, i));
        }
    }


}
