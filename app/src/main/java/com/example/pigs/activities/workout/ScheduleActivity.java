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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pigs.R;
import com.example.pigs.activities.authentication.LoginActivity;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.progress.ProgressActivity;
import com.example.pigs.controllers.WorkoutController;
import com.example.pigs.entities.Workout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private String selectedDate;
    private TextView foundWorkout;
    private LinearLayout workouts;
    private Boolean found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        found = false;
        workouts = findViewById(R.id.workouts);

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
                                Intent i = new Intent(ScheduleActivity.this, WorkoutActivity.class);
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

        // Initiate variable for calendar
        CalendarView calendarView = (CalendarView) findViewById(R.id.CalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            // Select day handler
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date
                foundWorkout = (TextView) findViewById(R.id.foundWorkout);
                // format selected date so we can compare to workout dates
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

    // Fetch all user workouts
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
                    workouts.removeAllViews();

                    for (Workout element : list) {
                        // Check if workout date matches selected date
                        if(selectedDate.equals(element.getDate())) {
                            // Create a List from String Array elements
                            TextView workoutName = new TextView(getApplicationContext());
                            workoutName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                            workoutName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            SpannableString content = new SpannableString(element.getName());
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            workoutName.setText(content);
                            workouts.addView(workoutName);
                            for (String exercise : element.getExercises()) {
                                TextView workoutExercise = new TextView(getApplicationContext());
                                workoutExercise.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                                workoutExercise.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                workoutExercise.setText(exercise);
                                workouts.addView(workoutExercise);
                            }
                            found = true;
                        }
                    }

                    // If no workout date matches selected date
                    if(found == false){
                        TextView notFound = new TextView(getApplicationContext());
                        notFound.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        notFound.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        notFound.setText("No workout found");
                        workouts.addView(notFound);
                    }
                }
            }
        };

        getWorkoutsTask.execute();
    }

    public void logout(View textView){
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Intent i = new Intent(ScheduleActivity.this, LoginActivity.class);
        startActivity(i);
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
