package com.example.pigs.activities.exercise;

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
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import com.example.pigs.R;
import com.example.pigs.activities.progress.ProgressActivity;
import com.example.pigs.activities.workout.CreateWorkoutActivity;
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.controllers.ExerciseController;

public class CreateExerciseActivity extends AppCompatActivity {
    private ExerciseController exerciseController;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        // Dropdown menu for categories
        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"Arms", "Back", "Chest", "Core", "Legs", "Shoulders", "Pick a category"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items) {
            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }
        };

        dropdown.setAdapter(adapter);
        dropdown.setSelection(adapter.getCount());

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Create drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Handle navigation
                        switch(menuItem.getItemId()){
                            case R.id.nav_exercises:{
                                Intent i = new Intent(CreateExerciseActivity.this, ExercisesActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_workouts:{
                                Intent i = new Intent(CreateExerciseActivity.this, CreateWorkoutActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_schedule:{
                                Intent i = new Intent(CreateExerciseActivity.this, ScheduleActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_progress:{
                                Intent i = new Intent(CreateExerciseActivity.this, ProgressActivity.class);
                                startActivity(i);
                                break;
                            }
                        }
                        return true;
                    }
                });
    }

    // Create exercise button handler executes an Asyncronous task to add exercise to database
    public void createExercise(View button){
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, Boolean> task = new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                EditText name = (EditText) findViewById(R.id.exercise_name);
                String exercise_name = name.getText().toString();
                EditText category = (EditText) findViewById(R.id.exercise_category);
                Spinner dropdown = (Spinner) findViewById(R.id.spinner);
                String exercise_category = dropdown.getSelectedItem().toString();

                // Validate fields are not empty
                if(exercise_name != "" && exercise_category != "") {
                    name.setText("");
                    dropdown.post(new Runnable() {
                        @Override
                        public void run() {
                            dropdown.setSelection(6);
                        }
                    });

                    // Call create exercise function from controller
                    return new ExerciseController().createExercise(exercise_name, exercise_category);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Boolean items) {
                System.out.println(items);
            }
        };
        task.execute();
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