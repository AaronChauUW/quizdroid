package edu.washington.chau93.quizdroid;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;

import edu.washington.chau93.quizdroid.managers.QuizManager;
import edu.washington.chau93.quizdroid.repositories.TopicRepository;

/**
 * Created by Aaron Chau on 2/14/2015.
 */
public class QuizApp extends Application {
    private static QuizApp myApp;
    private static TopicRepository topicRepo;
    private static QuizManager quizManager;

    private QuizApp(){}

    public static void initQuizApp(){
        if (myApp == null){
            myApp = new QuizApp();
        }
    }

    public static void createTopicRepo(JSONObject jsonObject){
        topicRepo = new TopicRepository(jsonObject);
    }

    public static QuizApp getQuizApp(){
        return myApp;
    }

    public static TopicRepository getTopicRepo() {
        return topicRepo;
    }

    public static QuizManager createNewQuizManager(String topic){
        quizManager = new QuizManager(topic);
        return quizManager;
    }

    public static QuizManager getQuizManager(){
        return quizManager;
    }

    @Override
    public void onCreate() {
        Log.d("Quiz App", "Quiz App created!");
        super.onCreate();
    }
}
