package edu.washington.chau93.quizdroid;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import edu.washington.chau93.quizdroid.managers.QuizManager;
import edu.washington.chau93.quizdroid.repositories.TopicRepository;

/**
 * Created by Aaron Chau on 2/14/2015.
 */
public class QuizApp extends Application {
    private static QuizApp myApp;
    private static TopicRepository topicRepo;
    private static QuizManager quizManager;
    private static final String TAG = "QuizApp";
    private static boolean updating = false;

    private QuizApp(){}

    public static void initQuizApp(){
        if (myApp == null){
            myApp = new QuizApp();
        }
    }

    public static void createTopicRepo(String uri_path, AssetManager am){
        File jsonQuestions = new File(URI.create(uri_path + "questions.json"));
        String jsonString = null;
        if(jsonQuestions.exists()) {
            try {
                // Create real topics
                FileInputStream stream = new FileInputStream(jsonQuestions);
                try {
                    FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                    jsonString = Charset.defaultCharset().decode(bb).toString();
                } finally {
                    stream.close();
                }
                Log.d(TAG, "questions.json exist! Make some questions!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                InputStream is = am.open("questions.json");
                int size = is.available();

                // Read the entire asset into a local byte buffer.
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                // Convert the buffer into a string.
                jsonString = new String(buffer);

            } catch (Exception e) { e.printStackTrace(); }

            Log.d(TAG, "question.json does not exist! D:<");
        }

        try {
            topicRepo = new TopicRepository(new JSONArray(jsonString));
        } catch (JSONException e) { e.printStackTrace(); }
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

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    // Check if the user is online
    public static boolean isOnline(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    // Check if the user is in Airplane mode
    public static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(
                context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0;
    }


    // Check if Quiz App is currently trying to update
    public static boolean isUpdating() {
        return updating;
    }

    // Set the updating status
    public static void setUpdating(boolean updating) {
        QuizApp.updating = updating;
    }

    @Override
    public void onCreate() {
        Log.d("Quiz App", "Quiz App created!");
        super.onCreate();
    }
}
