package com.example.pigs.activities.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.pigs.MainActivity;
import com.example.pigs.R;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.progress.ProgressActivity;
import com.example.pigs.activities.workout.CreateWorkoutActivity;
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.controllers.AuthenticationController;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.entities.Exercise;

import java.io.IOException;

/*
 * TODO: NOT IMPLEMENTED YET
 */

public class RegistrationActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    // Register button handler
    public void registerUser(View button){
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, Boolean> registerTask = new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                EditText username = (EditText) findViewById(R.id.register_username);
                String reg_username = username.getText().toString();
                EditText password = (EditText) findViewById(R.id.register_password);
                String reg_password = password.getText().toString();

                // Call register function from Authentication Controller
                return new AuthenticationController().register(reg_username, reg_password);
            }

            @Override
            protected void onPostExecute(Boolean items) {
                System.out.println(items);
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        };
        registerTask.execute();
    }
}