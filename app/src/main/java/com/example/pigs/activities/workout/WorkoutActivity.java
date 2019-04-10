package com.example.pigs.activities.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.pigs.R;
import com.example.pigs.activities.authentication.LoginActivity;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.progress.ProgressActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.controllers.WorkoutController;
import com.example.pigs.entities.Exercise;
import com.example.pigs.entities.Workout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WorkoutActivity extends AppCompatActivity {

    private static final String TAG = "WorkoutActivity";

    private ExerciseController exerciseController;
    private DrawerLayout drawerLayout;

    private ListView list;
    private ArrayList<String> arrayList;
    private String exercises;

    private TextView mDisplayDate;
    private LinearLayout workouts;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private Spinner dropdown;
    private ListView listView;
    private EditText workoutName;
    private EditText workoutCategory;
    private List<String> workoutExercises;
    private List<String> exerciseList;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate called in WorkoutActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        workouts = findViewById(R.id.workouts);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        getWorkouts();

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
                                Intent i = new Intent(WorkoutActivity.this, ExercisesActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_schedule:{
                                Intent i = new Intent(WorkoutActivity.this, ScheduleActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_progress:{
                                Intent i = new Intent(WorkoutActivity.this, ProgressActivity.class);
                                startActivity(i);
                                break;
                            }
                        }
                        return true;
                    }
                });
    }

    public void goToCreateWorkout(View button){
        Intent i = new Intent(WorkoutActivity.this, CreateWorkoutActivity.class);
        startActivity(i);
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
                        TextView workoutName = new TextView(getApplicationContext());
                        workoutName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        workoutName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        SpannableString content = new SpannableString(element.getName());
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                        workoutName.setText(content);
                        workouts.addView(workoutName);
                        TextView workoutDate = new TextView(getApplicationContext());
                        workoutDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        workoutDate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        workoutDate.setTypeface(null, Typeface.ITALIC);
                        workoutDate.setText("Date: " + element.getDate().split("T")[0]);
                        workouts.addView(workoutDate);
                        for(String exercise : element.getExercises()){
                            TextView workoutExercise = new TextView(getApplicationContext());
                            workoutExercise.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            workoutExercise.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            workoutExercise.setText(exercise);
                            workouts.addView(workoutExercise);
                        }
                    }
                }
            }
        };

        getWorkoutsTask.execute();
    }

    public void logout(View textView){
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Intent i = new Intent(WorkoutActivity.this, LoginActivity.class);
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
