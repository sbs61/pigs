package com.example.pigs.activities.exercise;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pigs.MainActivity;
import com.example.pigs.R;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.entities.Exercise;

public class ExercisesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncTask task = new ExercisesActivity.FetchItemsTask();
        task.execute();

        setContentView(R.layout.activity_main);
    }

    private class FetchItemsTask extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {
            Exercise ex = new Exercise(2L, "Bench press", "Chest");
            return new ExerciseController().createExercise(ex);
        }

        @Override
        protected void onPostExecute(Boolean items) {
            System.out.println(items);
        }
    }
}
