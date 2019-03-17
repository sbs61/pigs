package com.example.pigs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pigs.R;
import com.example.pigs.activities.exercise.CreateExerciseActivity;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.controllers.ExerciseController;

public class MainActivity extends AppCompatActivity {
    private ExerciseController exerciseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(MainActivity.this, ExercisesActivity.class);
        startActivity(i);
    }


}