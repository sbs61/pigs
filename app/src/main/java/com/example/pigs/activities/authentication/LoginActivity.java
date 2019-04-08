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

public class LoginActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView incorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        incorrect = findViewById(R.id.incorrect);
    }

    // Initiate register activity when Register button is clicked
    public void goToRegister(View button){
        Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    // Handler for login button, take input from username and password fields
    public void userLogin(View button){
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, String> loginTask = new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                EditText username = (EditText) findViewById(R.id.username);
                String login_username = username.getText().toString();
                EditText password = (EditText) findViewById(R.id.password);
                String login_password = password.getText().toString();

                // Call register function from Authentication Controller
                System.out.println(login_username + " " + login_password);
                return new AuthenticationController().login(login_username, login_password);
            }
            @Override
            protected void onPostExecute(String items) {
                System.out.println("wow" + items);
                if(items != null){
                    if(!items.equals("")) {
                        Intent i = new Intent(LoginActivity.this, ExercisesActivity.class);
                        startActivity(i);
                    }
                }
                incorrect.setVisibility(View.VISIBLE);
            }
        };
        loginTask.execute();
    }
}