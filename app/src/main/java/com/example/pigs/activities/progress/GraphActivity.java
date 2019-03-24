package com.example.pigs.activities.progress;

import android.content.Context;
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
import com.example.pigs.activities.workout.CreateWorkoutActivity;
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.controllers.WorkoutController;
import com.example.pigs.entities.Exercise;
import com.example.pigs.entities.Progress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    private ExerciseController exerciseController;

    private DrawerLayout drawerLayout;
    private GraphView graph;
    private GridLabelRenderer gridLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Setup dates
        // TODO: Fetch dates from workouts
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();

        graph = (GraphView) findViewById(R.id.graph);

        // set manual Y bounds
        /*
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(150);
        */

        // set date label formatter
        gridLabel = graph.getGridLabelRenderer();
        gridLabel.setLabelFormatter(new DateAsXAxisLabelFormatter(GraphActivity.this));
        gridLabel.setNumHorizontalLabels(4); // only 4 because of the space
        gridLabel.setNumVerticalLabels(6);

        // set manual X bounds
        /*
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        */


        AsyncTask task = new GraphActivity.FetchProgressTask();
        task.execute();

        // Round numbers
        graph.getGridLabelRenderer().setHumanRounding(true, true);
        graph.getGridLabelRenderer().setPadding(32);

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
                                Intent i = new Intent(GraphActivity.this, ExercisesActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_workouts:{
                                Intent i = new Intent(GraphActivity.this, CreateWorkoutActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_schedule:{
                                Intent i = new Intent(GraphActivity.this, ScheduleActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_progress:{
                                Intent i = new Intent(GraphActivity.this, ProgressActivity.class);
                                startActivity(i);
                                break;
                            }
                        }
                        return true;
                    }
                });
    }

    // Fetch exercises by name with async task
    private class FetchProgressTask extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            Integer userId = 1;
            return new WorkoutController().getProgress(userId);
        }

        @Override
        protected void onPostExecute(String items) {
            Gson gson = new Gson();
            //Exercise ex = gson.fromJson(items, Exercise.class);
            if(items != null) {
                List<Progress> list = gson.fromJson(items, new TypeToken<List<Progress>>() {
                }.getType());

                Calendar calendar = Calendar.getInstance();
                Date minDate = calendar.getTime();
                double minWeights = 0;
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                for (Progress element : list) {
                    // Add data points to graph
                    series.appendData(new DataPoint(element.getDate().getTime(), element.getWeight()), true, 40);
                    minDate = element.getDate();
                    minWeights = element.getWeight();
                }

                series.setDrawDataPoints(true);
                graph.addSeries(series);

                gridLabel = graph.getGridLabelRenderer();
                gridLabel.setLabelFormatter(new DateAsXAxisLabelFormatter(GraphActivity.this));
                gridLabel.setNumHorizontalLabels(4); // only 4 because of the space
                gridLabel.setNumVerticalLabels(6);

                /*
                graph.getViewport().setMinX(minDate.getTime());
                graph.getViewport().setMinY(minWeights-20);
                */

            }
        }
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