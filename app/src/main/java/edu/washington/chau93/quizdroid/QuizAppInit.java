package edu.washington.chau93.quizdroid;

import android.app.Application;

/**
 * Created by Aaron Chau on 2/16/2015.
 */
public class QuizAppInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    protected void initQuizApp(){
        QuizApp.initQuizApp();
    }
}
