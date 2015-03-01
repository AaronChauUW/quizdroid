package edu.washington.chau93.quizdroid.repositories;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private final String TAG = "TopicRepository";
    private Map<String, Topic> topics;


    public TopicRepository(){
        topics = new TreeMap<String, Topic>();

        // Temp fake questions and choices
        makeFakeQuestions();

        // Make topics
        createTopics("Mathematics", longDescription, shortDescription, questions, "mathematics");
        createTopics("Marvel Super Heroes", longDescription, shortDescription, questions, null);
        createTopics("Science!", longDescription, shortDescription, questions, "science");
    }

    public TopicRepository(JSONArray jsonArray) {
        topics = new TreeMap<String, Topic>();
        try {
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject joTopic = jsonArray.getJSONObject(i);
                String title = joTopic.getString("title");
                String desc = joTopic.getString("desc");
                JSONArray questions = joTopic.getJSONArray("questions");
                List<Question> quizQuestions = new ArrayList<Question>();
                for(int j = 0; j < questions.length(); j++){
                    JSONObject joQuestion = questions.getJSONObject(j);
                    String text = joQuestion.getString("text");
                    int correctChoice = joQuestion.getInt("answer") - 1;
                    JSONArray joPossibleChoices = joQuestion.getJSONArray("answers");
                    List<String> possibleChoices = new ArrayList<String>();
                    for(int k = 0; k < joPossibleChoices.length(); k++){
                        possibleChoices.add(joPossibleChoices.getString(k));
                    }
                    quizQuestions.add(new Question(text, possibleChoices, correctChoice));
                }
                createTopics(
                        title, desc, desc, quizQuestions,
                        title.replaceAll("[^\\w\\s]","").toLowerCase()
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Create the topics for the quiz
    private void createTopics(String title, String longDescription, String shortDescription,
                              List<Question> questions, String icon){
        if(icon == null){
            topics.put(title, new Topic(title, longDescription, shortDescription, questions));
        } else {
            topics.put(title, new Topic(title, longDescription, shortDescription, questions, icon));
        }
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
    private String longDescription;
    private String shortDescription;
    private List<Question> questions;

    private void makeFakeQuestions() {
        makeLongD();
        makeShortD();
        makeQuestions();
    }

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
