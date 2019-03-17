package com.example.pigs.activities.exercise;

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
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.entities.Exercise;

import java.io.IOException;

public class CreateExerciseActivity extends AppCompatActivity {
    private ExerciseController exerciseController;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

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

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch(menuItem.getItemId()){
                            case R.id.nav_shedule:{
                                Intent i = new Intent(CreateExerciseActivity.this, ScheduleActivity.class);
                                startActivity(i);
                            }
                            case R.id.nav_exercises:{
                                Intent i = new Intent(CreateExerciseActivity.this, ExercisesActivity.class);
                                startActivity(i);
                            }
                        }
                        return true;
                    }
                });
    }

    public void createExercise(View button){
        AsyncTask task = new CreateExerciseActivity.CreateExerciseTask();
        task.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CreateExerciseTask extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {
            EditText name = (EditText) findViewById(R.id.exercise_name);
            String exercise_name = name.getText().toString();
            EditText category = (EditText) findViewById(R.id.exercise_category);
            String exercise_category = category.getText().toString();
            if(exercise_name != "" && exercise_category != "") {
                name.setText("");
                category.setText("");
                return new ExerciseController().createExercise(exercise_name, exercise_category);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean items) {
            System.out.println(items);
        }
    }
}