package com.example.pigs;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pigs.R;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.entities.Exercise;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ExerciseController exerciseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncTask task = new FetchItemsTask();
        task.execute();

        setContentView(R.layout.activity_main);
    }

    private class FetchItemsTask extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            return new ExerciseController().getExercises();
        }

        @Override
        protected void onPostExecute(String items) {
            System.out.println(items);
        }
    }
}