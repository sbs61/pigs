package com.example.pigs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import com.example.pigs.R;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.controllers.ExerciseController;

import com.example.pigs.activities.workout.CreateWorkoutActivity;

public class MainActivity extends AppCompatActivity {
    private ExerciseController exerciseController;
    private CreateWorkoutActivity createWorkoutActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("test", "testing testing");

        Intent intent = new Intent(MainActivity.this, CreateWorkoutActivity.class);
        MainActivity.this.startActivity(intent);
        //setContentView(R.layout.activity_create_workout);


    }

}