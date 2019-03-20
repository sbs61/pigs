package com.example.pigs.activities.workout;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pigs.R;
import com.example.pigs.activities.exercise.CreateExerciseActivity;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.progress.ProgressActivity;

public class ScheduleActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

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
                            case R.id.nav_workouts:{
                                Intent i = new Intent(ScheduleActivity.this, CreateWorkoutActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_exercises:{
                                Intent i = new Intent(ScheduleActivity.this, ExercisesActivity.class);
                                startActivity(i);
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

        final int day = 25;
        final int month2 = 3;
        final int year2 = 2019;
        CalendarView calendarView = (CalendarView) findViewById(R.id.CalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(), "" + dayOfMonth + "." + (month+1) + "." + year, 5).show();
                TextView foundWorkout = (TextView) findViewById(R.id.foundWorkout);
                if(dayOfMonth == day && (month+1) == month2 && year == year2) {
                    foundWorkout.setText("Bench Press");
                }
                else {
                    foundWorkout.setText("No Workout Found");
                }
            }
        });
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
}
