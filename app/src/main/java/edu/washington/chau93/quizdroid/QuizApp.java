package edu.washington.chau93.quizdroid;

import android.app.Application;
import android.util.Log;

import edu.washington.chau93.quizdroid.managers.QuizManager;
import edu.washington.chau93.quizdroid.repositories.TopicRepository;

/**
 * Created by Aaron Chau on 2/14/2015.
 */
public class QuizApp extends Application {
    private static QuizApp myApp = new QuizApp();
    private static TopicRepository topicRepo = new TopicRepository();
    private static QuizManager quizManager;

//    I'm really confused. Whenever I try to make this private I get a IllegalAccessException.
//    java.lang.IllegalAccessException: access to constructor not allowed
//    private QuizApp(){}

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
