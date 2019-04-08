package com.example.pigs.activities.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pigs.R;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.progress.ProgressActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.controllers.WorkoutController;
import com.example.pigs.entities.Exercise;
import com.example.pigs.entities.Workout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/*
 * TODO: NOT FULLY IMPLEMENTED YET
 */

public class ScheduleActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private String selectedDate;
    private TextView foundWorkout;
    private Boolean found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        found = false;

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Create drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Handle navigation
                        switch(menuItem.getItemId()){
                            case R.id.nav_exercises:{
                                Intent i = new Intent(ScheduleActivity.this, ExercisesActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_workouts:{
                                Intent i = new Intent(ScheduleActivity.this, CreateWorkoutActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_progress:{
                                Intent i = new Intent(ScheduleActivity.this, ProgressActivity.class);
                                startActivity(i);
                                break;
                            }
                        }
                        return true;
                    }
                });

        // Create day to use for now
        // TODO: Fetch date from workout to compare to selected date on calendar
        final int day = 25;
        final int month2 = 3;
        final int year2 = 2019;

        // Initiate variable for calendar
        CalendarView calendarView = (CalendarView) findViewById(R.id.CalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            // Select day handler
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date
                foundWorkout = (TextView) findViewById(R.id.foundWorkout);
                if(month < 10) {
                    if(dayOfMonth < 10){
                        selectedDate = "" + year + "-0" + (month + 1) + "-0" + dayOfMonth + "T00:00:00.000+0000";
                    } else {
                        selectedDate = "" + year + "-0" + (month + 1) + "-" + dayOfMonth + "T00:00:00.000+0000";
                    }
                } else {
                    if(dayOfMonth < 10) {
                        selectedDate = "" + year + "-" + (month + 1) + "-0" + dayOfMonth + "T00:00:00.000+0000";
                    } else {
                        selectedDate = "" + year + "-" + (month + 1) + "-" + dayOfMonth + "T00:00:00.000+0000";
                    }
                }
                found = false;
                foundWorkout.setText("");
                getWorkouts();
            }
        });
    }

    public void getWorkouts(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, String> getWorkoutsTask = new AsyncTask<Object, Void, String>() {
            @Override
            @SuppressLint("WrongThread")
            protected String doInBackground(Object... params) {
                SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                int userId = Integer.parseInt(prefs.getString("userId", null));
                return new WorkoutController().getAllWorkouts(userId);
            }

            @Override
            protected void onPostExecute(String items) {
                Gson gson = new Gson();
                if(items != null) {
                    List<Workout> list = gson.fromJson(items, new TypeToken<List<Workout>>() {}.getType());

                    for (Workout element : list) {
                        // Create a List from String Array elements
                        if(selectedDate.equals(element.getDate())){
                            foundWorkout.setText(element.getName());
                            found = true;
                        }
                    }
                    if(found == false){
                        foundWorkout.setText("No workout found");
                    }
                }
            }
        };

        getWorkoutsTask.execute();
    }

    // Menu button handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
